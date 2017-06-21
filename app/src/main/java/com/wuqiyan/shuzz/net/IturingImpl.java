package com.wuqiyan.shuzz.net;

import com.wuqiyan.shuzz.comm.Constant;
import com.wuqiyan.shuzz.model.IturingBookModel;

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


    public void getAndroidPages(int type){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.ITURING_BASEURL).build();
        IturingApi ituringApi = retrofit.create(IturingApi.class);
        Call<ResponseBody> call = ituringApi.getIturingPages(type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(parseIturingPages(response.body().string()));
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
                    List<IturingBookModel> bookList = parseIturingBook(response.body().string());
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
    private List<IturingBookModel> parseIturingBook(String responseBody){
          List<IturingBookModel> models = new ArrayList<>();
          try {
              Document doc_content = Jsoup.parse(responseBody);
              Elements element_lis = doc_content.select("li.block-item");
              for (Element li : element_lis){
                  IturingBookModel bookModel=new IturingBookModel();
                  Document doc_li = Jsoup.parse(li.toString());
                  bookModel.bookImgUrl= doc_li.select("img").attr("src");
                  bookModel.bookName = doc_li.select("h4 a").text();
                  bookModel.author = doc_li.select(".author span").first().text();
                  bookModel.desc = doc_li.select("p.intro").text();
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
