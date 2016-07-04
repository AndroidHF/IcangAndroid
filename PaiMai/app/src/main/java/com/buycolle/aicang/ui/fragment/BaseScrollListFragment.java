package com.buycolle.aicang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.view.mainScrole.ScrollAbleFragment;
import com.buycolle.aicang.ui.view.xlistview.XListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by joe on 16/3/7.
 */
public class BaseScrollListFragment extends ScrollAbleFragment {


    @Bind(R.id.list)
    public XListView list;
    @Bind(R.id.ib_float_btn)
    public ImageButton ibFloatBtn;
    @Bind(R.id.tv_null)
    public TextView tv_null;

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

    @Override
    public View getScrollableView() {
        return list;
    }

    @Override
    public void refresh(int status) {

    }
}
