package com.binqiang.bmob;

import android.content.Context;
import cn.bmob.v3.BmobObject;

import com.binqiang.util.Config;

public class CommentMsg extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String belongUserName;
	String content;
	SharedInfo sharedInfo;
	public String getBelongUserName() {
		return belongUserName;
	}
	public void setBelongUserName(String belongUserName) {
		this.belongUserName = belongUserName;
	}
	public SharedInfo getDestSharedInfo() {
		return sharedInfo;
	}
	public void setDestSharedInfo(SharedInfo destSharedInfo) {
		this.sharedInfo = destSharedInfo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public static CommentMsg saveData(Context context, String content,
			SharedInfo sharedInfo){


		CommentMsg commentMsg = new CommentMsg();
		commentMsg.setBelongUserName(Config.g_user_name);
		commentMsg.setContent(content);
		commentMsg.setDestSharedInfo(sharedInfo);
		
		commentMsg.save(context);
		return commentMsg;
	}

}
