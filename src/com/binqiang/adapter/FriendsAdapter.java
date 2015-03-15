package com.binqiang.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.binqiang.adapter.CommunityAdapter.ListCell;
import com.binqiang.sharewithu.R;

public class FriendsAdapter extends BaseAdapter {

	
	private Context mContext;
	private List<BmobUser> mListUser = new ArrayList<BmobUser>();
	
	public FriendsAdapter(Context context){
		mContext = context;
	}
	
	public void addAllListUser(List<BmobUser> listUser){
		mListUser.addAll(listUser);
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		return mListUser.size();
	}

	@Override
	public Object getItem(int position) {
		return mListUser.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_friends, null);
			convertView.setTag(new CommunityAdapter.ListCell((TextView)convertView.findViewById(R.id.friends_user_name),
					null, null));
		}
		
		ListCell listCell = (ListCell)convertView.getTag();
		TextView tv = listCell.GetTextView();
		
		tv.setText(mListUser.get(position).getUsername());
		
		return convertView;
	}

}


