package com.fbm.nkd.cs403x_final;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Feed", "Camera", "Map" };

    static CameraFragment cameraFragment;
    static FeedFragment feedFragment;
    static MapFrag mapFragment;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        cameraFragment = new CameraFragment();
        feedFragment = new FeedFragment();
        mapFragment = new MapFrag();
    }

    /**
     * Used to update map fragment once new photos are loaded
     */
    public void refreshMapFrag(){
        mapFragment = new MapFrag();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = feedFragment;
                break;

            case 1:
                fragment = cameraFragment;
                break;

            case 2:
                fragment = mapFragment;
                break;

        }

        return fragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}