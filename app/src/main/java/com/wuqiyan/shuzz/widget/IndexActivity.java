package com.wuqiyan.shuzz.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.comm.SPUtils;
import com.wuqiyan.shuzz.dao.TagsDao;
import com.wuqiyan.shuzz.fragment.HomeFragment;
import com.wuqiyan.shuzz.fragment.SearchFragment;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by wuqiyan on 2017/6/26.
 */

public class IndexActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {

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
    private SearchView searchView;
    private View headerLayout;
    private Tencent mTencent;
    private SPUtils spUtils ;
    private ImageView ivAvatar;
    private TextView tvNickName;
    private UserInfo mInfo = null;
    private boolean needLogin = false;

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

        //headerlayout
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);

        initView();

        spUtils = new SPUtils(getApplicationContext());
        mTencent = Tencent.createInstance("1106317744",getApplicationContext());
        if (!spUtils.getString("openid","").equals("")) {

            long curTime = System.currentTimeMillis();
            long last = spUtils.getLong("last", 0l);
            if (curTime >= last) {//过期
                //需要重新登录
                needLogin = true;
            } else {
                needLogin = false;
                //加载用户信息
                loadUserInfo();
            }

        }
        else {
            needLogin = true;
        }
    }

    public void initView() {
        //显示toolbar
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle("书籍");
        setSupportActionBar(mtoolbar);
        mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        mtoolbar.setOnMenuItemClickListener(this);

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
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_book_black_48dp, "书籍").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.ic_search_black_48dp, "搜索").setInActiveColor(R.color.colorbttonfont).setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .initialise();

        setDefaultFragment(tags);

        //底部导航监听事件
        bottomNavigationBar.setTabSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_read:
                        Toast.makeText(IndexActivity.this,"read",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.item_love:
                        Toast.makeText(IndexActivity.this,"love",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.item_share:
                        Toast.makeText(IndexActivity.this,"share",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.item_loginout:
                        logout();
                        break;
                }
                return true;
            }
        });

        ivAvatar = (ImageView) headerLayout.findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needLogin){
                    qqLogin();
                }
                else {
                    Toast.makeText(IndexActivity.this,"您已登录！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvNickName = (TextView) headerLayout.findViewById(R.id.tvNickName);
        tvNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needLogin){
                   qqLogin();
                }else {
                    Toast.makeText(IndexActivity.this,"您已登录！",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
//                if (searchFragment == null){
                SearchFragment  searchFragment = new SearchFragment();
//                }
                switchContent(isFragment,searchFragment);
                break;
        }
    }

    @Override
    public void onTabUnselected(int i) {

    }

    @Override
    public void onTabReselected(int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

      if (requestCode != 11101) {
          if (data != null) {
              List<String> newTagsList = data.getStringArrayListExtra("NEW_BOOK_TAGS");
              FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
              ft.remove(homeFragment);
              HomeFragment newHomeFrag = new HomeFragment();
              newHomeFrag.setTags(newTagsList);
              ft.replace(R.id.maindfragment, newHomeFrag).commitAllowingStateLoss();
          }
      }
      else {
        Tencent.onActivityResultData(requestCode, resultCode, data,null);
      }

    }

    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {
                ft.hide(from).add(R.id.maindfragment, to).commitAllowingStateLoss();
            } else {
                ft.hide(from).show(to).commitAllowingStateLoss();
            }
        }
    }



    private void logout(){
        mTencent.logout(this);
        spUtils.putString("openid","");
        spUtils.putString("access_token","");
        spUtils.putLong("last",0l);
        spUtils.putString("nickname","点击头像使用QQ登录");
        spUtils.putString("figureurl_qq_2","");
        spUtils.putString("figureurl_qq_1","");
        spUtils.putString("gender","");
        spUtils.putString("province","");
        spUtils.putString("city","");
        ivAvatar.setImageResource(R.mipmap.ic_account_circle_white_48dp);
        tvNickName.setText("点击头像使用QQ登录");
        needLogin = true;
    }

    private void qqLogin(){
        mTencent.login(IndexActivity.this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                saveLoginInfo(o);
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void loadUserInfo(){
            String nickname = spUtils.getString("nickname","点击头像使用QQ登录");
            tvNickName.setText(nickname);
            String figureurl_qq_2 = spUtils.getString("figureurl_qq_2","");
            if (figureurl_qq_2.equals("")){
                ivAvatar.setImageResource(R.mipmap.ic_account_circle_white_48dp);
                return;
            }
            Picasso.with(getApplicationContext())
                .load(figureurl_qq_2)
                .transform(new CircleImageTransformation())
                .resize(dip2px(48),dip2px(48))
                .centerCrop()
                .placeholder(R.mipmap.ic_account_circle_white_48dp)
                .error(R.mipmap.ic_account_circle_white_48dp)
                .into(ivAvatar);
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void qqUserInfo(){
        mInfo = new UserInfo(this,mTencent.getQQToken());
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                saveUserInfo(o);
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void saveUserInfo(Object o){
        try {
            JSONObject jsonObject = (JSONObject) o;
            String nickname = jsonObject.optString("nickname");
            String gender = jsonObject.optString("gender");
            String province = jsonObject.optString("province");
            String city = jsonObject.optString("city");
            String figureurl_qq_1 = jsonObject.optString("figureurl_qq_1");
            String figureurl_qq_2 = jsonObject.optString("figureurl_qq_2");
            spUtils.putString("nickname",nickname);
            spUtils.putString("gender",gender);
            spUtils.putString("province",province);
            spUtils.putString("city",city);
            spUtils.putString("figureurl_qq_1",figureurl_qq_1);
            spUtils.putString("figureurl_qq_2",figureurl_qq_2);
            loadUserInfo();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void saveLoginInfo(Object o){
        try {
            JSONObject jsonObject = (JSONObject) o;
            String openid = jsonObject.optString("openid");
            String access_token = jsonObject.optString("access_token");
            String expires_in = String.valueOf(jsonObject.opt("expires_in"));
            long lastTime = System.currentTimeMillis() + Long.parseLong(expires_in)*1000;
            spUtils.putString("openid",openid);
            spUtils.putString("access_token",access_token);
            spUtils.putLong("last",lastTime);
            mTencent.setOpenId(openid);
            mTencent.setAccessToken(access_token,expires_in);
            qqUserInfo();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public class CircleImageTransformation implements Transformation {

        /**
         * A unique key for the transformation, used for caching purposes.
         */
        private static final String KEY = "circleImageTransformation";

        @Override
        public Bitmap transform(Bitmap source) {

            int minEdge = Math.min(source.getWidth(), source.getHeight());
            int dx = (source.getWidth() - minEdge) / 2;
            int dy = (source.getHeight() - minEdge) / 2;

            // Init shader
            Shader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.setTranslate(-dx, -dy);   // Move the target area to center of the source bitmap
            shader.setLocalMatrix(matrix);

            // Init paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setShader(shader);

            // Create and draw circle bitmap
            Bitmap output = Bitmap.createBitmap(minEdge, minEdge, source.getConfig());
            Canvas canvas = new Canvas(output);
            canvas.drawOval(new RectF(0, 0, minEdge, minEdge), paint);

            // Recycle the source bitmap, because we already generate a new one
            source.recycle();

            return output;
        }

        @Override
        public String key() {
            return KEY;
        }
    }
}
