package com.binqiang.sharewithu;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.binqiang.bmob.ShareBmob;
import com.binqiang.common.ToastShow;
import com.binqiang.service.ImageService;
import com.binqiang.util.Config;
import com.binqiang.view.MyImageView;

public class MyImageViewActivity extends Activity {
	
	MyImageView myImageView;
	ProgressBar mProgressBar;
	String      mFilePath;
	Context     mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageview);
        mContext = this;
        myImageView = (MyImageView) findViewById(R.id.myimageview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_img);
        mProgressBar.setVisibility(View.INVISIBLE);
		
        mFilePath = getIntent().getStringExtra(Config.KEY_IMAGE_EXTRA);
        System.out.println(mFilePath);
        Display display = getWindowManager().getDefaultDisplay(); 
        Point point = new Point();
        display.getSize(point);
        int width = point.x;  // deprecated
        int height = point.y;  // deprecated
        Bitmap bitmap = ImageService.decodeFile(new File(mFilePath), width, height);
		if(bitmap == null){
			ToastShow.ShowMsg(Config.getAppContext(), Config.FAILED_GET_IMAGE);
		}
		
		myImageView.SetImageInfo(bitmap);
	}
	
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		myImageView.SetImageViewSize();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_img, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == R.id.action_upload){
			mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
			final BmobFile bmobFile = new BmobFile(new File(mFilePath));
	        bmobFile.uploadblock(Config.getAppContext(), new UploadFileListener() {

	            @Override
	            public void onSuccess() {
	            	mProgressBar.setVisibility(View.INVISIBLE);
	            	String imagePath = bmobFile.getFileUrl(Config.getAppContext());
	                ToastShow.ShowMsg(Config.getAppContext(), ("上传图片成功:" + imagePath));
	                ShareBmob.updateImageTable(Config.getAppContext(), Config.g_user_name, imagePath, null);
	                
	            }

	            @Override
	            public void onProgress(Integer value) {
	            	System.out.println(value);
	            	mProgressBar.setProgress(value);
	            }

	            @Override
	            public void onFailure(int code, String msg) {
	            	mProgressBar.setVisibility(View.INVISIBLE);
	            	ToastShow.ShowMsg(Config.getAppContext(), ("上传图片失败:" + bmobFile.getFileUrl(Config.getAppContext())));
	            }
	        });
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	

}
