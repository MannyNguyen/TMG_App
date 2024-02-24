package com.android.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Coupon;
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
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ctyprosoft.tmg.GCMIntentService;
import com.ctyprosoft.tmg.LoginMenuActivity;
import com.ctyprosoft.tmg.R;
import com.google.android.gcm.GCMRegistrar;

public class RegisterActivity extends Activity {
	Context context = this;
	Spinner spinnerGender, spinnerM, spinnerD;
	EditText name, email, phone, yeard, confirm_phone;
	public Dialog waitingDialog;
	String txtname, txtemail, txtphone, txtphoneconfirm, txtgender, txtmonth,
			txtyeard, txtday, txtbirthday, txtdeviceId, txtsystem, txttoken;
	SharePreferance share;
	public static String mRegId;
	Resources res;
	public static boolean isActivity = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		innitVariables();
		getRegIdNotification();
		/*
		 * Action Bar
		 */
		initActionbar();
		if (share.getclickLoginFB())
			fillform();
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

	private void initActionbar() {
		ActionBar actionBar = getActionBar();
		actionBar
				.setTitle(getResources().getString(R.string.titleActionBarReg));
		View iconHome = View.inflate(context, R.layout.custom_actionbar_back,
				null);
		ImageView img = (ImageView) iconHome.findViewById(R.id.image2);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context, LoginMenuActivity.class));
				finish();
			}
		});
		TextView tv_label = (TextView) iconHome.findViewById(R.id.tv_label);
		tv_label.setText(getResources().getString(R.string.register));

		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));

		actionBar.setCustomView(iconHome);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	private void fillform() {
		name.setText(LoginMenuActivity.rg.getName());
		email.setText(LoginMenuActivity.rg.getEmail());
		try {

			if (LoginMenuActivity.rg.getGender().equals("male")
					|| LoginMenuActivity.rg.getGender().equals("name"))
				spinnerGender.setSelection(01);
			else
				spinnerGender.setSelection(02);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			String[] day = LoginMenuActivity.rg.getBirthday().split("/");
			String _month = day[0];
			String _day = day[1];
			String _yeard = day[2];
			yeard.setText(_yeard);
			spinnerM.setSelection(Integer.parseInt(_month) - 1);
			spinnerD.setSelection(Integer.parseInt(_day) - 1);
		} catch (Exception e) {
			// TODO: handle exception
		}

		phone.setFocusableInTouchMode(true);
		phone.requestFocus();
	}

	private void innitVariables() {
		share = new SharePreferance(context);

		spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
		spinnerM = (Spinner) findViewById(R.id.spinner_month);
		spinnerD = (Spinner) findViewById(R.id.spinner_day);
		addItemsOnSpinners();
		name = (EditText) findViewById(R.id.input_name);
		email = (EditText) findViewById(R.id.input_email_reg);
		phone = (EditText) findViewById(R.id.input_phone_reg);
		confirm_phone = (EditText) findViewById(R.id.input_phone_reg_confirm);
		yeard = (EditText) findViewById(R.id.edit_yeard);

		res = getResources();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(context, LoginMenuActivity.class));
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		startActivity(new Intent(context, LoginMenuActivity.class));
		finish();
	}

	private void getvaluesInput() {
		txtname = name.getText().toString();
		txtemail = email.getText().toString();
		txtphone = phone.getText().toString();
		txtphoneconfirm = confirm_phone.getText().toString();
		txtmonth = spinnerM.getSelectedItem().toString();
		txtday = spinnerD.getSelectedItem().toString();
		txtyeard = yeard.getText().toString();
		txtgender = spinnerGender.getSelectedItem().toString();
		
		// To check phone number 
		Pattern pattern = Pattern.compile("\\d{9}\\d+");
	    Matcher matcher = pattern.matcher(txtphone);
		
		if (txtname.equals(""))
			Utility.showDialog(context, res.getString(R.string.nameRequired));
		else if (txtemail.equals(""))
			Utility.showDialog(context, res.getString(R.string.emailRequired));
		else if (txtphone.equals(""))
			Utility.showDialog(context, res.getString(R.string.phoneRequired));
		else if (txtphone.length() > 12 || txtphone.length() < 10 || !matcher.matches())
			Utility.showDialog(context, res.getString(R.string.phoneInvalid));
		else if (!txtphone.equals(txtphoneconfirm))
			Utility.showDialog(context, res.getString(R.string.phoneNotMatch));
		else if (txtgender.equals(getResources().getString(R.string.gender)))
			Utility.showDialog(context, res.getString(R.string.genderRequired));
		else if (txtyeard.equals(""))
			Utility.showDialog(context,
					res.getString(R.string.birthdayRequired));
		else if (txtyeard.length() < 3 || txtyeard.length() > 4)
			Utility.showDialog(context, res.getString(R.string.birthdayInvalid));
		else
			new backgroungRegister().execute();

	}

	public void Submit(View view) {
		try {
			Vietnalyze.logEvent("register ");
			getvaluesInput();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	class backgroungRegister extends AsyncTask<String, Void, String> {
		Coupon cp = new Coupon();
		String status = "";
		boolean loginstatus;
		String strBirth;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waitingDialog = new Dialog(context);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			waitingDialog.show();
			txtbirthday = txtyeard + "-" + txtmonth + "-" + txtday;
			strBirth = txtday + "." + txtmonth + "." + txtyeard;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (!share.getclickLoginFB()) {
				String android_id = Secure.getString(
						RegisterActivity.this.getContentResolver(),
						Secure.ANDROID_ID);
				loginstatus = GetJsonFromAPI.checkLogin(API.registerString(
						txtname.toString(), txtemail.toString(),
						txtphone.toString(), txtgender, txtbirthday,
						android_id, Utility.systemString
								+ android.os.Build.VERSION.RELEASE, mRegId));
			} else {
				try {

					loginstatus = GetJsonFromAPI.checkLogin(API.checkLoginFB(
							LoginMenuActivity.rg.getFaceid(),
							LoginMenuActivity.rg.getImg_path(),
							txtname.toString(), txtemail.toString(),
							txtphone.toString(), txtgender, txtbirthday,
							LoginMenuActivity.rg.getDeviceID(),
							LoginMenuActivity.rg.getSystem(), mRegId));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			cp = GetJsonFromAPI.getMemberCard(API.getMemCard(email.getText()
					.toString()));
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			waitingDialog.cancel();
			if (loginstatus) {
				SharePreferance share = new SharePreferance(context);
				share.setEmail(txtemail);
				share.setUserName(txtname);
				share.setbirth(strBirth);
				share.setgender(cp.getCp_gender());
				share.setphone(cp.getCp_phone());

				if (share.getclickLoginFB()) {
					share.setimgAvarta(LoginMenuActivity.rg.getImg_path()
							+ "?height=200&width=200");
					share.setAccessToken(LoginMenuActivity.rg.getAccesstoken());
					share.setfbID(LoginMenuActivity.rg.getFaceid());
				} else {
					share.setimgAvarta("");
					share.setAccessToken("");
					share.setfbID("");
				}

				// set status Logged is true
				share.setStatusLogged(true);
				// Set Gender & Age for Vietnalyze
				int age = Calendar.getInstance().get(Calendar.YEAR)
						- Integer.parseInt(txtyeard);
				Vietnalyze.setAge(age);
				String sex = cp.getCp_gender();
				if (sex.equals("Male") || sex.equals("Nam"))
					Vietnalyze.setGender(true);
				else
					Vietnalyze.setGender(false);
				startActivity(new Intent(context, MemberStart.class));
				finish();
			} else
				toast(getResources().getString(R.string.emailUsed));
		}

	}

	// add items into spinner dynamically
	public void addItemsOnSpinners() {

		List<String> list = new ArrayList<String>(Arrays.asList(getResources()
				.getStringArray(R.array.gender)));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		List<String> list_month = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(R.array.month)));
		ArrayAdapter<String> dataAdapter_month = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list_month);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		List<String> list_day = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(R.array.day)));
		ArrayAdapter<String> dataAdapter_day = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list_day);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerGender.setAdapter(dataAdapter);
		spinnerM.setAdapter(dataAdapter_month);
		spinnerD.setAdapter(dataAdapter_day);
	}

	@SuppressLint("ShowToast")
	private void toast(String sms) {
		Toast.makeText(context, sms, 400).show();
	}
}
