package com.wuqiyan.shuzz.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wuqiyan on 17/6/21.
 */

public interface IturingApi {
    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
    @GET("tag/books/{type}")
    Call<ResponseBody> getIturingBook(@Path("type") int type,@Query("page") int page);

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
    @GET("tag/{type}")
    Call<ResponseBody> getIturingPages(@Path("type") int type);


    Call<ResponseBody> getIturingTags();
}
