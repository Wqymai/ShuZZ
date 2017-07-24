package com.wuqiyan.shuzz.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
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
    private CardView cardView_recommend;
    private TextView tv_desc;
    private TextView tv_catalog;
    private TextView tv_recommend;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private LinearLayout llLoadFail;
    private ImageView bg_iv;
    private ImageView backDrop;
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
        cardView_recommend = (CardView) findViewById(R.id.cv_recommend);
        tv_catalog = (TextView) findViewById(R.id.detail_catalog);
        tv_desc = (TextView) findViewById(R.id.detail_desc);
        tv_recommend = (TextView) findViewById(R.id.detail_recommend);
        progressBar = (ProgressBar) findViewById(R.id.pb_detail);
        llLoadFail = (LinearLayout) findViewById(R.id.ll_loadFail);
        bg_iv = (ImageView) findViewById(R.id.bg_iv);
        backDrop = (ImageView) findViewById(R.id.backdrop);

        loadBackdrop();
    }

    private void loadBackdrop() {

        Picasso.with(getApplicationContext()).load(bookModel.bookImgUrl)
                .placeholder(R.mipmap.imgloading)
                .error(R.mipmap.imghold)
                .into(backDrop);


        try{
          //背景高斯模糊效果
          Picasso.with(getApplicationContext()).load(bookModel.bookImgUrl).transform(new BlurTransformation(getApplicationContext())).into(bg_iv);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        new BookAskImpl().requestDetailAskInfo(bookModel.contentUrl, new OnLoadDetailListener() {
            @Override
            public void onDetail(DetailModel model) {
                if (model== null){
                    progressBar.setVisibility(View.GONE);
                    llLoadFail.setVisibility(View.VISIBLE);
                }
                else {

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
                    if (model.recommend != null && !model.recommend.equals("")){
                        progressBar.setVisibility(View.GONE);
                        cardView_recommend.setVisibility(View.VISIBLE);
                        tv_recommend.setText(model.recommend);
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                 progressBar.setVisibility(View.GONE);
                llLoadFail.setVisibility(View.VISIBLE);
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

    /**
     * 模糊
     * @author jia
     *
     */
    public class BlurTransformation implements Transformation {

        RenderScript rs;

        public BlurTransformation(Context context) {
            super();
            rs = RenderScript.create(context);
        }

        @SuppressLint("NewApi")
        @Override
        public Bitmap transform(Bitmap bitmap) {
            // 创建一个Bitmap作为最后处理的效果Bitmap
            Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            // 分配内存
            Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // 根据我们想使用的配置加载一个实例
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // 设置模糊半径
            script.setRadius(15);

            //开始操作
            script.forEach(output);

            // 将结果copy到blurredBitmap中
            output.copyTo(blurredBitmap);

            //释放资源
            bitmap.recycle();

            return blurredBitmap;
        }

        @Override
        public String key() {
            return "blur";
        }
    }

    public class PicassoRoundTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int widthLight = source.getWidth();
            int heightLight = source.getHeight();

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);
            Paint paintColor = new Paint();
            paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

            RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

            canvas.drawRoundRect(rectF, widthLight / 5, heightLight / 5, paintColor);

            Paint paintImage = new Paint();
            paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, 0, 0, paintImage);
            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "roundcorner";
        }
    }
}
