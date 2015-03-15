package com.binqiang.location;

import android.content.Context;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;

public class MyLocationManager {
	
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	private GpsLocationListener mMyLocationListener;
	private Context mContext;
	private boolean mIsInitial = false;
	
	
	public boolean getIsInitial() {
		return mIsInitial;
	}
	
	public boolean getIsStarted(){
		if(mIsInitial){
			return mLocationClient.isStarted();
		}
		return false;
	}


	public MyLocationManager(Context context){
		mContext = context;
	}
	
	public void initial() {
		if(mIsInitial)
			return;
		mLocationClient = new LocationClient(mContext);
		LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
        mLocationClient.setLocOption(option);
        mIsInitial = true;
		return;
	}
	
	public void setLocationListener(BaiduMap map){
		mMyLocationListener = new GpsLocationListener(map, mContext);
		mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
	}
	
	public void stopGps(){
		if(mLocationClient != null && mLocationClient.isStarted()){
			mLocationClient.unRegisterLocationListener(mMyLocationListener);
			mLocationClient.stop();
		}
	}
	
	public void startGps(){
		if(mLocationClient != null && !mLocationClient.isStarted()){
			mLocationClient.registerLocationListener(mMyLocationListener);
			mLocationClient.start();
		}
	}
}
