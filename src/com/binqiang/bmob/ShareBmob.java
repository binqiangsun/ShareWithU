package com.binqiang.bmob;

import android.content.Context;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

public class ShareBmob {
	
	
	public static void updateImageTable(Context context, String userName, String imagePath, String detailInfo){
		
		SharedInfo sharedInfo = new SharedInfo();
		sharedInfo.setUserName(userName);
		sharedInfo.setImagePath(imagePath);
		sharedInfo.setDetailInfo(detailInfo);
		
		sharedInfo.save(context, new SaveListener(){

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("insert data failed" + arg1);
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				System.out.println("insert data successful");
			}
			
		});
	}

}
