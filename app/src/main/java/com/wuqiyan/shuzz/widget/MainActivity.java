package com.wuqiyan.shuzz.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wuqiyan.shuzz.R;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class MainActivity extends AppCompatActivity {
    TagContainerLayout mTagContainerLayout;
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
//                ituring.getIturingTags();
            }
        });


        List<String> strs=new ArrayList<>();
        strs.add("java");
        strs.add("android");
        strs.add("ios");
        strs.add("jquery");
        strs.add("db");
        strs.add("js");
         mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        mTagContainerLayout.setTags(strs);

        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Toast.makeText(MainActivity.this, "click-position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTagLongClick(int position, String text) {
                if (position < mTagContainerLayout.getChildCount()) {
                    mTagContainerLayout.removeTag(position);
                }
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

    }


}
