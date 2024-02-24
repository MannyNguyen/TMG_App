package com.android.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import object.BrandList;
import object.MobileAD;
import object.RowItem;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.ImageDownloaderMainBanners;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import adapter.CustomListViewAdapter;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ctyprosoft.tmg.SearchNearBy;
import com.ctyprosoft.tmg.GCMIntentService;
//import com.ctyprosoft.tmg.GoogleMapActivity;
import com.ctyprosoft.tmg.LoginMenuActivity;
import com.ctyprosoft.tmg.R;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.slidingmenu.lib.SlidingMenu;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.entities.Profile;

import fragment.AinoSofiaFragment;
import fragment.FarahFragment;
import fragment.JonHenryFragment;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements
		OnItemClickListener, OnClickListener {
	public static int timeout = 0;
	Context context = this;
	Dialog dialog;
	private Dialog view;
	private ImageView hospital;
	static Boolean isTranslating = false;
	private ImageView office;
	private ImageView fire;
	private AnimationSet hospital_set;
	private AnimationSet office_set;
	private AnimationSet fire_set;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	JonHenryFragment John_Henry;
	AinoSofiaFragment Anio;
	FarahFragment Fara;
	ImageView img_john;
	ImageView img_aino;
	ImageView img_farah;
	HorizontalScrollView scroll;
	Fragment fragment = null;
	// /////////////////////////////////////////////////////////
	// ListView of LeftMenu
	// /////////////////////////////////////////////////////////
	public static String[] contents;

	private ListView listView;
	private ArrayList<RowItem> rowItems;
	private SlidingMenu menu;

	public static ArrayList<MobileAD> mobileADs;
	// Tab titles
	LinearLayout frag_container;
	public static Dialog waitingDialog;
	public static int cacheJohnHenry = 0;
	public static int cacheAino = 0;
	public static int cacheFarah = 0;
	public static int idBrand = 0;
	public static int positionClick = 2;
	AlertDialog.Builder builder;
	SharePreferance share;
	HashMap<String, String> hm_admobile;
	String[] array_titleAds;
	String Join = "John Henrry Vietnam";
	String Aino = "Aino Sofia - Designed in Tokyo, Paris";
	String Farah = "Farah Vintage Vietnam";
	ArrayList<BrandList> array_brand;
	ImageDownloaderMainBanners downloader;
	View over_john, over_aino, over_fara, over_all;
	int pos_tabBar = 1;// first item is JohnHenry
	public static int height, width;
	RelativeLayout retry;
	int item_width, item_height;
	Button btn_retry;
	ProgressBar processBar;
	String str_phone = "08 3512 9888";
	private static SimpleFacebook mSimpleFacebook;
	public static boolean isActivity = false;
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initVariableforUser();
		setLanguage();
		initActionBar();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		initSlidingMenu();
		setContentView(R.layout.activity_main);

		initVariable();
		if (Utility.isInternet(context)) {
			new BG_BrandList().execute();
			new HttpRequestMobileAD()
					.execute("http://tmg.ctyprosoft.com:81/API/MobileAD/GetMobileADList.aspx");
		} else {
			Utility.NoInternet(context,
					"No internet connection.Check your network");
			setListviewLeftMenu();
		}

	}

	private void initVariableforUser() {
		SharePreferance share = new SharePreferance(context);

		Utility.user_name = share.getUserName();
		Utility.email = share.getEmail();
		Utility.fb_avarta = share.getimgAvarta();
		Utility.cardNo = share.getCardNo();
		Utility.birthday = share.getbirth();
		Utility.phone = share.getphone();
		Utility.gender = share.getgender();
		Utility.fb_id = share.getfbID();
		Utility.accessToken = share.getAccessToken();

		Utility.first_coupon_guide = share.getFirstCouponGuide();
		Utility.first_game_guide = share.getFirstGameGuide();
		Utility.first_invite_guide = share.getFirstInviteGuide();

		Utility.first_brand = share.getStatusfirstBrand();
		Utility.first_city = share.getStatusfirstBrandCity();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = new Intent("MyCustomIntent");

		// add data to the Intent
		/*
		 * intent.putExtra("message", "abc"); intent.setAction("sendsms");
		 * sendBroadcast(intent);
		 */

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		isActivity = true;
		Log.i("Flurry", "onStart second");
		// your code

		// Vietnalyze
		Vietnalyze.onStartSession(this, "EU56N2MEJJZH77OULDK3");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("Flurry", "onStop");
		// your code

		Vietnalyze.onEndSession(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isActivity = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	void RetryBar() {
		if (Utility.isInternet(context) && array_brand.size() == 0) {
			new BG_BrandList().execute();
		} else {
			Utility.NoInternet(context,
					"No internet connection.Check your network");
			setListviewLeftMenu();
		}
	}

	private void initVariable() {
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
		item_width = width * 2 / 5;
		item_height = height / 10;
		scroll = (HorizontalScrollView) findViewById(R.id.scrollTop);
		scroll.getLayoutParams().height = item_height;
		retry = (RelativeLayout) findViewById(R.id.rela_retry_scroll);
		retry.getLayoutParams().height = item_height;

		findViewById(R.id.btn_retry_scroll).setOnClickListener(this);

		processBar = (ProgressBar) findViewById(R.id.process_bar);
		/*
		 * update clicked = 1 & check open Activity by Notification Intent
		 * Pending
		 */
		try {
			id = getIntent().getExtras().getString("id");
			if (!id.equals(""))
				GetJsonFromAPI.postRequest(API.updateCLickedNotification + id);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void setLanguage() {
		share = new SharePreferance(context);
		String value = share.getValueLanguage();
		if (!value.equals(""))
			LoginMenuActivity.changelanguage(context, value);
		else
			LoginMenuActivity.setdefaultLanguage(context);
	}

	/*
	 * To init all values of Actionbar Tabs
	 */
	private void initScrollbar() {

		John_Henry = new JonHenryFragment();
		Anio = new AinoSofiaFragment();
		Fara = new FarahFragment();
		fragment = John_Henry;
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
		img_john = (ImageView) findViewById(R.id.john);

		img_john.setOnClickListener(this);
		img_aino = (ImageView) findViewById(R.id.aino);

		img_aino.setOnClickListener(this);
		img_farah = (ImageView) findViewById(R.id.farah);
		img_farah.setOnClickListener(this);

		downloader = new ImageDownloaderMainBanners();
		downloader.download(array_brand.get(0).getLogo(), img_john);
		downloader.download(array_brand.get(1).getLogo(), img_aino);
		downloader.download(array_brand.get(2).getLogo(), img_farah);

		over_john = (View) findViewById(R.id.over_john);
		over_aino = (View) findViewById(R.id.over_aino);
		over_fara = (View) findViewById(R.id.over_fara);
		over_all = (View) findViewById(R.id.over_all);
		over_john.setVisibility(View.VISIBLE);

		img_john.getLayoutParams().width = item_width;
		img_aino.getLayoutParams().width = item_width;
		img_farah.getLayoutParams().width = item_width;

		img_john.getLayoutParams().height = item_height;
		img_aino.getLayoutParams().height = item_height;
		img_farah.getLayoutParams().height = item_height;

		over_john.getLayoutParams().width = item_width;
		over_aino.getLayoutParams().width = item_width;
		over_fara.getLayoutParams().width = item_width;
		over_all.getLayoutParams().width = item_width * 3;
	}

	/*
	 * To init all values for SlidingMenu
	 */
	private void initActionBar() {

		ActionBar actionBar = getActionBar();
		View iconHome = View.inflate(context, R.layout.custom_actionbar, null);
		ImageView img = (ImageView) iconHome.findViewById(R.id.image2);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.toggle();
			}
		});

		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));

		actionBar.setCustomView(iconHome);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	private void initSlidingMenu() {
		waitingDialog = new Dialog(MainActivity.this);
		waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		waitingDialog.setContentView(R.layout.waitingdialog);
		waitingDialog.setCancelable(false);

		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		// menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setBehindOffset(100);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.menu);

	}

	class BG_BrandList extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			processBar.setVisibility(View.VISIBLE);
			array_brand = new ArrayList<BrandList>();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				array_brand = GetJsonFromAPI.getBrandList(API.GetBrandList);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			processBar.setVisibility(View.INVISIBLE);
			try {
				initScrollbar();
				retry.setVisibility(View.GONE);
				scroll.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
				retry.setVisibility(View.VISIBLE);
				scroll.setVisibility(View.GONE);
			}

			waitingDialog.cancel();
		}

	}

	private void setListviewLeftMenu() {
		contents = getResources().getStringArray(R.array.list_drawer_array);
		rowItems = new ArrayList<RowItem>();
		initRowItem();
		// ///////////////////////////////////////////////
		// Check the AD List and then add it into rowItems at position 6
		try {

			// mobileADs = new ArrayList<MobileAD>();
			int adSize = mobileADs.size();
			for (int i = (adSize - 1); i >= 0; i--) {
				rowItems.add(6, new RowItem(MainActivity.class, 1, mobileADs
						.get(i).getTitle(), false));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		listView = (ListView) findViewById(R.id.list);
		CustomListViewAdapter adapter = new CustomListViewAdapter(this,
				R.layout.list_item, rowItems, mobileADs.size());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		if (positionClick != 2)
			listView.setSelection(positionClick);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int pos,
			long id) {
		// To close SlidingMenu if it is opening
		positionClick = pos;
		if (menu.isMenuShowing())
			menu.toggle();

		// logoutFace();
		final String item = rowItems.get(pos).getContent();
		final Resources res = context.getResources();
		for (int a = 0; a < listView.getChildCount(); a++) {
			listView.getChildAt(a).setBackgroundColor(Color.WHITE);
		}
		// Toast.makeText(context, item, 400).show();
		view.setBackgroundColor(Color.GRAY);

		menu.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int size = rowItems.size();

				/*
				 * Ngon ngu, moi dung ung dung , dich vu khach hang k0 mo
				 * activity
				 */
				if (rowItems.get(pos).getIntentClass() != MainActivity.class)
					startActivity(new Intent(context, rowItems.get(pos)
							.getIntentClass()));
				else if (pos == (size - 1)) // Logout
				{
					Vietnalyze.logEvent("logout");
					finish();
					positionClick = 2;
				} else if (item.equals(res.getString(R.string.language)))
					languageSetting();
				else if (pos == 2) {
					Vietnalyze.logEvent("Home");
				}
				// Mobile AD link
				try {

					for (String item_ads : array_titleAds) {
						if (item.equals(item_ads)) {
							Vietnalyze.logEvent("campaignMobile");
							Intent it = new Intent(context,
									FacebookActivity.class);
							it.putExtra("name", item);
							it.putExtra("url", hm_admobile.get(item));
							startActivity(it);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				// FB FanPage
				if (item.equals(Join)) {
					Vietnalyze.logEvent("JohnFanPage");
					Intent intent = new Intent(context, FacebookActivity.class);
					intent.putExtra("name", Join);
					intent.putExtra("url", array_brand.get(0).getFBPage());
					startActivity(intent);
				} else if (item.equals(Aino)) {
					Vietnalyze.logEvent("AinoFanPage");
					Intent intent = new Intent(context, FacebookActivity.class);
					intent.putExtra("name", Aino);
					intent.putExtra("url", array_brand.get(1).getFBPage());
					startActivity(intent);
				} else if (item.equals(Farah)) {
					Vietnalyze.logEvent("FarahFanPage");
					Intent intent = new Intent(context, FacebookActivity.class);
					intent.putExtra("name", Farah);
					intent.putExtra("url", array_brand.get(2).getFBPage());
					startActivity(intent);
				} else if (item.equals("Mời dùng ứng dụng")
						|| item.equals("App Invitation")) {
					// invite();
					Vietnalyze.logEvent("inviteFriend");
					checkLoginFB();
				} else if (item.equals("Dịch vụ khách hàng")
						|| item.equals("Customer Service")) {
					Vietnalyze.logEvent("serviceCustomer");
					if (prosoft.android.utility.Utility.canMakeCall(context))
						Utility.showDialogCall(context, str_phone);
				}

			}
		}, 0);

	}

	private void inviteFriend() {
		WebDialog.RequestsDialogBuilder requests = new WebDialog.RequestsDialogBuilder(
				MainActivity.this, Session.getActiveSession());// , params);
		requests.setTitle("wkhseiwu");
		requests.setMessage("Let use app with me");
		requests.setOnCompleteListener(new WebDialog.OnCompleteListener() {
			@Override
			public void onComplete(Bundle values, FacebookException error) {
				if (error != null) {
					if (error instanceof FacebookOperationCanceledException) {
						Toast.makeText(MainActivity.this, "Request cancelled",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MainActivity.this, "Network Error",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					final String requestId = values.getString("request");
					if (requestId != null) {
						// Toast.makeText(getActivity(), "Request sent",
						// Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(MainActivity.this, "Request cancelled",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		WebDialog dialog = requests.build();
		dialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			menu.toggle();
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing())
			menu.toggle();
		else
			super.onBackPressed();
	}

	private void invite() {
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@SuppressWarnings("deprecation")
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {

					// make request to the /me API
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										inviteFriend();

									} else {
										System.out.println("user == null ");
									}
								}
							});
				}
			}
		});
	}

	/*
	 * private void dialogCall() {
	 * 
	 * view = new Dialog(context);
	 * 
	 * view.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * view.setContentView(R.layout.custom_dialog); initButtons();
	 * view.getWindow().setBackgroundDrawable(new ColorDrawable(0)); // set the
	 * custom dialog components - text, image and button
	 * 
	 * // if button is clicked, close the custom dialog
	 * 
	 * view.show(); }
	 */

	/*
	 * public void initButtons() { hospital = (ImageView)
	 * view.findViewById(R.id.iv_emergency_hospital); office = (ImageView)
	 * view.findViewById(R.id.iv_emergency_office); fire = (ImageView)
	 * view.findViewById(R.id.iv_emergency_fire);
	 * 
	 * if (hospital != null) hospital.setOnClickListener(this); if (office !=
	 * null) office.setOnClickListener(this); if (fire != null)
	 * fire.setOnClickListener(this);
	 * 
	 * // hospital.setVisibility(View.INVISIBLE); //
	 * office.setVisibility(View.INVISIBLE); //
	 * fire.setVisibility(View.INVISIBLE);
	 * 
	 * int mid_height = context.getResources().getDimensionPixelSize(
	 * R.dimen.emergency_mid_height); int horizolfrommid =
	 * context.getResources().getDimensionPixelSize(
	 * R.dimen.emergency_horizol_from_mid); int verticalfrommid =
	 * context.getResources().getDimensionPixelSize(
	 * R.dimen.emergency_vertical_from_mid); int size =
	 * context.getResources().getDimensionPixelSize( R.dimen.emergency_size);
	 * mid_height += size; horizolfrommid += size; int duration = 800;
	 * 
	 * TranslateAnimation mid_trans_anim = new TranslateAnimation(0, 0,
	 * mid_height, 0); mid_trans_anim.setStartOffset(0);
	 * mid_trans_anim.setDuration(duration);
	 * 
	 * TranslateAnimation left_trans_anim = new TranslateAnimation(
	 * horizolfrommid, 0, mid_height - verticalfrommid, 0);
	 * left_trans_anim.setStartOffset(0); left_trans_anim.setDuration(duration);
	 * 
	 * TranslateAnimation right_trans_anim = new TranslateAnimation(
	 * -horizolfrommid, 0, mid_height - verticalfrommid, 0);
	 * right_trans_anim.setStartOffset(0);
	 * right_trans_anim.setDuration(duration);
	 * 
	 * Animation rot_anim = new RotateAnimation(0f, 360f,
	 * Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	 * rot_anim.setStartOffset(0); rot_anim.setDuration(duration);
	 * 
	 * Animation scale_anim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
	 * Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	 * scale_anim.setStartOffset(0); scale_anim.setDuration(duration);
	 * 
	 * hospital_set = new AnimationSet(false);
	 * hospital_set.addAnimation(rot_anim);
	 * hospital_set.addAnimation(scale_anim);
	 * hospital_set.addAnimation(mid_trans_anim);
	 * hospital_set.setFillAfter(true); hospital_set.setAnimationListener(new
	 * AnimationListener() {
	 * 
	 * @Override public void onAnimationStart(Animation animation) { // TODO
	 * Auto-generated method stub isTranslating = true; }
	 * 
	 * @Override public void onAnimationRepeat(Animation animation) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onAnimationEnd(Animation animation) { // TODO
	 * Auto-generated method stub isTranslating = false; } });
	 * hospital.startAnimation(hospital_set);
	 * 
	 * office_set = new AnimationSet(false); office_set.addAnimation(rot_anim);
	 * office_set.addAnimation(scale_anim);
	 * office_set.addAnimation(left_trans_anim); office_set.setFillAfter(true);
	 * office.startAnimation(office_set);
	 * 
	 * fire_set = new AnimationSet(false); fire_set.addAnimation(rot_anim);
	 * fire_set.addAnimation(scale_anim);
	 * fire_set.addAnimation(right_trans_anim); fire_set.setFillAfter(true);
	 * fire.startAnimation(fire_set); }
	 */

	public void closeButtons() {
		view.dismiss();
		if (hospital_set == null || office_set == null || fire_set == null)
			return;

		hospital.clearAnimation();
		fire.clearAnimation();
		office.clearAnimation();

		int mid_height = context.getResources().getDimensionPixelSize(
				R.dimen.emergency_mid_height);
		int horizolfrommid = context.getResources().getDimensionPixelSize(
				R.dimen.emergency_horizol_from_mid);
		int verticalfrommid = context.getResources().getDimensionPixelSize(
				R.dimen.emergency_vertical_from_mid);
		int size = context.getResources().getDimensionPixelSize(
				R.dimen.emergency_size);
		mid_height += size;
		horizolfrommid += size;
		int duration = 800;

		TranslateAnimation mid_trans_anim = new TranslateAnimation(0, 0, 0,
				mid_height);
		mid_trans_anim.setStartOffset(0);
		mid_trans_anim.setDuration(duration);

		TranslateAnimation left_trans_anim = new TranslateAnimation(0,
				horizolfrommid, 0, mid_height - verticalfrommid);
		left_trans_anim.setStartOffset(0);
		left_trans_anim.setDuration(duration);

		TranslateAnimation right_trans_anim = new TranslateAnimation(0,
				-horizolfrommid, 0, mid_height - verticalfrommid);
		right_trans_anim.setStartOffset(0);
		right_trans_anim.setDuration(duration);

		Animation rot_anim = new RotateAnimation(360f, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rot_anim.setStartOffset(0);
		rot_anim.setDuration(duration);

		Animation scale_anim = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale_anim.setStartOffset(0);
		scale_anim.setDuration(duration);

		hospital_set = new AnimationSet(false);
		hospital_set.addAnimation(rot_anim);
		hospital_set.addAnimation(scale_anim);
		hospital_set.addAnimation(mid_trans_anim);
		hospital_set.setFillAfter(true);
		hospital_set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				isTranslating = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				isTranslating = false;
				hospital.setVisibility(View.INVISIBLE);
				office.setVisibility(View.INVISIBLE);
				fire.setVisibility(View.INVISIBLE);

			}
		});
		hospital.startAnimation(hospital_set);

		office_set = new AnimationSet(false);
		office_set.addAnimation(rot_anim);
		office_set.addAnimation(scale_anim);
		office_set.addAnimation(left_trans_anim);
		office_set.setFillAfter(true);
		office.startAnimation(office_set);

		fire_set = new AnimationSet(false);
		fire_set.addAnimation(rot_anim);
		fire_set.addAnimation(scale_anim);
		fire_set.addAnimation(right_trans_anim);
		fire_set.setFillAfter(true);
		fire.startAnimation(fire_set);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.john:
			Vietnalyze.logEvent("JohnHenryTab");
			// img_john.setBackgroundResource(R.drawable.tabeffect_johnhenry);
			// img_aino.setBackgroundResource(R.drawable.tab_aino);
			// img_farah.setBackgroundResource(R.drawable.tab_farahvintage);
			over_john.setVisibility(View.VISIBLE);
			over_aino.setVisibility(View.INVISIBLE);
			over_fara.setVisibility(View.INVISIBLE);
			scroll.scrollBy(-500, 1);
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, John_Henry)
					.commit();
			pos_tabBar = 1;
			break;
		case R.id.aino:
			Vietnalyze.logEvent("AinoTab");
			// img_aino.setBackgroundResource(R.drawable.tabeffect_ainosofia);
			// img_john.setBackgroundResource(R.drawable.tab_johnhenry);
			// img_farah.setBackgroundResource(R.drawable.tab_farahvintage);
			over_john.setVisibility(View.INVISIBLE);
			over_aino.setVisibility(View.VISIBLE);
			over_fara.setVisibility(View.INVISIBLE);
			if (pos_tabBar == 1)
				scroll.scrollBy(100, 2);
			else
				scroll.scrollBy(-100, 2);
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, Anio).commit();
			break;
		case R.id.farah:
			Vietnalyze.logEvent("FarahTab");
			// img_farah.setBackgroundResource(R.drawable.tabeffect_farahvintage);
			// img_john.setBackgroundResource(R.drawable.tab_johnhenry);
			// img_aino.setBackgroundResource(R.drawable.tab_aino);
			over_john.setVisibility(View.INVISIBLE);
			over_aino.setVisibility(View.INVISIBLE);
			over_fara.setVisibility(View.VISIBLE);
			scroll.scrollBy(500, 10);
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, Fara).commit();
			pos_tabBar = 3;
			break;
		case R.id.btn_retry_scroll:

			if (array_brand == null || array_brand.size() == 0) {
				retry.setVisibility(View.INVISIBLE);
				RetryBar();
			}
			break;
		}

	}

	/**
	 * To init values of LeftMenu Listview's item
	 */
	private void initRowItem() {
		rowItems.add(new RowItem(MemberActivity.class, R.drawable.username,
				contents[0], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.ic_launcher,
				contents[1], true));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.home,
				contents[2], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.ic_launcher,
				contents[3], true));
		rowItems.add(new RowItem(CouponActivity.class, R.drawable.coupons,
				contents[4], false));
		rowItems.add(new RowItem(SlotMachineActivity.class, R.drawable.game,
				contents[5], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.ic_launcher,
				contents[6], true));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.john_henry,
				contents[7], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.aino_sofia,
				contents[8], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.farah_vintage,
				contents[9], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.ic_launcher,
				contents[10], true));
		rowItems.add(new RowItem(SearchNearBy.class, R.drawable.search_icon,
				contents[11], false));

		rowItems.add(new RowItem(ListNearByCityActivity.class,
				R.drawable.location_icon, contents[12], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.ic_launcher,

		contents[13], true));
		rowItems.add(new RowItem(PurchaseActivity.class,
				R.drawable.purchase_historry, contents[14], false));
		rowItems.add(new RowItem(CouponHisActivity.class,
				R.drawable.coupon_history, contents[15], false));
		rowItems.add(new RowItem(NotificationActivity.class,
				R.drawable.notification_history, contents[16], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.ic_launcher,
				contents[17], true));
		rowItems.add(new RowItem(Account.class, R.drawable.setting_icon,
				contents[18], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.english_icon,
				contents[19], false));
		rowItems.add(new RowItem(MainActivity.class, R.drawable.share_icon,
				contents[20], false));
		if (Utility.canMakeCall(context))
			rowItems.add(new RowItem(MainActivity.class,
					R.drawable.customer_service, contents[21], false));
		rowItems.add(new RowItem(LoginMenuActivity.class,
				R.drawable.signout_icon, contents[22], false));

	}

	private void initMobileAdList(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		mobileADs = new ArrayList<MobileAD>();
		hm_admobile = new HashMap<String, String>();
		array_titleAds = new String[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject;
			try {

				jsonObject = jsonArray.getJSONObject(i);

				int ID = jsonObject.getInt("ID");

				String Title = jsonObject.getString("Title");
				String OpenUrl = jsonObject.getString("OpenUrl");
				String ThumbImg = jsonObject.getString("ThumbImg");
				hm_admobile.put(Title, OpenUrl);
				array_titleAds[i] = Title;
				mobileADs.add(new MobileAD(Title, OpenUrl, ThumbImg, ID));

			} catch (JSONException e) {
				mobileADs = new ArrayList<MobileAD>();
			}

		}
		Log.d("TAG", "array = " + mobileADs.size());

	}

	/**
	 * To get Mobile ad list from url
	 * 
	 * @author My PC
	 * 
	 */
	private class HttpRequestMobileAD extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(result);
				// init MobileAdList
				initMobileAdList(jsonArray);
			} catch (JSONException e) {
				mobileADs = new ArrayList<MobileAD>();
			}

			setListviewLeftMenu();

		}
	}

	private void languageSetting() {
		View view = View.inflate(this, R.layout.checkbox, null);
		TextView tvVi = (TextView) view.findViewById(R.id.checkboxVi);
		tvVi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				share.setStatusLanguage("vi_VN");
				LoginMenuActivity.changelanguage(context, "vi");
				setListviewLeftMenu();
				dialog.cancel();
			}
		});
		TextView tvEn = (TextView) view.findViewById(R.id.checkboxEn);
		tvEn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				share.setStatusLanguage("en");
				LoginMenuActivity.changelanguage(context, "en");
				setListviewLeftMenu();
				dialog.cancel();
			}
		});
		tvVi.setText(getResources().getString(R.string.vietnamese));
		tvEn.setText(getResources().getString(R.string.english));

		builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.settingLanguage));
		builder.setView(view)
				.setCancelable(true)
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		dialog = builder.create();
		dialog.show();

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
			getProfileExample();

		}

		@Override
		public void onNotAcceptingPermissions() {
			// toast("You didn't accept read permissions");
		}
	};

	private void checkLoginFB() {

		boolean islogin = mSimpleFacebook.isLogin();
		if (islogin)
			startActivity(new Intent(this, InviteFBActivity.class));
		else
			mSimpleFacebook.login(mOnLoginListener);
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
			}

			@Override
			public void onComplete(Profile profile) {

				if (Utility.fb_id.equals("")) {
					Session session = Session.getActiveSession();
					Utility.accessToken = session.getAccessToken();
					Utility.fb_avarta = "http://graph.facebook.com/"
							+ profile.getUsername() + "/picture";
					share.setAccessToken(session.getAccessToken());
					share.setimgAvarta(Utility.fb_avarta);
					GetJsonFromAPI.postRequest(API.getupdatefbID(Utility.email,
							Utility.fb_avarta));
				}
				startActivity(new Intent(MainActivity.this,
						InviteFBActivity.class));
			}
		};

		mSimpleFacebook.getProfile(onProfileRequestListener);

	}
}
