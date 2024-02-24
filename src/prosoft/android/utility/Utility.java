package prosoft.android.utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

import com.android.activity.NoInternetActivity;
import com.ctyprosoft.tmg.R;

public class Utility {

	/**
	 * To check Internet is available or not.
	 * 
	 * @return true if Internet is available and else return false
	 */
	public static String systemString = "Android_Ver.";
	public static String IdCouponDetail = "idCoupon";
	public static String IdPurchaseDetail = "idPurchase";
	// Variable for usefalser
	public static String user_name = "";
	public static String fb_id = "";
	public static String email = "";
	public static String fb_avarta = "";
	public static String cardNo = "";
	public static String birthday = "";
	public static String phone = "";
	public static String gender = "";
	public static String accessToken = "";
	
	public static boolean first_coupon_guide;
	public static boolean first_game_guide;
	public static boolean first_invite_guide;
	
	public static boolean first_brand;
	public static boolean first_city;

	public static void NoInternet(Context context, String sms) {
		context.startActivity(new Intent(context, NoInternetActivity.class));
	}

	public static void showDialog(Context context, String sms) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(sms);

		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		Dialog dialog = builder.create();
		// TextView messageText =
		// (TextView)dialog.findViewById(android.R.id.message);
		// messageText.setGravity(Gravity.CENTER);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
	}

	public static boolean isInternet(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if ((mWifi != null && mWifi.isConnected())
				|| (mMobile != null && mMobile.isConnected()))
			return true;
		return false;

	}

	public static boolean canMakeCall(Context context) {
		boolean status = false;
		PackageManager pm = context.getPackageManager();

		 if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) 
			 status = true;
		return status;
	}

	public static void showDialogCall(final Context context,
			final String str_phone) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getResources()
				.getString(R.string.CallConfirmationDesc)
				.replace("%@", str_phone));
		builder.setTitle(context.getResources().getString(
				R.string.CallConfirmation));
		builder.setPositiveButton(
				context.getResources().getString(R.string.CallConfirmationYes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent callIntent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + str_phone));
						context.startActivity(callIntent);
					}
				});
		builder.setNegativeButton(
				context.getResources().getString(
						R.string.CallConfirmationCancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	public static float convertDpToPixels(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static void toast(Context context, String sms) {
		Toast.makeText(context, sms, Toast.LENGTH_LONG).show();
	}

	public static void saveInfoUser(Context context, String _email,
			Boolean logged, String _userName, String _cardNo, String _birthday,
			String _gender, String _phone, String _linkAvarta,
			String _accessToken, String _fbId) {
		SharePreferance share = new SharePreferance(context);
		share.setEmail(_email);
		share.setStatusLogged(logged);
		share.setUserName(_userName);
		share.setCardNo(_cardNo);
		share.setbirth(_birthday);
		share.setgender(_gender);
		share.setphone(_phone);
		share.setimgAvarta(_linkAvarta);
		// b/c normal User.Reset fbID & AccessToken is empty.(Warning case
		// previous user is FB User)
		share.setAccessToken(_accessToken);
		share.setfbID(_fbId);
	}

}
