package com.binqiang.ui.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;

import com.binqiang.adapter.CommunityAdapter;
import com.binqiang.bmob.CommentMsg;
import com.binqiang.bmob.SharedInfo;
import com.binqiang.common.ToastShow;
import com.binqiang.sharewithu.MainActivity;
import com.binqiang.sharewithu.R;
import com.binqiang.util.Config;
import com.binqiang.view.ActionItem;
import com.binqiang.view.MyPopupWindow.OnItemOnClickListener;
import com.binqiang.view.PopupEditText;
import com.binqiang.view.PopupEditText.OnSendClickListener;

public class FriendsFragment extends Fragment implements OnItemOnClickListener, OnSendClickListener {
	
	private Context mContext;
	private CommunityAdapter mAdapter;
	private ListView mListView;
	List<SharedInfo> mListSharedInfo = new ArrayList<SharedInfo>();
	BmobQuery<SharedInfo> mQuery = new BmobQuery<SharedInfo>();
	BmobQuery<CommentMsg> mCommQuery = new BmobQuery<CommentMsg>();
	PopupEditText mPopupEditText;
	int     mQueryCount = 0;
	SharedInfo     mObjId   = null;    //评论的对象
	ProgressDialog mProDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = this.getActivity().getBaseContext();
		View rootView = inflater.inflate(R.layout.activity_community_list, container, false);
		mListView = (ListView)rootView.findViewById(R.id.community_friend_listview);
		mAdapter = new CommunityAdapter(this);
		mListView.setAdapter(mAdapter);
		
		//等待对话框
		mProDialog = new ProgressDialog(this.getActivity());
		mProDialog.setMessage("正在加载数据");
		mProDialog.show();
		
		//popup window
		DisplayMetrics dm = new DisplayMetrics();
		this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		mPopupEditText = new PopupEditText(mContext, dm.widthPixels, 300);
		mPopupEditText.setSendOnClickListener(this);
		
		//获取参数
		Bundle args = getArguments();
		if(args != null){
			String userName = args.getString(Config.USERNAME);
			if(userName != null && !userName.isEmpty()){
				mQuery.addWhereEqualTo(Config.USERNAME, userName);
			}
		}
		mQuery.order(Config.FIELD_CREATEAT_BACK);
		mCommQuery.order(Config.FIELD_CREATEAT);
		//query.setLimit(Config.MAX_INFO_NUM);
		getQueryResult();
		getCommentQuery();
		//加载更多 事件
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int index,
					long arg3) {
				if(index == mListView.getCount()-1){
					getQueryResult();
				}
			}
			
		});
		return rootView;
	}
	
	
	private void getQueryResult() {
		mQuery.setSkip((mQueryCount++)*10);
		mQuery.findObjects(MainActivity.g_context, new FindListener<SharedInfo>(){

			@Override
			public void onError(int arg0, String arg1) {
				ToastShow.ShowMsg(mContext, "get information failed.");
				if(mProDialog.isShowing())
					mProDialog.dismiss();
			}

			@Override
			public void onSuccess(List<SharedInfo> listSharedInfo) {
				
				System.out.println(listSharedInfo.size());
				mListSharedInfo.addAll(listSharedInfo);
				mAdapter.addAllListSharedInfo(listSharedInfo);
				
				if(mProDialog.isShowing())
					mProDialog.dismiss();
			}
		});
		
		
	}
	
	private void getCommentQuery(){
		mCommQuery.include("sharedInfo");
		mCommQuery.setLimit(50);
		mCommQuery.findObjects(mContext, new FindListener<CommentMsg>(){

			@Override
			public void onError(int arg0, String arg1) {
				ToastShow.ShowMsg(mContext, "get information failed.");
				if(mProDialog.isShowing())
					mProDialog.dismiss();
			}

			@Override
			public void onSuccess(List<CommentMsg> listResult) {
				Iterator<CommentMsg> iterator = listResult.iterator();
				while(iterator.hasNext()){
					CommentMsg commMsg = iterator.next();
					SharedInfo sharedInfo = commMsg.getDestSharedInfo();
					if(sharedInfo != null){
						String infoObjId = sharedInfo.getObjectId();
						mAdapter.addOneCommentMsg(infoObjId, commMsg, false);
					}
				}
				mAdapter.addAllCommentMsg();
				if(mProDialog.isShowing())
					mProDialog.dismiss();
			}
			
		});
		
		
	}
	
	public void refreshData(){
		mListSharedInfo.clear();
		mQueryCount = 0;
		mAdapter.clearData();
		getQueryResult();
		getCommentQuery();
	}
	
	@Override
	public void onItemClick(ActionItem item, int posId, int position) {
		System.out.println("评论的对象：" + posId);
		mPopupEditText.show(mListView);
		mObjId = mListSharedInfo.get(posId);
	}
	@Override
	public void onItemClick(String content) {
		
		//上传评论数据
		CommentMsg msg = CommentMsg.saveData(mContext, content, mObjId);
		//关联
		BmobRelation relation = new BmobRelation();
		relation.add(msg);
	    mObjId.setCommentMsg(relation);
	    mObjId.update(mContext);
		//
		mAdapter.addOneCommentMsg(mObjId.getObjectId(), msg, true);
		
		//发送推送消息
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put(Config.USERNAME, Config.g_user_name);
			jsonObj.put(Config.STR_CONTENT, content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		BmobPushManager<BmobInstallation> bmobPush = new BmobPushManager<BmobInstallation>(mContext);
		BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
		query.addWhereEqualTo(Config.FIELD_UID, mObjId.getUserName());
		bmobPush.setQuery(query);
		bmobPush.pushMessage(jsonObj);
	}
	
	
	

}
