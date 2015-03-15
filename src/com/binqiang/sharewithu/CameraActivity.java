package com.binqiang.sharewithu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.binqiang.util.Config;
import com.binqiang.util.PhotoConfig;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

	
	private Activity mActivity;
	
	private File homeDir; 
	private int mCurCamId = CameraInfo.CAMERA_FACING_BACK;
	private boolean focus = false;

	private Camera myCamera;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private ScaleGestureDetector myGesDet;
	private AutoFocusCallback myAutoFocusCallback; 

	private ImageButton btnCollect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//without title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		mActivity = this;

		mSurfaceView = (SurfaceView) findViewById(R.id.preview);
		btnCollect = (ImageButton) findViewById(R.id.take_picture);

		

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setKeepScreenOn(true);

		myAutoFocusCallback = new AutoFocusCallback() {

			public void onAutoFocus(boolean success, Camera camera) {
			}
		};

		homeDir = Config.getMediaFile();
		if (!homeDir.exists() || !homeDir.isDirectory()) {
			if(!homeDir.mkdirs())
				return;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		myGesDet.onTouchEvent(event);
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			focus = true;
			break;
		case MotionEvent.ACTION_UP:
			if (!focus)
				break;
			else {
				GetFocus(event.getRawX(), event.getRawY());
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			focus = false;
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		createCamera();
		GetFocus(mSurfaceView.getWidth() / 2, mSurfaceView.getHeight() / 2);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		createCamera();
		GetFocus(mSurfaceView.getWidth() / 2, mSurfaceView.getHeight() / 2);
	}

	public void createCamera() {
		try {
			if (myCamera == null) {
				myCamera = Camera.open(mCurCamId);

				Camera.Parameters param = myCamera.getParameters();
				String info = param.flatten();
				param.setPictureFormat(ImageFormat.JPEG);
				param.getSupportedPictureSizes();
				Size maxSize = PhotoConfig.getMaxPixel(param, false);
				Size maxPreviewSize = PhotoConfig.getMaxPixel(param, true);
				Log.d("cam",
						"previewsize: " + String.valueOf(maxPreviewSize.width)
								+ " " + String.valueOf(maxPreviewSize.height));
				param.setPictureSize(maxSize.width, maxSize.height);
				param.setPreviewSize(maxPreviewSize.width,
						maxPreviewSize.height);
				param.setJpegQuality(100);
				Log.v("Info", info);
				LayoutParams layoutParams = mSurfaceView.getLayoutParams();

				Point outSize = new Point();
				getWindowManager().getDefaultDisplay().getSize(outSize);

				Log.d("cam", "outsize: " + String.valueOf(outSize.x) + " "
						+ String.valueOf(outSize.y));
				
				if ((outSize.x / outSize.y) > (maxPreviewSize.width / maxPreviewSize.height)) {
					layoutParams.height = outSize.x * maxPreviewSize.height
							/ maxPreviewSize.width;
					layoutParams.width = outSize.x;
				} else {
					layoutParams.width = outSize.y * maxPreviewSize.width
							/ maxPreviewSize.height;
					layoutParams.height = outSize.y;
				}
				Log.d("cam",
						"layout params: " + String.valueOf(layoutParams.width)
								+ " " + String.valueOf(layoutParams.height));
				mSurfaceView.setLayoutParams(layoutParams);
				myCamera.setParameters(param);
				myCamera.setPreviewDisplay(mSurfaceView.getHolder());
				myCamera.startPreview();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void takePicture(View v) throws InterruptedException {
		if (myCamera != null) {
			try {
				btnCollect.setClickable(false);
				
				myCamera.takePicture(null, null, new MyPictureCallback());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				btnCollect.setClickable(true);
			}
		}
	}

	
	public void ChangeCamera(View v) {
		int cameraCount = 0;
		cameraCount = Camera.getNumberOfCameras();
		System.out.println(cameraCount);
		mCurCamId = (mCurCamId == CameraInfo.CAMERA_FACING_FRONT)?CameraInfo.CAMERA_FACING_BACK:CameraInfo.CAMERA_FACING_FRONT;
		myCamera.stopPreview();
		myCamera.release();
		myCamera = null;
		myCamera = Camera.open(mCurCamId);
		try {
			myCamera.setPreviewDisplay(mSurfaceHolder);
		} catch (IOException e) {

			e.printStackTrace();
		}
		myCamera.startPreview();
	}

	private final class MyPictureCallback implements Camera.PictureCallback {

		@Override
		public void onPictureTaken(byte[] stream, Camera arg1) {
			
			try {
				File jpgFile = new File(homeDir,
						Config.GetCurrentTimeString() + ".jpg");
				FileOutputStream fos = new FileOutputStream(jpgFile);
				fos.write(stream);
				fos.close();

				String strJpgName = jpgFile.getAbsolutePath();
				Intent intent = new Intent(mActivity, MyImageViewActivity.class);
				intent.putExtra(Config.KEY_IMAGE_EXTRA, strJpgName);
				startActivity(intent);
				mActivity.finish();
			} catch (IOException e) {
				myCamera.startPreview();
				e.printStackTrace();
				Log.v("Info", e.getMessage());
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("cam", "surfaceDestroyed");
		if (myCamera != null) {
			myCamera.stopPreview();
			myCamera.release();
			myCamera = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}




	public void GetFocus(double touchX, double touchY) {
		double surWidth = mSurfaceView.getWidth();
		double surHeight = mSurfaceView.getHeight();
		double focusAreaSize = 20;
		int areaSize = (int) (focusAreaSize / surWidth * 2000);
		
		if (touchX > surWidth - focusAreaSize || touchX < focusAreaSize
				|| touchY > surHeight - focusAreaSize || touchY < focusAreaSize)
			return;
		if (myCamera != null) {
			Parameters params = myCamera.getParameters();
			int centerX = (int) (touchX / surWidth * 2000 - 1000);
			int centerY = (int) (touchY / surHeight * 2000 - 1000);
			Rect rect = new Rect(centerX - areaSize, centerY - areaSize,
					centerX + areaSize, centerY + areaSize);

			List<Area> focusAreas = new ArrayList<Camera.Area>();
			focusAreas.add(new Area(rect, 1));
			if (params.getMaxNumFocusAreas() > 0) {
				params.setFocusMode(Parameters.FLASH_MODE_AUTO);
				params.setFocusAreas(focusAreas);
			}

			myCamera.setParameters(params);
			myCamera.autoFocus(myAutoFocusCallback);
		}
	}

}
