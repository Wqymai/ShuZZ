package com.wuqiyan.shuzz.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wuqiyan.shuzz.widget.BookFragment;

import java.util.List;

/**
 * Created by wuqiyan on 2017/6/26.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private Context context;
    private List<String> tags;

    public SectionsPagerAdapter(FragmentManager fm,List<String> tags ,Context context){
        super(fm);
        this.tags = tags;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        BookFragment fragment=new BookFragment();
        Bundle args = new Bundle();
        args.putString("kw", tags.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tags.get(position);
    }
}
