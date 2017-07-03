package com.wuqiyan.shuzz.net;

import android.util.Log;

import com.wuqiyan.shuzz.comm.Constant;
import com.wuqiyan.shuzz.model.BookModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



    private int lastPage = 0;
    public void requestBookAskInfo(String kw, final int page){
        if (lastPage == page){
            return;
        }
        lastPage = page;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.BOOKASK_BASEURL).build();
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
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onLoadBookListener.onFailure(t.getMessage());
            }
        });
    }

    private List<BookModel> parseBookInfos(String responseBody){
        Log.i("wxl", "response=" + responseBody);
        List<BookModel> models = new ArrayList<>();
        try{
            Document document = Jsoup.parse(responseBody);
            Elements eles_div = document.select("div.s-left > .am-g");
            if (eles_div.isEmpty()){
                return null;
            }
            for (Element div : eles_div){
                BookModel book = new BookModel();
                String bookName = div.select(".s-tittle a").text();
                book.bookImgUrl = div.select(".big-cover img").attr("src").replace("!b","!m");
                book.bookName = delHTMLTag(bookName);
                book.contentUrl = div.select(".s-tittle a").attr("href");
                book.author = div.select(".s-tittle + div").text();
                book.desc = div.select(".s-con").text();
                book.publishingHouse =div.select("p:not(.s-con)").first().text() ;
                models.add(book);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return models;
    }

    //([\s\S]*?)[\|(.*?)\|(.*?)》]
    private Map<String,String> matchInfo(String str){
        Pattern p;
        if (str.contains("|")){
            p = Pattern.compile("([\\s\\S]*?)\\|(.*?)\\|(.*?)》");
        }
        else {
            p = Pattern.compile("([\\s\\S]*?)》");
        }


        Map<String,String> map=new HashMap<>();
        try {

            Matcher m = p.matcher(str);
            if (m.find()){
                map.put("name",m.group(1));
                map.put("author",m.group(2));
                map.put("publish",m.group(3));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    public  String delHTMLTag(String htmlStr){
        if (htmlStr ==null || htmlStr.equals("")){
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
