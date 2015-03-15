package com.binqiang.bmob;

import cn.bmob.v3.BmobObject;

public class GpsInfoTable extends BmobObject {
	@Override
	public void setTableName(String arg0) {
		// TODO Auto-generated method stub
		super.setTableName(arg0);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String latitude;
	String longitude;
	String userName;
	String deviceId;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		GpsInfoTable gpsInfo = new GpsInfoTable();
		gpsInfo.setObjectId(this.getObjectId());
		gpsInfo.setDeviceId(this.getDeviceId());
		gpsInfo.setLatitude(this.getLatitude());
		gpsInfo.setLongitude(this.getLongitude());
		gpsInfo.setUserName(this.getUserName());
		gpsInfo.setUpdatedAt(this.getUpdatedAt());
		gpsInfo.setCreatedAt(this.getCreatedAt());
		return gpsInfo;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
