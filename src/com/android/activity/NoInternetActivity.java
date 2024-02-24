package com.android.activity;

import prosoft.android.utility.ImageDownloaderMainBanners;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ctyprosoft.tmg.R;

public class NoInternetActivity extends Activity implements OnClickListener{
	Context context = this;
	Resources res;
	SharePreferance share;
	ImageDownloaderMainBanners downloader;
	Button btn_setting,btn_cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initVariable();
		
		setContentView(R.layout.no_internet_connection);
		customLayout();
	}
	@Override
	public void onBackPressed() {
		// your code.
		// delete api
		if (Utility.isInternet(context))
			finish();
		else
		{
			Intent it = new Intent(Intent.ACTION_MAIN);
			it.addCategory(Intent.CATEGORY_HOME);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(it);
		}

	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
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
		btn_setting = (Button)		findViewById(R.id.btn_setting);
		btn_cancel = (Button)		findViewById(R.id.btn_exit);
		
		btn_setting.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_setting:
			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			break;
		case R.id.btn_exit:
			if (Utility.isInternet(context))
				finish();
			else
			{
				Intent it = new Intent(Intent.ACTION_MAIN);
				it.addCategory(Intent.CATEGORY_HOME);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(it);
			}
			break;

		default:
			break;
		}
	}

}
