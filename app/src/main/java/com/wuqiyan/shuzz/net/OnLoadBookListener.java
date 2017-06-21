package com.wuqiyan.shuzz.net;

import com.wuqiyan.shuzz.model.IturingBookModel;

import java.util.List;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface OnLoadBookListener {
    void onSuccess(List<IturingBookModel> books);
    void onFailure(String error);
}
