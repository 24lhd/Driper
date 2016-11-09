package com.haui.adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Duong on 11/9/2016.
 */

public class PagerAdaptorSteps extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        private static int NUM_PAGES = 5;

        public PagerAdaptorSteps(FragmentManager fragmentManager, int num_pager) {
            super(fragmentManager);
            NUM_PAGES=num_pager;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            return PageStepsFragment.newInstance("Page Number " + position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        public void onPageScrollStateChanged(int state) {

        }
}
