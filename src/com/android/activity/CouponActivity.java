package com.android.activity;

import java.util.ArrayList;
import java.util.Locale;

import object.Coupon;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.ImageLoader;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;

import com.android.activity.PurchaseActivity.backgroundGetPCList;
import com.ctyprosoft.tmg.R;

import adapter.AdapterCoupon;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CouponActivity extends Activity implements OnItemClickListener, OnScrollListener, OnClickListener {
	Context context = this;
	AdapterCoupon adapter;
	ListView lv ;
	ArrayList<Coupon> array;
	public Dialog waitingDialog;
	String email;
	public static int positionClick = -1;
	private static int currentPage = 1;
	private static int positionGridView = 0;
	int firstVisiblePosition;
	int visibleItem;
	int totalItem;
	public static int totalPage = 0;
	Button btn_retry;
	RelativeLayout rela_cover;
	SharePreferance share;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			Vietnalyze.logEvent("couponList");
		} catch (Exception e) {
			// TODO: handle exception
		}
		iniActionBar();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		setContentView(R.layout.activity_coupon);
		
		initLayout();
		
		
        
		
	}
	private void initLayout()
	{
		share = new SharePreferance(context);
		email = Utility.email;
		if(email.equals(""))
			email = share.getEmail();
		
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
				if(MainActivity.isActivity == false)
					startActivity(new Intent(context,MainActivity.class));
				finish();
			}
		});
		TextView tv_label = (TextView) iconHome.findViewById(R.id.tv_label);
		tv_label.setText(getResources().getString(R.string.coupon));
		
		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));
		
		actionBar.setCustomView(iconHome);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        
        actionBar.setDisplayShowHomeEnabled(true);
        /*
		 * update clicked = 1 & check open Activity by Notification Intent Pending
		 */
        try {
			id = getIntent().getExtras().getString("id");
			if(!id.equals(""))
				GetJsonFromAPI.postRequest(API.updateCLickedNotification + id);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Utility.isInternet(context))
			new backgroundGetCpList().execute();
		else 
			Utility.NoInternet(context, null);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 menu.add(0, 1, 0, "TEXT" )
	        .setIcon(R.drawable.help)
	        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Utility.showDialog(context, getResources().getString(R.string.helpCoupon));
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
		if(MainActivity.isActivity == false)
			startActivity(new Intent(context,MainActivity.class));
		finish();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		for (int a = 0; a < lv.getChildCount(); a++) {
			lv.getChildAt(a).setBackgroundColor(Color.WHITE);
		}

		arg1.setBackgroundColor(Color.GRAY);
		positionClick = arg2;
		Intent it = new Intent(context,CouponDetailActivity.class);
		it.putExtra(Utility.IdCouponDetail, String.valueOf(lv.getItemIdAtPosition(arg2)));
		startActivity(it);
//		startActivity(new Intent(context,CouponDetailActivity.class));
	}
	class backgroundGetCpList extends AsyncTask<String, Void, String>
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
			array = new ArrayList<Coupon>();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				array = GetJsonFromAPI.getCpList(API.getCPListString(email
						,String.valueOf(currentPage)));
//				totalPage = GetJsonFromAPI.getTotalPage(API.getCPListString(email
//						,String.valueOf(currentPage)));
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
				adapter = new AdapterCoupon(CouponActivity.this,array);
				lv.setAdapter(adapter);
				lv.setSelection(positionGridView);
				lv.setOnScrollListener(CouponActivity.this);
				lv.setOnItemClickListener(CouponActivity.this);
				waitingDialog.cancel();
				if(Utility.first_coupon_guide)
				{
					share.setFirstCouponGuide(false);
					Utility.first_coupon_guide = false;
					Utility.showDialog(context, getResources().getString(R.string.helpCoupon));
				}
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
			new backgroundGetCpList().execute();
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_retry:
			onResume();
			break;

		default:
			break;
		}
	}
}
