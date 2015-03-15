package com.binqiang.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.binqiang.sharewithu.CommunityActivity;
import com.binqiang.sharewithu.R;

public class PopupEditText extends PopupWindow {
	
	Context mContext;
	
	OnSendClickListener mSendClickListener;
	private final int[] mLocation = new int[2];
	EditText  mEditText;
	Button    mSendBtn;
	Rect mRect;
	
	public PopupEditText(Context context) {
		
		this(context, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.popup_edittext, null);
		setContentView(view);
	}

	public PopupEditText(Context context, int width, int height) {
		this.mContext = context;


		setFocusable(true);

		setTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setOutsideTouchable(true);

		setWidth(width);
		setHeight(height);

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.popup_edittext, null);
		setContentView(view);
		
		mEditText = (EditText)view.findViewById(R.id.et_comment);
		mSendBtn = (Button)view.findViewById(R.id.btn_comment_send);
		mSendBtn.setOnClickListener(onclick);
	}
	
	public void show(final View view) {
		
		showAtLocation(view, Gravity.NO_GRAVITY, 0, 925);
		
		(new Handler()).postDelayed(new Runnable() {

            public void run() {
            	mEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
            	mEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));                       

            }
        }, 100);
		
	}
	
	OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			mSendClickListener.onItemClick(mEditText.getText().toString());
			mEditText.setText("");
		}

	};
	
	public void setSendOnClickListener(OnSendClickListener onSendClickListener) {
		this.mSendClickListener = onSendClickListener;
	}
	
	public static interface OnSendClickListener{
		public void onItemClick(String content);
	}

}
