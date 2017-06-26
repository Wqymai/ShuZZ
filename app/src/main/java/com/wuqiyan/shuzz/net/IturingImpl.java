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


    public void getIturingPages(final String type){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.ITURING_BASEURL).build();
        IturingApi ituringApi = retrofit.create(IturingApi.class);
        int num= getBookNumberInIturing(type);
        if (num == -1){
            return;
        }
        Call<ResponseBody> call = ituringApi.getIturingPages(num);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String pages = parseIturingPages(response.body().string());
                    new SPUtils(mContext,"conf").putString(type,pages);
                    onLoadPagesListener.onSuccess(Integer.parseInt(pages));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void getIturingBook(String type,int page){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.ITURING_BASEURL).build();
        IturingApi ituringApi = retrofit.create(IturingApi.class);
        int num = getBookNumberInIturing(type);
        if (num == -1){
            return;
        }
        Call<ResponseBody> call = ituringApi.getIturingBook(num,page);
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

    private int getBookNumberInIturing(String type){
        int num = -1;
        if (type == null){
            return num;
        }
        switch (type){
            case Constant.ANDROID:
                num = Constant.ITURING_ANDROID;
                break;
            case Constant.PYTHON:
                num = Constant.ITURING_PYTHON;
                break;
            case Constant.JAVASCRIPT:
                num = Constant.ITURING_JAVASCRIPT;
                break;
            case Constant.HTML5:
                num = Constant.ITURING_HTML5;
                break;
            case Constant.LINUX:
                num = Constant.ITURING_LINUX;
                break;
            case Constant.CSHARP:
                num = Constant.ITURING_CSHARP;
                break;
            case Constant.IOS:
                num = Constant.ITURING_IOS;
                break;
            case Constant.JQUERY:
                num = Constant.ITURING_JQUERY;
                break;
            case Constant.DB:
                num = Constant.ITURING_DB;
                break;
            case Constant.MACHINELEARN:
                num = Constant.ITURING_MACHINELEARN;
                break;
            default:
                break;
        }
        return num;
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
        String pages="0";
        Document doc_content = Jsoup.parse(responseBody);
        Elements ul_eles=doc_content.select(".block-books .PagedList-pager ul");
        if (!ul_eles.isEmpty()){
            pages=ul_eles.first().select("li").last().select("a").text();
        }
        return pages;
    }
}
