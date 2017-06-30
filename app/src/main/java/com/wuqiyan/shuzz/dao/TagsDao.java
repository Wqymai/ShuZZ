package com.wuqiyan.shuzz.dao;

import com.wuqiyan.shuzz.application.SApplication;
import com.wuqiyan.shuzz.model.TagModel;
import com.wuqiyan.shuzz.model.TagModelDao;

import java.util.ArrayList;
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

    public static List<String> queryTagsByState(int state){
        List<String> tagStrs=new ArrayList<>();
        org.greenrobot.greendao.query.Query<TagModel> query = SApplication.getDaoInstant().getTagModelDao().queryBuilder().where(TagModelDao.Properties.TagState.eq(state)).build();
        List<TagModel> list = query.list();
        for (TagModel tagModel :list){
            tagStrs.add(tagModel.getTagName());
        }
        return tagStrs;
    }
    public static void updateTagsState(String tagName,int state){
      TagModel tagModel = SApplication.getDaoInstant().getTagModelDao().queryBuilder().where(TagModelDao.Properties.TagName.eq(tagName)).unique();
      tagModel.setTagState(state);
      SApplication.getDaoInstant().getTagModelDao().update(tagModel);
    }
}
