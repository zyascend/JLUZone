package com.zyascend.JLUZone.model.net;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.zyascend.JLUZone.entity.ConstValue;
import com.zyascend.JLUZone.entity.MainImage;
import com.zyascend.JLUZone.entity.News;
import com.zyascend.JLUZone.news.NewsPresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/8/4.
 */
public class JsoupUtils implements JsoupListener {

    private static JsoupUtils INSTANCE;
    private String mTag;
    private static final String TAG = "TAG_JsoupUtils";
    private NewsCallBack mNewsListCallback;
    private ContentCallback mContentCallback;

    private JsoupUtils(){
    }

    public static JsoupUtils getInstance(){
        if (INSTANCE == null) {
            synchronized (JsoupUtils.class){
                if (INSTANCE == null) {
                    INSTANCE = new JsoupUtils();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getJwNews(NewsCallBack newsCallBack,int page) {
        Log.d(TAG, "getJwNews: ");
        mTag = NewsPresenter.TAG_JWC;
        mNewsListCallback = newsCallBack;
        new NewsListTask().execute(ConstValue.JWC_URL+page);
    }

    @Override
    public void getSchoolNews(NewsCallBack newsCallBack,int page) {
        mTag = NewsPresenter.TAG_XIAO;
        mNewsListCallback = newsCallBack;
        new NewsListTask().execute(ConstValue.XIAO_URL + page);
    }

    @Override
    public void getJwContent(String url, ContentCallback callback) {
        mTag = NewsPresenter.TAG_JWC_CONTENT;
        mContentCallback = callback;
        new NewsContentTask().execute(url);
    }

    @Override
    public void getXiaoContent(String url, ContentCallback callback) {
        mTag = NewsPresenter.TAG_XIAO_CONTENT;
        mContentCallback = callback;
        new NewsContentTask().execute(url);
    }

    @Override
    public void getMainImages(ImageCallback callback) {
        ImageCallback mImageCallback = callback;
        Log.d(TAG, "getMainImages: ");
    }


    private class NewsListTask extends AsyncTask<String ,Void,List<News>>{

        @Override
        protected List<News> doInBackground(String... params) {
            Log.d(TAG, "doInBackground: ");
            List<News> newses = null;
            if (TextUtils.equals(mTag, NewsPresenter.TAG_JWC)){
                newses = loadJwcNews(params[0]);
            }else if(TextUtils.equals(mTag,NewsPresenter.TAG_XIAO)){
                newses = loadXiaoNews(params[0]);
            }

            return newses;
        }

        private List<News> loadXiaoNews(String param) {

            Log.d(TAG, "loadJwcNews: hostUrl = "+param);
            List<News> list = new ArrayList<>();
            String title = "";
            String date = "";
            String editor = "";
            String url = "";
            long id = 0;
            try{
                Document document = Jsoup.connect(param).get();
                Element div = document.getElementById("listContent");
                Element table = div.getElementsByTag("table").first();
                Elements tr = table.getElementsByTag("tbody").first().getElementsByTag("tr");
                Log.d(TAG, "tr =  "+tr.size());
                for (Element e : tr){

                    Elements td = e.getElementsByTag("td");
                    title = td.get(0).getElementsByTag("a").attr("title");
                    url = td.get(0).getElementsByTag("a").attr("href");
                    date = td.get(2).text();
                    editor = td.get(1).getElementsByTag("a").text();
                    id = Long.parseLong(url.split("/")[2].replace("show","").replace(".html",""));


                    url = "http://oa.jlu.edu.cn/"+url;
                    News news = new News(id,title,editor,date,"",url);
                    list.add(news);

//                    Log.d(TAG, "id = "+id);
//                    Log.d(TAG, "title = "+title);
//                    Log.d(TAG, "url = "+url);
//                    Log.d(TAG, "date = "+date);
//                    Log.d(TAG, "editor = "+editor);

                }

            }catch (Exception e){
                Log.d(TAG, "error  = " + e.toString());
                onGetListError(e);
            }

            return list;
        }

        private List<News> loadJwcNews(String param) {

            Log.d(TAG, "loadJwcNews: hostUrl = "+param);
            List<News> list = new ArrayList<>();
            String title = "";
            String date = "";
            String editor = "";
            String url = "";
            long id = 0;

            try {

                Document document = Jsoup.connect(param).get();
                Elements content = document.getElementsByAttributeValue("id","content");
                Elements ul= content.first().getElementsByTag("ul");
                Elements li = ul.first().getElementsByTag("li");
                for (Element e : li){
                    date = e.getElementsByClass("right").text();
                    title = e.getElementsByTag("a").first().removeClass("new").text();
                    url = e.getElementsByTag("a").first().attr("href");
                    editor = e.getElementsByClass("dep").get(0).text();

                    //处理数据
                    id = Long.parseLong(url.split("=")[3]);
                    title = title.replace("new","");
                    url = "http://oldjwc.jlu.edu.cn/"+url;
                    editor = editor.replace("(","").replace(")","");
                    //处理date

//                    date = date.replaceAll("\\s","");
                    News news = new News(id,title,editor,date,"",url);
                    list.add(news);

                }
            }catch (Exception e){
                onGetListError(e);
            }
            Log.d(TAG, "loadJwcNews: "+ list.size());
            return list;
        }
        @Override
        protected void onPostExecute(List<News> newses) {
            super.onPostExecute(newses);

            if (mNewsListCallback != null){
                mNewsListCallback.onSuccess(newses);
            }


        }
    }
    private class NewsContentTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String conent = null;
            if (TextUtils.equals(mTag,NewsPresenter.TAG_XIAO_CONTENT)){
                conent = loadXiaoContent(params[0]);
            }else if (TextUtils.equals(mTag,NewsPresenter.TAG_JWC_CONTENT)){
                conent = loadJWcContent(params[0]);
            }
            return conent;
        }

        private String loadJWcContent(String param) {
            String content = "";
            try {
                Document document = Jsoup.connect(param).get();
                Elements elements = document.getElementsByClass("content");
                content = elements.toString();
                Log.d(TAG, "getContent: "+elements.toString());
            }catch (Exception e){
                onGetContentError(e);
            }
            return content;
        }

        private String loadXiaoContent(String url) {
            String content = "";
            try {
                Document document = Jsoup.connect(url).get();
                Element element = document.getElementById("showContent");
                content = element.toString();
                Log.d(TAG, "Content: "+content);
            }catch (Exception e){
                onGetContentError(e);
            }
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute: before+ " + s);
            super.onPostExecute(s);

            if (mContentCallback != null){
                mContentCallback.onSuccess(s);
                Log.d(TAG, "onPostExecute: "+s);
            }
        }
    }

    private void onGetListError(final Exception e) {

       new Handler(Looper.getMainLooper()).post(new Runnable() {
           @Override
           public void run() {
               if (mNewsListCallback != null ){
                   mNewsListCallback.onFailure(e);
               }
           }
       });
    }

    private void onGetContentError(final Exception e){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mContentCallback != null ){
                    mContentCallback.onFailure(e);
                }
            }
        });
    }
}
