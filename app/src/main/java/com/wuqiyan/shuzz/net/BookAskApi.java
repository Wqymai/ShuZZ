package com.wuqiyan.shuzz.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by wuqiyan on 17/7/3.
 */

public interface BookAskApi {

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
    @GET("s/kw_{keyword}/t_1/pn_{pagenumber}.html")
    Call<ResponseBody> getBookAskInfo(@Path("keyword") String keyword,@Path("pagenumber") int pagenumber);
}
