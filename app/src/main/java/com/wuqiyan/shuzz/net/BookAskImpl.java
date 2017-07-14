package com.wuqiyan.shuzz.net;

import com.wuqiyan.shuzz.comm.Constant;
import com.wuqiyan.shuzz.model.BookModel;
import com.wuqiyan.shuzz.model.DetailModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by wuqiyan on 17/7/3.
 */

public class BookAskImpl {


    private OnLoadBookListener onLoadBookListener;


    public void setOnLoadBookListener(OnLoadBookListener onLoadBookListener){
        this.onLoadBookListener = onLoadBookListener;
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .build();



    private int lastPage = 0;
    public void requestBookAskInfo(String kw, final int page){

        if (lastPage == page && page!=1){
            return;
        }
        lastPage = page;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.BOOKASK_BASEURL).client(client).build();
        BookAskApi bookAskApi = retrofit.create(BookAskApi.class);
        Call<ResponseBody> call = bookAskApi.getBookAskInfo(kw,page);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String html = response.body().string();
                    List<BookModel> list = parseBookInfos(html);
                    if (list != null){
                        onLoadBookListener.onSuccess(list);
                    }
                    //判断有没有下一页
                    int pagenext = page + 1;
                    if (html.contains("pn_" + pagenext)){
                        onLoadBookListener.onPageNext(true);
                    }
                    else {
                        onLoadBookListener.onPageNext(false);
                    }
                } catch (Exception e) {
                    onLoadBookListener.onFailure(e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onLoadBookListener.onFailure(t.getMessage());
            }
        });
    }

    public void requestDetailAskInfo(String url, final OnLoadDetailListener listener){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.BOOKASK_BASEURL).client(client).build();
        BookAskApi bookAskApi = retrofit.create(BookAskApi.class);
        Call<ResponseBody> call = bookAskApi.getDetailAskInfo(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String html = response.body().string();
                    listener.onDetail(parseDetailInfos(html));

                } catch (IOException e) {
                    listener.onFailure(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    private DetailModel parseDetailInfos(String responseBody){
        DetailModel detailModel = new DetailModel();
        try {
            Document document = Jsoup.parse(responseBody);
            Elements desc_ele = document.select("#descript p");
            if (!desc_ele.isEmpty()){
                detailModel.desc = desc_ele.html().replace("<br>","\n");
            }
            Elements catalog_ele = document.select("#catalog");
            if (!catalog_ele.isEmpty()){
                detailModel.catalog = catalog_ele.html().replace("<br>","\n");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return detailModel;
    }



    private List<BookModel> parseBookInfos(String responseBody){

        List<BookModel> models = new ArrayList<>();
        try{
            Document document = Jsoup.parse(responseBody);
            Elements eles_div = document.select("div.s-left > .am-g");
            if (eles_div.isEmpty()){
                return null;
            }
            for (Element div : eles_div){
                BookModel book = new BookModel();
                String bookName = delBookName(div.select(".s-tittle a").text());
                book.bookImgUrl = div.select(".big-cover img").attr("src").replace("!b","!m");
                book.bookName = delHTMLTag(bookName);
                book.contentUrl = div.select(".s-tittle a").attr("href");
                book.author = div.select(".s-tittle + div").text();
                book.desc = replaceBlank(div.select(".s-con").text());
                book.publishingHouse =  div.select("p:not(.s-con)").first().text();
                models.add(book);
            }
        }catch (Exception e){
            onLoadBookListener.onFailure(e.getMessage());
            e.printStackTrace();
        }
        return models;
    }

    public  String replaceBlank(String str) {

        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest.replace("简介：","");
    }
    public String delBookName(String name){
        if (name == null || name.equals("")){
            return name;
        }
        String str = name;
        Pattern p = Pattern.compile("《(.*?)\\|(.*?)\\|(.*?)》");
        Matcher m= p.matcher(name);
        if (m.find()){
            str = "《"+m.group(1)+"》";
        }
        String tempStr = replaceBlank(str.replace(": 英文","").replace("编委会编著.",""));
        return tempStr;
    }

    public  String delHTMLTag(String htmlStr){
        if (htmlStr == null || htmlStr.equals("")){
                return htmlStr;
        }
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

}
