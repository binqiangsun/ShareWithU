package com.binqiang.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.binqiang.sharewithu.CameraActivity;
import com.binqiang.sharewithu.CommunityActivity;
import com.binqiang.sharewithu.R;
import com.binqiang.sharewithu.SettingActivity;
import com.binqiang.util.Config;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainMenu {
	
	private Context mContext;
	private Intent  mIntent;
	
	public MainMenu(Context context){
		mContext = context;            //context should be an Activity
	}
	
	
	public void SetMainMenu(){
		ImageView icon = new ImageView(mContext); // Create an icon
		icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.button_action_blue));

		FloatingActionButton actionButton = new FloatingActionButton.Builder((Activity)mContext)
		                                        .setContentView(icon)
		                                        .build();
		// repeat many times:
		SubActionButton buttonGps = GetSubActionButton(R.drawable.ic_action_location_found, Config.ID_BTN_GPS);
		SubActionButton buttonPla = GetSubActionButton(R.drawable.ic_action_place, Config.ID_BTN_PLA);
		SubActionButton buttonPic = GetSubActionButton(R.drawable.ic_action_picture,  Config.ID_BTN_PIC);
		SubActionButton buttonCom = GetSubActionButton(R.drawable.ic_action_important,  Config.ID_BTN_COM);
		SubActionButton buttonSet = GetSubActionButton(R.drawable.ic_action_settings,  Config.ID_BTN_SET);
		FloatingActionMenu actionMenu = new FloatingActionMenu.Builder((Activity)mContext)
        .addSubActionView(buttonGps)
        //.addSubActionView(buttonPla)
        .addSubActionView(buttonPic)
        .addSubActionView(buttonCom)
        .addSubActionView(buttonSet)
        .attachTo(actionButton)
        .build();
		
		//set click listener
		buttonGps.setOnClickListener(listener);
		buttonPla.setOnClickListener(listener);
		buttonPic.setOnClickListener(listener);
		buttonCom.setOnClickListener(listener);
		buttonSet.setOnClickListener(listener);
	}
	
	private OnClickListener listener = new OnClickListener(){

		@Override
		public void onClick(View view) {
			if(view.getId() == Config.ID_BTN_PLA){
			}
			else if(view.getId() == Config.ID_BTN_SET){
				mIntent = new Intent(mContext, SettingActivity.class);
				mIntent.putExtra(Config.SER_KEY, Config.get_gps_service_switch());
				((Activity) mContext).startActivityForResult(mIntent, Config.ACT_SEND_KEY, null);
			}
			else if(view.getId() == Config.ID_BTN_PIC){
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
			else if(view.getId() == Config.ID_BTN_COM){
				mIntent = new Intent(mContext, CommunityActivity.class);
				((Activity) mContext).startActivity(mIntent);
			}
			else if(view.getId() == Config.ID_BTN_GPS){
				MapConfig.NavigationGps();
			}
		}
		
	};
	
	private SubActionButton GetSubActionButton(int resourceId, int btnId){
		SubActionButton.Builder itemBuilder = new SubActionButton.Builder((Activity)mContext);
		// repeat many times:
		ImageView itemIcon = new ImageView(mContext);
		itemIcon.setImageDrawable(mContext.getResources().getDrawable(resourceId)); 
		SubActionButton retButton = itemBuilder.setContentView(itemIcon).build();
		retButton.setId(btnId);
		return retButton;
	}

}
