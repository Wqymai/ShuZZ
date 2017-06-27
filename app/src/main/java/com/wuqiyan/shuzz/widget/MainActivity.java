package com.wuqiyan.shuzz.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wuqiyan.shuzz.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView= (TextView) findViewById(R.id.enter_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IndexActivity.class));
//                IturingImpl ituring=new IturingImpl(getApplicationContext());
//                ituring.getAndroid_Ituring(1);
//                ituring.setOnLoadBookListener(new OnLoadBookListener() {
//                    @Override
//                    public void onSuccess(List<BookModel> books) {
//                        Log.i("TAG",books.toString());
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//
//                    }
//                });
//                ituring.getAndroidPages(Constant.ITURING_ANDROID);
            }
        });

    }


}
