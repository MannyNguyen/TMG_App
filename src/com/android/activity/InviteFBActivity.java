package com.android.activity;

import java.util.ArrayList;
import java.util.List;

import object.InviteFB;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;

import com.android.activity.SlotMachineActivity.BG_GetInfoGameFirst;
import com.ctyprosoft.tmg.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnInviteListener;

import adapter.AdapterInviteFb;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class InviteFBActivity extends Activity implements OnItemClickListener,
		OnScrollListener, OnClickListener {
	Context context = this;
	AdapterInviteFb adapter;
	ListView lv;
	ArrayList<InviteFB> array, array_temp;
	public Dialog waitingDialog;
	SharePreferance share;
	String email;

	private int currentPage = 1;
	private int positionGridView = 0;
	int firstVisiblePosition;
	int visibleItem;
	int totalItem;
	public static int totalPage = 0;
	Button btn_retry;
	RelativeLayout rela_cover;
	ImageView img_check, img_checkAll;
	private SimpleFacebook mSimpleFacebook;
	public int flag_checkAll = 0;
	String[] friend;
	ArrayList<String> array_id;
	String str_fbId, str_fbName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		iniActionBar();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_inviter);

		initVariable();

	}

	private void initVariable() {
		// email = Utility.email;
		email = "anhtu502002@yahoo.com";

		rela_cover = (RelativeLayout) findViewById(R.id.rela_retry);
		Button btn_retry = (Button) findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(this);
		img_checkAll = (ImageView) findViewById(R.id.img_checkall);

		array_id = new ArrayList<String>();
		
		share = new SharePreferance(context);
		
	}

	private void iniActionBar() {
		ActionBar actionBar = getActionBar();
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
		tv_label.setText(getResources().getString(R.string.invitefb));

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
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		if(Utility.isInternet(context))
			new backgroundGetCpList().execute();
		else 
			Utility.NoInternet(context, null);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
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
			flag_checkAll = 0;
			finish();
			break;
		case 1:
			Utility.showDialog(context,
					getResources().getString(R.string.helpInvite));
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
		flag_checkAll = 0;
		finish();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		flag_checkAll = 0;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// if(array_temp.size() > 50)
		// Utility.showDialog(context,
		// getResources().getString(R.string.limitInvite));
		int i = 0;
		for (InviteFB a : array) {
			if (a.isCheck())
				i++;
		}
		img_check = (ImageView) arg1.findViewById(R.id.img_invite);
		if (!array.get(arg2).isCheck()) {
			if(i >= 50)
				Utility.showDialog(context, getResources().getString(R.string.limitInvite));
			else
			{
				img_check.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.icontick));
				array.get(arg2).setCheck(true);
			}
			
		} else {
			img_check.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.iconadd));
			array.get(arg2).setCheck(false);
		}

	}

	class backgroundGetCpList extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waitingDialog = new Dialog(context);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			waitingDialog.show();
			array = new ArrayList<InviteFB>();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				array = GetJsonFromAPI.getFBFriend(array_temp, API
						.getFBFriendList(Utility.fb_id, Utility.accessToken,
								String.valueOf(currentPage)));
				array_temp = new ArrayList<InviteFB>(array);
//				totalPage = GetJsonFromAPI.getTotalPage(API.getCPListString(
//						email, String.valueOf(currentPage)));
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
			} else {
				rela_cover.setVisibility(View.INVISIBLE);
				lv = (ListView) findViewById(R.id.lv_coupon);
				adapter = new AdapterInviteFb(InviteFBActivity.this, array);
				adapter.notifyDataSetChanged();
				lv.setAdapter(adapter);
				lv.setSelection(positionGridView);
				lv.setOnScrollListener(InviteFBActivity.this);
				lv.setOnItemClickListener(InviteFBActivity.this);
				waitingDialog.cancel();
				if(Utility.first_invite_guide)
				{
					share.setFirstGameGuide(false);
					Utility.first_invite_guide = false;
					Utility.showDialog(context, getResources().getString(R.string.helpInvite));
				}
			}
			// set icon check all is deactive every time reload
			img_checkAll.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.icontick_deactive));
			flag_checkAll = 0;

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

	public void sendInvite(View view) {
		// listener for invite action
		array_id.clear();
		str_fbId = "";
		str_fbName = "";
		for (InviteFB a : array) {
			if (a.isCheck()) {
				array_id.add(a.getFb_id());
				str_fbId += a.getFb_id() + ",";
				str_fbName += a.getFb_name() + ",";
			}

		}

		friend = array_id.toArray(new String[array_id.size()]);
		final OnInviteListener onInviteListener = new SimpleFacebook.OnInviteListener() {

			@Override
			public void onFail(String reason) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onException(Throwable throwable) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(List<String> invitedFriends, String requestId) {
				// TODO Auto-generated method stub
				// update to Backend friend invited
				GetJsonFromAPI.postRequest(API.getupdateFriendInvited(str_fbId,
						str_fbName, Utility.email));
				// Utility.toast(context,"complete");
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		};
		String[] friends = new String[] { "100000582035042",// me
				"100004462446319",// Đông

		};
		System.out.println("friend=" + friend.toString());
		if (friend.length > 0)
			mSimpleFacebook.invite(friend, "I invite you to use this app",
					onInviteListener);
	}

	public void checkAll(View view) {
		try {
			if (array_temp.size() > 50)
				Utility.showDialog(context,
						getResources().getString(R.string.limitInvite));
			else {
				array.clear();
				if (flag_checkAll == 0) {
					img_checkAll.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icontick));
					flag_checkAll = 1;
					for (InviteFB a : array_temp)
						a.setCheck(true);
				} else {
					img_checkAll.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icontick_deactive));
					flag_checkAll = 0;
					for (InviteFB a : array_temp)
						a.setCheck(false);
				}
				array.addAll(array_temp);
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}
}
