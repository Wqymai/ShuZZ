package com.wuqiyan.shuzz.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.comm.Constant;


/**
 * Created by wuqiyan on 17/6/26.
 */

public class FragmentActivity extends android.support.v4.app.FragmentActivity implements View.OnClickListener {
    TextView book_tab;
    TextView article_tab;
    ViewGroup container;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ViewGroup main_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_tab);
        initView();
        initEvent();
    }

    private void initView(){
        book_tab= (TextView) findViewById(R.id.book_tab);
        article_tab= (TextView) findViewById(R.id.article_tab);
        container = (ViewGroup) findViewById(R.id.list_container);
        switchTab(0);


    }
    private void switchTab(int tab){


        ViewGroup viewGroup;
        if (tab == 0){
            viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.book_layout,null);
            container.addView(viewGroup);
            main_content= (ViewGroup) viewGroup.findViewById(R.id.main_content);
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = (ViewPager) viewGroup.findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            main_content.setVisibility(View.VISIBLE);
            TabLayout tabLayout = (TabLayout) viewGroup.findViewById(R.id.tabs);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }else {
            main_content.setVisibility(View.INVISIBLE);
            viewGroup =(ViewGroup) getLayoutInflater().inflate(R.layout.article_layout,null);
            container.addView(viewGroup);
        }
    }
    private  void initEvent(){
        book_tab.setOnClickListener(this);
        article_tab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.book_tab:
                switchTab(0);
                break;
            case R.id.article_tab:
                switchTab(1);
                break;
        }
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
            RecycleFragment fragment=new RecycleFragment();
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
