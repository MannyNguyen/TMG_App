package com.android.activity;

import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import adapter.TabsCouponHisAdapter;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctyprosoft.tmg.R;
import com.slidingmenu.lib.SlidingMenu;

@SuppressLint("NewApi")
public class CouponHisActivity extends FragmentActivity implements TabListener {
	public static int avai = 0;
	Context context = this;
	static Boolean isTranslating = false;
	// /////////////////////////////////////////////////////////
	// ListView of LeftMenu
	// /////////////////////////////////////////////////////////

	private SlidingMenu menu;

	// /////////////////////////////////////////////////////////
	// Action Bar Tabs
	// /////////////////////////////////////////////////////////
	private ViewPager viewPager;
	private TabsCouponHisAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs;
	LinearLayout frag_container;
	int used_his = 2;
	int _getExtra = 0;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_his);
		initActionbarTabs();

	}

	/*
	 * To init all values of Actionbar Tabs
	 */
	private void initActionbarTabs() {
		// Initilization
		/*
		 * update clicked = 1 & check open Activity by Notification Intent Pending
		 */
		try {
			Bundle bundle = getIntent().getExtras();
			_getExtra = bundle.getInt("used");
			// update clicked
			id = bundle.getString("id");
			if(!id.equals(""))
				GetJsonFromAPI.postRequest(API.updateCLickedNotification + id);
		} catch (Exception e) {
			// TODO: handle exception
		}

		tabs = getResources().getStringArray(R.array.CP_His);
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		View iconHome = View.inflate(context, R.layout.custom_actionbar_back,
				null);
		ImageView img = (ImageView) iconHome.findViewById(R.id.image2);
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
		tv_label.setText(getResources().getString(R.string.couponhis));

		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));

		actionBar.setCustomView(iconHome);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
		mAdapter = new TabsCouponHisAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Adding Tabs
		if (_getExtra == 2) {
			actionBar.addTab(actionBar.newTab().setText(tabs[0])
					.setTabListener(this), 0, false);
			actionBar.addTab(actionBar.newTab().setText(tabs[1])
					.setTabListener(this), 1, true);
			actionBar.addTab(actionBar.newTab().setText(tabs[2])
					.setTabListener(this), 2, false);
		} else {
			for (String tab_title : tabs) {
				actionBar.addTab(actionBar.newTab().setText(tab_title)
						.setTabListener(this));
			}
		}
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

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
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("Flurry", "onStart second");
		// your code

		// Vietnalyze
		Vietnalyze.onStartSession(context, "EU56N2MEJJZH77OULDK3");
		Vietnalyze.logEvent("hisCoupon");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("Flurry", "onStop");
		// your code

		Vietnalyze.onEndSession(context);
	}
	@Override
	public void onBackPressed() {
		if(MainActivity.isActivity == false)
			startActivity(new Intent(context,MainActivity.class));
		finish();
	}

}
