package com.wuqiyan.shuzz.model;

/**
 * Created by wuqiyan on 17/6/27.
 */

public class TagModel {
    private String tagId;
    private String tagName;

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
