package com.wuqiyan.shuzz.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.wuqiyan.shuzz.application.SysApplication;

/**
 * Created by wuqiyan on 17/7/24.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.addActivity(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, IndexActivity.class));
                finish();
            }
        },1000);

    }
}
