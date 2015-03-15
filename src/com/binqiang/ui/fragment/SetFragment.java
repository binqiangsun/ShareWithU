package com.binqiang.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import cn.bmob.v3.BmobUser;

import com.binqiang.sharewithu.R;
import com.binqiang.util.Config;

public class SetFragment extends Fragment {


	Switch    switchGps;
	Button    btnLogout;
	Intent    mServiceIntent;
	boolean      mIsGpsOn;
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_setting, container, false);
		
		mIsGpsOn = Config.get_gps_service_switch();
		switchGps = (Switch) rootView.findViewById(R.id.switch_gps);
		btnLogout = (Button) rootView.findViewById(R.id.btn_logout);
		switchGps.setChecked(mIsGpsOn);
		switchGps.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton btn, boolean is_open) {
				// TODO Auto-generated method stub
				if(is_open){
					mIsGpsOn = true;
					Config.set_gps_service_switch(true);
				}
				else{
					mIsGpsOn = false;
					Config.set_gps_service_switch(false);
				}
			}
			
		});
		btnLogout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				BmobUser.logOut(getActivity().getApplicationContext());
				getActivity().finish();
			}
			
		});
		
		
		return rootView;
	}
	
	

}
