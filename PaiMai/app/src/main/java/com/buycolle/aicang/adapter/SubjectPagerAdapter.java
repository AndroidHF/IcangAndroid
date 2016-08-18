package com.buycolle.aicang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.buycolle.aicang.ui.view.mainScrole.ScrollAbleFragment;

import java.util.List;


public class SubjectPagerAdapter extends FragmentPagerAdapter {
	private FragmentManager fm;
	private List<ScrollAbleFragment> fragList;

	public SubjectPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm=fm;

	}



	public SubjectPagerAdapter(FragmentManager fm, List<ScrollAbleFragment> fragList) {
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
