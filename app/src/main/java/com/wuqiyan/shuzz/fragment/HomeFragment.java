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

public class HomeFragment extends Fragment {

    private View rootView;
    private SectionsPagerAdapter adapter;
    private List<String> tags;
    private final int REQUEST_CODE = 100;


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
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.container);
        //关键的一个知识点getChidFragmentManager
        adapter = new SectionsPagerAdapter(getChildFragmentManager(),tags, getContext());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(10);

        //TabLayout
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //显示当前那个标签页
//        viewPager.setCurrentItem(1);
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
            startActivityForResult(new Intent(getActivity(),TagActivity.class),REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }
}
