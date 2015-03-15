package com.binqiang.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.binqiang.location.MyLocationManager;
import com.binqiang.setting.MainMenu;
import com.binqiang.setting.MapConfig;
import com.binqiang.sharewithu.R;
import com.binqiang.sharewithu.ShareWithUApplication;
import com.binqiang.user.UserInfo;
import com.binqiang.util.Config;

public class MapFragment extends Fragment {
	
	MapView   mMapView;
	BaiduMap  mMap;
	MapStatus mMapStatus;
	MyLocationManager myLocationManager;
	UserInfo  mUserGpsInfo;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		//map setting
		mMapView =     (MapView) rootView.findViewById(R.id.bmapView);
		mMap = mMapView.getMap();
		initialBaiduMap();
		
		//设置定位监听
		myLocationManager = ShareWithUApplication.getLocationInstance(this.getActivity().getApplicationContext());
		myLocationManager.setLocationListener(mMap);
		
		//好友位置
		mUserGpsInfo = new UserInfo(this.getActivity().getApplicationContext(), mMap);
		mUserGpsInfo.GetUserInfo();
		
		//主界面的菜单
		//MainMenu mainMenu = new MainMenu(this.getActivity());
		//mainMenu.SetMainMenu();
		
		return rootView;
	}
	
	
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		myLocationManager.stopGps();
	}

	

	@Override
	public void onResume() {
		super.onResume();
		myLocationManager.startGps();
	}


	@Override
	public void onStop() {
		super.onStop();
		myLocationManager.stopGps();
	}


	private void initialBaiduMap(){
		mMapStatus = new MapStatus.Builder().target(Config.GetDefaultPosition()).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		mMap.setMapStatus(mMapStatusUpdate);
		//hide the zoom view
		int childCount = mMapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
                View child = mMapView.getChildAt(i);
                if (child instanceof ZoomControls) {
                        zoom = child;
                        break;
                }
        }
        zoom.setVisibility(View.GONE);
        
        //hide baidu logo
        mMapView.removeViewAt(1);
        
        
		mMap.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng arg0) {
				mMap.hideInfoWindow();
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
		});
		MapConfig.SetBaiduMap(mMap);
		
	}
	

}
