package com.wuqiyan.shuzz.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.model.BookModel;
import com.wuqiyan.shuzz.model.DetailModel;
import com.wuqiyan.shuzz.net.BookAskImpl;
import com.wuqiyan.shuzz.net.OnLoadDetailListener;

/**
 * Created by wuqiyan on 17/7/13.
 */

public class DetailActivity extends AppCompatActivity {

    private BookModel bookModel;
    private CardView cardView_desc;
    private CardView cardView_catalog;
    private TextView tv_desc;
    private TextView tv_catalog;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);


        bookModel = (BookModel) getIntent().getSerializableExtra("bookInfo");
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle(bookModel.bookName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(cheeseName);

        cardView_catalog = (CardView) findViewById(R.id.cv_catalog);
        cardView_desc = (CardView) findViewById(R.id.cv_desc);
        tv_catalog = (TextView) findViewById(R.id.detail_catalog);
        tv_desc = (TextView) findViewById(R.id.detail_desc);
        progressBar = (ProgressBar) findViewById(R.id.pb_detail);

        loadBackdrop();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);

        Picasso.with(getApplicationContext()).load(bookModel.bookImgUrl)
                .placeholder(R.mipmap.imgloading)
                .error(R.mipmap.imghold)
                .into(imageView);

        new BookAskImpl().requestDetailAskInfo(bookModel.contentUrl, new OnLoadDetailListener() {
            @Override
            public void onDetail(DetailModel model) {
                if (model!= null){
                    if (model.desc != null && !model.desc.equals("")){

                        progressBar.setVisibility(View.GONE);
                        cardView_desc.setVisibility(View.VISIBLE);
                        tv_desc.setText(model.desc);
                    }
                    if (model.catalog != null && !model.catalog.equals("")){

                        progressBar.setVisibility(View.GONE);
                        cardView_catalog.setVisibility(View.VISIBLE);
                        tv_catalog.setText(model.catalog);
                    }
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
