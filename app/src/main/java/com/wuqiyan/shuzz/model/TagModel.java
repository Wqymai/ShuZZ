package com.wuqiyan.shuzz.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuqiyan on 17/6/27.
 */

@Entity
public class TagModel {


    private String tagId;
    private String tagName;


    @Generated(hash = 351456455)
    public TagModel(String tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    @Generated(hash = 1705063549)
    public TagModel() {
    }


    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "tagid="+tagId+" tagname="+tagName;
    }
}
