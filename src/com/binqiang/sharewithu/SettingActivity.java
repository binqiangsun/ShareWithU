package com.binqiang.sharewithu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import cn.bmob.v3.BmobUser;

import com.binqiang.sharewithu.R;
import com.binqiang.util.Config;

public class SettingActivity extends Activity {


	Switch    switchGps;
	Button    btnLogout;
	Intent    mServiceIntent;
	Context   mCon = this;
	boolean      mIsGpsOn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		mIsGpsOn = getIntent().getBooleanExtra(Config.SER_KEY, false);
		
		switchGps = (Switch) findViewById(R.id.switch_gps);
		btnLogout = (Button) findViewById(R.id.btn_logout);
		switchGps.setChecked(mIsGpsOn);
		switchGps.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton btn, boolean is_open) {
				// TODO Auto-generated method stub
				if(is_open){
					mIsGpsOn = true;
				}
				else{
					mIsGpsOn = false;
				}
			}
			
		});
		btnLogout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				BmobUser.logOut(getApplicationContext());
				finish();
			}
			
		});
	}
	

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
        intent.putExtra(Config.SER_KEY, mIsGpsOn);
        setResult(Config.ACT_RET_KEY, intent);
		super.finish();
	}

	
	
	
	

}
