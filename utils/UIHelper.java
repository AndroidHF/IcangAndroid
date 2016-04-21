package com.example.hufeng.com.example.hufeng.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.hufeng.fragments.HomeFragment;

/**
 * Created by hufeng on 2016/3/28.
 */
public class UIHelper {
    //界面的跳转(无数据传递)
    public static void jump(Context context,Class clazz){
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);
    }

    public static void jumpForResultByFragment(HomeFragment fragment,Class clazz){
        Intent intent = new Intent(fragment.getActivity(),clazz);
        fragment.startActivity(intent);
    }

    //弹出Toast提示窗口
    public static void ToastShow(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_LONG).show();
    }
}
