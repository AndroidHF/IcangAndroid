package com.buycolle.aicang.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.buycolle.aicang.AppConfig;
import com.buycolle.aicang.R;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class SmileGVAdapter extends BaseAdapter {
    private static final int TYPE_INNER = 0;
    private static final int TYPE_OUTER = 1;
    private static final int TYPE_COUNT = 2;

    private Context context;
    private ArrayList<String> list;
    private int type;

    private int height;

    public SmileGVAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        setGVHeight();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.row_expression, null);
            holder = new ViewHolder();
            holder.initHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String filename = list.get(position);
        if (type == TYPE_INNER) {
            try {
                Field field = R.drawable.class.getField(filename);
                int resId = field.getInt(new R.drawable());
                holder.img.setImageResource(resId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        FrameLayout layout;

        public void initHolder(View view) {
            img = (ImageView) view.findViewById(R.id.iv_expression);
            layout = (FrameLayout) view.findViewById(R.id.ll_expression);
        }
    }

    public void setGVHeight(int height) {
        this.height = height;
    }


    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        try {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    private void setGVHeight() {
        int ConfigHeight = AppConfig.getAppConfig(context).getPhoneKeyBordHeight();
        this.height = ConfigHeight;
    }
}
