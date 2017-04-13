package com.zyascend.JLUZone.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    @Bind(R.id.fab_goto)
    FloatingActionButton fabGoto;
    @Bind(R.id.fab_locate)
    FloatingActionButton fabLocate;
    private double lat = ConstValue.NANLING_LAT;
    private double lon = ConstValue.NANLING_LOT;
    private GotoAdapter adapter;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener myListener;
    private boolean isFirstLocation = true;
    private PoiSearch mPoiSearch;
    private boolean canSearchNext = true;

    RecyclerView recyclerView;
    RecyclerView destinationView;
    private DestinationAdapter destinationAdapter;
    private BottomSheetDialog dialog;

    @Override
    protected void doOnCreate() {
        mToolbar.setTitle("我的位置");
        locateSelf();
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

    private void changeMap(int position) {
        Log.d(TAG, "changeMap: position");
        switch (position){
            case 0:
                lat = ConstValue.NANLING_LAT;
                lon = ConstValue.NANLING_LOT;
                break;
            case 1:
                lat = ConstValue.QIAN_NAN_LAT;
                lon = ConstValue.QIAN_NAN_LOT;
                break;
            case 2:
                lat = ConstValue.NANHU_LAT;
                lon = ConstValue.NANHU_LOT;
                break;
            case 3:
                lat = ConstValue.HEPIN_LAT;
                lon = ConstValue.HEPIN_LOT;
                break;
            case 4:
                lat = ConstValue.XINMIN_LAT;
                lon = ConstValue.XINMIN_LOT;
                break;
            case 5:
                lat = ConstValue.CHAOYANG_LAT;
                lon = ConstValue.CHAOYANG_LOT;
                break;


//            default:
//                if (mLocationClient == null) break;
//                mLocationClient.start();
//                break;

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
        Log.d(TAG, "moveCenter: ");
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
        Log.d(TAG, "onLocated: lat = "+lat);
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
                showGotoDialog();
                break;
            case R.id.fab_locate:
                locateSelf();
                break;
        }
    }

    private void locateSelf() {
        isFirstLocation = true;
        if (mLocationClient == null)return;
        mLocationClient.start();
    }

    private void showGotoDialog() {

//        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
//        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
//        contentView.setLayoutParams(layoutParams);
//        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
//        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
//        bottomDialog.show();


        dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.layout_goto, null);
        adapter = new GotoAdapter(this);
        adapter.setOnItemClickListener(this);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(adapter);
        dialog.setContentView(contentView);
        dialog.show();
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
                searchNearBy(key);
                Looper.loop();
            }
        }).start();
        dialog.dismiss();
    }

    private void searchNearBy(String key) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_change_map:
                showDestinationDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDestinationDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_destination, null);
        destinationAdapter = new DestinationAdapter(this);
        destinationView = (RecyclerView) view.findViewById(R.id.recyclerView);
        destinationView.setLayoutManager(new LinearLayoutManager(this));
        destinationView.setAdapter(destinationAdapter);
        destinationAdapter.setOnItemClickListener(new BaseReAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeMap(position);
                mToolbar.setTitle(MapActivity.this.getResources().getStringArray(R.array.map_list)[position]);
                dialog.dismiss();
            }
        });

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
