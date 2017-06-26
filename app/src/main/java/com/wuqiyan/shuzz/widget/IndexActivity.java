package com.wuqiyan.shuzz.widget;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.comm.Constant;

public class IndexActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);



        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String type = null;
            switch (position){
                case 0:
                    type = Constant.ANDROID;
                    break;
                case 1:
                    type = Constant.PYTHON;
                    break;
                case 2:
                    type = Constant.JAVASCRIPT;
                    break;
                case 3:
                    type = Constant.HTML5;
                    break;
                case 4:
                    type = Constant.LINUX;
                    break;
                case 5:
                    type = Constant.CSHARP;
                    break;
                case 6:
                    type = Constant.IOS;
                    break;
                case 7:
                    type = Constant.JQUERY;
                    break;
                case 8:
                    type = Constant.DB;
                    break;
                case 9:
                    type = Constant.MACHINELEARN;
                    break;

            }
            BookFragment fragment=new BookFragment();
            Bundle args = new Bundle();
            args.putString(Constant.BOOKTYPE, type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {

            return 11;
        }

    }
}
