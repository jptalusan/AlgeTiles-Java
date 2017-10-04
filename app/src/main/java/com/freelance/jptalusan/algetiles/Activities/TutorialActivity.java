package com.freelance.jptalusan.algetiles.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.freelance.jptalusan.algetiles.R;

/**
 * Created by jptalusan on 9/30/17.
 */

public class TutorialActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    public static ViewPager viewPager;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("JP", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new TutorialFragmentAdapter(getSupportFragmentManager()));
    }

    public class TutorialFragmentAdapter extends FragmentPagerAdapter
    {
        TutorialFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("JP", position + "");
//            if(position < 8)
                return TutorialFragment.newInstance(position);
//            else
//                return new LoginFragment();
        }

        @Override
        public int getCount() {
            return 10;
        }
    }
}
