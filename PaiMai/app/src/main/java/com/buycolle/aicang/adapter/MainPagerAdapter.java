package com.buycolle.aicang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.buycolle.aicang.ui.fragment.BaseFragment;

import java.util.List;


public class MainPagerAdapter extends FragmentPagerAdapter {
	private FragmentManager fm;
	private List<BaseFragment> fragList;

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm=fm;
		
	}
	
	
	
	public MainPagerAdapter(FragmentManager fm, List<BaseFragment> fragList) {
		super(fm);
		this.fm=fm;
		this.fragList = fragList;
	}




	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragList==null?0:fragList.size();
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragList.get(position);
	}




	
	
}
