package com.binqiang.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class SharedInfo extends BmobObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String userName;
	String imagePath;
	String detailInfo;
	BmobFile imageFile;
	BmobRelation commentMsg;
	
	public BmobRelation getCommentMsg() {
		return commentMsg;
	}
	public void setCommentMsg(BmobRelation commentMsg) {
		this.commentMsg = commentMsg;
	}
	public BmobFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(BmobFile imageFile) {
		this.imageFile = imageFile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getDetailInfo() {
		return detailInfo;
	}
	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}
	

}
