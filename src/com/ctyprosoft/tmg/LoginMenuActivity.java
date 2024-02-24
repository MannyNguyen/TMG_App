package com.ctyprosoft.tmg;

import java.util.Locale;
import object.Coupon;
import object.Register;

import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.activity.MainActivity;
import com.android.activity.RegisterActivity;
import com.android.activity.Vinalyze;
import com.ctyprosoft.tmg.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.Facebook;
import com.facebook.widget.LoginButton;
import com.google.android.gcm.GCMRegistrar;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.entities.Profile;

public class LoginMenuActivity extends Vinalyze implements OnClickListener {
	protected static final String TAG = MainActivity.class.getName();
	Context context = this;
	LoginButton btn_fb_login;
	static SharePreferance share;
	Dialog waitingDialog;
	EditText email, phone;
	private String faceID, accessToken, imagePath, name_fb, email_fb, phone_fb,
			gender_fb, birthday_fb, deviceID, system, token;
	String str_email, str_phone;
	Resources res;
	public static Coupon cp_getInfoUserFb;
	/*
	 * Variable Facebook Login
	 */
	private SimpleFacebook mSimpleFacebook;
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	public static Facebook facebook;
	public static Register rg;
	public static String mRegId;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		prepareActivity();

		setContentView(R.layout.login_menu);

		initVariables();

	}

	private void getRegIdNotification() {
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		// Get GCM registration id
		mRegId = GCMRegistrar.getRegistrationId(this);
		// Check if regid already presents
		if (mRegId.equals("")) {

			GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
		}
		Log.d("TAG", "mRegId: " + mRegId);

	}

	private void prepareActivity() {
		/*
		 * set language depend previous setting.if not,set default by setting
		 * device
		 */
		// reset position List Left is Home
		MainActivity.positionClick = 2;
		// set logged flag is false
		share = new SharePreferance(context);
		share.setStatusLogged(false);

		share = new SharePreferance(context);
		String value = share.getValueLanguage();
		if (!value.equals(""))
			changelanguage(context, value);// if value # null.Set language
											// depend user save
		else
			setdefaultLanguage(context);// if not set language depend device
										// language
		// clear FB
		callFacebookLogout(context);
	}

	@SuppressLint("NewApi")
	private void initVariables() {
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		// edit text fields
		email = (EditText) findViewById(R.id.input_email);
		phone = (EditText) findViewById(R.id.input_phone);
		// get info device & version Android
		deviceID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		system = Utility.systemString + android.os.Build.VERSION.RELEASE;
		// hidden Action bar b/c still need dialog the same Activitys has
		// actionBar
		ActionBar ab = getActionBar();
		ab.hide();
		// Login FB Button
		btn_fb_login = (LoginButton) findViewById(R.id.login_button);
		btn_fb_login.setOnClickListener(this);
		res = getResources();
		getRegIdNotification();
	}

	/*
	 * @Override public void onSaveInstanceState(Bundle outState) {
	 * super.onSaveInstanceState(outState);
	 * outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
	 * }
	 */
	@SuppressWarnings("unused")
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			System.out.println("state.isOpened()");

		}
	}

	public static void changelanguage(Context context, String language) {
		String languageToLoad = language; // your language
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
	}

	public static void setdefaultLanguage(Context context) {
		String languageToLoad = Locale.getDefault().toString(); // your language
		String[] en = languageToLoad.split("_");
		if (en[0].equals("en"))
			languageToLoad = "en";
		else if (en[0].equals("vi"))
			languageToLoad = "vi_VN";
		else
			languageToLoad = "en";

		share.setStatusLanguage(languageToLoad);
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		context.getResources().updateConfiguration(config,
				context.getResources().getDisplayMetrics());
	}

	public void SignInWithFace() {
		// start Facebook Login
		Vietnalyze.logEvent("loginWithFB");
		mSimpleFacebook.login(mOnLoginListener);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void GotoMainPage(View view) {
		Intent it = new Intent(context, MainActivity.class);
		startActivity(it);
		finish();
	}

	public void GotoRegister(View view) {
		rg = new Register(faceID, imagePath, name_fb, email_fb, phone_fb,
				gender_fb, birthday_fb, deviceID, system, token, accessToken);
		Intent it = new Intent(context, RegisterActivity.class);
		startActivity(it);
		finish();
	}

	public void login(View view) {
		Vietnalyze.logEvent("loginNormal");
		str_email = email.getText().toString();
		str_phone = phone.getText().toString();
		if (str_email.equals(""))
			Utility.showDialog(context, res.getString(R.string.emailRequired));
		else if (str_phone.equals(""))
			Utility.showDialog(context, res.getString(R.string.phoneRequired));
		else if (str_email.equals("")
				|| !checkEmailValid(email.getText().toString(), "@")
				|| !checkEmailValid(email.getText().toString(), ".com")) {
			Utility.showDialog(context, res.getString(R.string.emailInvalid));
		} else if (Utility.isInternet(context))
			new BG_checkLogin().execute();// check email valid or invalid by
											// APIs
		else
			Utility.NoInternet(context, "");// intent NoInternet Activity
	}

	class BG_checkLogin extends AsyncTask<String, Void, String> {

		boolean loginstatus;
		Coupon cp = new Coupon();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waitingDialog = new Dialog(context);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			waitingDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				loginstatus = GetJsonFromAPI.checkLogin(API.checkLogin(
						str_email, str_phone, deviceID, mRegId));
				cp = GetJsonFromAPI.getMemberCard(API.getMemCard(str_email));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (loginstatus) {
				/*
				 * share.setEmail(str_email); // set status Logged is true
				 * share.setStatusLogged(true);
				 * share.setUserName(cp.getCp_type());
				 * share.setCardNo(cp.getCpNo()); String[] birth =
				 * cp.getCp_birthday().split("/"); String birthday = birth[2] +
				 * "." + birth[1] + "." + birth[0]; share.setbirth(birthday);
				 * share.setgender(cp.getCp_gender());
				 * share.setphone(cp.getCp_phone());
				 * share.setimgAvarta(cp.getImg_path());
				 * 
				 * share.setAccessToken(""); share.setfbID("");
				 */
				String[] birth = cp.getCp_birthday().split("/");
				String birthday = birth[2] + "." + birth[1] + "." + birth[0];
				Utility.saveInfoUser(context, str_email, true,
						cp.getCp_type()// userName,
						, cp.getCpNo(), birthday, cp.getCp_gender(), str_phone,
						cp.getImg_path(), "", "");

				GotoMainPage(null);
			} else
				Utility.toast(context,
						getResources()
								.getString(R.string.emailorphoneincorrect));
			waitingDialog.cancel();
		}

	}

	class BG_loginByFB extends AsyncTask<String, Void, String> {

		boolean loginstatus;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waitingDialog = new Dialog(context);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			waitingDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {

				loginstatus = GetJsonFromAPI.checkLogin(API.checkFBAccount(
						faceID, deviceID, mRegId));

				// cp = GetJsonFromAPI.getMemberCard(API.getMemCard(email_fb));
				// imagePath = GetJsonFromAPI.getImageAvartaFacebook(imagePath);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (loginstatus) {
				/*
				 * share.setStatusLogged(true); share.setCardNo(cp.getCpNo());
				 * share.setUserName(cp.getCp_type()); String[] birth =
				 * cp.getCp_birthday().split("/"); String birthday = birth[2] +
				 * "." + birth[1] + "." + birth[0]; share.setbirth(birthday);
				 * share.setgender(cp.getCp_gender());
				 * share.setphone(cp.getCp_phone()); share.setEmail(email_fb);
				 * share.setimgAvarta(cp.getImg_path()); share.setfbID(faceID);
				 * share.setAccessToken(accessToken);
				 */
				String[] birth = cp_getInfoUserFb.getCp_birthday().split("/");
				String birthday = birth[2] + "." + birth[1] + "." + birth[0];
				Utility.saveInfoUser(context, cp_getInfoUserFb.getCp_Email(),
						true, cp_getInfoUserFb.getCp_type(),
						cp_getInfoUserFb.getCpNo(), birthday,
						cp_getInfoUserFb.getCp_gender(),
						cp_getInfoUserFb.getCp_phone(),
						cp_getInfoUserFb.getImg_path(), accessToken, faceID);
				GotoMainPage(null);
			} else
				GotoRegister(null);
			waitingDialog.cancel();
		}

	}

	public boolean checkEmailValid(String string, String charac) {
		return string.indexOf(charac) > -1;
	}

	public void GoToRegister(View view) {
		share.setclickLoginFB(false);
		startActivity(new Intent(context, RegisterActivity.class));
		finish();
	}

	/**
	 * Logout From Facebook
	 */
	public static void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved
			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			// clear your preferences if saved

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_button:
			share.setclickLoginFB(true);
			mSimpleFacebook.login(mOnLoginListener);

			break;

		default:
			break;
		}
	}

	private void getProfileExample() {
		// listener for profile request
		final OnProfileRequestListener onProfileRequestListener = new SimpleFacebook.OnProfileRequestListener() {

			@Override
			public void onFail(String reason) {
				// insure that you are logged in before getting the profile
				Log.w(TAG, reason);
			}

			@Override
			public void onException(Throwable throwable) {
				Log.e(TAG, "Bad thing happened", throwable);
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while fetching
				// profile
				Log.i(TAG, "Thinking...");
			}

			@Override
			public void onComplete(Profile profile) {
				Log.d("TAG", "Oncomplete");
				if (profile != null)
					Log.d("TAG", "Profile Id: " + profile.getId());
				Session session = Session.getActiveSession();
				accessToken = session.getAccessToken();
				faceID = profile.getId();
				imagePath = "http://graph.facebook.com/" + faceID + "/picture";

				name_fb = profile.getName();
				email_fb = profile.getEmail();
				birthday_fb = profile.getBirthday();
				gender_fb = profile.getGender();

				Log.d("TAG", "Profile: " + "name_fb-" + name_fb + " email_fb:"
						+ email_fb + " imagePath: " + imagePath);

				cp_getInfoUserFb = new Coupon();
				new BG_loginByFB().execute();
			}
		};

		mSimpleFacebook.getProfile(onProfileRequestListener);

	}

}
