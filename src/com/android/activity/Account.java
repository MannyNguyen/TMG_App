package com.android.activity;

import prosoft.android.utility.ImageDownloaderMainBanners;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;

import com.ctyprosoft.tmg.R;

import android.Vietnalyze;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Account extends Activity{
	Context context = this;
	Resources res;
	SharePreferance share;
	ImageDownloaderMainBanners downloader;
	EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Vietnalyze.logEvent("accountInfo");
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initVariable();
		initActionBar();
		
		setContentView(R.layout.activity_acc);
		customLayout();
		
		
	
	}
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//save into file
			share.setUserName(et.getText().toString());
			// reset value in Utility
			Utility.user_name = et.getText().toString();
			finish();
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		// your code.
		// delete api
		//save into file
		share.setUserName(et.getText().toString());
		// reset value in Utility
		Utility.user_name = et.getText().toString();
		finish();

	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//save into file
		share.setUserName(et.getText().toString());
		// reset value in Utility
		Utility.user_name = et.getText().toString();
	}
	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		View iconHome = View.inflate( context, R.layout.custom_actionbar_back, null);
		ImageView img = (ImageView)iconHome.findViewById(R.id.image2);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView tv_label = (TextView) iconHome.findViewById(R.id.tv_label);
		tv_label.setText(res.getString(R.string.accountsetting));
		
		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));
		
		actionBar.setCustomView(iconHome);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
	}
	private void initVariable()
	
	{
		downloader = new ImageDownloaderMainBanners();
		res = getResources();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	}
	private void customLayout()
	{
		share = new SharePreferance(context);
		et = (EditText)		findViewById(R.id.et_name);
		et.setText(Utility.user_name);
		
		TextView tv_email = (TextView)	findViewById(R.id.tv_email);
		tv_email.setText(Utility.email);
		
		TextView tv_birth = (TextView)	findViewById(R.id.tv_birthday);
		tv_birth.setText(Utility.birthday);
		
		TextView tv_phone = (TextView)	findViewById(R.id.tv_phone);
		tv_phone.setText(Utility.phone);
		
		TextView tv_gender = (TextView)	findViewById(R.id.tv_gender);
		tv_gender.setText(Utility.gender);
		
		ImageView img_cover = (ImageView)	findViewById(R.id.img_profile);
		downloader.download(Utility.fb_avarta, img_cover);
	}

}
