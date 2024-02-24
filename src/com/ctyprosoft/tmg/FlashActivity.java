package com.ctyprosoft.tmg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.android.activity.MainActivity;
import com.google.android.gcm.GCMRegistrar;

public class FlashActivity extends Activity {
	// change time delay here
	int time = 2000;
	Handler delay = new Handler();
	Context context = this;
	SharePreferance share;
	String packageName;

	// Token for Notification
	
	String filename = ".tmg.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);
		share = new SharePreferance(context);
		
		/*
		 * if(!share.getStatusCreateIconHome()) createFilevsSavestatus("");
		 * 
		 * String status = readFromFile(); if (status.equals("")) {
		 * createIconHome(); createFilevsSavestatus("1");
		 * share.setStatusCreateIconHome(true); }
		 */
		// function delay
		getKeyHash();
	}
	
	public void getKeyHash(){
		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.ctyprosoft.tmg", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("TAG", "KeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Utility.isInternet(context))
			StayHere(time);
		else
			Utility.NoInternet(context,
					"No internet connection.Check your network");
	}

	private void deleteIcon() {
		packageName = getApplicationContext().getPackageName();
		Intent launchIntent = context.getPackageManager()
				.getLaunchIntentForPackage(packageName);
		String className = launchIntent.getComponent().getClassName();

		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(packageName, className);
//		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		shortcutIntent.putExtra("someParameter", "HelloWorld");

		Intent removeIntent = new Intent();
		removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,  getResources()
				.getString(R.string.app_name));
		removeIntent.putExtra("duplicate", false);
		removeIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(context,
						R.drawable.app_iconandroid));
		removeIntent
				.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
		context.sendBroadcast(removeIntent);
	}

	private void createIconHome() {
		packageName = getApplicationContext().getPackageName();
		Intent launchIntent = context.getPackageManager()
				.getLaunchIntentForPackage(packageName);
		String className = launchIntent.getComponent().getClassName();

		Intent shortcutIntent = new Intent();
		shortcutIntent.setClassName(packageName, className);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources()
				.getString(R.string.app_name));
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(context,
						R.drawable.app_iconandroid));
		addIntent.putExtra("duplicate", false); // Just create once
		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(addIntent);
	}

	
	private void StayHere(int time) {
		delay.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				share = new SharePreferance(context);
				if (share.getStatusLogged()) {
					startActivity(new Intent(context, MainActivity.class));
					finish();
				} else {
					// startActivity(new Intent(context,MainActivity.class));
					Intent GoMenuPage = new Intent(context,
							LoginMenuActivity.class);
					startActivity(GoMenuPage);
					// End this activity
					finish();
				}

			}
		}, time);
	}
	
	private void createFilevsSavestatus(String name) {
		// catches IOException below

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root);
		File file = new File(myDir, filename);
		myDir.mkdirs();

		FileOutputStream outputStream;

		try {
			outputStream = new FileOutputStream(file);
			outputStream.write(name.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Reading the file back...
	}

	private String readFromFile() {

		String ret = "";
		File sdcard = Environment.getExternalStorageDirectory()
				.getAbsoluteFile();
		File file = new File(sdcard, filename);

		// Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			// You'll need to add proper error handling here
		}

		return text.toString();
	}
}