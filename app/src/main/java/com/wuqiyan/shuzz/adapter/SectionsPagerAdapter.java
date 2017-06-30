package com.wuqiyan.shuzz.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wuqiyan on 2017/6/26.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

//    private String[] titles = new String[]{"Android","Python","JavaScript","Html5","Linux","C#","IOS","Jquery","数据库","机器学习"};
//    public int COUNT = titles.length;
    private Context context;
    private List<String> tags;

    public SectionsPagerAdapter(FragmentManager fm,List<String> tags ,Context context){
        super(fm);
        this.tags = tags;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
//        String type = null;
//        switch (position){
//            case 0:
//                type = Constant.ANDROID;
//                break;
//            case 1:
//                type = Constant.PYTHON;
//                break;
//            case 2:
//                type = Constant.JAVASCRIPT;
//                break;
//            case 3:
//                type = Constant.HTML5;
//                break;
//            case 4:
//                type = Constant.LINUX;
//                break;
//            case 5:
//                type = Constant.CSHARP;
//                break;
//            case 6:
//                type = Constant.IOS;
//                break;
//            case 7:
//                type = Constant.JQUERY;
//                break;
//            case 8:
//                type = Constant.DB;
//                break;
//            case 9:
//                type = Constant.MACHINELEARN;
//                break;
//
//        }
//        BookFragment fragment=new BookFragment();
//        Bundle args = new Bundle();
//        args.putString(Constant.BOOKTYPE, type);
//        fragment.setArguments(args);

        String str = tags.get(position);
        return new Fragment();
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
