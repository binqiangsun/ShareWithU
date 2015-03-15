package com.binqiang.ui.fragment;


import android.app.Fragment;

public class FragmentFactory {
	
	public static Fragment getFragmentById(int pos){
		Fragment resultFragment = null;
		switch(pos){
		case 0:
			resultFragment = new FriendsFragment();
			break;
		default:
			resultFragment = new SelfFragment();
			break;
		}
		return resultFragment;
	}

}
