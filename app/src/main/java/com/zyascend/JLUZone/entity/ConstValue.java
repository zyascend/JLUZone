package com.zyascend.JLUZone.entity;

/**
 *
 * Created by Administrator on 2016/7/6.
 */
public class ConstValue {

    public static final String KEY_STU_INFO = "key_stu_info";
    public static final String SAVED_NUM = "key_num";
    public static final String SAVED_NAME = "key_name";
    public static final String SAVED_STUID = "key_id";
    public static final String SAVED_PASSWORD = "key_passWord";
    public static final String IS_AUTO_LOGIN = "key_isAutoLogin";

    public static final String IS_SAVE_PWD = "key_isSavePassWord";

    public static final String IS_LOGIN_OUTSIDE = "key_isLoginOutside";
    //校外登录
    public static final String LOGIN_OUT_URL = "http://cjcx.jlu.edu.cn/score/action/security_check.php";
    //校内登录
    public static final String LOGIN_IN_URL = "http://uims.jlu.edu.cn/ntms/j_spring_security_check";
    //获取当前登录用户信息
    public static final String CURRENT_INFO = "http://uims.jlu.edu.cn/ntms/action/getCurrentUserInfo.do";
    //获取学期列表
    //登陆后查成绩
    public static final String SEMES_SCORE_URL = "http://uims.jlu.edu.cn/ntms/service/res.do";
    public static final String SCORE_DETAIL_URL = "http://uims.jlu.edu.cn/ntms/score/course-score-stat.do";

    public static final String CURRENT_WEEK = "current_week";
    public static final int SCORE_TYPE_NEW = 0;
    public static final int SCORE_TYPE_ALL = 1;
    public static final int SCORE_TYPE_YEAR = 2;

    public static final String JWC_URL = "http://oldjwc.jlu.edu.cn/?file=info&act=list&id=28&page=";
    public static final String XIAO_URL = "http://oa.jlu.edu.cn/list.asp?s=1&page=";
    public static final String URL_JOB_CONTENT = "http://jdjyw.jlu.edu.cn/index.php?r=app/recruit/details&id=";

    public static final String TAG_XIAOZHAO = "xiaozhao";
    public static final String TAG_SHIXI = "shixi";

    public static final String URL_XIAOZHAO = "http://jdjyw.jlu.edu.cn/index.php?r=app/recruit&type=1&page=";
    public static final String URL_SHIXI = "http://jdjyw.jlu.edu.cn/index.php?r=app/recruit&type=3&page=";
    public static final String URL_MAIN_IMAGE = "http://www.jlu.edu.cn/";
    public static final String URL_HOST_JWC = "http://oldjwc.jlu.edu.cn";
    public static final String URL_HOST_XIAO = "http://oa.jlu.edu.cn/";
}
