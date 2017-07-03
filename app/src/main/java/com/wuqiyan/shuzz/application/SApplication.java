package com.wuqiyan.shuzz.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.wuqiyan.shuzz.dao.TagsDao;
import com.wuqiyan.shuzz.model.DaoMaster;
import com.wuqiyan.shuzz.model.DaoSession;
import com.wuqiyan.shuzz.net.IturingImpl;

import java.io.IOException;
import java.util.Collections;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by wuqiyan on 17/6/29.
 */

public class SApplication extends Application {
    private static DaoSession daoSession;
    @Override
    public void onCreate() {

        super.onCreate();
        setUpDatabase();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

       if (!TagsDao.hasTags()){
           new IturingImpl(this).getIturingTags();
       }
       //设置picasso请求https
        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();
        Picasso.setSingletonInstance(new Picasso.Builder(this).
                downloader(new ImageDownLoader(client))
                .build());
    }
    private void setUpDatabase(){
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"book.db",null);
        SQLiteDatabase db=helper.getWritableDatabase();
        DaoMaster daoMaster=new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
    public static DaoSession getDaoInstant(){
        return daoSession;
    }

    public class ImageDownLoader implements Downloader {
        OkHttpClient client = null;

        public ImageDownLoader(OkHttpClient client) {
            this.client = client;
        }

        @Override
        public Response load(Uri uri, int networkPolicy) throws IOException {

            CacheControl cacheControl = null;
            if (networkPolicy != 0) {
                if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                    cacheControl = CacheControl.FORCE_CACHE;
                } else {
                    CacheControl.Builder builder = new CacheControl.Builder();
                    if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                        builder.noCache();
                    }
                    if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                        builder.noStore();
                    }
                    cacheControl = builder.build();
                }
            }

            Request.Builder builder = new Request.Builder().url(uri.toString());
            if (cacheControl != null) {
                builder.cacheControl(cacheControl);
            }

            okhttp3.Response response = client.newCall(builder.build()).execute();
            int responseCode = response.code();
            if (responseCode >= 300) {
                response.body().close();
                throw new ResponseException(responseCode + " " + response.message(), networkPolicy,
                        responseCode);
            }

            boolean fromCache = response.cacheResponse() != null;

            ResponseBody responseBody = response.body();
            return new Response(responseBody.byteStream(), fromCache, responseBody.contentLength());

        }

        @Override
        public void shutdown() {

            Cache cache = client.cache();
            if (cache != null) {
                try {
                    cache.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
