package com.wuqiyan.shuzz.widget;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wuqiyan.shuzz.R;

/**
 * Created by wuqiyan on 2017/6/26.
 */

public class Index2Activity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {

    private NavigationView navigationView;
    private Toolbar mtoolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private HomeFragment homeFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_tab);
        homeFragment = new HomeFragment();


        //navgationview
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        initView();

    }

    public void initView() {
        //显示toolbar
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle("书籍");
        setSupportActionBar(mtoolbar);
        mtoolbar.setBackgroundColor(Color.parseColor("#3F51B5"));
        //绑定侧边栏
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mtoolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //显示底部导航
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor("#FCFCFC");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "书籍").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorblue))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "文章").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorAccent))
                .setFirstSelectedPosition(0)
                .initialise();
        setDefaultFragment();


        //底部导航监听事件
        bottomNavigationBar.setTabSelectedListener(this);
    }


    //设置启动页
    private void setDefaultFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.maindfragment, homeFragment).commit();
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTabSelected(int i) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (i) {
            case 0:
                mtoolbar.setTitle("书籍");
                ft.replace(R.id.maindfragment, homeFragment).commit();
                break;
            case 1:
                ft.replace(R.id.maindfragment, homeFragment).commit();
                mtoolbar.setTitle("文章");
                break;

        }

    }

    @Override
    public void onTabUnselected(int i) {

    }

    @Override
    public void onTabReselected(int i) {

    }
}
