package com.binqiang.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import cn.bmob.v3.BmobUser;

import com.baidu.mapapi.model.LatLng;
import com.binqiang.sharewithu.R;

public class Config {
	
	
	public  final static String SER_KEY = "com.example.setting.settingactivity";  
	public final static String APP_ID = "com.binqiang.sharewithu";
	public  final static int ACT_RET_KEY = 1001;
	public  final static int ACT_SEND_KEY = 1000;
	public final static String KEY_GPS_SWITCH = "gpsswitch";
	public static final int PICK_IMAGE = 1;
	public static final String FIELD_CREATEAT_BACK = "-createdAt";
	public static final String FIELD_CREATEAT = "createdAt";
	public static final int HTTP_TIME_OUT = 5000;
	public static final String HTTP_GET = "GET";
	public static final int HTTP_SUCCESS_CODE = 200;
	public static final int READ_FAIL_CODE = -1;
	public static final CharSequence TEXT_OK = "确定";
	public static final int ID_BTN_GPS = 0;
	public static final int ID_BTN_PIC = 2;
	public static final int ID_BTN_COM = 3;
	public static final int ID_BTN_SET = 4;
	public static final int ID_BTN_PLA = 1;
	public static final String USERNAME = "userName";
	public static final String FAILED_GET_USER = "获取用户名失败...";
	public static String SENDER_GPS = "unknow";
	public static final String CONTENT_OPEN_GPS = "在哪";
	public static final String CONTENT_CLOSE_GPS = "知道了";
	public static final String INPUT_EMAIL = "请输入邮箱";
	public static final String ALTER_DIALOG_TITLE_GPS = "允许该好友通过短信查看你的gps信息？";
	public static final String INPUT_TELEPHONE = "请输入好友手机号码";
	public static final String SHOW_MSG_SET_SUCCESS = "设置成功";
	public static final String SHOW_MSG_TELE_NUM_FAILED = "设置失败，请输入十一位手机号码";
	public static final String KEY_IMAGE_EXTRA = "filename";
	public static final CharSequence UPLOAD = "上传";
	public static final String FAILED_GET_IMAGE = "获取图片失败";
	public static final int MAX_INFO_NUM = 50;
	public static final String TEXT_MSG_LOCATION = "我现在在：";
	
	public static String g_device_id = "";
	public static String g_user_name = "";
	public static boolean g_gps_service_switch = false; 
	
	private static File g_cache_folder = null;
	private static File g_sharewithu_folder = null;
	private static File g_media_folder = null;
	public static boolean bMyLocal = true;
	public static boolean isFristLocation = true;
	
	/** 
     * 一分钟的毫秒值，用于判断上次的更新时间 
     */  
    public static final long ONE_MINUTE = 60 * 1000;  
  
    /** 
     * 一小时的毫秒值，用于判断上次的更新时间 
     */  
    public static final long ONE_HOUR = 60 * ONE_MINUTE;  
  
    /** 
     * 一天的毫秒值，用于判断上次的更新时间 
     */  
    public static final long ONE_DAY = 24 * ONE_HOUR;  
  
    /** 
     * 一月的毫秒值，用于判断上次的更新时间 
     */  
    public static final long ONE_MONTH = 30 * ONE_DAY;  
  
    /** 
     * 一年的毫秒值，用于判断上次的更新时间 
     */  
    public static final long ONE_YEAR = 12 * ONE_MONTH;
	private static final String UPDATE_JUST_NOW = "刚刚更新";
	public static final String DEST_OBJ_ID = "destSharedInfo";
	public static final String STR_PID  = "8491799755e88b880caa13a9696c85dd";
	public static final String FIELD_UID = "uid";
	public static final String STR_CONTENT = "content";
	public static final String STR_SELECT_ITEM = "selected_item";
	public static final CharSequence STR_SHARE = "分享";
	public static final CharSequence STR_FIND = "发现";
	public static final CharSequence STR_MYSELF = "我";
	
	private static Context mAppContext;
	
	public static void setContext(Context context){
		mAppContext = context;
	}
	
	public static Context getAppContext(){
		return mAppContext;
	}
	
	
	public static boolean isbMyLocal() {
		return bMyLocal;
	}


	public static void setbMyLocal(boolean bMyLocal) {
		Config.bMyLocal = bMyLocal;
	}
	
	public static final void setGpsTelePhone(String strTeleNum){
		SharedPreferences sharePreferPos = mAppContext.getSharedPreferences(APP_ID, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePreferPos.edit();
		editor.putString("gpstelenum", strTeleNum);
		editor.commit();
	}
	
	public static final String getGpsTelePhone(Context context){
		SharedPreferences sharePreferPos = context.getSharedPreferences(APP_ID, Activity.MODE_PRIVATE);
		String strTeleNum = sharePreferPos.getString("gpstelenum", "00000000000");
		return strTeleNum;
	}


	public static final void SetDefaultPosition(LatLng position){
		SharedPreferences sharePreferPos = mAppContext.getSharedPreferences(APP_ID, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePreferPos.edit();
		editor.putString("lat", String.valueOf(position.latitude));
		editor.putString("lng", String.valueOf(position.longitude));
		editor.commit();
	}
	
	
	public static final LatLng GetDefaultPosition(){
		
		SharedPreferences sharePreferPos = mAppContext.getSharedPreferences(APP_ID, Activity.MODE_PRIVATE);
		double lat = Double.valueOf(sharePreferPos.getString("lat", "30.5"));
		double lng = Double.valueOf(sharePreferPos.getString("lng", "120.5"));
		LatLng position = new LatLng(lat, lng);
		return position;
		
	}
	
	public static final boolean IsLogin(Context context){
		BmobUser user = BmobUser.getCurrentUser(context);
		if(user != null){
			g_user_name = user.getUsername();
			System.out.println(g_user_name);
			return true;
		}
		else{
			return false;
		}
	}
	
	public static final void Logout(Context context){
		BmobUser.logOut(context);
	}


	public static void setDeviceId(String deviceId) {
		// TODO Auto-generated method stub
		g_device_id = deviceId;
	}


	public static boolean GetGpsSwitch(Context context) {
		// TODO Auto-generated method stub
		SharedPreferences sharePreferGps = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
		return sharePreferGps.getBoolean(KEY_GPS_SWITCH, true);
	}
	
	public static void SetGpsSwitch(Context context, boolean is_open) {
		// TODO Auto-generated method stub
		SharedPreferences sharePreferGps = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePreferGps.edit();
		editor.putBoolean(KEY_GPS_SWITCH, is_open);
		editor.commit();
	}
	
	public static boolean get_gps_service_switch() {
		return g_gps_service_switch;
	}


	public static void set_gps_service_switch(boolean g_gps_service_switch) {
		Config.g_gps_service_switch = g_gps_service_switch;
	}
	
	public static void initCache(){
        g_sharewithu_folder = new File(Environment.getExternalStorageDirectory(), "ShareWithU");  
        if(!g_sharewithu_folder.exists()){  
        	g_sharewithu_folder.mkdirs();  
        }
        g_cache_folder = new File(g_sharewithu_folder, "cache");  
        if(!g_cache_folder.exists()){  
        	g_cache_folder.mkdirs();  
        }
        System.out.println(g_cache_folder.getAbsolutePath());
	}


	public static File getCache() {
		return g_cache_folder;
	}
	
	public static void clearCache(){
		/*File[] files = g_cache_folder.listFiles();  
        for(File file :files){  
            file.delete();  
        }  
        g_cache_folder.delete();  */
	}


	public static boolean ensureTeleNum(String teleNum) {
		
		if(teleNum == null || teleNum.length() != 11){
			return false;
		}
		return true;
	}


	public static File getMediaFile() {
		if(g_media_folder == null){
			g_media_folder = new File(g_sharewithu_folder, "media");  
		}
		return g_media_folder;
	}
	
	public static String GetCurrentTimeString() {
		String strRes = "";
		try {
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
			
			Date curDate = new Date(System.currentTimeMillis());
			strRes = formatDate.format(curDate);
		} catch (Exception e) {
			strRes = String.valueOf(System.currentTimeMillis());
		}
		return strRes;
	}
	
	public static String getUpdateTime(Context context, long lastUpdateTime) {
        long currentTime = System.currentTimeMillis();  
        long timePassed = currentTime - lastUpdateTime;  
        long timeIntoFormat;  
        String updateAtValue;
        if (timePassed < ONE_MINUTE) {  
            updateAtValue = UPDATE_JUST_NOW;  
        } else if (timePassed < ONE_HOUR) {  
            timeIntoFormat = timePassed / ONE_MINUTE;  
            updateAtValue = timeIntoFormat + "分钟前";
        } else if (timePassed < ONE_DAY) {  
            timeIntoFormat = timePassed / ONE_HOUR;  
            updateAtValue = timeIntoFormat + "小时前";
        } else if (timePassed < ONE_MONTH) {  
            timeIntoFormat = timePassed / ONE_DAY;
            updateAtValue = timeIntoFormat + "天前";  
        } else if (timePassed < ONE_YEAR) {  
            timeIntoFormat = timePassed / ONE_MONTH;  
            updateAtValue = timeIntoFormat + "个月前";
        } else {  
            timeIntoFormat = timePassed / ONE_YEAR;  
            updateAtValue = timeIntoFormat + "年前";  
        }  
        return updateAtValue;
    }
	
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}


	public static int dip2px(Context context, float px) {
		final float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}



}
