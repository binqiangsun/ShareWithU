package com.binqiang.sharewithu;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.binqiang.ui.fragment.ContactFragment;
import com.binqiang.ui.fragment.FriendsFragment;
import com.binqiang.ui.fragment.MapFragment;
import com.binqiang.ui.fragment.SetFragment;
import com.binqiang.util.Config;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	Intent    mIntent;
	public static Context g_context = null;
	private Context mContext;
	private Button[] mTabs;
	private android.app.Fragment mapFragment;
	private FriendsFragment friendFragment;
	private ContactFragment contactFragment;
	private SetFragment     setFragment;
	private Fragment[] fragments;
	private int index;
	private int currentTabIndex;
	
	ImageView iv_recent_tips,iv_contact_tips;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SDKInitializer.initialize(getApplicationContext());
		initial();
		Is_Login();
		
		//主界面的菜单
		//MainMenu mainMenu = new MainMenu(this.getApplicationContext());
		//mainMenu.SetMainMenu();
		initView();
		initTab();
	}

	private void initView(){
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_main_act_map);
		mTabs[1] = (Button) findViewById(R.id.btn_main_act_contract);
		mTabs[2] = (Button) findViewById(R.id.btn_main_act_find);
		mTabs[3] = (Button) findViewById(R.id.btn_main_act_set);
		iv_contact_tips = (ImageView)findViewById(R.id.iv_contact_tips);
		//把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}
	
	private void initTab(){
		mapFragment = new MapFragment();
		friendFragment = new FriendsFragment();
		contactFragment = new ContactFragment();
		setFragment =     new SetFragment();
		fragments = new Fragment[] {mapFragment, contactFragment, friendFragment, setFragment};
		// 添加显示第一个fragment
		FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
		fragmentTran.replace(R.id.container_main_act_fragment, mapFragment);
		fragmentTran.commit();
		currentTabIndex = 0;
	}
	
	
	
	/**
	 * button点击事件
	 * @param view
	 */
	public void onTabSelect(View view) {
		switch (view.getId()) {
		case R.id.btn_main_act_map:
			index = 0;
			break;
		case R.id.btn_main_act_contract:
			index = 1;
			break;
		case R.id.btn_main_act_find:
			index = 2;
			break;
		default:
			index = 3;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.container_main_act_fragment, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		//把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	
	private void initial()
	{
		mContext = g_context = this;
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		Config.setDeviceId(TelephonyMgr.getDeviceId()); 
		Config.initCache();
	}
	
	private void Is_Login(){
		//login or not
		if(!Config.IsLogin(this)){
			mIntent = new Intent(MainActivity.this, LoginActivity.class);
			startActivityForResult(mIntent, Config.ACT_SEND_KEY, null);
		}
	}


	@Override
	protected void onDestroy() {
		Config.clearCache();
		super.onDestroy();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_refresh){
			friendFragment.refreshData();
		}
		else if(item.getItemId() == R.id.action_upload){
			/*mIntent = new Intent(MainActivity.this, SettingActivity.class);
			mIntent.putExtra(Config.SER_KEY, Config.get_gps_service_switch());
			startActivityForResult(mIntent, Config.ACT_SEND_KEY, null);*/
			
			new AlertDialog.Builder(mContext)
			.setSingleChoiceItems(R.array.get_photo, 0, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int position) {
					if(position == 0){
						//take photos
						Intent intent = new Intent(mContext, CameraActivity.class);
						mContext.startActivity(intent);
					}
					else{
						//open the photo selector.
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"), Config.PICK_IMAGE);
					}
				}
	        })
	        .show();
		}
		else if(item.getItemId() == R.id.action_friends){
			mIntent = new Intent(MainActivity.this, FriendsActivity.class);
			startActivity(mIntent);
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Config.ACT_SEND_KEY && resultCode == Config.ACT_RET_KEY)
        {
            boolean retIsOn = data.getBooleanExtra(Config.SER_KEY, false);
            if(retIsOn != Config.get_gps_service_switch())
            {
            	//myLocationManager.stopGps();
            }
            Is_Login();
        }
		else if(requestCode == Config.PICK_IMAGE && data != null && data.getData() != null) {
	        Uri _uri = data.getData();

	        //User had pick an image.
	        Cursor cursor = getContentResolver().query(_uri, new String[] 
	        		{ android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	        cursor.moveToFirst();

	        //Link to the image
	        final String imageFilePath = cursor.getString(0);
	        cursor.close();
	        
	        //
	        mIntent = new Intent(MainActivity.this, MyImageViewActivity.class);
			mIntent.putExtra(Config.KEY_IMAGE_EXTRA, imageFilePath);
			startActivity(mIntent);
	        
	        
	    }
	}
	
	
}
