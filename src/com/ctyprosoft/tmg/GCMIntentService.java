package com.ctyprosoft.tmg;

import java.util.Random;

import object.InviteFB;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.SharePreferance;

import com.android.activity.CouponActivity;
import com.android.activity.CouponHisActivity;
import com.android.activity.MainActivity;
import com.android.activity.RegisterActivity;
import com.ctyprosoft.tmg.R;
import com.google.android.gcm.GCMBaseIntentService;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "TAG";
	public static final String SENDER_ID = "823769555954";
	static String news = "news";
	static String cp_reminder = "cp_reminder";
	static String cp_used = "cp_used";
	static String invite = "invite";
	static Class<?> class_intent;
	public static String id = "";
	public GCMIntentService() {
		super(SENDER_ID);
		Log.d("TAG", "GCMIntentService");
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.d(TAG, "GCMIntentService Device registered: regId = "
				+ registrationId);

		RegisterActivity.mRegId = registrationId;
		// displayMessage(context, "Your device registred with GCM");

	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.d(TAG, "GCMIntentService Device unregistered");
		// displayMessage(context, getString(R.string.gcm_unregistered));

	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.d(TAG, "GCMIntentService Received message");
		String message = intent.getExtras().getString("message");
		Bundle bundle = intent.getExtras();
		/*
		 * for (String key : bundle.keySet()) { Object value = bundle.get(key);
		 * Log.d(TAG, String.format("%s %s (%s)", key, value.toString(),
		 * value.getClass().getName())); }
		 */
		// displayMessage(context, message);
		// notifies user
		
		
		generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.d(TAG, "GCMIntentService Received deleted messages notification");
		// String message = getString(R.string.gcm_deleted, total);
		// //displayMessage(context, message);
		// // notifies user
		// generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.d(TAG, "GCMIntentService Received error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.d(TAG, "GCMIntentService Received recoverable error: " + errorId);
		// displayMessage(context,
		// getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		if (message == null)
			return;

		String type = "one";
		
		Log.d("TAG", "message : " + message);
		try {
			if (message.contains("###")) {
				String[] parts = message.split("###");
				message = parts[0];
				id = parts[1];
				type = parts[2];
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Log.d("TAG", "generateNotification" + message);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);
		Intent notificationIntent = null;
		SharePreferance share = new SharePreferance(context);
		int i = 0;
		if(!share.getStatusLogged())
		{
			i=1;
			if(RegisterActivity.isActivity)
				class_intent = RegisterActivity.class;
			else
				class_intent = LoginMenuActivity.class;
		}
		else
		{
			if (type.equals(news)) {
				class_intent = MainActivity.class;
			} else if (type.equals(cp_reminder) || type.equals(invite)) {
				class_intent = CouponActivity.class;

			} else if (type.equals(cp_used)) {
				class_intent = CouponHisActivity.class;
			}
		}
		
		notificationIntent = new Intent(context, class_intent);
		notificationIntent.putExtra("used", 2);
		notificationIntent.putExtra("fromNotification", true);
		notificationIntent.putExtra("id", id);
		
		// set intent so it does not start a new activity
		if(i == 0)	
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		else//get activity from stack for use.If not has in stack, create new one
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// To setting multiple notification
		Random r = new Random();
		int random = r.nextInt(1000);
		PendingIntent intent = PendingIntent.getActivity(context, random,
				notificationIntent, PendingIntent.FLAG_ONE_SHOT);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(random, notification);

	}

}
