package com.wuqiyan.shuzz.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.dao.TagsDao;
import com.wuqiyan.shuzz.fragment.HomeFragment;
import com.wuqiyan.shuzz.fragment.SearchFragment;

import java.util.List;

/**
 * Created by wuqiyan on 2017/6/26.
 */

public class IndexActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener,Toolbar.OnMenuItemClickListener {

    private NavigationView navigationView;
    private Toolbar mtoolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private List<String> tags;
    private final int REQUEST_CODE = 100;
    private Fragment isFragment;
    private int menuType = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_layout);
        tags = TagsDao.queryTagsByState(0);

        homeFragment = new HomeFragment();
        isFragment = homeFragment;


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
        mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mtoolbar.setOnMenuItemClickListener(this);

        //绑定侧边栏
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mtoolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //显示底部导航
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor("#f5f6f5");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_book_black_48dp, "书籍").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorpurple))
                .addItem(new BottomNavigationItem(R.mipmap.ic_description_black_48dp, "搜索").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colororange))
                .setFirstSelectedPosition(0)
                .initialise();

        setDefaultFragment(tags);

        //底部导航监听事件
        bottomNavigationBar.setTabSelectedListener(this);
    }


    //设置启动页
    private void setDefaultFragment(List<String> tagsList) {
        homeFragment.setTags(tagsList);
        getSupportFragmentManager().beginTransaction().replace(R.id.maindfragment, homeFragment).commit();
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTabSelected(int i) {

        switch (i) {
            case 0:
                menuType = 0;
                mtoolbar.setTitle("书籍");
                if (homeFragment == null){
                    homeFragment = new HomeFragment();
                    homeFragment.setTags(tags);
                }
                switchContent(isFragment,homeFragment);
                break;
            case 1:
                menuType = 1;
                mtoolbar.setTitle("搜索");
                if (searchFragment == null){
                searchFragment = new SearchFragment();
                }
                switchContent(isFragment,searchFragment);
                break;
        }
        invalidateOptionsMenu();

    }

    @Override
    public void onTabUnselected(int i) {

    }

    @Override
    public void onTabReselected(int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        switch (menuType){
            case 0:
                inflater.inflate(R.menu.add_item, menu);
                break;
            case 1:
                inflater.inflate(R.menu.search_item, menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.add_tag){
            startActivityForResult(new Intent(IndexActivity.this,TagActivity.class),REQUEST_CODE);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> newTagsList = data.getStringArrayListExtra("NEW_BOOK_TAGS");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(homeFragment);
        HomeFragment newHomeFrag = new HomeFragment();
        newHomeFrag.setTags(newTagsList);
        ft.replace(R.id.maindfragment,newHomeFrag).commitAllowingStateLoss();
    }

    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {
                ft.hide(from).add(R.id.maindfragment, to).commit();
            } else {
                ft.hide(from).show(to).commit();
            }
        }
    }

}
