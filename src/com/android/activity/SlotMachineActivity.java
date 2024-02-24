package com.android.activity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import object.Game;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelAdapter;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.activity.CouponActivity.backgroundGetCpList;
import com.ctyprosoft.tmg.R;
import com.facebook.Session;
import com.google.android.gms.internal.di;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.SimpleFacebook.OnPublishListener;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Profile;

public class SlotMachineActivity extends Activity implements OnClickListener {
	Button mix;
	Game tempGame;
	Handler handle;
	ActionBar actionBar;
	int currentWin = 0;
	int nextWin = 5;
	int cirle1 = 0;
	int cirle2 = 0;
	int cirle3 = 0;
	int[] rd = new int[] { 30, 31, 32 };
	Context context = this;
	TextView tv_credits, tv_totalWin;
	String number_credits = "0000030";
	String number_win = "";
	String value_voucher = "";
	Dialog waiting;
	Game game;
	String email;
	View view_dialog;
	Dialog dialog;
	MediaPlayer mp;
	MediaPlayer mp_win;
	Button btn_retry;
	RelativeLayout rela_cover;
	SharePreferance share;
	private static SimpleFacebook mSimpleFacebook;
	String ThumbImgShare, Value_VoucherShare, Id, gameId;
	int flag = 0;
	int _checkResum = 0;
	int count = 0;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vietnalyze.logEvent("luckySpin");
		initActionBar();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.slot_machine_layout);
		initvariable();

	}

	private void initvariable() {
		email = Utility.email;

		handle = new Handler();

		rela_cover = (RelativeLayout) findViewById(R.id.rela_retry);
		Button btn_retry = (Button) findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(this);

		share = new SharePreferance(context);
		tempGame = new Game();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (_checkResum == 0) {
			_checkResum = 1;
			if (Utility.isInternet(context))
				new BG_GetInfoGameFirst().execute();
			else
				Utility.NoInternet(context, null);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initLayout() {
		tv_credits = (TextView) findViewById(R.id.tv_credit);
		if (Integer.parseInt(number_credits) < 0)
			number_credits = "0";
		tv_credits.setText(number_credits);
		tv_totalWin = (TextView) findViewById(R.id.tv_won);
		tv_totalWin.setText(number_win);
		LinearLayout linear_wrapper_slot_machine = (LinearLayout) findViewById(R.id.linear_wrapper_slot_machine);
		LinearLayout wrap_linear_wrapper_slot_machine = (LinearLayout) findViewById(R.id.wrapper_linear_wrapper_slot_machine);
		android.widget.RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.FILL_PARENT,
				getWindowManager().getDefaultDisplay().getHeight() / 2);
		linear_wrapper_slot_machine.setLayoutParams(params);
		wrap_linear_wrapper_slot_machine.setLayoutParams(params);
		initWheel(R.id.slot_1);
		initWheel(R.id.slot_2);
		initWheel3(R.id.slot_3);
		mix = (Button) findViewById(R.id.btn_mix);
		mix.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				initWheel(R.id.slot_1);
				initWheel(R.id.slot_2);
				initWheel3(R.id.slot_3);
				tempGame = game;
				if (!tempGame.getCpValue().equals(""))
					getInfoWinShare(tempGame);
				mix.setEnabled(false);
				if (nextWin != 5)
					currentWin = nextWin;
				number_credits = String.valueOf(Integer
						.parseInt(number_credits) - 1);

				if (Integer.parseInt(number_credits) >= 0) {

					tv_credits.setText(number_credits);

					if (currentWin == 1) {
						int number = getRandom(rd);
						cirle1 = cirle2 = cirle3 = number;
						mixWheel1(R.id.slot_1, number);
						mixWheel2(R.id.slot_2, number);
						mixWheel3(R.id.slot_3, number);
					} else {
						cirle1 = getRandom(rd);
						cirle2 = getRandom(rd);
						cirle3 = cirle1 + 2;
						mixWheel1(R.id.slot_1, cirle1);
						mixWheel2(R.id.slot_2, cirle2);
						mixWheel3(R.id.slot_3, cirle3);
					}
					if (Integer.parseInt(number_credits) != -1) {
						count = count + 1;
						new BG_GetInfoGame().execute();
					}

				} else {
					showDialogOutCredits();
					mix.setEnabled(true);
				}

			}
		});

	}

	void showDialogOutCredits() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(getResources().getString(R.string.outCredits));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}

	void openDialog() {

		mp_win = MediaPlayer.create(getApplicationContext(), R.raw.win_music);
		mp_win.start();
		number_win = String.valueOf(Integer.parseInt(number_win) + 1);
		tv_totalWin.setText(number_win);
		dialog = new Dialog(context);
		view_dialog = View.inflate(context, R.layout.custom_dialog_luckypin,
				null);
		TextView value = (TextView) view_dialog.findViewById(R.id.value);
		ImageView img = (ImageView) view_dialog
				.findViewById(R.id.img_getvoucher);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// get Voucher
				Vietnalyze.logEvent("getCoupon");
				new BG_GetVoucher().execute();

			}
		});
		value.setText(Value_VoucherShare);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void onBackPressed() {
		// your code.
		// delete api
		try {
			mp.stop();
			// delete voucher api
			GetJsonFromAPI.postRequest(API.deleteVoucher(game.getGameID(),
					email));
		} catch (Exception e) {
			// TODO: handle exception
		}
		finish();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		_checkResum = 0;
		try {
			mp.stop();
			// delete voucher api
			GetJsonFromAPI.postRequest(API.deleteVoucher(game.getGameID(),
					email));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "TEXT").setIcon(R.drawable.help)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// delete api
			try {
				mp.stop();
				// delete voucher api
				GetJsonFromAPI.postRequest(API.deleteVoucher(game.getGameID(),
						email));
			} catch (Exception e) {
				// TODO: handle exception
			}
			finish();

			break;
		case 1:
			Utility.showDialog(context,
					getResources().getString(R.string.helpGame));
			break;
		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	// Wheel scrolled flag
	private boolean wheelScrolled = false;

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener3 = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {

			wheelScrolled = false;
			handle.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					if (currentWin == 1) {
						currentWin = 0;
						openDialog();

					} else
						mix.setEnabled(true);
				}
			}, 200);

		}
	};
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			TextView text = (TextView) findViewById(R.id.pwd_status);
			text.setText("");
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {

			wheelScrolled = false;

		}
	};

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {

			} else {
				mix.setEnabled(false);

			}
		}
	};

	/**
	 * Updates status
	 */
	private void updateStatus() {
		TextView text = (TextView) findViewById(R.id.pwd_status);
		if (test()) {
			text.setText("Congratulation!");
		} else {
			text.setText("");
		}
	}

	/**
	 * Initializes wheel
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	private void initWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new SlotMachineAdapter(this));
		wheel.setCurrentItem(1);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
	}

	private void initWheel3(int id) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new SlotMachineAdapter(this));
		wheel.setCurrentItem(1);

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener3);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
	}

	private void initWheelStop(int id) {
		WheelView wheel = getWheel(id);
		wheel.stopScrolling();
	}

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * Tests wheels
	 * 
	 * @return true
	 */
	private boolean test() {
		int value = getWheel(R.id.slot_1).getCurrentItem();
		return testWheelValue(R.id.slot_2, value)
				&& testWheelValue(R.id.slot_3, value);
	}

	/**
	 * Tests wheel value
	 * 
	 * @param id
	 *            the wheel Id
	 * @param value
	 *            the value to test
	 * @return true if wheel value is equal to passed value
	 */
	private boolean testWheelValue(int id, int value) {
		return getWheel(id).getCurrentItem() == value;
	}

	/**
	 * Mixes wheel
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel1(int id, int cirle) {
		WheelView wheel = getWheel(id);
		// scroll in 3 second
		// int rd = (int) (Math.random() * 50);
		// int rd = 1;
		wheel.scroll(-250 + cirle, 5000);
		// wheel.scroll( 3 + rd, 3000);
	}

	private void mixWheel2(int id, int cirle) {
		WheelView wheel = getWheel(id);
		// scroll in 3 second
		// int rd = (int) (Math.random() * 50);
		// int rd = 1;
		wheel.scroll(-250 + cirle, 6500);
		// wheel.scroll( 3 + rd, 3000);
	}

	private void mixWheel3(int id, int cirle) {
		WheelView wheel = getWheel(id);
		// scroll in 3 second
		int rd = (int) (Math.random() * 50);
		// int rd = 1;
		wheel.scroll(-250 + cirle, 9000);
		// wheel.scroll( 3 + rd, 3000);
	}

	class BG_GetInfoGameFirst extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waiting = new Dialog(context);
			waiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waiting.setContentView(R.layout.waitingdialog);
			waiting.setCancelable(false);
			waiting.show();
			game = new Game();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				game = GetJsonFromAPI.getGameInfo(API.getInfoGame(email));
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
				waiting.cancel();
				MainActivity.timeout = 0;
				rela_cover.setVisibility(View.VISIBLE);
			} else {
				rela_cover.setVisibility(View.INVISIBLE);
				// getInfoWinShare(game);
				try {
					currentWin = Integer.parseInt(game.getWin());
					if (currentWin == 1) {
						number_win = String.valueOf(Integer.parseInt(game
								.getTotal_wins()) - 1);
						getInfoWinShare(game);
					} else
						number_win = game.getTotal_wins();
					number_credits = String.valueOf(Integer.parseInt(game
							.getCredits()) + 1);// b/c api auto minus
					initLayout();
					waiting.cancel();
					mp = MediaPlayer.create(getApplicationContext(),
							R.raw.bg_mucis);
					mp.setLooping(true);
					mp.start();
					if (Utility.first_game_guide) {
						share.setFirstGameGuide(false);
						Utility.first_game_guide = false;
						Utility.showDialog(context,
								getResources().getString(R.string.helpGame));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
	}

	class BG_GetInfoGame extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			game = new Game();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				game = GetJsonFromAPI.getGameInfo(API.getInfoGame(email));

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
				waiting.cancel();
				MainActivity.timeout = 0;
				rela_cover.setVisibility(View.VISIBLE);
			} else {
				if (flag == 1 && Integer.parseInt(number_credits) != -1) {
					// first time

				} else
					flag = 1;
				rela_cover.setVisibility(View.INVISIBLE);
				try {
					// if ((count % 2) == 0)
					// nextWin = 1;
					// else
					nextWin = Integer.parseInt(game.getWin());
					// getInfoWinShare(game);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
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
			// if (!waiting.isShowing())
			// waiting.show();
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

	class BG_GetVoucher extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waiting = new Dialog(context);
			waiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waiting.setContentView(R.layout.waitingdialog);
			waiting.setCancelable(false);
			waiting.show();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				GetJsonFromAPI.postRequest(API.getVoucher(Id, gameId, email));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.setCancelable(true);
			mix.setEnabled(true);
			ImageView img = (ImageView) view_dialog
					.findViewById(R.id.img_getvoucher);
			img.setBackgroundResource(R.drawable.share);
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// share FB
					Vietnalyze.logEvent("shareWinGame");
					mSimpleFacebook = SimpleFacebook
							.getInstance(SlotMachineActivity.this);
					boolean islogin = mSimpleFacebook.isLogin();
					if (islogin)
						getProfileExample();
					else
						mSimpleFacebook.login(mOnLoginListener);
					dialog.cancel();
					// reset value win or fail got in previous play
				}
			});
			waiting.cancel();
		}

	}

	/**
	 * Slot machine adapter
	 */
	private class SlotMachineAdapter extends AbstractWheelAdapter {
		// Image size
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		final int IMAGE_WIDTH = width * 2 / 7;
		final int IMAGE_HEIGHT = height / 6;

		// Slot machine symbols
		private final int items[] = new int[] { R.drawable.john_game,
				R.drawable.aino_game, R.drawable.farah_game };

		// Cached images
		private ArrayList<SoftReference<Bitmap>> images;

		// Layout inflater
		private Context context;

		/**
		 * Constructor
		 */
		public SlotMachineAdapter(Context context) {
			this.context = context;
			images = new ArrayList<SoftReference<Bitmap>>(items.length);
			//images = new ArrayList<SoftReference<Bitmap>>();
			for (int id : items) {
				images.add(new SoftReference<Bitmap>(loadImage(id)));
			}
		}

		/**
		 * Loads image from resources
		 */
		private Bitmap loadImage(int id) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), id);
			Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH,
					IMAGE_HEIGHT, true);
			
			
			bitmap.recycle();
			return scaled;
		}

		@Override
		public int getItemsCount() {
			return items.length;
		}

		// Layout params for image view
		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				IMAGE_WIDTH, IMAGE_HEIGHT);

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			ImageView img;
			if (cachedView != null) {
				img = (ImageView) cachedView;
			} else {
				img = new ImageView(context);
			}

			img.setLayoutParams(params);
			SoftReference<Bitmap> bitmapRef = images.get(index);
			Bitmap bitmap = bitmapRef.get();
			if (bitmap == null) {
				bitmap = loadImage(items[index]);
				images.set(index, new SoftReference<Bitmap>(bitmap));
			}
			img.setImageBitmap(bitmap);
			img.setScaleType(ScaleType.FIT_XY);
			
//			int width_pad = ((width / 3) - (IMAGE_WIDTH)) / 2;
//			img.setPadding(width_pad, 7, 0, 0);
//
//			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img
//					.getLayoutParams();
//			lp.addRule(0); // this will make it
//			// center according to its parent
//			lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//			img.setLayoutParams(lp);

			return img;
		}
	}

	public int getRandom(int[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
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
		tv_label.setText(getResources().getString(R.string.member_game));
		actionBar = getActionBar();
		actionBar.setCustomView(iconHome);
		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_retry:
			onResume();
			break;
		}
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
				if (!waiting.isShowing())
					waiting.show();
			}

			@Override
			public void onComplete(Profile profile) {

				// no Update fbID
				if (Utility.fb_id.equals("")) {
					Session session = Session.getActiveSession();
					Utility.fb_id = profile.getId();
					Utility.accessToken = session.getAccessToken();
					Utility.fb_avarta = "http://graph.facebook.com/"
							+ profile.getUsername() + "/picture";

					share.setAccessToken(session.getAccessToken());
					share.setimgAvarta(Utility.fb_avarta);
					GetJsonFromAPI.postRequest(API.getupdatefbID(Utility.email,
							Utility.fb_avarta));
				}
				publishFeedExample();
			}
		};

		mSimpleFacebook.getProfile(onProfileRequestListener);

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
				waiting.cancel();
				Utility.showDialog(context, "Done");
			}
		};

		// feed builder
		final Feed feed = new Feed.Builder()
				.setMessage(getResources().getString(R.string.app_name))
				.setName(Value_VoucherShare).setCaption("").setDescription("")
				.setPicture(ThumbImgShare).build();

		// click on button and publish
		mSimpleFacebook.publish(feed, onPublishListener);
	}

	private void getInfoWinShare(Game game) {
		ThumbImgShare = game.getThumbImg();
		Value_VoucherShare = game.getCpValue();
		Id = game.getID();
		gameId = game.getGameID();

	}

}
