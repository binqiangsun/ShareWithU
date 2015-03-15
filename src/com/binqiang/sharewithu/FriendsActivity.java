package com.binqiang.sharewithu;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.binqiang.adapter.FriendsAdapter;
import com.binqiang.common.ToastShow;
import com.binqiang.util.Config;

public class FriendsActivity extends Activity {
	
	private Context mContext;
	private FriendsAdapter mAdapter;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);
		mContext = this;
		mAdapter = new FriendsAdapter(mContext);
		mListView = (ListView) findViewById(R.id.listview_friend);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemLongClickListener(itemLongClickListener);
		
		InitialFriends();
	}

	
	
	
	//get all friends
	private void InitialFriends() {
		BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
		query.order(Config.FIELD_CREATEAT);
		query.findObjects(MainActivity.g_context, new FindListener<BmobUser>(){

			@Override
			public void onError(int arg0, String strErr) {
				ToastShow.ShowMsg(mContext, strErr);
			}

			@Override
			public void onSuccess(List<BmobUser> listUser) {
				mAdapter.addAllListUser(listUser);
			}
		});
		
	}
	
	private  OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View arg1, int arg2,
			long arg3) {
		
		final View registerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_dialog_register, null);
		final TextView textView = (TextView) registerView.findViewById(R.id.et_mail);
		textView.setHint(Config.INPUT_TELEPHONE);
		
		new AlertDialog.Builder(mContext)
		.setView(registerView)
        .setTitle(Config.ALTER_DIALOG_TITLE_GPS)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(Config.ensureTeleNum(textView.getText().toString())){
					Config.setGpsTelePhone(textView.getText().toString());
					ToastShow.ShowMsg(mContext, Config.SHOW_MSG_SET_SUCCESS);
				}
				else{
					ToastShow.ShowMsg(mContext, Config.SHOW_MSG_TELE_NUM_FAILED);
				}
			}
        	
        }).show();
		return false;
	}
	
	};
	
	
	

}
