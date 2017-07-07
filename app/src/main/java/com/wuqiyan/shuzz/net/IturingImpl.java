package com.wuqiyan.shuzz.net;

import android.content.Context;

import com.wuqiyan.shuzz.comm.Constant;
import com.wuqiyan.shuzz.comm.SPUtils;
import com.wuqiyan.shuzz.dao.TagsDao;
import com.wuqiyan.shuzz.model.BookModel;
import com.wuqiyan.shuzz.model.TagModel;

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

import static com.wuqiyan.shuzz.comm.Constant.ITURING_ANDROID;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_CSHARP;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_DB;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_HTML5;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_IOS;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_JAVASCRIPT;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_JQUERY;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_LINUX;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_MACHINELEARN;
import static com.wuqiyan.shuzz.comm.Constant.ITURING_PYTHON;

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
                    System.out.println(response.body().string());
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

    public void getIturingTags(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.ITURING_BASEURL).build();
        IturingApi ituringApi = retrofit.create(IturingApi.class);
        Call<ResponseBody> call = ituringApi.getIturingTags();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    List<TagModel> tagList = parseIturingTags(response.body().string());
                    TagsDao.insertTags(tagList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }






    private List<TagModel> parseIturingTags(String responseBody){
        List<TagModel> list=new ArrayList<>();
        try {
            Document doc_content = Jsoup.parse(responseBody);
            Elements ele = doc_content.select(".col-md-3 .block:not(.hot-tags) .tags a");
            if (!ele.isEmpty()){
                    TagModel tagModel;
                    for (Element e :ele){
                        tagModel=new TagModel();
                        tagModel.setTagId(Long.parseLong(e.attr("tagid")));
                        tagModel.setTagName(e.text().toLowerCase());
                        tagModel.setTagState(1);
                        tagModel.setTp(0l);
                        if (!checkTags(tagModel)){
                            list.add(tagModel);
                        }

                    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //添加默认显示的
        return addDefaultTags(list);
    }

    private List<TagModel> addDefaultTags(List<TagModel> tagsList){
        long currTime = System.currentTimeMillis();
        tagsList.add(new TagModel((long) 49,"android",0,currTime));
        tagsList.add(new TagModel((long) 11,"python",0,currTime - 1000));
        tagsList.add(new TagModel((long) 6,"javascript",0,currTime - 2000));
        tagsList.add(new TagModel((long) 238,"html5",0,currTime - 3000));
        tagsList.add(new TagModel((long) 25,"linux",0,currTime - 4000));
        tagsList.add(new TagModel((long) 35,"c#",0,currTime - 5000));
        tagsList.add(new TagModel((long) 367,"ios",0,currTime - 6000));
        tagsList.add(new TagModel((long) 283,"jquery",0,currTime - 7000));
//        tagsList.add(new TagModel((long) 90,"数据库",0,currTime - 8000));
//        tagsList.add(new TagModel((long) 69,"机器学习",0,currTime - 9000));
        return tagsList;
    }
    private boolean checkTags(TagModel tag){
        boolean alr = false;
        if (tag.getTagId().equals((long) ITURING_ANDROID)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_PYTHON)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_JAVASCRIPT)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_HTML5)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_LINUX)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_CSHARP)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_IOS)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_JQUERY)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_DB)) {
            alr = true;

        } else if (tag.getTagId().equals((long) ITURING_MACHINELEARN)) {
            alr = true;

        }
        return alr;
    }

    private int getBookNumberInIturing(String type){
        int num = -1;
        if (type == null){
            return num;
        }
        switch (type){
            case Constant.ANDROID:
                num = ITURING_ANDROID;
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
