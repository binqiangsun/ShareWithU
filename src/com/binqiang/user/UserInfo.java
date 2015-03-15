package com.binqiang.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.binqiang.bmob.GpsInfoTable;
import com.binqiang.service.ImageService;
import com.binqiang.sharewithu.CommunityActivity;
import com.binqiang.sharewithu.R;
import com.binqiang.util.Config;

public class UserInfo {
	
	private Context mContext;
	private Map<String, GpsInfoTable> mapInfos = new HashMap<String, GpsInfoTable>();
	Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
	private BaiduMap mMap;
	private View   mPoiView;
	
	public UserInfo(Context con, BaiduMap map){
		this.mContext = con;
		mMap = map;
	}
	

	//get gps info from bmob
	public void GetUserInfo(){
		
		BmobQuery<GpsInfoTable> query = new BmobQuery<GpsInfoTable>();
		query.findObjects(mContext, new FindListener<GpsInfoTable>(){

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				//ToastShow.ShowMsg(mContext, "用户信息查询失败");
			}

			@Override
			public void onSuccess(List<GpsInfoTable> objects) {
				// TODO Auto-generated method stub
				int size  = objects.size();
				for(int i = 0; i < size; i ++)
				{
					try {
						mapInfos.put(objects.get(i).getUserName(), (GpsInfoTable) objects.get(i).clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ShowGpsInfo();
				//ToastShow.ShowMsg(mContext, "共有"+objects.size()+"个用户");
				
			}
			
		});
	}
	
	public void RefreshGpsInfo(){
		GetUserInfo();
	}
	
	
	//add markers
	private void ShowGpsInfo(){
		
		
		
		
		//
		Iterator<String> iterator = mapInfos.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Bitmap bitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_gcoding)
					.copy(Bitmap.Config.ARGB_8888, true);
			Bitmap bitmapText = ImageService.writeTextOnBitmap(bitmap1, mapInfos.get(key).getUserName());
			BitmapDescriptor bitmapDescript = BitmapDescriptorFactory.fromBitmap(bitmapText);
			LatLng userPos = new LatLng(Double.parseDouble(mapInfos.get(key).getLatitude()), 
					Double.parseDouble(mapInfos.get(key).getLongitude()));
			String userName = mapInfos.get(key).getUserName();
			Marker marker   = null;
		    if(!mapMarkers.containsKey(userName))
		    {
		    	 marker = (Marker)mMap.addOverlay(new MarkerOptions()
								.position(userPos)
								.title(mapInfos.get(key).getUserName())
								.icon(bitmapDescript));
		    	mapMarkers.put(userName, marker);
		    }
		    else
		    {
		    	marker = mapMarkers.get(userName);
		    	marker.setPosition(userPos);
		    }
		}
		
		//set poi click listener.
		mMap.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker marker) {
				if(!mapInfos.containsKey(marker.getTitle()))
					return false;
				if(mPoiView == null){
					mPoiView = LayoutInflater.from(mContext).inflate(R.layout.view_poi_info, null);
					mPoiView.setOnClickListener(clickListener);
				}
				TextView txtPoiName = (TextView)mPoiView.findViewById(R.id.poi_name);
				TextView txtPoiTime = (TextView)mPoiView.findViewById(R.id.poi_time);
				
				GpsInfoTable gpsInfo = mapInfos.get(marker.getTitle());
				txtPoiName.setText(gpsInfo.getUserName());
				txtPoiTime.setText(gpsInfo.getUpdatedAt());
				InfoWindow mInfoWindow = new InfoWindow(mPoiView, marker.getPosition(), -47);  
				//显示InfoWindow  
				mMap.showInfoWindow(mInfoWindow);
				marker.getTitle();
				return false;
			}
			
		});
		
		
			
		}
	
	private OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View view) {
			TextView txtPoiName = (TextView)view.findViewById(R.id.poi_name);
			Intent intent = new Intent(mContext, CommunityActivity.class);
			intent.putExtra(Config.USERNAME, txtPoiName.getText().toString());
			mContext.startActivity(intent);
		}
		
	};
	
}
	

