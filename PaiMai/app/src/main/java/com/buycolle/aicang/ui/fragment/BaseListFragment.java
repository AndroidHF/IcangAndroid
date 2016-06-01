package com.buycolle.aicang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.view.xlistview.XListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/7.
 */
public class BaseListFragment extends BaseFragment {


    @Bind(R.id.list)
    protected XListView list;
    @Bind(R.id.ib_float_btn)
    ImageButton ibFloatBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_paimai_ing, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
