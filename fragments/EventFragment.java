package com.example.hufeng.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hufeng.aicangproject.R;
import com.example.hufeng.views.FixViewPager;

/**
 * Created by hufeng on 2016/3/29.
 */
public class EventFragment extends Fragment {
    private FixViewPager fixViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event,container,false);
        return view;
    }
}
