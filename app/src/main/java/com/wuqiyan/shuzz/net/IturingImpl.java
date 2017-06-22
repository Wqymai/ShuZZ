package com.wuqiyan.shuzz.net;

import android.content.Context;

import com.wuqiyan.shuzz.comm.Constant;
import com.wuqiyan.shuzz.comm.SPUtils;
import com.wuqiyan.shuzz.model.BookModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by wuqiyan on 17/6/21.
 */

public class IturingImpl{


    private OnLoadBookListener onLoadBookListener;
    private OnLoadPagesListener onLoadPagesListener;

    public void setOnLoadBookListener(OnLoadBookListener onLoadBookListener){
        this.onLoadBookListener = onLoadBookListener;
    }
    public void setOnLoadPagesListener(OnLoadPagesListener onLoadPagesListener){
        this.onLoadPagesListener=onLoadPagesListener;
    }
    private Context mContext;
    public IturingImpl(Context context){
        this.mContext=context;
    }


    public void getAndroidPages(int type){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.ITURING_BASEURL).build();
        IturingApi ituringApi = retrofit.create(IturingApi.class);
        Call<ResponseBody> call = ituringApi.getIturingPages(type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String pages = parseIturingPages(response.body().string());
                    new SPUtils(mContext,"conf").putString("android",pages);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void getAndroid_Ituring(int page){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.ITURING_BASEURL).build();
        IturingApi ituringApi = retrofit.create(IturingApi.class);
        Call<ResponseBody> call = ituringApi.getIturingBook(Constant.ITURING_ANDROID,page);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    List<BookModel> bookList = parseIturingBook(response.body().string());
                    onLoadBookListener.onSuccess(bookList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    onLoadBookListener.onFailure(throwable.getMessage());
            }
        });

    }
    private List<BookModel> parseIturingBook(String responseBody){
          List<BookModel> models = new ArrayList<>();
          try {
              Document doc_content = Jsoup.parse(responseBody);
              Elements element_lis = doc_content.select("li.block-item");
              for (Element li : element_lis){
                  BookModel bookModel=new BookModel();
                  Document doc_li = Jsoup.parse(li.toString());

                  bookModel.bookImgUrl = doc_li.select("img").attr("src");

                  bookModel.bookName = doc_li.select("h4 a").text();

                  Elements author_elements = doc_li.select(".author span");

                  if (author_elements.isEmpty()){
                      bookModel.author="";
                  }
                  else {
                     bookModel.author = author_elements.first().text();
                  }

                  Elements desc_elements=doc_li.select("p.intro");
                  if (desc_elements.isEmpty()){

                      bookModel.desc="";

                  }else {
                      bookModel.desc = desc_elements.text();
                  }

                  models.add(bookModel);
              }
          }catch (Exception e){
              e.printStackTrace();
          }
          return models;
    }

    private String parseIturingPages(String responseBody){
        Document doc_content = Jsoup.parse(responseBody);
        return doc_content.select(".PagedList-pager ul").first().select("li").last().select("a").text();
    }
}
