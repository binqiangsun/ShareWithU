package com.binqiang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ActionItem {
	public Drawable mDrawable;
	public CharSequence mTitle;

	public ActionItem(Drawable drawable, CharSequence title) {
		this.mDrawable = drawable;
		this.mTitle = title;
	}

	public ActionItem(CharSequence title) {
		this.mDrawable = null;
		this.mTitle = title;
	}

	public ActionItem(Context context, int titleId, int drawableId) {
		this.mTitle = context.getResources().getText(titleId);
		this.mDrawable = context.getResources().getDrawable(drawableId);
	}

	public ActionItem(Context context, String title, int drawableId) {
		this.mTitle = title;
		this.mDrawable = context.getResources().getDrawable(drawableId);
	}

	public void setItemTv(CharSequence tv) {
		mTitle = tv;
	}
}
