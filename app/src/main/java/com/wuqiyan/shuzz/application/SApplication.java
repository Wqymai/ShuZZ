package com.wuqiyan.shuzz.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.wuqiyan.shuzz.dao.TagsDao;
import com.wuqiyan.shuzz.model.DaoMaster;
import com.wuqiyan.shuzz.model.DaoSession;
import com.wuqiyan.shuzz.net.IturingImpl;

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
           Log.i("wqy","需要获取Tag");
           new IturingImpl(this).getIturingTags();
       }
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
}
