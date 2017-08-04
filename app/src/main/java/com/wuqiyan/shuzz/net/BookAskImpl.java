package com.wuqiyan.shuzz.net;

import android.util.Log;

import com.wuqiyan.shuzz.comm.Constant;
import com.wuqiyan.shuzz.dao.TagsDao;
import com.wuqiyan.shuzz.model.BookModel;
import com.wuqiyan.shuzz.model.DetailModel;
import com.wuqiyan.shuzz.model.TagModel;

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



//    private int lastPage = 0;
    public void requestBookAskInfo(String kw, final int page){

//        if (lastPage == page && page!=1){
//            return;
//        }
//        lastPage = page;
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
                    else {
                        onLoadBookListener.onFailure(Constant.LIST_NULL);
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

    public void requestMookTagInfos(){
        addDefaultTags();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.imooc.com/").client(client).build();
        BookAskApi bookAskApi = retrofit.create(BookAskApi.class);
        Call<ResponseBody> call = bookAskApi.getMoocTagInfo();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    List<TagModel> tagList = parseMoocTags(response.body().string());
                    TagsDao.insertTags(tagList);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public List<TagModel> parseMoocTags(String responseBody){
        if (responseBody == null || responseBody.equals("") || responseBody==""){
            return null;
        }
        List<TagModel> list = new ArrayList<>();
        try {
            Document doc_content = Jsoup.parse(responseBody);
            Elements elements = doc_content.select(".course-nav-row");
            if (!elements.isEmpty()){
                Document document = Jsoup.parse(elements.get(1).toString());
                Elements elements1 = document.select(".course-nav-item:not(.on)");
                if (!elements1.isEmpty()){
                    for (Element e: elements1) {
                        Elements e1 = e.select("li a");
                        TagModel tagModel = new TagModel();
                        tagModel.setTagId(Long.parseLong(e1.attr("data-id")));
                        tagModel.setTagName(e1.text().toLowerCase());
                        tagModel.setTagState(1);
                        tagModel.setTp(0l);
                        if (!checkTags(tagModel)){
                            list.add(tagModel);
                        }
                    }
                }
            }

        }catch (Exception e){
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    public void requestJikeTagInfos(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.JIKE_BASEURL).client(client).build();
        BookAskApi bookAskApi = retrofit.create(BookAskApi.class);
        Call<ResponseBody> call = bookAskApi.getJikeTagInfo("");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String html = response.body().string();
                    parseJikeTags(html);

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private List<TagModel> parseJikeTags(String responseBody){
        List<TagModel> list=new ArrayList<>();
        try {
            Document doc_content = Jsoup.parse(responseBody);
            Elements eles = doc_content.select("dd.cf");
            if (!eles.isEmpty()){
                for (Element  element : eles){
                    Elements eles_a = element.select("a");
                    if (!eles_a.isEmpty()){
                        for (Element e : eles_a){
                            String name = e.text().toLowerCase();

                            Log.i("wqy",name);
                            TagModel tagModel = new TagModel();
                            tagModel.setTagId(Long.parseLong(e.attr("cgid")));
                            tagModel.setTagName(name);
                            tagModel.setTagState(1);
                            tagModel.setTp(0l);
                            list.add(tagModel);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
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
            Elements recommend_ele = document.select("#edt_view p");
            if (!recommend_ele.isEmpty()){
                detailModel.recommend = recommend_ele.html().replace("<br>","\n");
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


    private void addDefaultTags(){
        List<TagModel> tagsList=new ArrayList<>();
        long currTime = System.currentTimeMillis();
        tagsList.add(new TagModel((long) 220,"java",0,currTime));
        tagsList.add(new TagModel((long) 1362,"c#",0,currTime - 1000));
        tagsList.add(new TagModel((long) 1118,"python",0,currTime - 2000));
        tagsList.add(new TagModel((long) 1,"php",0,currTime - 3000));
        tagsList.add(new TagModel((long) 223,"android",0,currTime - 4000));
        tagsList.add(new TagModel((long) 955,"ios",0,currTime - 5000));
        tagsList.add(new TagModel((long) 1331,"c++",0,currTime - 6000));
        tagsList.add(new TagModel((long) 952,"mysql",0,currTime - 7000));
        TagsDao.insertTags(tagsList);
    }
    private boolean checkTags(TagModel tag){
        boolean alr = false;
        if (tag.getTagId().equals((long) 220)) {
            alr = true;

        } else if (tag.getTagId().equals((long) 1362)) {
            alr = true;

        } else if (tag.getTagId().equals((long) 1118)) {
            alr = true;

        } else if (tag.getTagId().equals((long) 1)) {
            alr = true;

        } else if (tag.getTagId().equals((long) 223)) {
            alr = true;

        } else if (tag.getTagId().equals((long) 955)) {
            alr = true;

        } else if (tag.getTagId().equals((long) 1331)) {
            alr = true;

        } else if (tag.getTagId().equals((long) 952)) {
            alr = true;

        }
        return alr;
    }

}
