package com.binqiang.sharewithu;

import android.app.Application;
import android.content.Context;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.binqiang.location.MyLocationManager;
import com.binqiang.util.Config;




public class ShareWithUApplication extends Application {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public static MyLocationManager locationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		locationManager = new MyLocationManager(this.getApplicationContext());
		locationManager.initial();
		Config.setContext(this.getApplicationContext());
		
		// 使用推送服务时的初始化操作
	    BmobInstallation.getCurrentInstallation(this).save();
	    // 启动推送服务
	    Bmob.initialize(this, Config.STR_PID);
	    BmobPush.startWork(this, Config.STR_PID);
	}
	
	public static MyLocationManager getLocationInstance(Context context){
		if(locationManager == null){
			locationManager = new MyLocationManager(context);
		}
		return locationManager;
	}
}
