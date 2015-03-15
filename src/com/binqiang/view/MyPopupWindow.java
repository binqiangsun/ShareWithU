package com.binqiang.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.binqiang.sharewithu.R;

public class MyPopupWindow extends PopupWindow {

	
	private TextView tvPraise;
	private TextView tvComment;

	private Context mContext;
	
	private int     mObjId;

	protected final int LIST_PADDING = 10;

	
	private Rect mRect = new Rect();

	
	private final int[] mLocation = new int[2];

	
	private boolean mIsDirty;
	
	private OnItemOnClickListener mItemOnClickListener;


	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

	public MyPopupWindow(Context context) {
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	public MyPopupWindow(Context context, int width, int height) {
		this.mContext = context;


		setFocusable(true);

		setTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setOutsideTouchable(true);

		setWidth(width);
		setHeight(height);

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.comment_popu, null);
		setContentView(view);
		
		tvPraise = (TextView) view.findViewById(R.id.popu_praise);
		tvComment = (TextView) view.findViewById(R.id.popu_comment);
		tvPraise.setOnClickListener(onclick);
		tvComment.setOnClickListener(onclick);
	}

	//position:list列表中的位置
	public void show(final View view, int objId) {
		mObjId = objId;
		view.getLocationOnScreen(mLocation);
		
		tvPraise.setText(mActionItems.get(0).mTitle);

		showAtLocation(view, Gravity.NO_GRAVITY, mLocation[0] - this.getWidth()
				- 10, mLocation[1] - ((this.getHeight() - view.getHeight()) / 2));
	}

	OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			switch (v.getId()) {
			case R.id.popu_comment:
				mItemOnClickListener.onItemClick(mActionItems.get(1), mObjId, 1);
				break;
			case R.id.popu_praise:
				mItemOnClickListener.onItemClick(mActionItems.get(0), mObjId, 0);
				break;
			}
		}

	};

	/**
	 * ���������
	 */
	public void addAction(ActionItem action) {
		if (action != null) {
			mActionItems.add(action);
			mIsDirty = true;
		}
	}

	/**
	 * ���������
	 */
	public void cleanAction() {
		if (mActionItems.isEmpty()) {
			mActionItems.clear();
			mIsDirty = true;
		}
	}


	public ActionItem getAction(int position) {
		if (position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}

	public void setItemOnClickListener(
			OnItemOnClickListener onItemOnClickListener) {
		this.mItemOnClickListener = onItemOnClickListener;
	}
	
	public static interface OnItemOnClickListener {
		public void onItemClick(ActionItem item, int objId, int position);
	}
}
