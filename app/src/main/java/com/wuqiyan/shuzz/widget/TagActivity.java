package com.wuqiyan.shuzz.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.wuqiyan.shuzz.R;
import com.wuqiyan.shuzz.dao.TagsDao;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by wuqiyan on 17/6/30.
 */

public class TagActivity extends Activity {

    TagContainerLayout tagContainer_already;
    TagContainerLayout tagContainer_add;
    List<String> temp_tags=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_layout);

        List<String> strs0 = TagsDao.queryTagsByState(0);
        temp_tags = strs0;
        tagContainer_already = (TagContainerLayout) findViewById(R.id.tagcontainer_already);
        tagContainer_already.setTags(strs0);

        tagContainer_already.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {
            }

            @Override
            public void onTagCrossClick(int position) {
                if (position < tagContainer_already.getChildCount()) {
                    tagContainer_already.removeTag(position);

                    //更新数据库
                    TagsDao.updateTagsState(temp_tags.get(position),1);

                    temp_tags = tagContainer_already.getTags();

                }
            }

        });

        List<String> strs1= TagsDao.queryTagsByState(1);
        tagContainer_add = (TagContainerLayout) findViewById(R.id.tagcontainer_add);
        tagContainer_add.setTags(strs1);
        tagContainer_add.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (tagContainer_already.getTags().contains(text)){
                    Toast.makeText(TagActivity.this,"重复添加",Toast.LENGTH_SHORT).show();
                }
                else {
                    tagContainer_already.addTag(text);

                    //更新数据库
                    TagsDao.updateTagsState(text,0);

                    tagContainer_add.removeTag(position);

                }
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        FloatingActionButton getTags= (FloatingActionButton) findViewById(R.id.save_fab);
        getTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> tagsList = tagContainer_already.getTags();
                Intent intent=new Intent();
                intent.putStringArrayListExtra("NEW_BOOK_TAGS", (ArrayList<String>) tagsList);
                setResult(0,intent);
                finish();
            }
        });

    }
}
