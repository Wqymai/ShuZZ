package com.wuqiyan.shuzz.net;

import com.wuqiyan.shuzz.model.BookModel;

import java.util.List;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface OnLoadBookListener {
    void onSuccess(List<BookModel> books);
    void onFailure(String error);
}
