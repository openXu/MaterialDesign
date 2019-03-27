package com.openxu.md.chartactivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.openxu.md.R;
import com.openxu.md.databinding.ActivityChartBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChartBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_chart);

        binding.tabs.setTabMode(TabLayout.MODE_FIXED);
        SimpleFragmentPagerAdapter pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),this);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);
    }

    static class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private Context context;
        private static final String[] mTitles = {"tab1", "tab2", "tab3"};
        private static final int[] colors = {Color.RED, Color.BLUE, Color.YELLOW};

        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }
        @Override
        public Fragment getItem(int position) {
            return TestFragment.newInstance(colors[position]);
        }
        @Override
        public int getCount() {
            return mTitles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}
