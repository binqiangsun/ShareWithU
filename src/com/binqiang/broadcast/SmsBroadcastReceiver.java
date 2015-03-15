package com.binqiang.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.binqiang.common.ToastShow;
import com.binqiang.location.MyLocationManager;
import com.binqiang.setting.MapConfig;
import com.binqiang.sharewithu.ShareWithUApplication;
import com.binqiang.util.Config;

public class SmsBroadcastReceiver extends BroadcastReceiver {

	private static final Object SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private MyLocationManager myLocationManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction() != null && intent.getAction().equals(SMS_RECEIVED)){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				Object[] obj = (Object[])bundle.get("pdus");
				SmsMessage smsMessage[] = new SmsMessage[obj.length];
				for(int i = 0; i < obj.length; i++){
					smsMessage[i] = SmsMessage.createFromPdu((byte[])obj[i]);
					String sender = smsMessage[i].getOriginatingAddress();
					String content = smsMessage[i].getMessageBody();
					String teleNum = Config.getGpsTelePhone(context);
					System.out.println(sender + " " + content + " " + teleNum + " " + Config.SENDER_GPS);
					if(sender.contains(teleNum) && content.equals(Config.CONTENT_OPEN_GPS)){
						ToastShow.ShowMsg(context, "好友正在请求你的GPS位置");
						myLocationManager = ShareWithUApplication.getLocationInstance(context);
						if(myLocationManager.getIsInitial() && myLocationManager.getIsStarted()){
							return;
						}
						myLocationManager.initial();
						myLocationManager.setLocationListener(null);
						
						//
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SmsSendTextMsg(context, teleNum, GetLocationMsg());
					}
					else if(sender.contains(teleNum) && content.equals(Config.CONTENT_CLOSE_GPS)){
						myLocationManager = ShareWithUApplication.getLocationInstance(context);
						if(myLocationManager != null){
							System.out.println("stop gps");
							myLocationManager.stopGps();
							
						}else{
							System.out.println("null");
						}
					}
				}
			}
		}

	}
	
	private String GetLocationMsg(){
		StringBuilder strBuild = new StringBuilder(Config.TEXT_MSG_LOCATION);
		strBuild.append(MapConfig.getG_addr());
		strBuild.append("\n");
		strBuild.append(MapConfig.get_curtime());
		return strBuild.toString();
	}
	
	private void SmsSendTextMsg(Context context, String receive_num, String content){
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
		SmsManager sManager = SmsManager.getDefault();
		sManager.sendTextMessage(receive_num, null, content, pi, null);
		ToastShow.ShowMsg(context, "已回复好友当前位置");
	}

}
