package com.zyascend.JLUZone.model.net;

import com.zyascend.JLUZone.base.BaseCallBack;
import com.zyascend.JLUZone.entity.MainImage;
import com.zyascend.JLUZone.entity.News;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/8/4.
 */
public interface JsoupListener {

    void getJwNews(NewsCallBack newsCallBack,int page);
    void getSchoolNews(NewsCallBack newsCallBack,int page);
    void getJwContent(String url ,ContentCallback callback);
    void getXiaoContent(String url ,ContentCallback callback);
    void getMainImages(ImageCallback callback);
    interface ImageCallback extends BaseCallBack<List<MainImage>>{}
    interface NewsCallBack extends BaseCallBack<List<News>>{}
    interface ContentCallback extends BaseCallBack<String>{}
}
