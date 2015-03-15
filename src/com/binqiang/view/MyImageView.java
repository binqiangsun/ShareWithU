package com.binqiang.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;


/* by sun 2014-4-11
 * 实现ImageView图片预览-放大缩小功能
 * edited 2014-4-14
 * 改进：在ImageView范围内缩放，缩放范围控制。
 * */

public class MyImageView extends ImageView implements OnTouchListener {

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setOnTouchListener(this);
	}


	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setOnTouchListener(this);
	}


	public MyImageView(Context context) {
		super(context);
		setOnTouchListener(this);
	}
	
	
	Bitmap mBitmap;
	private int mMode;    //模式：1-拉动；2-缩放
	final private int DRAGMODE = 1;
	final private int ZOOMMODE = 2;
	
	private PointF   mStarPt = new PointF();
	private float    mStarDis;
	private PointF   mMidPt = new PointF();
	
	private Matrix mMatrix = new Matrix();
	private Matrix mCurrMatrix = new Matrix();
	
	private boolean m_IsHorCon = false;
	private boolean m_IsVerCon = false;
	
	private class ImageState
	{
		float left;
		float right;
		float top;
		float bottom;
		float width;    //图片的宽度
		float height;   //图片的高度
		
		float GetLeft(){ return left;}
		float GetRight(){ return right;}
		float GetTop(){ return top;}
		float GetBottom(){ return bottom;}
		float GetWidth(){ return width;}
		float GetHeight(){ return height;}
		void SetLeft(float l){left = l;}
		void SetRight(float r){right = r;}
		void SetTop(float t){top = t;}
		void SetBottom(float b){bottom  = b;}
		void SetWidth(float w){ width = w;}
		void SetHeight(float h){ height = h;}
	}
	
	
	//图片位置参数
	private ImageState mMapState = new ImageState();
	
	private float mScrWidth;    //ImageView宽度
	private float mScrHeight;   //ImageView高度
	private float mMaxWidth;
	private float mMaxHeight;
	private float mMinWidth;
	private float mMinHeight;
	
	public void SetImageInfo(Bitmap bm)
	{
		mBitmap = bm;
		this.setImageBitmap(bm);
		if(bm != null)
		{
			mMaxWidth = bm.getWidth()*2;
			mMaxHeight = bm.getHeight()*2;
			mMinWidth = bm.getWidth() / 2;
			mMinHeight = bm.getHeight() / 2;
		}
		else
		{
			mMaxWidth = 0;
			mMaxHeight = 0;
			mMinWidth = 0;
			mMinHeight = 0;
		}
		GetCurrentRange();
	}
	
	//get width and height of imageview in onWindowFocusChanged
	public void SetImageViewSize()
	{
		mScrWidth = this.getWidth();
		mScrHeight = this.getHeight();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction() & MotionEvent.ACTION_MASK)
		{
		//单指按下
		case MotionEvent.ACTION_DOWN:
		{
		    mMode = DRAGMODE;
		    mStarPt.set(event.getX(), event.getY());
		    mCurrMatrix = this.getImageMatrix();
		    Log.v("touch", "drag");
		    break;
		}
		//多指缩放的按下
		case MotionEvent.ACTION_POINTER_DOWN:
		{
			mMode = ZOOMMODE;
			mStarDis = GetDistance(event);
			mMidPt   = GetMidPt(event);
			mCurrMatrix = this.getImageMatrix();
			break;
		}
		//移动
		case MotionEvent.ACTION_MOVE:
		{
			if(mMode == DRAGMODE)
			{
				float left = 0, top = 0, right = 0, bottom = 0;
				float dx = event.getX() - mStarPt.x;
				float dy = event.getY() - mStarPt.y;
				left = mMapState.GetLeft() + dx;
				right = mMapState.GetRight() + dx;
				top  = mMapState.GetTop() + dy;
				bottom = mMapState.GetBottom() + dy;
				
				//图片宽度大于ImageView
				System.out.println("before: " + left + " " + m_IsHorCon);
				System.out.println("before: " + right);
				System.out.println("srcWidth: " + mScrWidth);
				if (m_IsHorCon) {
					//左移动，越界控制
					if (left > 0) {
						left = 0;
						dx = left - mMapState.GetLeft();
					}
					if (right < mScrWidth) {
						right = mScrWidth;
						dx = right - mMapState.GetRight();
					}
				}
				else {
					if(left < 0)
					{
						left = 0;
						dx = left - mMapState.GetLeft();
					}
					if(right > mScrWidth)
					{
						right = mScrWidth;
						dx = mScrWidth - mMapState.GetRight();
					}
						
				}
				System.out.println("after: " + left);
				System.out.println("after: " + right);
				if (m_IsVerCon) {
					if (top > 0) {
						dy = 0 - mMapState.GetTop();
					}

					if (bottom < mScrHeight) {
						dy = mScrHeight-mMapState.GetBottom();
					}
				} else {
					if (top < 0) {
						dy = 0 - mMapState.GetTop();
					}

					if (bottom > mScrHeight) {
						dy = mScrHeight - mMapState.GetBottom();
					}
				}
				
				if(Math.abs(dx) > mScrWidth)
					dx = mScrWidth;
				if(Math.abs(dy) > mScrHeight)
					dy = mScrHeight;
				
				mMatrix.set(mCurrMatrix);
				mMatrix.postTranslate(dx, dy);
				mStarPt.set(event.getX(), event.getY());
			}
			else if(mMode == ZOOMMODE)
			{
				float endDis = GetDistance(event);
				if (endDis > 10f)
				{
					float fScale = endDis /mStarDis;
					mMatrix.set(mCurrMatrix);
					if(fScale < 1)
					{
						if(mMapState.GetWidth() < mMinWidth && mMapState.GetHeight() < mMinHeight)
							return true;
					}
					else
					{
						if(mMapState.GetWidth() > mMaxWidth && mMapState.GetHeight() > mMaxHeight)
							return true;
					}
					
					mMatrix.postScale(fScale, fScale, mMidPt.x, mMidPt.y);
					
					//String strMsg = String.format("%d, %d", mImageView.getWidth(), mBmp.getWidth());
					//Log.v("touch", strMsg);
					mStarDis = endDis;
				}
			}
			this.setImageMatrix(mMatrix);
			this.refreshDrawableState();
			break;
		}
		//单指抬起
		case MotionEvent.ACTION_UP:
		//多指抬起
		case MotionEvent.ACTION_POINTER_UP:
		{
			mMode = 0;
			break;
		}
		
		}
		
		GetCurrentRange();
		//判断是否越界了
		if(mMapState.GetWidth() > mScrWidth)
			m_IsHorCon = true;
		else
			m_IsHorCon = false;
		if(mMapState.GetHeight() > mScrHeight)
			m_IsVerCon = true;
		else
			m_IsVerCon = false;
		return true;
	}
	
	private float GetDistance(MotionEvent event)
	{
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx*dx+dy*dy);
	}
	
	  
    private PointF GetMidPt(MotionEvent event) {  
        float midX = (event.getX(1) + event.getX(0)) / 2;  
        float midY = (event.getY(1) + event.getY(0)) / 2;  
        return new PointF(midX, midY);  
    }  
    
    
    //然后获取ImageView的matrix，根据matrix的getValues获得3x3矩阵。

    public void GetCurrentRange()
    {
    	Matrix matrix = this.getImageMatrix();
		Rect rect = this.getDrawable().getBounds();
		
		float[] values = new float[9];
		matrix.getValues(values);
		
		mMapState.SetLeft(values[2]);
		mMapState.SetTop(values[5]);
		if(Math.abs(mMapState.GetTop()) > mScrHeight)
		{
		}
		mMapState.SetWidth(rect.width()*values[0]);
		mMapState.SetHeight(rect.height()*values[0]);
		float right = mMapState.GetLeft() + mMapState.GetWidth();
		float bottom = mMapState.GetTop() + mMapState.GetHeight();
		mMapState.SetRight(right);
		if(bottom == mMapState.GetTop())
		{
		}
		mMapState.SetBottom(bottom);
    }

}


