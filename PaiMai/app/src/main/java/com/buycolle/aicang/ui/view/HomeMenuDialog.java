package com.buycolle.aicang.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.buycolle.aicang.LoginConfig;
import com.buycolle.aicang.R;
import com.buycolle.aicang.bean.MainMenuBean;

import java.util.ArrayList;


/**
 * Created by joe on 16/1/5.
 */
public class HomeMenuDialog extends Dialog {

    private Context mContext;

    public CallBack callBack;

    private GridView gv_view;
    private TextView tv_cancle;
    private TextView tv_sure;

    private ArrayList<MainMenuBean> selectBeans;
    private ArrayList<MainMenuBean> orinalBeans;
    private ArrayList<MainMenuBean> tempBeans;
    private MyAdapter myAdapter;


    public HomeMenuDialog(final Context context, ArrayList<MainMenuBean> selectBean) {
        super(context, R.style.CustomMaterialDialog);
        setContentView(R.layout.dialog_home_menu);
        selectBeans = selectBean;
        orinalBeans = new ArrayList<>();
        tempBeans = new ArrayList<>();
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mContext = context;
        setCanceledOnTouchOutside(false);
        gv_view = (GridView) findViewById(R.id.gv_view);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        initOrinal();
        gv_view.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        myAdapter = new MyAdapter();
        gv_view.setAdapter(myAdapter);
        gv_view.setOnItemClickListener(myAdapter);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (callBack != null) {
                    if (tempBeans.size() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("个性化" + ",");
                        stringBuilder.append("全部" + ",");
                        for (MainMenuBean tempBean : tempBeans) {
                            stringBuilder.append(tempBean.getTitle() + ",");
                        }
                        String value = stringBuilder.toString();
                        LoginConfig.updateHomeMenu(mContext, value.substring(0, value.length() - 1));
                        callBack.ok(true);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("个性化" + ",");
                        stringBuilder.append("全部" + ",");
                        String value = stringBuilder.toString();
                        LoginConfig.updateHomeMenu(mContext, value.substring(0, value.length() - 1));
                        callBack.ok(false);
                    }

                }
            }
        });
    }

    private void initOrinal() {
        orinalBeans.add(new MainMenuBean("漫画", R.drawable.show_menu_1));
        orinalBeans.add(new MainMenuBean("BD、DVD", R.drawable.show_menu_2));
        orinalBeans.add(new MainMenuBean("游戏", R.drawable.show_menu_3));
        orinalBeans.add(new MainMenuBean("书籍", R.drawable.show_menu_4));
        orinalBeans.add(new MainMenuBean("周边", R.drawable.show_menu_5));
        orinalBeans.add(new MainMenuBean("手办、模型", R.drawable.show_menu_6));
        orinalBeans.add(new MainMenuBean("音乐、演出", R.drawable.show_menu_7));
        orinalBeans.add(new MainMenuBean("服装、COS", R.drawable.show_menu_8));
        orinalBeans.add(new MainMenuBean("其他", R.drawable.another_icon));

        for (MainMenuBean selectBean : selectBeans) {
            for (MainMenuBean orinalBean : orinalBeans) {
                if (selectBean.getTitle().equals(orinalBean.getTitle())) {
                    orinalBean.setIsSelect(true);
                    tempBeans.add(orinalBean);
                }
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

    }

    public HomeMenuDialog setCallBack(CallBack back) {
        this.callBack = back;
        return this;

    }

    public interface CallBack {
        void ok(boolean isChange);

        void cancle();
    }


    private class MyAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        @Override
        public int getCount() {
            return orinalBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return orinalBeans.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_home_menu_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.res = (ImageView) convertView.findViewById(R.id.res);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MainMenuBean mainMenuBean = orinalBeans.get(i);
            holder.title.setText(mainMenuBean.getTitle());
            if (mainMenuBean.isSelect()) {
                holder.res.setImageResource(getTitleBeanRes(mainMenuBean.getTitle()));
            } else {
                holder.res.setImageResource(getTitleBeanUnSelRes(mainMenuBean.getTitle()));
            }
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MainMenuBean mainMenuBean = orinalBeans.get(i);
            if (mainMenuBean.isSelect()) {
                tempBeans.remove(mainMenuBean);
                mainMenuBean.setIsSelect(false);
            } else {
                tempBeans.add(mainMenuBean);
                mainMenuBean.setIsSelect(true);
            }
            notifyDataSetChanged();
        }

        public class ViewHolder {
            TextView title;
            ImageView res;
        }
    }

    /**
     * 获取已经选择的
     *
     * @param title
     * @return
     */
    private int getTitleBeanRes(String title) {
        if ("漫画".equals(title)) {
            return R.drawable.show_menu_1_sel;
        } else if ("BD、DVD".equals(title)) {
            return R.drawable.show_menu_2_sel;
        } else if ("游戏".equals(title)) {
            return R.drawable.show_menu_3_sel;
        } else if ("书籍".equals(title)) {
            return R.drawable.show_menu_4_sel;
        } else if ("手办、模型".equals(title)) {
            return R.drawable.show_menu_6_sel;
        } else if ("周边".equals(title)) {
            return R.drawable.show_menu_5_sel;
        } else if ("音乐、演出".equals(title)) {
            return R.drawable.show_menu_7_sel;
        } else if ("服装、COS".equals(title)) {
            return R.drawable.show_menu_8_sel;
        } else if ("其他".equals(title)) {
            return R.drawable.another_icon_sel;
        }
        return 0;
    }

    private int getTitleBeanUnSelRes(String title) {
        if ("漫画".equals(title)) {
            return R.drawable.show_menu_1;
        } else if ("BD、DVD".equals(title)) {
            return R.drawable.show_menu_2;
        } else if ("游戏".equals(title)) {
            return R.drawable.show_menu_3;
        } else if ("书籍".equals(title)) {
            return R.drawable.show_menu_4;
        } else if ("手办、模型".equals(title)) {
            return R.drawable.show_menu_6;
        } else if ("周边".equals(title)) {
            return R.drawable.show_menu_5;
        } else if ("音乐、演出".equals(title)) {
            return R.drawable.show_menu_7;
        } else if ("服装、COS".equals(title)) {
            return R.drawable.show_menu_8;
        } else if ("其他".equals(title)) {
            return R.drawable.another_icon;
        }
        return 0;
    }

}
