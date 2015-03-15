package com.binqiang.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.ImageView;

import cn.bmob.v3.datatype.BmobFile;

import com.binqiang.util.Config;

public class ImageService {
	
	
	


	//cache is a folder of cache files.
	public static String getImageFromHttp(Context context, String urlPath, File cacheFile) throws IOException{
		String name = urlPath.substring(urlPath.lastIndexOf("/") + 1);
		File file = new File(cacheFile, name);
		if(!file.exists())
		{
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(Config.HTTP_TIME_OUT);
			conn.setRequestMethod(Config.HTTP_GET);
			conn.setDoInput(true);
			if(conn.getResponseCode() == Config.HTTP_SUCCESS_CODE){
				
				InputStream inputStream = conn.getInputStream();
				FileOutputStream fileOs = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = inputStream.read(buffer)) != Config.READ_FAIL_CODE){
					fileOs.write(buffer, 0, len);
				}
				inputStream.close();
				fileOs.close();
				
			}
		}
		return file.getAbsolutePath();
	}
	
	
	
	
	//decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f, int imgWidth, int imgHeight){
	    try {
	        //Decode image size
	        BitmapFactory.Options option1 = new BitmapFactory.Options();
	        option1.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,option1);

	        int scale=2;
	        while(option1.outWidth/scale/2>=imgWidth && option1.outHeight/scale/2>=imgHeight)
	            scale*=2;

	        
	        BitmapFactory.Options option2 = new BitmapFactory.Options();
	        option2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, option2);
	    } catch (FileNotFoundException e) {}
	    return null;
	}
	
	
	public static Bitmap writeTextOnBitmap(Bitmap bitmap, String text) {
		if(text.isEmpty()){
			return bitmap;
		}
		Canvas canvas = new Canvas(bitmap);
		Typeface font = null;
		font = Typeface.create(font, Typeface.BOLD);
		Paint paint = new Paint();
		paint.setTypeface(font);
		paint.setAntiAlias(true);
		paint.setColor(Color.GRAY);
		paint.setStyle(Style.FILL);
		paint.setTextAlign(Paint.Align.LEFT);
		int fontSize = getTextFontSize(text, paint, (int)(bitmap.getWidth()*0.9), (int)(bitmap.getHeight()*0.9));
		paint.setTextSize(fontSize);
		canvas.drawText(text, 0,
				(float)(bitmap.getHeight()*0.8), paint);  
		return bitmap;
		}
	
	public static int getTextFontSize(String text, Paint paint, int width, int height){

		int text_check_w = 0;
		int text_check_h = 0;

		int incr_text_size = 1;
		boolean found_desired_size = true;
		Rect bounds = new Rect();
		while (found_desired_size){
		    paint.setTextSize(incr_text_size);// have this the same as your text size

		    paint.getTextBounds(text, 0, text.length(), bounds);

		    text_check_h =  bounds.height();
		    text_check_w =  bounds.width();
		    incr_text_size++;

		    if (width <= text_check_w || height <= text_check_h){
		    	found_desired_size = false;
			}
		}
		return incr_text_size;
	}




	public static void setImageView(Context context, String url,
			ImageView imageview, int width, int height) {
		BmobFile bmobFile = new BmobFile(null);
		bmobFile.loadImageThumbnail(context, imageview, width, height);
	}

}
