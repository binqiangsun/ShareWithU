package com.binqiang.setting;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;

public class MapConfig {
	@SuppressLint("SimpleDateFormat") private final static SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");  

	
	private static BaiduMap mMap = null;
	private static double g_latitude  = 0.0;
	private static double g_longitude = 0.0;
	private static String g_addr      = null;
	private static String g_curtime   = null;
	
	public static String get_curtime() {
		return g_curtime;
	}

	public static void set_curtime(String g_curtime) {
		MapConfig.g_curtime = g_curtime;
	}

	public static String getG_addr() {
		return g_addr;
	}

	public static void setG_addr(String g_addr) {
		MapConfig.g_addr = g_addr;
	}

	public static void SetBaiduMap(BaiduMap map){
		mMap = map;
	}

	public static double get_latitude() {
		return g_latitude;
	}
	
	public static double get_longitude() {
		return g_longitude;
	}

	public static void set_latlog(double latitude, double longitude) {
		g_latitude = latitude;
		g_longitude = longitude;
	}
	
	

	public static void NavigationGps() {
		if(mMap == null){
			return;
		}
		LatLng myPos = new LatLng(g_latitude, g_longitude);  
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(myPos);  
        mMap.animateMapStatus(u);
	}

	public static void set_latlogAddr(double latitude, double longitude,
			String addrStr) {
		Date curDate = new Date(System.currentTimeMillis());
		set_curtime(timestampFormat.format(curDate));
		g_latitude = latitude;
		g_longitude = longitude;
		if(addrStr == null)
		{
			System.out.println("地址为空");
			return;
		}
		System.out.println(addrStr);
		g_addr      = addrStr;
	}

	
	

}
