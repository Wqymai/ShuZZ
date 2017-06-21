package com.wuqiyan.shuzz.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface IturingApi {
    @GET("tag/books/{type}")
    Call<ResponseBody> getIturingBook(@Path("type") int type,@Query("page") int page);

    @GET("tag/{type}")
    Call<ResponseBody> getIturingPages(@Path("type") int type);
}
