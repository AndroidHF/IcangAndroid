package com.buycolle.aicang.ui.activity.comment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.BaseActivity;
import com.buycolle.aicang.ui.view.FixedViewPager;
import com.buycolle.aicang.ui.view.MyHeader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/10.
 */
public class MainComentActivity extends BaseActivity {

    @Bind(R.id.my_header)
    MyHeader myHeader;
    @Bind(R.id.tv_all_comment)
    public TextView tvAllComment;
    @Bind(R.id.tv_good_comment_count)
    public TextView tvGoodCommentCount;
    @Bind(R.id.ll_good_comment)
    LinearLayout llGoodComment;
    @Bind(R.id.tv_bad_comment_count)
    public TextView tvBadCommentCount;
    @Bind(R.id.ll_bad_comment)
    LinearLayout llBadComment;
    @Bind(R.id.vp_main_container)
    FixedViewPager vpMainContainer;
    private ArrayList<View> views;

    MainPagerAdapter mainPagerAdapter;

    private int currentIndex = 0;
    private int userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comment);
        ButterKnife.bind(this);
        views = new ArrayList<>();
        currentIndex = _Bundle.getInt("currentIndex");
        userid = _Bundle.getInt("userid");

        myHeader.init("查看评论", new MyHeader.Action() {
            @Override
            public void leftActio() {
                finish();
            }
        });
        views.add(tvAllComment);
        views.add(llGoodComment);
        views.add(llBadComment);
        initListener();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        vpMainContainer.setAdapter(mainPagerAdapter);
        vpMainContainer.setOffscreenPageLimit(views.size() - 1);

        initStatus(currentIndex);
    }

    private void initListener() {
        tvAllComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initStatus(0);
            }
        });
        llGoodComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initStatus(1);
            }
        });
        llBadComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initStatus(2);
            }
        });
    }


    private void initStatus(int index) {
        for (int i = 0; i < views.size(); i++) {
            if (index == i) {
                views.get(i).setBackgroundResource(R.drawable.shape_orange_black);
            } else {
                views.get(i).setBackgroundResource(R.drawable.shape_white_black);
            }
        }
        vpMainContainer.setCurrentItem(index);
    }


    public class MainPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager fm;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return CommentsFragment.newInstance(position, userid);
        }


    }

}
