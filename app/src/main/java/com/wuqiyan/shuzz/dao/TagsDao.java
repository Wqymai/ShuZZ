package com.wuqiyan.shuzz.dao;

import com.wuqiyan.shuzz.application.SApplication;
import com.wuqiyan.shuzz.model.TagModel;

import java.util.List;

import static com.wuqiyan.shuzz.application.SApplication.getDaoInstant;

/**
 * Created by wuqiyan on 17/6/29.
 */

public class TagsDao {
    public static void insertTags(List<TagModel> tags){
        getDaoInstant().getTagModelDao().insertOrReplaceInTx(tags);
    }
    public static boolean hasTags(){
        List<TagModel> list=SApplication.getDaoInstant().getTagModelDao().loadAll();
        if (list.size()>0){
            return true;
        }
        else {
            return false;
        }

    }
}
