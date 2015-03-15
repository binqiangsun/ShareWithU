package com.binqiang.sharewithu;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.binqiang.common.ToastShow;
import com.binqiang.util.Config;

@SuppressLint("ShowToast")
public class LoginActivity extends Activity {
	
	private Button btnLogin;
	private Button btnRegister;
	private EditText etLoginName;
	private EditText etLoginPass;
	
	private LoginActivity mActivity = this;
	
	
	public static final String msg_login_failed = "Name or Password is invalid.";
	public static final String STR_LOGIN        = "登陆";
	public static final String STR_REGISTER        = "注册";
	
	
	
	private void init()
	{
		this.etLoginName = (EditText)this.findViewById(R.id.et_account);
		this.etLoginPass = (EditText)this.findViewById(R.id.et_pwd);
        this.btnLogin = (Button)this.findViewById(R.id.btn_login);
        this.btnRegister = (Button)this.findViewById(R.id.btn_register);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		this.init();
		
		//
		this.btnLogin.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View view){
				final String strLoginName = etLoginName.getText().toString();
				final String strLoginPass = etLoginPass.getText().toString();
				
				if(btnLogin.getText().toString().equals(STR_LOGIN))
				{
					LoginByUser(strLoginName, strLoginPass);
				}
			}
		});
		
		
		
		this.btnRegister.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final String strLoginName = etLoginName.getText().toString();
				final String strLoginPass = etLoginPass.getText().toString();
				if(strLoginName.isEmpty() || strLoginPass.isEmpty())
				{
					ToastShow.ShowMsg(mActivity, "用户名或密码不能为空");
					return;
				}
				
				final View registerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_dialog_register, null);
				final EditText textView = (EditText) registerView.findViewById(R.id.et_mail);
				textView.setHint(Config.INPUT_EMAIL);
				new AlertDialog.Builder(mActivity)
				.setView(registerView)
                .setTitle(R.string.btn_signup)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						String strEmail = textView.getText().toString();
						if(strEmail != null){
							RegisterByUser(strLoginName, strLoginPass, strEmail);
						}
					}
                	
                }).show();
			}
		});
	}
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
        intent.putExtra(Config.SER_KEY, true);
        setResult(Config.ACT_RET_KEY, intent);
		super.finish();
	}

	public void GetBack(){
		this.finish();
	}
	
	private void LoginByUser(final String strLoginName, String strLoginPass)
	{

		if(strLoginName.isEmpty() || strLoginPass.isEmpty())
		{
			ToastShow.ShowMsg(mActivity, "用户名或密码不能为空");
			return;
		}
		
		BmobUser user = new BmobUser();
		user.setUsername(strLoginName);
		user.setPassword(strLoginPass);
		user.login(mActivity, new cn.bmob.v3.listener.SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ToastShow.ShowMsg(mActivity, "Welcome, "+ strLoginName);
				if(Config.IsLogin(mActivity)){
					GetBack();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ToastShow.ShowMsg(mActivity, "登陆失败："+arg1);
			}
		});
	}
	
	private void RegisterByUser(final String strLoginName, String strLoginPass, String strRegisterMail)
	{
		
		BmobUser user = new BmobUser();
		user.setUsername(strLoginName);
		user.setPassword(strLoginPass);
		user.setEmail(strRegisterMail);
		user.signUp(mActivity, new cn.bmob.v3.listener.SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ToastShow.ShowMsg(mActivity, "欢迎你, "+ strLoginName);
				GetBack();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ToastShow.ShowMsg(mActivity, "登陆失败："+arg1);
			}
		});
	}

}
