package com.android.activity;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctyprosoft.tmg.R;

public class FacebookActivity extends Activity{
	public static int fontsize = 18;
	WebView myWebView;
	public Dialog waitingDialog;
	String title;
	ActionBar actionBar;
	Context context = this;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		Intent intent = getIntent();
		title = intent.getExtras().getString("name");
		String  url = intent.getExtras().getString("url");
		System.out.println("url="+url);
		initActionBar();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.webview_facebook);
		
		myWebView = (WebView)findViewById(R.id.webView_face);
		myWebView.setWebViewClient(new myWebClient());
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setUseWideViewPort(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.loadUrl(url);
		}
	private void initActionBar() {

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
		tv_label.setText(title);
		actionBar = getActionBar();
		actionBar.setCustomView(iconHome);
		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
	}
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	public class myWebClient extends WebViewClient
	{
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        // TODO Auto-generated method stub
	        super.onPageStarted(view, url, favicon);
	        view.clearView();
	        waitingDialog = new Dialog(FacebookActivity.this);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
//			waitingDialog.setCancelable(false);
			waitingDialog.show();
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // TODO Auto-generated method stub

	        view.loadUrl(url);
	        return true;

	    }
	    @Override
	    public void onPageFinished(WebView view, String url) {
	    	// TODO Auto-generated method stub
	    	super.onPageFinished(view, url);
	    	waitingDialog.dismiss();

	    }
	}
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
	     if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	         // do something on back.
	    	// Toast.makeText(MainActivity.this, "back", Toast.LENGTH_LONG).show();
	    	 finish();
	         return true;
	     }

	     return super.onKeyDown(keyCode, event);
	 }
}