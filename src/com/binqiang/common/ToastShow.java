package com.binqiang.common;

import android.content.Context;
import android.widget.Toast;

public class ToastShow {
	
	public static void ShowMsg(Context context, String strMsg){
		Toast.makeText(context, strMsg, Toast.LENGTH_SHORT).show();
	}

}
