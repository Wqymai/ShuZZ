package com.wuqiyan.shuzz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.adapter.SectionsPagerAdapter;
import com.wuqiyan.shuzz.widget.TagActivity;

import java.util.List;

/**
 * Created by wuqiyan on 2017/6/26.
 */

public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private View rootView;
    private SectionsPagerAdapter adapter;
    private List<String> tags;
    private final int REQUEST_CODE = 100;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.book_layout, container, false);
        initView(rootView);
        return rootView;

    }

    public void initView(View rootView) {
        viewPager = (ViewPager) rootView.findViewById(R.id.container);
        //关键的一个知识点getChidFragmentManager
        adapter = new SectionsPagerAdapter(getChildFragmentManager(),tags, getContext());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(10);


        //TabLayout
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_item,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_tag){
            Intent intent =new Intent(getActivity(),TagActivity.class);
            intent.putExtra("currTag",tags.get(viewPager.getCurrentItem()));
            startActivityForResult(intent,REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (data != null){
//            List<String> newTagsList = data.getStringArrayListExtra("NEW_BOOK_TAGS");
////            this.setTags(newTagsList);
//            int currIndex = data.getIntExtra("currIndex",0);
////            adapter.setTags(newTagsList);
//
//            adapter = new SectionsPagerAdapter(getChildFragmentManager(),newTagsList, getContext());
//            viewPager.setAdapter(adapter);
//            viewPager.setCurrentItem(currIndex);
//        }
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
