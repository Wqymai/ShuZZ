package com.wuqiyan.shuzz.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuqiyan on 17/6/27.
 */
@Entity
public class TagModel {

    @Id
    private Long tagId;
    private String tagName;
    private int tagState;
    private long tp;

    public long getTp() {
        return tp;
    }

    public void setTp(long tp) {
        this.tp = tp;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public int getTagState() {
        return tagState;
    }

    public void setTagState(int tagState) {
        this.tagState = tagState;
    }

    @Keep
    public TagModel(Long tagId, String tagName, int tagState,long tp) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagState = tagState;
        this.tp = tp;
    }


    public TagModel() {
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
