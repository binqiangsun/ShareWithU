package com.binqiang.location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.binqiang.bmob.GpsInfoTable;
import com.binqiang.setting.MapConfig;
import com.binqiang.sharewithu.MainActivity;
import com.binqiang.sharewithu.R;
import com.binqiang.util.Config;


public class GpsLocationListener implements BDLocationListener {


	GpsInfoTable gpsDataBmob = new GpsInfoTable();
	private BaiduMap mMap;
	private Context mContext;
	private Marker  mGpsMarker;
	private BitmapDescriptor mCurrentMarker;
	
	public GpsLocationListener(BaiduMap map, Context context){
		mMap = map;
		mContext = context;
	}
	

	@Override
	public void onReceiveLocation(final BDLocation location) {
		// get data and post to the server
		if(null != location)
		{
			
            
			
    		
    		//update bmob data source
    		BmobQuery<GpsInfoTable> query = new BmobQuery<GpsInfoTable>();
    		query.addWhereEqualTo("deviceId", Config.g_device_id);
    		if(!Config.g_user_name.equals("")){
    			query.addWhereEqualTo("userName", Config.g_user_name);
    		}
    		query.findObjects(MainActivity.g_context, new FindListener<GpsInfoTable>(){

				@Override
				public void onError(int arg0, String arg1) {
				}

				@Override
				public void onSuccess(List<GpsInfoTable> result) {
					if(result.size() > 0)
					{
						gpsDataBmob.setLatitude(String.valueOf(location.getLatitude()));
						gpsDataBmob.setLongitude(String.valueOf(location.getLongitude()));
						gpsDataBmob.update(MainActivity.g_context, result.get(0).getObjectId(), new UpdateListener(){

							@Override
							public void onFailure(int arg0, String arg1) {
								System.out.println("update data failed" + arg1);
							}

							@Override
							public void onSuccess() {
								System.out.println("update data successful");
							}
							
						});
					}
					else
					{
						gpsDataBmob.setDeviceId(Config.g_device_id);
						gpsDataBmob.setUserName(Config.g_user_name);
						gpsDataBmob.setLatitude(String.valueOf(location.getLatitude()));
						gpsDataBmob.setLongitude(String.valueOf(location.getLongitude()));
						gpsDataBmob.save(MainActivity.g_context, new SaveListener(){

							@Override
							public void onFailure(int arg0, String arg1) {
								System.out.println("insert data failed" + arg1);
							}

							@Override
							public void onSuccess() {
								System.out.println("insert data successful");
							}
							
						});
					}
				}
    			
    		});
    		MapConfig.set_latlogAddr(location.getLatitude(), location.getLongitude(), location.getAddrStr());
    		//update the my location
    		SetMyLocation(location);
		}
		
	}
	
	
	private void SetMyLocation(final BDLocation location)
	{
		if(mMap == null){
			return;
		}
		if(Config.isbMyLocal())  
        {  
			LatLng pt = new LatLng(location.getLatitude(), location.getLongitude());
			/*MyLocationData locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())
			.latitude(location.getLatitude())
			.longitude(location.getLongitude()).build();*/
			if(mCurrentMarker == null){
				mCurrentMarker = BitmapDescriptorFactory  
	                    .fromResource(R.drawable.ic_gps);
			}
	        if(mGpsMarker == null){
	        	
		        OverlayOptions options=new MarkerOptions()  
		        .icon(mCurrentMarker)
		        .position(pt);
	        	mGpsMarker = (Marker) mMap.addOverlay(options);  
	        }
	        else{
	        	mGpsMarker.setPosition(pt);
	        }
            MyLocationConfiguration config = new MyLocationConfiguration(  
                    LocationMode.FOLLOWING, true, mCurrentMarker);  
            mMap.setMyLocationConfigeration(config);
            
            
            if (Config.isFristLocation)  
            {  
                Config.isFristLocation = false;  
                LatLng myPos = new LatLng(location.getLatitude(),  
                        location.getLongitude());  
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(myPos, 17);  
                mMap.animateMapStatus(u);  
            }  
        }  
	}
	
	public void SetBaiduMap(BaiduMap map){
		mMap = map;
	}
	

}
