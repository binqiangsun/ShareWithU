package com.binqiang.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelfFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		
		textView.setText("功能还未完成o_o");
		textView.setTextSize(30);
		
		return textView;
		
	}
	
	

}
