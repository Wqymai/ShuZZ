package com.wuqiyan.shuzz.net;

import com.wuqiyan.shuzz.model.DetailModel;

/**
 * Created by wuqiyan on 17/7/14.
 */

public interface OnLoadDetailListener {
    void onDetail(DetailModel model);
    void onFailure(String error);

}
