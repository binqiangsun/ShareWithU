package com.binqiang.adapter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.binqiang.bmob.CommentMsg;
import com.binqiang.bmob.SharedInfo;
import com.binqiang.service.ImageService;
import com.binqiang.sharewithu.CommunityActivity;
import com.binqiang.sharewithu.MyImageViewActivity;
import com.binqiang.sharewithu.R;
import com.binqiang.ui.fragment.FriendsFragment;
import com.binqiang.util.Config;
import com.binqiang.view.ActionItem;
import com.binqiang.view.MyPopupWindow;

public class CommunityAdapter extends BaseAdapter {

	
	List<SharedInfo> mListSharedInfo = new ArrayList<SharedInfo>();
	Map<String, List<CommentMsg>> mMapCommentMsg = new HashMap<String, List<CommentMsg>>();
	Context mContext = null;
	private MyPopupWindow popupWindow;
	private boolean bUpdateSharedInfo = false;
	private boolean bUpdateCommentMsg = false;
	
	
	public CommunityAdapter(FriendsFragment friendsFragment){
		mContext = friendsFragment.getActivity();
		popupWindow = new MyPopupWindow(mContext, Config.dip2px(mContext, 165), Config.dip2px(
				mContext, 40));
		popupWindow
				.addAction(new ActionItem(mContext, "赞", R.drawable.circle_praise));
		
		popupWindow.addAction(new ActionItem(mContext, "评论",
				R.drawable.circle_comment));
		popupWindow.setItemOnClickListener(friendsFragment);
	}
	
	
	public void addAllListSharedInfo(List<SharedInfo> listSharedInfo){
		mListSharedInfo.addAll(listSharedInfo);
		bUpdateSharedInfo = true;
		notifyDataSet();
	}
	
	public void addAllCommentMsg(){
		//mMapCommentMsg.putAll(mapCommentMsg);
		bUpdateCommentMsg = true;
		notifyDataSet();
	}
	
	public void addOneCommentMsg(String objId, CommentMsg msg, boolean isUpdate){
		if(mMapCommentMsg.containsKey(objId)){
			List<CommentMsg> listMsg = mMapCommentMsg.get(objId);
			listMsg.add(msg);
			mMapCommentMsg.put(objId, listMsg);
		}
		else{
			List<CommentMsg> listMsgNew = new ArrayList<CommentMsg>();
			listMsgNew.add(msg);
			mMapCommentMsg.put(objId, listMsgNew);
		}
		if(isUpdate){
			notifyDataSetChanged();
		}
	}
	
	public void clearData(){
		mListSharedInfo.clear();
		mMapCommentMsg.clear();
	}
	
	private void notifyDataSet(){
		if(bUpdateCommentMsg && bUpdateSharedInfo){
			notifyDataSetChanged();
			bUpdateSharedInfo = false;
			bUpdateSharedInfo = false;
		}
	}
	
	@Override
	public int getCount() {
		return mListSharedInfo.size();
	}

	@Override
	public Object getItem(int position) {
		return mListSharedInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		System.out.println("position"+position);
		if(position == getCount()-1){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_addmore, null);
			return convertView;
		}
		if(convertView == null || convertView.getTag() == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_community, null);
			ListCell listCell = new ListCell((TextView)convertView.findViewById(R.id.community_user_name), 
					(ImageView)convertView.findViewById(R.id.community_img_view), (TextView)convertView.findViewById(R.id.txt_community_time));
			listCell.imgButton = (ImageButton) convertView.findViewById(R.id.community_btn_comment);
			listCell.listView  = (ListView)    convertView.findViewById(R.id.list_community_comm);
			convertView.setTag(listCell);
		}
		SharedInfo sharedInfo = mListSharedInfo.get(position);
		ListCell listCell = (ListCell)convertView.getTag();
		TextView tv = listCell.GetTextView();
		ImageView imgView = listCell.GetImageView();
		TextView tvTime = listCell.GetTimeTextView();
		ImageButton imgBtn = listCell.imgButton;
		ListView    listView = listCell.listView;
		
		//获取时间间隔
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date updateDate = null;
		try {
			updateDate = formatDate.parse(sharedInfo.getCreatedAt());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String updateTime = Config.getUpdateTime(mContext, updateDate.getTime());
		
		//
		tv.setText(sharedInfo.getUserName());
		tvTime.setText(updateTime);
		imgView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
		imgView.setTag(sharedInfo.getImagePath());
		imgView.setOnClickListener(imgClickListener);
		imgBtn.setTag(position);
		imgBtn.setOnClickListener(btnClickListener);
		listView.setTag(sharedInfo.getObjectId());
		getCommentMsg(listView, sharedInfo.getObjectId());
		//
		AsyncImageTask imageTask = new AsyncImageTask(mContext, imgView);
		imageTask.execute(sharedInfo.getImagePath());
		return convertView;
	}
	
	private OnClickListener imgClickListener = new OnClickListener(){

		@Override
		public void onClick(View imgView) {
			String strUrlPath = imgView.getTag().toString();
			String name = strUrlPath.substring(strUrlPath.lastIndexOf("/") + 1);
			File file = new File(Config.getCache(), name);
			if(!file.exists())
				return;
			Intent intent = new Intent(mContext, MyImageViewActivity.class);
			intent.putExtra(Config.KEY_IMAGE_EXTRA, file.getAbsolutePath());
			System.out.println(imgView.getTag().toString());
			mContext.startActivity(intent);
			
		}
		
	};
	
	private OnClickListener btnClickListener = new OnClickListener(){

		@Override
		public void onClick(View view) {
			
			//titlePopup.setAnimationStyle(R.style.cricleBottomAnimation);
			popupWindow.show(view, (Integer)view.getTag());
			
		}
		
		
	};
	
	//AsyncTask to get file from http.
	//获取缩略图
	private class AsyncImageTask extends AsyncTask<String, Integer, Bitmap>{

		private ImageView mImageView;
		private Context   mContext;
		private String    mImagePath;
		
		public AsyncImageTask(Context context, ImageView imageView){
			this.mContext = context;
			this.mImageView = imageView;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				mImagePath = params[0];
				String strFilePath = ImageService.getImageFromHttp(mContext, params[0], Config.getCache());
				return ImageService.decodeFile(new File(strFilePath), 300, 300);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			String strTag = mImageView.getTag().toString();
			if(bitmap != null && strTag.equals(mImagePath)){
				mImageView.setImageBitmap(bitmap);
			}
		}
	}
	
	//加载评论
	private void getCommentMsg(final ListView listView, String destObjId){
		ArrayAdapter<String> adapter = new 
				ArrayAdapter<String>(mContext, R.layout.list_item_comment);
		if(mMapCommentMsg.containsKey(destObjId)){
			List<CommentMsg> listCommMsg = mMapCommentMsg.get(destObjId);
			Iterator<CommentMsg> iterator = listCommMsg.iterator();
			while(iterator.hasNext()){
				CommentMsg msg = iterator.next();
				String content = msg.getBelongUserName() + ": " + msg.getContent();
				if(content != null){
					adapter.add(content);
				}
			}
		}
		listView.setAdapter(adapter);
		fixListViewHeight(listView);
		/*
		ArrayAdapter<String> adapter = new 
				ArrayAdapter<String>(mContext,android.R.layout.simple_expandable_list_item_1);
		adapter.add("1");
		adapter.add("1");
		listView.setAdapter(adapter);*/
	}
	
	public void fixListViewHeight(ListView listView) {   
        // 如果没有设置数据适配器，则ListView没有子项，返回。  
        ListAdapter listAdapter = listView.getAdapter();  
        int totalHeight = 0;   
        if (listAdapter == null) {   
            return;   
        }   
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {     
            View listViewItem = listAdapter.getView(index , null, listView);  
            // 计算子项View 的宽高   
            listViewItem.measure(0, 0);    
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        // listView.getDividerHeight()获取子项间分隔符的高度   
        // params.height设置ListView完全显示需要的高度    
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        listView.setLayoutParams(params);   
    }   
	
	
	public static class ListCell{
		private TextView tvCellLable;
		private ImageView imageCellLable;
		private TextView  tvTime;
		public  ImageButton imgButton;
		public  ListView   listView;
		
		public ListCell(){
			
		}
		
		public ListCell(TextView tv, ImageView imgView, TextView tvTime){
			tvCellLable = tv;
			imageCellLable = imgView;
			this.tvTime = tvTime;
		}
		
		
		public TextView GetTextView(){
			return tvCellLable;
		}
		
		public ImageView GetImageView(){
			return imageCellLable;
		}
		
		public TextView GetTimeTextView(){
			return tvTime;
		}
	}
}
