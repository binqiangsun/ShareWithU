package com.binqiang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.binqiang.sharewithu.R;

public class MyRefreshListView extends ListView {
	
	View header; //

	public MyRefreshListView(Context context) {
		super(context);
		InitView(context);
	}

	public MyRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitView(context);
	}

	public MyRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitView(context);
	}
	
	/*
	 * 
	 * 
	 * */
	private void InitView(Context context){
		LayoutInflater inflate = LayoutInflater.from(context);
		header = inflate.inflate(R.layout.list_header, null);
		this.addView(header);
	}
	

}
