package com.android.activity;

import java.util.ArrayList;

import object.Notification;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import adapter.AdapterNotification;
import android.Vietnalyze;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctyprosoft.tmg.GCMIntentService;
import com.ctyprosoft.tmg.R;

public class NotificationActivity extends Vinalyze implements OnItemClickListener, OnScrollListener, OnClickListener {
	Context context = this;
	AdapterNotification adapter;
	ListView lv ;
	public static ArrayList<Notification> array;
	public Dialog waitingDialog;
	String email,cardno;
	
	private static int currentPage = 1;
	private static int positionGridView = 0;
	int firstVisiblePosition;
	int visibleItem;
	int totalItem;
	public static int totalPage = 0;
	
	Button btn_retry;
	RelativeLayout rela_cover;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Vietnalyze.logEvent("hisNotification");
		iniActionBar();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		setContentView(R.layout.activity_coupon);//just list view
		initVariable();
		
	}
	private void initVariable()
	{
		email = Utility.email;
		cardno = Utility.cardNo;
		
		
		rela_cover = (RelativeLayout)	findViewById(R.id.rela_retry);
		Button btn_retry = (Button)		findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(this);
		
		
	}
	private void iniActionBar()
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
		tv_label.setText(getResources().getString(R.string.notificationHis));
		
		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));
		
		actionBar.setCustomView(iconHome);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Utility.isInternet(context))
			new backgroundGetPCList().execute();
		else
			Utility.NoInternet(context, getResources().getString(R.string.nointernet));
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		showDetail(arg2);
		
	}
	class backgroundGetPCList extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waitingDialog = new Dialog(context);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			waitingDialog.show();
			array = new ArrayList<Notification>();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
//				totalPage = GetJsonFromAPI.getTotalPage(API.getNotiList(email,
//						String.valueOf(currentPage)));
				array = GetJsonFromAPI.getNotifiList(API.getNotiList(email,
						String.valueOf(currentPage)));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (MainActivity.timeout == 1) {
				waitingDialog.cancel();
				MainActivity.timeout = 0;
				rela_cover.setVisibility(View.VISIBLE);
			}
			else
			{
				rela_cover.setVisibility(View.INVISIBLE);
				lv = (ListView)findViewById(R.id.lv_coupon);
				adapter = new AdapterNotification(NotificationActivity.this,array);
				lv.setAdapter(adapter);
				lv.setSelection(positionGridView);
				lv.setOnScrollListener(NotificationActivity.this);
				lv.setOnItemClickListener(NotificationActivity.this);
				
				waitingDialog.cancel();
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		System.out.println("firstVisibleItem=" + firstVisibleItem
				+ "  visibleItemCount=" + visibleItemCount
				+ "  totalItemCount=" + totalItemCount);
		firstVisiblePosition = firstVisibleItem;
		totalItem = totalItemCount;
		visibleItem = visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		boolean flag = (currentPage == totalPage + 1);
		System.out.println("currentPage=" + currentPage + " totalPage="
				+ totalPage + "  flag=" + flag);
		if (firstVisiblePosition == (totalItem - visibleItem)
				&& scrollState == 0 && flag == false) {
			currentPage++;
			System.out.println("currentPage " + currentPage);
			positionGridView = totalItem - visibleItem;
			new backgroundGetPCList().execute();
		}
	}
	Dialog dialog;
	private void showDetail(final int pos)
	{
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View custom_view = View.inflate(context, R.layout.custom_dialog_notification, null);
		ImageView close = (ImageView)	custom_view.findViewById(R.id.img_close);
		close.setOnClickListener(this);
		TextView sms = (TextView)		custom_view.findViewById(R.id.tv_sms_noti_detail);
		TextView day = (TextView)		custom_view.findViewById(R.id.tv_day_noti_detail);
		sms.setText(array.get(pos).getMessage());
		day.setText(array.get(pos).getCreateDate());
		dialog.setContentView(custom_view);
		dialog.show();
		if(array.get(pos).getClicked().equals(""))
			GetJsonFromAPI.postRequest(API.updateCLickedNotification + array.get(pos).getID());
		//update listView
		array.get(pos).setClicked("1");
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_close:
			dialog.cancel();
			break;
		case R.id.btn_retry:
			onResume();
			break;
		default:
			break;
		}
	}
}
