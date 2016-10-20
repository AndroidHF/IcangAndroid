package com.buycolle.aicang.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.buycolle.aicang.Constans;
import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.adapter.UserCenterMenuAdapter;
import com.buycolle.aicang.api.ApiCallback;
import com.buycolle.aicang.event.EditUserInfoEvent;
import com.buycolle.aicang.event.LogOutEvent;
import com.buycolle.aicang.event.LoginEvent;
import com.buycolle.aicang.event.UpdateTanNoticeEvent;
import com.buycolle.aicang.ui.activity.LoginActivity;
import com.buycolle.aicang.ui.activity.RegisterActivity;
import com.buycolle.aicang.ui.activity.userinfo.UserInfoActivity;
import com.buycolle.aicang.util.UIHelper;
import com.buycolle.aicang.util.superlog.JSONUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joe on 16/3/2.
 */
public class UserCenterFragmentNew extends BaseFragment {


    @Bind(R.id.list1)
    ListView list1;
    @Bind(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    private boolean isRun = false;
    private int pageIndex = 1;
    private int pageNum = 10;

    View headerView;
    private RelativeLayout rl_top_header_not_login;
    private Button btn_login, btn_register;


    private RelativeLayout rl_top_header_login;
    private CircleImageView profile_image;
    private TextView tv_user_name, tv_user_desc;
    private UserCenterMenuAdapter usercentermenuadapter;


    private void findHeaderView(View headerView) {
        rl_top_header_not_login = (RelativeLayout) headerView.findViewById(R.id.rl_top_header_not_login);
        rl_top_header_login = (RelativeLayout) headerView.findViewById(R.id.rl_top_header_login);
        btn_login = (Button) headerView.findViewById(R.id.btn_login);
        btn_register = (Button) headerView.findViewById(R.id.btn_register);
        profile_image = (CircleImageView) headerView.findViewById(R.id.profile_image);
        tv_user_name = (TextView) headerView.findViewById(R.id.tv_user_name);
        tv_user_desc = (TextView) headerView.findViewById(R.id.tv_user_desc);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jump(mActivity, RegisterActivity.class);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jump(mActivity, LoginActivity.class);
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.jump(mActivity, UserInfoActivity.class);
            }
        });
        //mApplication.setImages(Constans.canglaoshiPath, profile_image);
        //change by :胡峰，头像的处理
        mApplication.setTouImages(Constans.canglaoshiPath,profile_image);

    }

    //登录触发
    public void onEventMainThread(LoginEvent event) {
        if (mApplication.isLogin()) {
            initLoginView();
        } else {
            initNoLoginView();
        }
        loadTanNotice();
    }

    //更新了 叹号的显示事件
    public void onEventMainThread(UpdateTanNoticeEvent event) {
        if (event.getStatus() == 0||event.getStatus() == 1||event.getStatus() == 2) {
            loadTanNotice();
        }
    }

    //登出触发
    public void onEventMainThread(LogOutEvent event) {
        if (mApplication.isLogin()) {
            initLoginView();
        } else {
            initNoLoginView();
        }
        usercentermenuadapter.setStatus(0, 0, 0, 0);
    }


    //更新个人资料
    public void onEventMainThread(EditUserInfoEvent event) {
        if (event.getStatus() == 0) {//头像更新
            String path = LoginConfig.getUserInfoImage(mContext);
            mApplication.setImages(path, profile_image);
        }
        if (event.getStatus() == 1) {//个性签名更新
            String gexing = LoginConfig.getUserInfoGeXing(mContext);
            if (TextUtils.isEmpty(gexing)) {
                tv_user_desc.setText("这个用户懒，什么都没有留下");
            } else {
                tv_user_desc.setText(gexing);
            }
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = inflater.inflate(R.layout.view_usercenter_header_not_login_new, null);
        findHeaderView(headerView);
        if (mApplication.isLogin()) {
            initLoginView();
        } else {
            initNoLoginView();
        }
        list1.addHeaderView(headerView);

        usercentermenuadapter = new UserCenterMenuAdapter(mActivity);
        list1.setAdapter(usercentermenuadapter);
        viewFlipper.setDisplayedChild(0);
        loadTanNotice();
    }

    private boolean hasMoreData = false;

    private void loadTanNotice() {
        if(!LoginConfig.isLogin(mContext)){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("self_user_id", LoginConfig.getUserInfo(mContext).getUser_id());
            jsonObject.put("sessionid", LoginConfig.getUserInfo(mContext).getSessionid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mApplication.apiClient.jpushrecord_gettipbyapp(jsonObject, new ApiCallback() {
            @Override
            public void onApiStart() {

            }

            @Override
            public void onApiSuccess(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    if (JSONUtil.isOK(resultObj)) {
                        int my_buy = resultObj.getJSONObject("infos").getInt("my_buy");
                        int my_seller = resultObj.getJSONObject("infos").getInt("my_seller");
                        int my_show = resultObj.getJSONObject("infos").getInt("my_show");
                        int my_qa = resultObj.getJSONObject("infos").getInt("my_qa");
                        int my_buy_ing = resultObj.getJSONObject("infos").getInt("my_buy_ing");
                        int my_buy_end = resultObj.getJSONObject("infos").getInt("my_buy_end");
                        int my_seller_ing = resultObj.getJSONObject("infos").getInt("my_seller_ing");
                        int my_seller_selt = resultObj.getJSONObject("infos").getInt("my_seller_selt");
                        int my_qa_q = resultObj.getJSONObject("infos").getInt("my_qa_q");
                        int my_qa_a = resultObj.getJSONObject("infos").getInt("my_qa_a");
                        usercentermenuadapter.setStatus(my_buy, my_seller, my_show,my_qa);
                        usercentermenuadapter.getStatus(my_buy_ing,my_buy_end,my_seller_ing,my_seller_selt,my_qa_q,my_qa_a);
                    } else {
                        UIHelper.t(mContext, JSONUtil.getServerMessage(resultObj));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiFailure(Request request, Exception e) {

            }
        });
    }


    private void initLoginView() {
        rl_top_header_not_login.setVisibility(View.GONE);
        rl_top_header_login.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(LoginConfig.getUserInfoGeXing(mContext))) {
            tv_user_desc.setText("这个用户懒，什么都没有留下");
        } else {
            tv_user_desc.setText(LoginConfig.getUserInfoGeXing(mContext));
        }
        tv_user_name.setText(LoginConfig.getUserInfo(mContext).getUser_nick());
        String path = LoginConfig.getUserInfoImage(mContext);
        //change by :胡峰，头像的处理
        mApplication.setTouImages(path,profile_image);

    }

    private void initNoLoginView() {
        rl_top_header_not_login.setVisibility(View.VISIBLE);
        rl_top_header_login.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void refreshByState(int state) {
        super.refreshByState(state);
        if (state == 0) {//刷新叹号什么
            loadTanNotice();
            pageIndex=1;
        }
    }
}
