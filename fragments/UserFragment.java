package com.example.hufeng.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.example.hufeng.aicangproject.R;
import com.example.hufeng.views.XListView;

/**
 * Created by hufeng on 2016/3/29.
 */
public class UserFragment extends Fragment {
    private ViewFlipper viewFlipper;
    private XListView xListView1;
    private XListView xListView2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
//        xListView1 = (XListView) view.findViewById(R.id.list1);
//        xListView2 = (XListView) view.findViewById(R.id.list2);
        viewFlipper.startFlipping();
        return view;
    }
}
