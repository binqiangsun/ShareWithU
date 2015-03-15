package com.binqiang.bmob;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.bmob.push.PushConstants;

import com.binqiang.sharewithu.R;
import com.binqiang.util.Config;

public class MyPushMessageReceiver extends BroadcastReceiver {

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
			int k = 1;
			String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			System.out.println("消息内容："+message);
			try {
				JSONObject jsonObject = new JSONObject(message);
			
				// 发送通知
				NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				 
				Notification notify = new Notification();  
				notify.icon = R.drawable.ic_launcher;  
				notify.tickerText = jsonObject.getString(Config.USERNAME);  
				notify.when = System.currentTimeMillis();  
				//n.flags=Notification.FLAG_ONGOING_EVENT;
	            Intent intentApp = new Intent(context, com.binqiang.sharewithu.CommunityActivity.class);
		        PendingIntent pi = PendingIntent.getActivity(context, 0, intentApp, 0);
		        notify.setLatestEventInfo(context, jsonObject.getString(Config.USERNAME), jsonObject.getString(Config.STR_CONTENT), pi);  
		        notify.defaults |= Notification.DEFAULT_SOUND;
		        notify.flags = Notification.FLAG_AUTO_CANCEL;
		        nm.notify(k, notify);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

	}

}
