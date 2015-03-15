package com.binqiang.util;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.Surface;

import com.binqiang.sharewithu.CameraActivity;

public class PhotoConfig {

	public static Size getMaxPixel(Parameters params, boolean preview) {
		List<Camera.Size> sizeList = null;
		if (preview) {
			sizeList = params.getSupportedPreviewSizes();
		} else {
			sizeList = params.getSupportedPictureSizes();
		}

		Iterator<Camera.Size> itor1 = sizeList.iterator();
		Size maxSize = itor1.next();
		while (itor1.hasNext()) {
			Camera.Size cur = itor1.next();
			if (cur.width >= maxSize.width && cur.height >= maxSize.height)
				maxSize = cur;
		}
		return maxSize;
	}

	// 相机设置方向，保证翻转不会影响预览
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}


	

}
