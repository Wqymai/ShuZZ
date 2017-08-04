package com.wuqiyan.shuzz.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.net.BookAskImpl;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView= (TextView) findViewById(R.id.enter_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Tencent tencent = Tencent.createInstance("1106317744",getApplicationContext());
//                tencent.login(MainActivity.this, "all", new IUiListener() {
//                    @Override
//                    public void onComplete(Object o) {
//                        Log.i("wqy","1===成功了");
//                        JSONObject jo = (JSONObject)o ;
//                        Log.i("wqy","json=" + String.valueOf(jo));
//                    }
//
//                    @Override
//                    public void onError(UiError uiError) {
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//                startActivity(new Intent(MainActivity.this, IndexActivity.class));
//                IturingImpl ituring=new IturingImpl(getApplicationContext());
//                ituring.getIturingBook("49",1);
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
//
//                    @Override
//                    public void onPageNext(boolean hasNext) {
//
//                    }
//                });
//                ituring.getIturingTags();

                BookAskImpl impl=new BookAskImpl();
                impl.requestMookTagInfos();
//                impl.setOnLoadBookListener(new OnLoadBookListener() {
//                    @Override
//                    public void onSuccess(List<BookModel> books) {
//
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        System.out.println(error);
//                    }
//
//                    @Override
//                    public void onPageNext(boolean hasNext) {
//
//                    }
//                });


            }
        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Tencent.onActivityResultData(requestCode, resultCode, data, new IUiListener() {
//            @Override
//            public void onComplete(Object o) {
//                Log.i("wqy","1===成功了");
//                JSONObject jo = (JSONObject)o ;
//                Log.i("wqy","json=" + String.valueOf(jo));
//            }
//
//            @Override
//            public void onError(UiError uiError) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
//    }

}
