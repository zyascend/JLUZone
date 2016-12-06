package com.zyascend.JLUZone.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.zyascend.JLUZone.R;
import com.zyascend.JLUZone.base.BaseActivity;
import com.zyascend.JLUZone.base.BaseReAdapter;
import com.zyascend.JLUZone.entity.ConstValue;
import com.zyascend.JLUZone.explore.ExplorePresenter;
import com.zyascend.JLUZone.utils.mapApi.MapUtils;
import com.zyascend.JLUZone.utils.mapApi.MyPoiOverlay;
import com.zyascend.JLUZone.utils.mapApi.PoiOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * Created by Administrator on 2016/12/4.
 */

public class MapActivity extends BaseActivity<MapContract.View, MapPresenter> implements MapContract.View, BaseReAdapter.OnItemClickListener, OnGetPoiSearchResultListener {

    private static final int PERCODE_OK = 0;
    private static final String TAG = "TAG_MapActivity";
    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bottom_sheet)
    NestedScrollView bottomSheet;
    @Bind(R.id.fab_goto)
    FloatingActionButton fabGoto;
    @Bind(R.id.fab_locate)
    FloatingActionButton fabLocate;
    private double lat = ConstValue.NANLING_LAT;
    private double lon = ConstValue.NANLING_LOT;
    private BottomSheetBehavior<NestedScrollView> behavior;
    private GotoAdapter adapter;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener myListener;
    private boolean isFirstLocation = true;
    private PoiSearch mPoiSearch;
    private boolean canSearchNext = true;

    @Override
    protected void doOnCreate() {

    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMyPermission();
        } else {
            init();
        }
    }

    private void init(){
        initMaps();
        initBottomSheet();
        initData();
    }

    private void initBottomSheet() {
        behavior = BottomSheetBehavior.from(bottomSheet);
        adapter = new GotoAdapter(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(adapter);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkMyPermission() {
        List<String> perLists = new ArrayList<String>();
        String[] per = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (int i = 0; i < per.length; i++) {
            if (noPermisson(per[i])) {
                perLists.add(per[i]);
            }
        }
        if (perLists.isEmpty()) {
            init();
            return;
        }

        String[] strings = new String[perLists.size()];
        requestPermissions(perLists.toArray(strings), PERCODE_OK);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean noPermisson(String per) {
        return this.checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case PERCODE_OK:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    init();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "Code missed", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initData() {
        String type = getIntent().getStringExtra(ExplorePresenter.INTENT_MAP);
        String[] maps = getResources().getStringArray(R.array.map_list);
        if (TextUtils.equals(type, maps[0])) {
            mPresenter.locateMySelf(lat, lon);
            return;
        } else if (TextUtils.equals(type, maps[2])) {
            //125.287352,43.829059
            lat = ConstValue.QIAN_NAN_LAT;
            lon = ConstValue.QIAN_NAN_LOT;
        } else if (TextUtils.equals(type, maps[1])) {
            //125.341302,43.860974
            lat = ConstValue.NANLING_LAT;
            lon = ConstValue.NANLING_LOT;
        } else if (TextUtils.equals(type, maps[3])) {
            //125.287352,43.829059
            lat = ConstValue.QIAN_BEI_LAT;
            lon = ConstValue.QIAN_BEI_LOT;
        } else if (TextUtils.equals(type, maps[4])) {
            //125.287352,43.829059
            lat = ConstValue.XINMIN_LAT;
            lon = ConstValue.XINMIN_LOT;
        } else if (TextUtils.equals(type, maps[5])) {
            //125.287352,43.829059
            lat = ConstValue.NANHU_LAT;
            lon = ConstValue.NANHU_LOT;
        } else if (TextUtils.equals(type, maps[6])) {
            //125.287352,43.829059
            lat = ConstValue.HEPIN_LAT;
            lon = ConstValue.HEPIN_LOT;
        } else if (TextUtils.equals(type, maps[7])) {
            //125.287352,43.829059
            lat = ConstValue.CHAOYANG_LAT;
            lon = ConstValue.CHAOYANG_LOT;
        }
        moveCenter();
    }

    private void initMaps() {
        //获取BaiduMap对象
        mBaiduMap = mMapView.getMap();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        myListener = new MyLocationListener(mPresenter);
        mLocationClient.registerLocationListener(myListener);
        //配置定位参数
        mLocationClient.setLocOption(MapUtils.getLocationOption());

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

    }

    private void moveCenter() {
        LatLng cenpt = new LatLng(lat,lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(16)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }


    @Override
    public void onLocated(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        //这个判断是为了防止每次定位都重新设置中心点和marker
        if(isFirstLocation){
            isFirstLocation = false;
            setMarker();
            setUserMapCenter();
        }
    }

    private void setUserMapCenter() {
        Log.d(TAG,"setUserMapCenter : lat : "+ lat+" lon : " + lon);
        LatLng cenpt = new LatLng(lat,lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void setMarker() {
        Log.d(TAG,"setMarker : lat : "+ lat+" lon : " + lon);
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_marker);
        if (bitmap == null){
            return;
        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @OnClick({R.id.fab_goto, R.id.fab_locate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_goto:
                if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.fab_locate:
                if (mLocationClient == null) break;
                mLocationClient.start();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {

        if (!canSearchNext){
            Toast.makeText(this, "搜索中，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        final String key = getResources().getStringArray(R.array.goto_list)[position];
        if (TextUtils.isEmpty(key)){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                searchNeayBy(key);
                Looper.loop();
            }
        }).start();
    }

    private void searchNeayBy(String key) {
        // POI初始化搜索模块，注册搜索事件监听
        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
        poiNearbySearchOption.keyword(key);
        poiNearbySearchOption.location(new LatLng(lat, lon));
        poiNearbySearchOption.radius(500);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(20);  // 默认每页10条
        mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        // 获取POI检索结果
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(this, "未找到结果",Toast.LENGTH_LONG).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//          mBaiduMap.clear();
            if(result.getAllPoi()!= null && result.getAllPoi().size()>0){
                mBaiduMap.clear();
                //创建PoiOverlay
                PoiOverlay overlay = new MyPoiOverlay(mBaiduMap,MapActivity.this);
                //设置overlay可以处理标注点击事件
                mBaiduMap.setOnMarkerClickListener(overlay);
                //设置PoiOverlay数据
                overlay.setData(result);
                //添加PoiOverlay到地图中
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }
        canSearchNext = true;
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected MapPresenter getPresenter() {
        return new MapPresenter();
    }

    @Override
    protected void loadFragment() {

    }
}
