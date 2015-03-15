package com.binqiang.photo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.provider.MediaStore;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

public class PhotoInfo {
	Context mContext;
	BaiduMap mMap;
	List<String> aryPhotoPaths = new ArrayList<String>();
	List<HashMap<Object, String>> aryPhotoInfos = new ArrayList<HashMap<Object, String>>();
	
	
	public PhotoInfo(Context con, BaiduMap map){mContext = con; mMap = map;}
	
	public void GetPhotoPath() throws IOException{
		String   path = "";
		String[] mediaInfo = new String[]{MediaStore.Images.ImageColumns._ID, 
				MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN};
		final Cursor cur = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
					mediaInfo, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN+" DESC");
		if(cur!=null)
		{
			cur.moveToFirst();
			while(cur.moveToNext()){
				path=cur.getString(1);
				System.out.println("File Path: "+path);
				if(path.indexOf("Camera")==-1)
					continue;
				System.out.println("File Path: "+path);
				aryPhotoInfos.add(GetPhotoExif(path));
				
				
            }
		}
	}
	
	public HashMap<Object, String> GetPhotoExif(String path) throws IOException
	{
		HashMap<Object, String> ret = new HashMap<Object, String>();
		ExifInterface exif = new ExifInterface(path);
		
        String Date_Time = getExifTag(exif, ExifInterface.TAG_DATETIME);

        String latValue = getExifTag(exif, ExifInterface.TAG_GPS_LATITUDE);
        String latRef   = getExifTag(exif, ExifInterface.TAG_GPS_LATITUDE_REF);
        String lngValue = getExifTag(exif, ExifInterface.TAG_GPS_LONGITUDE);
        String lngRef   = getExifTag(exif, ExifInterface.TAG_GPS_LONGITUDE_REF);
        double  fLat = 0.0;
        double  fLog = 0.0;

        ret.put("time", Date_Time);
        //ret.put("latitude", GPS_Latitude);
        //ret.put("longitude", GPS_Longitude);
        //LatLng pos = new LatLng(Double.valueOf(GPS_Latitude), Double.valueOf(GPS_Longitude));
        if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
            fLat = convertRationalLatLonToFloat(latValue, latRef);
            fLog = convertRationalLatLonToFloat(lngValue, lngRef);
            //return null;
        } else {
            return null;
        }
        LatLng pos = new LatLng(fLat, fLog);
        mMap.addOverlay(new MarkerOptions()
        		.position(pos)
                .title(Date_Time));
        return ret;
	}
	
	private String getExifTag(ExifInterface exif, String tag) 
	{ 
		String attribute = exif.getAttribute(tag); 
		return (null != attribute ? attribute : ""); 
	} 
	
	private static float convertRationalLatLonToFloat(
            String rationalString, String ref) {
        try {
            String [] parts = rationalString.split(",");


            String [] pair;
            pair = parts[0].split("/");
            int degrees = (int) (Float.parseFloat(pair[0].trim())
                    / Float.parseFloat(pair[1].trim()));


            pair = parts[1].split("/");
            int minutes = (int) ((Float.parseFloat(pair[0].trim())
                    / Float.parseFloat(pair[1].trim())));


            pair = parts[2].split("/");
            float seconds = Float.parseFloat(pair[0].trim())
                    / Float.parseFloat(pair[1].trim());


            float result = degrees + (minutes / 60F) + (seconds / (60F * 60F));
            if ((ref.equals("S") || ref.equals("W"))) {
                return -result;
            }
            return result;
        } catch (RuntimeException ex) {
            // if for whatever reason we can't parse the lat long then return
            // null
            return 0f;
        }
    }

	
	

}
