package com.android.activity;

import java.util.Calendar;
import java.util.EnumMap;
import java.util.Map;

import object.Coupon;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.SharePreferance;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ctyprosoft.tmg.R;
import com.ctyprosoft.tmg.R.id;
import com.ctyprosoft.tmg.R.layout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MemberStart extends Activity {
	Context context = this;
	TextView tvBrightness;
	LinearLayout layoutBrightness;

	// For Volume and brightness. 100% = screen height
	private int numberPixelPerPercent = 20;
	// For media time position. 6min = screen width
	private int numberPixelPerSecond = 20;
	private int iX, iY;
	private int screenWidth;
	private int screenHeight;
	private String TOUCH_STATE = "";
	final private String ENHANCE_NONE = "ENHANCE_NONE";
	final private String ENHANCE_VOLUME_IDLE = "ENHANCE_VOLUME_IDLE";
	final private String ENHANCE_VOLUME_START = "ENHANCE_VOLUME_START";
	final private String ENHANCE_BRIGHTNESS_IDLE = "ENHANCE_BRIGHTNESS_IDLE";
	final private String ENHANCE_BRIGHTNESS_START = "ENHANCE_BRIGHTNESS_START";
	final private String ENHANCE_MEDIA_POSITION = "ENHANCE_MEDIA_POSITION";
	final private int MAX_CLICK_DURATION = 400;
	final private int MAX_CLICK_DISTANCE = 10;
	private AudioManager audioManager;
	private int maxVolume;
	private int curVolume;
	private int curVolumeInPercent;
	private int curBrightnessInt = 0;
	private int updatedBrightnessInt = 0;
	private long startClickTime;
	private long mCurrentTime;
	private long mCurrentChangedTime;
	String str_name,str_cardNo,str_email;
	public Dialog waitingDialog;
	Coupon cp;
	SharePreferance share;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initActionbar();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		share = new SharePreferance(context);
		
		str_email = share.getEmail();
		new BG_GetMemCardDetail().execute();
		
	}
	
	class BG_GetMemCardDetail extends AsyncTask<String, Void, String>
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
			cp = new Coupon();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
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
			// barcode text
			str_cardNo = cp.getCpNo();
			share.setCardNo(str_cardNo);
			str_name = cp.getCp_type();
			
			waitingDialog.cancel();
			setContentView(R.layout.member_getstart);
			innitLayout();

		}
		
	}
	private void initActionbar()
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
		tv_label.setText(getResources().getString(R.string.member_label));
		
		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));
		
		actionBar.setCustomView(iconHome);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.hide();
	}
	public void GoHomePage(View view)
	{
		startActivity(new Intent(context,MainActivity.class));
		finish();
	}
	private void innitLayout()
	{
		
        
		setScreenResolution(this);
		float standardsBrigh = 255 * 6/ 10;
		if(getCurrBrighness() < standardsBrigh)
		{
			setBrightness((int)standardsBrigh);
		}
		layoutBrightness = (LinearLayout) findViewById(R.id.layout_video_info_brightness);
		tvBrightness = (TextView)findViewById(R.id.tv_brightness_percent);
		// barcode data

		// barcode image
		Bitmap bitmap = null;
		ImageView iv = (ImageView) findViewById(R.id.img_barcode);
		int height = getWindowManager().getDefaultDisplay().getHeight();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				width, width/2);
		layoutParams.addRule(RelativeLayout.ABOVE,R.id.tv_code);
		iv.setLayoutParams(layoutParams);
		try {

			bitmap = encodeAsBitmap(str_cardNo, BarcodeFormat.CODE_128,
					width, width/2);
			iv.setImageBitmap(bitmap);

		} catch (WriterException e) {
			e.printStackTrace();
		}
		TextView tv = (TextView) findViewById(R.id.tv_code);
		TextView tv_memberNameBelow = (TextView) findViewById(R.id.tv_member_name);
		TextView tv_cardNo = (TextView) findViewById(R.id.tv_cardNo);
		TextView tv_nameabove = (TextView) findViewById(R.id.tv_nameMemberAbove);
		
		
		
		tv.setText(str_cardNo);
		tv_memberNameBelow.setText(getResources().getString(R.string.member) + str_name	);
		tv_nameabove.setText(str_name);
		tv_cardNo.setText(getResources().getString(R.string.cardNo) + str_cardNo);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv_memberNameBelow.setGravity(Gravity.CENTER_HORIZONTAL);
		tv_cardNo.setGravity(Gravity.CENTER_HORIZONTAL);
		tv_nameabove.setGravity(Gravity.CENTER_HORIZONTAL);
	}
	@Override
	public void onBackPressed() {
	    // your code.
		finish();
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
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	public void setLayoutInfo(LinearLayout layoutBrightness,
			LinearLayout layoutVolume, LinearLayout layoutTime) {
		this.layoutBrightness = layoutBrightness;
		this.tvBrightness = (TextView) this.layoutBrightness
				.findViewById(R.id.tv_brightness_percent);
	}

	public void setLayoutInfoVisibility(String TOUCH_STATE) {
		if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START
				&& !layoutBrightness.isShown())
			layoutBrightness.setVisibility(View.VISIBLE);
		else if (TOUCH_STATE == ENHANCE_NONE) {
			layoutBrightness.setVisibility(View.INVISIBLE);
		}
	}
	public void setScreenResolution(Context ctx) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		screenWidth = displaymetrics.widthPixels;
		screenHeight = displaymetrics.heightPixels;
		numberPixelPerPercent = screenHeight / 100;
		numberPixelPerSecond = screenWidth / 360;
		System.out.println("ScreenWidth=" + screenWidth + "\n ScreenHeight="
				+ screenHeight + "\n numberPixelPerPercent="
				+ numberPixelPerPercent);
	}
	private void touchEventHandler(String TOUCH_STATE, int iX, int iY,
			MotionEvent ev) {
		// System.out.println("TouchEventHandler");
		if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START) {
			if (Math.abs(iX - ev.getX()) < 100) {
				int changedPercent = (int) ((iY - ev.getY()) / numberPixelPerPercent);
				int brightnessInt = curBrightnessInt + changedPercent;
				int brightnessInPercent = brightnessInt * 100 / 255;
				if (brightnessInt > 255) {
					brightnessInt = 255;
					brightnessInPercent = 100;
				} else if (brightnessInt < 12) {
					brightnessInt = 11;
					brightnessInPercent = 5;
				}
				updatedBrightnessInt = brightnessInt;

				// update window brightness
				float brightnessFloat = (brightnessInt) / (float) 255;
				setBrightness(brightnessFloat);
				setLayoutTextInfo(TOUCH_STATE,
						String.valueOf(brightnessInPercent));
				System.out.println("percentOfChanged=" + changedPercent
						+ "\n curBrightness=" + brightnessInt);
			}
		}
	}

	private void setBrightness(float brightness) {
		// float brightness = (curBrightnessValue + value) / (float) 255;
		WindowManager.LayoutParams lp = ((Activity) this).getWindow()
				.getAttributes();
		lp.screenBrightness = brightness;
		((Activity) MemberStart.this).getWindow().setAttributes(lp);
	}
	private void setBrightness(int brightness) {
		android.provider.Settings.System.putInt(this.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		android.provider.Settings.System.putInt(this.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
	}
	public void setLayoutTextInfo(String TOUCH_STATE, String mText) {
		if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START)
			tvBrightness.setText(mText + "%");
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("ACTION_DOWN");
			System.out.println("x=" + ev.getX() + ", y=" + ev.getY());
			startClickTime = Calendar.getInstance().getTimeInMillis();
			TOUCH_STATE = ENHANCE_NONE;
			iX = (int) ev.getX();
			iY = (int) ev.getY();

			if (iY < (screenHeight ) ) {
				if (iX < (screenWidth )) {
					TOUCH_STATE = ENHANCE_BRIGHTNESS_IDLE;
				} else if (iX > (screenWidth * 9 / 10)) {
					TOUCH_STATE = ENHANCE_VOLUME_IDLE;
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START
					|| TOUCH_STATE == ENHANCE_VOLUME_START
					|| TOUCH_STATE == ENHANCE_MEDIA_POSITION) {
				touchEventHandler(TOUCH_STATE, iX, iY, ev);
			} else {
				if (TOUCH_STATE == ENHANCE_BRIGHTNESS_IDLE
						&& Math.abs(iY - ev.getY()) > 50
						&& Math.abs(iX - ev.getX()) < 50) {
					TOUCH_STATE = ENHANCE_BRIGHTNESS_START;
					try {
						curBrightnessInt = android.provider.Settings.System
								.getInt(MemberStart.this.getContentResolver(),
										android.provider.Settings.System.SCREEN_BRIGHTNESS);
					} catch (SettingNotFoundException e) {
						e.printStackTrace();
					}

				} else if (TOUCH_STATE == ENHANCE_VOLUME_IDLE
						&& Math.abs(iY - ev.getY()) > 50
						&& Math.abs(iX - ev.getX()) < 50) {
					TOUCH_STATE = ENHANCE_VOLUME_START;
					maxVolume = audioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					curVolume = audioManager
							.getStreamVolume(AudioManager.STREAM_MUSIC);
					curVolumeInPercent = curVolume * 100 / maxVolume;

				} 
				setLayoutInfoVisibility(TOUCH_STATE);
			}
			break;

		case MotionEvent.ACTION_UP:
			// check click event occurred
			long clickDuration = Calendar.getInstance().getTimeInMillis()
					- startClickTime;
			int dx = Math.abs((int) (ev.getX() - iX));
			int dy = Math.abs((int) (ev.getY() - iY));

			// update system brightness
			if (TOUCH_STATE == ENHANCE_BRIGHTNESS_START) {
				setBrightness(updatedBrightnessInt);
			}

			TOUCH_STATE = ENHANCE_NONE;
			setLayoutInfoVisibility(TOUCH_STATE);
			break;

		default:
			break;
		}
		return true;

	}
	private  float getCurrBrighness()
	{
		float curBrightnessValue = 0;
		try {
		    curBrightnessValue=android.provider.Settings.System.getInt(
		    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return curBrightnessValue;
	}
}
