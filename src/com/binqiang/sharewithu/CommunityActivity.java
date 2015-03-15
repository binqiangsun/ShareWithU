package com.binqiang.sharewithu;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import com.binqiang.ui.fragment.FragmentFactory;
import com.binqiang.util.Config;

public class CommunityActivity extends Activity implements  ActionBar.TabListener{
	
	private Context mContext;
	ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_main);
		mActionBar = getActionBar();
		
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.addTab(mActionBar.newTab().setText(Config.STR_SHARE)
				.setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText(Config.STR_FIND)
				.setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText(Config.STR_MYSELF)
				.setTabListener(this));
	}
	
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState.containsKey(Config.STR_SELECT_ITEM)){
			mActionBar.setSelectedNavigationItem(savedInstanceState.getInt(Config.STR_SELECT_ITEM));
		}
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(Config.STR_SELECT_ITEM, mActionBar.getSelectedNavigationIndex());
	}



	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		//
		Fragment fragment = FragmentFactory.getFragmentById(tab.getPosition());
		if(fragment == null){
			return;
		}
		Bundle args = new Bundle();
		args.putString(Config.USERNAME, getIntent().getStringExtra(Config.USERNAME));
		fragment.setArguments(args);
		FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
		fragmentTran.replace(R.id.community_container, fragment);
		fragmentTran.commit();
	}
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	

}
