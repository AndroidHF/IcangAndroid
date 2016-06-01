package com.buycolle.aicang.commen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.buycolle.aicang.R;
import com.buycolle.aicang.ui.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 预览多图片
 * Created by joe on 15/11/21.
 */
public class PhotoViewMultPicActivity extends BaseActivity {


    @Bind(R.id.vp_base_app)
    MyViewPager vpBaseApp;
    @Bind(R.id.tv_index)
    TextView tvIndex;
    private ArrayList<String> paths;
    private int curPosition = 0;

    private boolean noShowCount = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview_new);
        ButterKnife.bind(this);
        if (_Bundle != null) {
            paths = _Bundle.getStringArrayList("path");
            curPosition = _Bundle.getInt("position", 0);
            noShowCount = _Bundle.getBoolean("noShowCount", false);
            if (noShowCount) {
                tvIndex.setVisibility(View.GONE);
            }
            vpBaseApp.setAdapter(new FragAdapter(getSupportFragmentManager()));
            vpBaseApp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tvIndex.setText(position + 1 + "/" + paths.size());
                    curPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            vpBaseApp.setCurrentItem(curPosition);
            tvIndex.setText(curPosition + 1 + "/" + paths.size());
        }
    }

    public class FragAdapter extends FragmentPagerAdapter {


        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImagesViewFragment.newInstance(paths.get(position));
        }

        @Override
        public int getCount() {
            return paths.size();
        }
    }

}
