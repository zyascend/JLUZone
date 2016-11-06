package com.zyascend.JLUZone.model.net;

import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import com.zyascend.JLUZone.base.BaseCallBack;
import com.zyascend.JLUZone.entity.AvgScore;
import com.zyascend.JLUZone.entity.ConstValue;
import com.zyascend.JLUZone.entity.Course;

import com.zyascend.JLUZone.entity.CourseResult;
import com.zyascend.JLUZone.entity.Job;
import com.zyascend.JLUZone.entity.JobContent;
import com.zyascend.JLUZone.entity.JobListResult;
import com.zyascend.JLUZone.entity.MyRequestBody;
import com.zyascend.JLUZone.entity.MyResponseBody;
import com.zyascend.JLUZone.entity.RequestParams;
import com.zyascend.JLUZone.entity.Score;
import com.zyascend.JLUZone.entity.ScoreDetail;
import com.zyascend.JLUZone.entity.ScoreValue;
import com.zyascend.JLUZone.entity.StuInfo;
import com.zyascend.JLUZone.entity.Term;
import com.zyascend.JLUZone.entity.Weather;
import com.zyascend.JLUZone.model.data.DataUtils;
import com.zyascend.JLUZone.utils.ActivityUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Looper.getMainLooper;

/**
 *
 * Created by Administrator on 2016/7/6.
 */
public class HttpUtils implements HttpUtilsListener {

    private static final String TAG = "TAG_HTTPUTILS";
    public static final MediaType MYJSON=MediaType.parse("application/json; charset=utf-8");
    private static final int TYPE_LOGIN = 0;
    private static final int TYPE_STU_INFO = 1;
    private static final int TYPE_TERM = 2;
    private static final int TYPE_SCORE = 3;
    private static final int TYPE_SCHEDULE = 4;
    private static final int TYPE_JOB_LIST = 5;
    private static final int TYPE_JOB_CONTENT = 6;
    private static final int TYPE_AVG_SCORE = 7;
    private static final int TYPE_SCORE_DETAIL = 8;
    private static final int TYPE_WEATHER = 9;

    private static HttpUtils INSTANCE;
    private static Handler mHandler;
    private OkHttpClient mHttpClient;
    public static int mStudId;
    public static String mStudName;
    private int mTermId;
    private LoginCallBack mLoginCallBack;

    private HttpUtils(){

        mHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
            //Tip：这里的key必须是String HttpUrl不行
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();

        mHandler = new Handler(getMainLooper());

    }

    public static HttpUtils getInstance(){
        if (INSTANCE == null){
            synchronized (HttpUtils.class){
                if (INSTANCE == null){
                    INSTANCE = new HttpUtils();
                }
            }
        }
        return INSTANCE;
    }

    public OkHttpClient getHttpClient(){
        return mHttpClient;
    }

    @Override
    public void cancel() {
        mHttpClient.dispatcher().cancelAll();
        Log.d(TAG, "cancel: 取消所有请求");
    }

    @Override
    public void login(boolean isLoginOutside, String user, String passWord,
                      LoginCallBack loginCallBack) {
        Log.d(TAG, "login: called");
        String url = isLoginOutside ? ConstValue.LOGIN_OUT_URL : ConstValue.LOGIN_IN_URL;
        String md5PassWord = getMD5Str("UIMS" + user + passWord);
        mLoginCallBack = loginCallBack;

        RequestBody requestBody = new FormBody.Builder()
                .add("j_username", user)
                .add("j_password",md5PassWord)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(TYPE_LOGIN));
    }

    @Override
    public void getCurrentInfo() {
        Log.d(TAG, "getCurrentInfo: ");
        Request request = new Request.Builder()
                .url(ConstValue.CURRENT_INFO)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(TYPE_STU_INFO));
    }

    @Override
    public void getSemester(TermCallBack callback){
        Log.d(TAG, "getSemester: ");
        String requestString = "{\"tag\":\"search@teachingTerm\",\"branch\":\"default\",\"params\":{}}";
        RequestBody requestBody = RequestBody.create(MYJSON,requestString);
        Request request = new Request.Builder()
                .url(ConstValue.SEMES_SCORE_URL)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callback,TYPE_TERM));


    }

    @Override
    public void getNewScore(int row,ScoreCallBack scoreCallBack){
        Log.d(TAG, "getNewScore: ");
        MyRequestBody body = new MyRequestBody();
        RequestParams params = new RequestParams();
        //{"tag":"archiveScore@queryCourseScore","branch":"latest","params":{},"rowLimit":15}
        body.setParams(params);
        body.setBranch("latest");
        body.setRowLimit(row);
        body.setTag("archiveScore@queryCourseScore");
        enqueue(body,scoreCallBack);
    }

    @Override
    public void getAllScore(ScoreCallBack callBack) {
        Log.d(TAG, "getAllScore: ");

        String requestString = "{\"type\":\"search\",\"tag\":\"archiveScore@queryCourseScore\",\"branch\":\"byYear\",\"params\":{\"studId\":"
                +mStudId
                +"},\"orderBy\":\"teachingTerm.termId, course.courName\"}";

        RequestBody requestBody = RequestBody.create(MYJSON,requestString);
        Request request = new Request.Builder()
                .url(ConstValue.SEMES_SCORE_URL)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callBack,TYPE_SCORE));


//        MyRequestBody body = new MyRequestBody();
//        RequestParams spec = new RequestParams();
//        spec.setStudId(mStudId);
//        body.setParams(spec);
//        body.setBranch("byYear");
//        body.setOrderBy("teachingTerm.termId, course.courName");
//        body.setTag("archiveScore@queryCourseScore");
//        body.setType("search");
//        enqueue(body,callBack);
    }

    @Override
    public void getYearScore(int year, ScoreCallBack callBack) {
        //{"type":"search","tag":"archiveScore@queryCourseScore",
        // "branch":"byYear","params":{"studId":238615,"year":"2015"},"orderBy":"teachingTerm.termId, course.courName"}
        Log.d(TAG, "getYearScore: ");

        String requestString = "{\"type\":\"search\",\"tag\":\"archiveScore@queryCourseScore\",\"branch\":\"byYear\",\"params\":{\"studId\":"+mStudId
                +",\"year\":\""+year+"\"},\"orderBy\":\"teachingTerm.termId, course.courName\"}";

        RequestBody requestBody = RequestBody.create(MYJSON,requestString);
        Request request = new Request.Builder()
                .url(ConstValue.SEMES_SCORE_URL)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callBack,TYPE_SCORE));

//        MyRequestBody body = new MyRequestBody();
//        RequestParams spec = new RequestParams();
//        spec.setStudId(mStudId);
//        spec.setYear(year);
//        body.setParams(spec);
//        body.setBranch("byYear");
//        body.setOrderBy("teachingTerm.termId, course.courName");
//        body.setTag("archiveScore@queryCourseScore");
//        body.setType("search");
//        enqueue(body,callBack);
    }

    @Override
    public void getAvgScore(AvgScoreCallback callback) {
        String requestString = "{\"type\":\"query\",\"res\":\"stat-avg-gpoint\",\"params\":{\"studId\":"+mStudId+"}}";
        RequestBody requestBody = RequestBody.create(MYJSON,requestString);
        Request request = new Request.Builder()
                .url(ConstValue.SEMES_SCORE_URL)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callback,TYPE_AVG_SCORE));
    }

    @Override
    public void getScoreDetail(String asId, ScoreDetailCallback callback) {
        String requestString = "{\"asId\":\""+asId+"\"}";
        RequestBody requestBody = RequestBody.create(MYJSON,requestString);
        Request request = new Request.Builder()
                .url(ConstValue.SCORE_DETAIL_URL)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callback,TYPE_SCORE_DETAIL));
    }

    @Override
    public void getXiaoZhaoList(int page, JobListCallback callback) {
        Request request = new Request.Builder()
                .url(ConstValue.URL_XIAOZHAO+page)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callback, TYPE_JOB_LIST));

    }

    @Override
    public void getShixiList(int page, JobListCallback callback) {
        Request request = new Request.Builder()
                .url(ConstValue.URL_SHIXI+page)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callback, TYPE_JOB_LIST));

    }

    @Override
    public void getJobContent(int id,JobContentCallback callback) {

        Request request = new Request.Builder()
                .url(ConstValue.URL_JOB_CONTENT + id)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callback, TYPE_JOB_CONTENT));
    }

    @Override
    public void getWeather(final WeatherCallback callback) {
        String httpUrl = "http://wthrcdn.etouch.cn/weather_mini?citykey=101060101";
        Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callback,TYPE_WEATHER));
    }

    private void enqueue(MyRequestBody body,ScoreCallBack callBack){

        RequestBody requestBody = RequestBody.create(MYJSON,JSON.toJSONString(body));
        Request request = new Request.Builder()
                .url(ConstValue.SEMES_SCORE_URL)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callBack,TYPE_SCORE));
        Log.d(TAG, "enqueue: called");
    }

    @Override
    public void getSchedule(int termId, ScheduleCallBack callBack) {
        mTermId = termId;
        String requestString = "{\"tag\":\"teachClassStud@schedule\",\"branch\":\"default\",\"params\":{\"termId\":"+termId+",\"studId\":"+mStudId+"}}";
        RequestBody requestBody = RequestBody.create(MYJSON,requestString);
        Request request = new Request.Builder()
                .url(ConstValue.SEMES_SCORE_URL)
                .post(requestBody)
                .build();
        mHttpClient.newCall(request).enqueue(new MyOKHttpCallback(callBack,TYPE_SCHEDULE));

    }

    private List<Score> mapScore(List<ScoreValue> scoreValues) {


        List<Score> scores = new ArrayList<Score>();
        if (ActivityUtils.NotNullOrEmpty(scoreValues)){

            for (ScoreValue scoreValue : scoreValues) {
                Score item = new Score();

                item.setCourseId(Long.parseLong(scoreValue.getCourse().getCourseId()));
                item.setScore(scoreValue.getScore());
                item.setCredit(scoreValue.getCredit());
                item.setGpoint(scoreValue.getGpoint());
                item.setIsPass(scoreValue.getIsPass());
                item.setName(scoreValue.getCourse().getCourName());
                item.setAsId(Integer.parseInt(scoreValue.getAsId()));
                item.setTermId(Integer.parseInt(scoreValue.getTeachingTerm().getTermId()));
                item.setTermName(scoreValue.getTeachingTerm().getTermName());
                item.setStuId(Integer.parseInt(scoreValue.getStudent().getStudId()));
                item.setStuName(scoreValue.getStudent().getName());

                scores.add(item);
            }
        }
        return scores;

    }

    public String getMD5Str(String str)  {

        StringBuilder md5StrBuff = new StringBuilder();
        try {
            MessageDigest messageDigest = null;

            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));

            byte[] byteArray = messageDigest.digest();


            for (byte aByteArray : byteArray) {
                if (Integer.toHexString(0xFF & aByteArray).length() == 1)
                    md5StrBuff.append("0").append(
                            Integer.toHexString(0xFF & aByteArray));
                else
                    md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    private class MyOKHttpCallback implements Callback{

        private WeatherCallback weatherCallback= null;
        private ScoreDetailCallback detScoreCallback= null;
        private AvgScoreCallback avgCallback= null;
        private JobContentCallback jobContentCallback= null;
        private JobListCallback jobCallback= null;
        private ScheduleCallBack scheduleCallback= null;
        private int type;
        private TermCallBack termCallBack = null;
        private ScoreCallBack scoreCallBack = null;


        MyOKHttpCallback(int typeStuInfo) {
            this.type = typeStuInfo;
        }

        MyOKHttpCallback(TermCallBack callback, int typeTerm) {
            this.type = typeTerm;
            this.termCallBack = callback;
        }

        MyOKHttpCallback(ScoreCallBack callback, int type) {
            this.type = type;
            this.scoreCallBack = callback;
        }

        MyOKHttpCallback(ScheduleCallBack callBack, int typeSchedule) {
            this.type = typeSchedule;
            this.scheduleCallback = callBack;
        }

        MyOKHttpCallback(JobListCallback callback, int type) {
            this.type = type;
            this.jobCallback = callback;
        }

        MyOKHttpCallback(JobContentCallback callback, int type) {
            this.type = type;
            this.jobContentCallback = callback;
        }

        MyOKHttpCallback(AvgScoreCallback callback, int type) {
            this.avgCallback = callback;
            this.type = type;
        }

        MyOKHttpCallback(ScoreDetailCallback callback, int typeScoreDetail) {
            this.detScoreCallback = callback;
            this.type = typeScoreDetail;
        }

        MyOKHttpCallback(WeatherCallback callback, int typeWeather) {
            this.weatherCallback = callback;
            this.type = typeWeather;
        }


        @Override
        public void onFailure(final Call call, IOException e) {
            final IOException exception = e;

            mHandler.post(new Runnable() {
                public void run() {
                    switch (type){
                        case TYPE_LOGIN:
                            mLoginCallBack.onFailure(exception);
                            break;
                        case TYPE_TERM:
                            termCallBack.onFailure(exception);
                            break;
                        case TYPE_SCORE:
                            scoreCallBack.onFailure(exception);
                            break;
                        case TYPE_SCHEDULE:
                            scheduleCallback.onFailure(exception);
                            break;
                        case TYPE_JOB_LIST:
                            jobCallback.onFailure(exception);
                            break;
                        case TYPE_JOB_CONTENT:
                            jobContentCallback.onFailure(exception);
                            break;
                        case TYPE_AVG_SCORE:
                            avgCallback.onFailure(exception);
                            break;
                        case TYPE_SCORE_DETAIL:
                            detScoreCallback.onFailure(exception);
                            break;
                        case TYPE_WEATHER:
                            weatherCallback.onFailure(exception);
                            break;
                        case TYPE_STU_INFO:
                            if (mLoginCallBack != null){
                                mLoginCallBack.onFailure(exception);
                            }
                            break;
                        default:
                            Log.d(TAG, "onFailure: e= "+exception.toString());
                            break;
                    }
                }
            });


        }

        @Override
        public void onResponse(Call call, Response response){
           switch (type){
               case TYPE_LOGIN:
                   Log.d(TAG, "onResponse: login ok");
                   getCurrentInfo();
                   break;
               case TYPE_STU_INFO:
                   Log.d(TAG, "onResponse: info ok");
                   responseStuInfo(response);
                   break;
               case TYPE_TERM:
                   responseTerm(response);
                   break;
               case TYPE_SCORE:
                   responseScore(response);
                   break;
               case TYPE_SCHEDULE:
                   responseSchedule(response);
                   break;
               case TYPE_JOB_LIST:
                   responseJobList(response);
                   break;
               case TYPE_JOB_CONTENT:
                   responseJobContent(response);
                   break;
               case TYPE_AVG_SCORE:
                   responseAvgScore(response);
                   break;
               case TYPE_SCORE_DETAIL:
                   responseDetailScore(response);
               case TYPE_WEATHER:
                   responseWeather(response);
                   break;
           }
        }

        private void responseWeather(Response response) {
            Weather weather = new Weather();
            try {
                String res = response.body().string();
                Log.d(TAG, "responseWeather: "+res);
                weather = JSON.parseObject(res,Weather.class);
            }catch (Exception e){
                e.printStackTrace();
                IOException ioException = new IOException("解析失败");
                onFailure(null,ioException);
                Log.d(TAG, "responseDetailScore: error = "+e.toString());
            }
            final Weather finalWeather = weather;
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (weatherCallback!= null)
                    weatherCallback.onSuccess(finalWeather);
                }
            });

        }

        private void responseDetailScore(Response response) {
            ScoreDetail detail = new ScoreDetail();
            try {
//                Log.d(TAG, "responseDetailScore: "+response.body().string());
                String s = response.body().string();
                Log.d(TAG, "responseDetailScore: "+s);
                detail = JSON.parseObject(s,ScoreDetail.class);
//                detail = new Gson().fromJson(response.body().string(),ScoreDetail.class);

            } catch (Exception e) {
                e.printStackTrace();
                IOException ioException = new IOException("解析失败");
                onFailure(null,ioException);
                Log.d(TAG, "responseDetailScore: error = "+e.toString());
            }
            final ScoreDetail finalDetail = detail;

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    detScoreCallback.onSuccess(finalDetail);
                }
            });
        }

        private void responseAvgScore(Response response) {
            AvgScore score = new AvgScore();
            try {
                score = JSON.parseObject(response.body().string(),AvgScore.class);
                Log.d(TAG, "responseAvgScore: response = "+response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
                IOException ioException = new IOException("解析失败");
                onFailure(null,ioException);
            }
            final AvgScore finalScore = score;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    avgCallback.onSuccess(finalScore);
                }
            });
        }

        private void responseJobContent(Response response) {
            JobContent content = null;
            try {
                content = JSON.parseObject(response.body().string(),JobContent.class);
            } catch (Exception e) {
                e.printStackTrace();
                IOException ioException = new IOException("解析失败");
                onFailure(null,ioException);
            }
            final JobContent finalContent = content;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    jobContentCallback.onSuccess(finalContent);
                }
            });

        }

        private void responseJobList(Response response) {
            JobListResult results = null;
            try {
                results = JSON.parseObject(response.body().string(),JobListResult.class);
            } catch (Exception e) {
                e.printStackTrace();
                IOException ioException = new IOException("解析失败");
                onFailure(null,ioException);
            }
            final List<Job> jobs = mapJobs(results);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    jobCallback.onSuccess(jobs);
                }
            });
        }

        private void responseSchedule(Response response){
            String jsonData;
            List<CourseResult> results = null;
            try {
                jsonData = response.body().string();
                Log.d(TAG, "responseSchedule: "+jsonData);
                JSONObject responses = JSON.parseObject(jsonData);

                MyResponseBody responseBody = JSON.parseObject(responses.toString(),MyResponseBody.class);
                results = JSON.parseObject(responseBody.getValue(), new TypeReference<ArrayList<CourseResult>>() {
                });

            } catch (Exception e) {
                e.printStackTrace();
                IOException ioException = new IOException("未登录");
                onFailure(null,ioException);
            }

            final List<Course> courses = mapCourse(results);
            mHandler.post(new Runnable() {
                public void run() {
                    scheduleCallback.onSuccess(courses);
                }
            });
        }

        private void responseScore(Response response)  {
            String jsonData;
            List<ScoreValue> scoreValues = null;
            try {

                jsonData = response.body().string();
                JSONObject responses = JSON.parseObject(jsonData);
                MyResponseBody responseBody = JSON.parseObject(responses.toString(),MyResponseBody.class);
                scoreValues = JSON.parseObject(responseBody.getValue(), new TypeReference<ArrayList<ScoreValue>>() {
                });

            } catch (Exception e) {
                IOException ioException = new IOException("未登录");
                onFailure(null,ioException);
                e.printStackTrace();
            }

            final List<Score> scoreList = mapScore(scoreValues);

            Log.d(TAG, "responseScore: size = "+scoreList.size());

            new Handler(getMainLooper()).post(new Runnable() {
                public void run() {
                    scoreCallBack.onSuccess(scoreList);
                }
            });
        }

        private void responseTerm(Response response){

            String jsonData;
            List<Term> list = null;
            try {
                jsonData = response.body().string();
                Log.d(TAG, "responseTerm: "+jsonData);
                JSONObject responses = JSON.parseObject(jsonData);
                MyResponseBody body = JSON.parseObject(responses.toString(), MyResponseBody.class);
                Log.d(TAG, "responseTerm: value = "+body.getValue());
                list = JSON.parseObject(body.getValue(), new TypeReference<ArrayList<Term>>() {});
            } catch (Exception e) {
                IOException ioException = new IOException("解析出错");
                onFailure(null,ioException);
                e.printStackTrace();
            }

            final List<Term> finalList = list;
            new Handler(getMainLooper()).post(new Runnable() {
                public void run() {
                    termCallBack.onSuccess(finalList);
                }
            });
        }

        private void responseStuInfo(Response response){
            Log.d(TAG, "responseStuInfo: ");
            String jsonData;
            int term = 0;
            try {
                jsonData = response.body().string();
                JSONObject responses = JSON.parseObject(jsonData);
                JSONObject defRes = JSON.parseObject(responses.getString("defRes"));

                term = defRes.getInteger("teachingTerm");

                mStudId = responses.getInteger("userId");
                mStudName = responses.getString("nickName");

            } catch (Exception e) {
                e.printStackTrace();
            }

            final StuInfo stuInfo = new StuInfo();

            stuInfo.name = mStudName;
            stuInfo.stuId = mStudId;
            stuInfo.currentTerm = term;


            if (mStudId == 0){
                onFailure(null,new IOException());
                return;
            }

            Log.d(TAG, "onResponse: 获取登录信息成功");
            Log.d(TAG, "onResponse: stuId = "+mStudId+"stuName = "+mStudName+"term "+term);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mLoginCallBack != null){
                        mLoginCallBack.onSuccess(stuInfo);
                    }else {
                        Log.d(TAG, "run: loginCallback = null");
                    }
                }
            });
        }
    }

    private List<Job> mapJobs(JobListResult result) {
        List<Job> jobs = new ArrayList<>();
        if (result != null){
            List<JobListResult.DataBean> datas = result.getData();
            for (JobListResult.DataBean data : datas) {
                Job job = new Job();
                job.setId(Long.parseLong(data.getTid()));
                job.setTitle(data.getTitle());
                job.setAddress(data.getAddress());
                job.setDate(data.getUpdatetime());
                job.setHits(Integer.parseInt(data.getHits()));
                job.setType(data.getType());
                jobs.add(job);
            }
        }
        return jobs;
    }

    private List<Course> mapCourse(List<CourseResult> results) {

        //垃圾数据，一点也不规范，cao
        List<Course> courses = new ArrayList<>();
        if (ActivityUtils.NotNullOrEmpty(results)){
            for (CourseResult result : results) {
                if (result.getTeachClassMaster().getLessonSchedules()!= null && ! result.getTeachClassMaster().getLessonSchedules().isEmpty()){
                    for (int i = 0; i < result.getTeachClassMaster().getLessonSchedules().size(); i++) {
                        Course course = new Course();
                        course.setId(Long.parseLong(result.getTeachClassMaster().getLessonSegment().getLssgId()+ i));
                        course.setName(result.getTeachClassMaster().getLessonSegment().getFullName());
                        course.setPlace(result.getTeachClassMaster().getLessonSchedules().get(i).getClassroom().getFullName());

                        CourseResult.TeachClassMasterBean.LessonSchedulesBean.TimeBlockBean time =
                                result.getTeachClassMaster().getLessonSchedules().get(i).getTimeBlock();

                        if (time != null){
                            if (time.getBeginWeek() != null
                                    && time.getEndWeek()!= null){
                                course.setBeginWeek(Integer.parseInt(time.getBeginWeek()));
                                course.setEndWeek(Integer.parseInt(time.getEndWeek()));
                            }
                            if (time.getDayOfWeek()!= null){
                                course.setDayOfWeek(Integer.parseInt(time.getDayOfWeek()));
                            }
                            course.setTime(time.getName());
                        }

                        course.setTeacher(result.getTeachClassMaster().getLessonTeachers().get(0).getTeacher().getName());
                        course.setTermId(mTermId);
                        Log.d(TAG, "mapCourse: setteerm = "+mTermId);
                        courses.add(course);
                    }
                }
            }
        }
        return courses;
    }

}
