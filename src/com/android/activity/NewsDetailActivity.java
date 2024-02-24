package com.android.activity;

import java.util.ArrayList;

import object.News;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.ImageDownloaderMainBanners;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import adapter.AdapterCoupon;
import android.Vietnalyze;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ctyprosoft.tmg.R;
import com.facebook.Session;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.SimpleFacebook.OnPublishListener;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Profile;

public class NewsDetailActivity extends Vinalyze {
	Context context = this;
	AdapterCoupon adapter;
	ListView lv;
	ArrayList<News> array;
	public Dialog waitingDialog;
	SharePreferance share;
	ImageDownloaderMainBanners downloader;
	private static SimpleFacebook mSimpleFacebook;
	String img, title, content, subtitle;
	private ProgressBar mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_new_detail);
		initActionBar();

		new backgroundGetCpList().execute();
	}

	private void initActionBar() {
		share = new SharePreferance(context);
		ActionBar actionBar = getActionBar();
		View iconHome = View.inflate(context, R.layout.custom_actionbar_back,
				null);
		ImageView img = (ImageView) iconHome.findViewById(R.id.image2);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView tv_label = (TextView) iconHome.findViewById(R.id.tv_label);
		tv_label.setText(getResources().getString(R.string.newsdetail));

		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));

		actionBar.setCustomView(iconHome);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "TEXT").setIcon(R.drawable.share_height_cover)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// getMenuInflater().inflate(R.menu.mymenu, menu);
		return true;
	}

	// Login listener
	private OnLoginListener mOnLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
		}

		@Override
		public void onException(Throwable throwable) {
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
		}

		@Override
		public void onLogin() {
			// change the state of the button or do whatever you want
			// toast("You are logged in");
			// getProfileExample();
			getProfileExample();

		}

		@Override
		public void onNotAcceptingPermissions() {
			// toast("You didn't accept read permissions");
		}
	};

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case 1:
			Vietnalyze.logEvent("shareNewDetail");
			mSimpleFacebook = SimpleFacebook.getInstance(this);
			boolean islogin = mSimpleFacebook.isLogin();
			if (islogin)
				getProfileExample();
			else
				mSimpleFacebook.login(mOnLoginListener);

			break;
		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	class backgroundGetCpList extends AsyncTask<String, Void, String> {

		ImageView img_detail;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waitingDialog = new Dialog(context);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			waitingDialog.show();
			TextView tv_title = (TextView) findViewById(R.id.news_detail_title);
			TextView tv_subtitle = (TextView) findViewById(R.id.news_detail_subtitle);
			TextView tv_content = (TextView) findViewById(R.id.news_detail_content);
			img_detail = (ImageView) findViewById(R.id.img_news_detail);
			img_detail.getLayoutParams().height = MainActivity.height / 3;
			Bundle bundle = getIntent().getExtras();
			title = bundle.getString("title");
			content = bundle.getString("content");
			subtitle = bundle.getString("subtitle");
			tv_title.setText(title);
			tv_subtitle.setText(subtitle);
			tv_content.setText(content);
			img = bundle.getString("img");
			downloader = new ImageDownloaderMainBanners();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				downloader.download(img, img_detail);
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (waitingDialog.isShowing())
				waitingDialog.cancel();
		}
	}

	private void publishFeedExample() {
		// listener for publishing action
		final OnPublishListener onPublishListener = new SimpleFacebook.OnPublishListener() {

			@Override
			public void onFail(String reason) {
			}

			@Override
			public void onException(Throwable throwable) {
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while publishing
			}

			@Override
			public void onComplete(String postId) {
				waitingDialog.cancel();
				Utility.showDialog(context, "Done");
			}
		};

		// feed builder
		final Feed feed = new Feed.Builder()
				.setMessage(getResources().getString(R.string.app_name))
				.setName(title).setCaption(subtitle).setDescription(content)
				.setPicture(img).build();

		// click on button and publish
		mSimpleFacebook.publish(feed, onPublishListener);
	}
	private void getProfileExample() {
		// listener for profile request
		final OnProfileRequestListener onProfileRequestListener = new SimpleFacebook.OnProfileRequestListener() {

			@Override
			public void onFail(String reason) {
				// insure that you are logged in before getting the profile
			}

			@Override
			public void onException(Throwable throwable) {
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while fetching
				// profile
				waitingDialog.show();
			}

			@Override
			public void onComplete(Profile profile) {
				
				//no Update fbID
				if(Utility.fb_id.equals(""))
				{
					Session session = Session.getActiveSession();
					Utility.accessToken = session.getAccessToken();
					Utility.fb_avarta = "http://graph.facebook.com/" + profile.getUsername()
							+ "/picture";
					share.setAccessToken( session.getAccessToken());
					share.setimgAvarta(Utility.fb_avarta);
					GetJsonFromAPI.postRequest(API.getupdatefbID( Utility.email,Utility.fb_avarta));
				}
				
				publishFeedExample();
			}
		};

		mSimpleFacebook.getProfile(onProfileRequestListener);

	}

}
