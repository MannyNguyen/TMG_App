package com.android.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import object.City;
import object.Location_Shop;
import object.Stores;

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

import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import adapter.AdapterNearByCity;
import android.Vietnalyze;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ctyprosoft.tmg.DetailItemStoreList;
import com.ctyprosoft.tmg.R;
import com.ctyprosoft.tmg.SearchNearBy;

import eu.erikw.PullToRefreshListView;

public class ListNearByCityActivity extends Vinalyze implements OnClickListener {
	Context context = this;
	int firstVisiblePosition;
	int visibleItem;
	int totalItem;
	ArrayList<Stores> array;
	public static Resources res;
	public static int positionClick = -1;
	AdapterNearByCity adapter;
	PullToRefreshListView lv;

	// private ArrayList<Location_Shop> shops;
	ArrayList<String> arraycity;

	private ArrayList<City> cities;
	ImageView guidebox;
	SharePreferance share;
	int i = 0;
	Button btn_retry;
	RelativeLayout rela_cover;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Vietnalyze.logEvent("storesList");
		setContentView(R.layout.activity_list_nearby);
		SharePreferance share = new SharePreferance(context);
		if (!share.getStatusfirstBrandCity()) {
			initGuideBox();
		}

		// shops = GoogleMapActivity.locations;
		rela_cover = (RelativeLayout) findViewById(R.id.rela_retry);
		Button btn_retry = (Button) findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Utility.isInternet(context))
			new HttpRequestGetListCity()
					.execute("http://tmg.ctyprosoft.com:81/API/City/GetCityList.aspx");
		else
			Utility.NoInternet(context, null);
	}

	private void initGuideBox() {
		share = new SharePreferance(context);
		guidebox = (ImageView) findViewById(R.id.guide_box);
		if (share.getValueLanguage().equals("en")) {
			guidebox.setImageResource(R.drawable.guidebox);
		} else {
			guidebox.setImageResource(R.drawable.guidebox_vi);
		}
		initGuideBoxAni();
	}

	private void initGuideBoxAni() {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		guidebox = (ImageView) findViewById(R.id.guide_box);
		guidebox.setVisibility(View.VISIBLE);

		Drawable drawable = getResources().getDrawable(R.drawable.guidebox);
		float ratio = 1.0f * drawable.getIntrinsicHeight()
				/ drawable.getIntrinsicWidth();
		int wid = width / 2;
		int hei = (int) (wid * ratio);
		RelativeLayout.LayoutParams guidebox_layoutparams = new RelativeLayout.LayoutParams(
				wid, hei);
		// guidebox_layoutparams.addRule(RelativeLayout.ALIGN_TOP,
		// R.id.rl_taxilist_body);
		guidebox_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		guidebox_layoutparams.setMargins(-1, Dp2Px(10), Dp2Px(10), -1);

		guidebox.setLayoutParams(guidebox_layoutparams);
		// load the animation
		Animation animMove = AnimationUtils.loadAnimation(context, R.anim.move);
		// set animation listener
		animMove.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// guidebox.startAnimation(animation);
			}
		});
		guidebox.startAnimation(animMove);
	}

	void stopGuidebox() {
		try {
			guidebox.clearAnimation();
			guidebox.setVisibility(View.INVISIBLE);
			share.setStatusfirstCity(true);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	int Dp2Px(int pxvalue) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				pxvalue, r.getDisplayMetrics());
		return (int) px;
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

	private void innitAdapterLv() {
		lv = (PullToRefreshListView) findViewById(R.id.listView);
		adapter = new AdapterNearByCity(context, array);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				for (int a = 0; a < lv.getChildCount(); a++) {
					lv.getChildAt(a).setBackgroundColor(Color.WHITE);
				}

				arg1.setBackgroundColor(Color.GRAY);
				positionClick = pos;
				Intent i = new Intent(ListNearByCityActivity.this,
						DetailItemStoreList.class);
				i.putExtra("pos", pos + "");
				startActivity(i);

			}
		});

	}

	private void addDataForNewsPage() {

		array = new ArrayList<Stores>();
		for (int i = 0; i < SearchNearBy.locations.size(); i++) {
			array.add(new Stores(SearchNearBy.locations.get(i).getmStoreName(),
					SearchNearBy.locations.get(i).getmAddress(),
					SearchNearBy.locations.get(i).getmDistance(),
					SearchNearBy.locations.get(i).getmLogo()));
		}

	}

	/**
	 * To init action bar
	 */
	private void initActionBar() {
		// configure action bar
		// action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		View iconHome = View.inflate(context,
				R.layout.custom_actionbar_storelist, null);
		ImageView img = (ImageView) iconHome.findViewById(R.id.image2);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// TextView tv_label = (TextView) iconHome.findViewById(R.id.tv_label);
		// tv_label.setText(getResources().getString(R.string.storelist));

		Spinner spinner = (Spinner) iconHome.findViewById(R.id.spinner);
		int n = cities.size();
		final String[] entries = new String[n];

		for (int i = 0; i < cities.size(); i++) {
			entries[i] = cities.get(i).getName();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, entries);

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v, int pos,
					long id) {
				i = i + 1;
				if (i == 2)
					stopGuidebox();
				new HttpRequestSearchStoreListById()
						.execute("http://tmg.ctyprosoft.com:81/API/Stores/SearchStoreList.aspx?cityid="
								+ cities.get(pos).getID());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		// configure custom view
		LinearLayout listNavLayout = new LinearLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.gravity = Gravity.LEFT;

		listNavLayout.addView(iconHome, params1);

		// listNavLayout.addView(spinner, params);
		// listNavLayout.setGravity(Gravity.RIGHT); // <-- align the spinner to
		// the
		// right
		// configure action bar
		actionBar.setCustomView(listNavLayout);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
	}

	/**
	 * To get List Store by city id
	 * 
	 * @author My PC
	 * 
	 */
	private class HttpRequestGetListCity extends
			AsyncTask<String, String, String> {
		private Dialog progressDialog;

		private HttpRequestGetListCity() {
			progressDialog = new Dialog(ListNearByCityActivity.this);
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.waitingdialog);
			progressDialog.setCancelable(false);
			progressDialog.show();
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
			if (MainActivity.timeout == 1) {
				progressDialog.cancel();
				MainActivity.timeout = 0;
				rela_cover.setVisibility(View.VISIBLE);
			} else {
				rela_cover.setVisibility(View.INVISIBLE);
				try {
					JSONArray jsonArray = new JSONArray(result);

					// init locations
					initCity(jsonArray);

					initActionBar();

					progressDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * To get List Store by city id
	 * 
	 * @author My PC
	 * 
	 */
	private class HttpRequestSearchStoreListById extends
			AsyncTask<String, String, String> {
		private Dialog progressDialog;

		private HttpRequestSearchStoreListById() {
			progressDialog = new Dialog(ListNearByCityActivity.this);
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.setContentView(R.layout.waitingdialog);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
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

			try {
				JSONArray jsonArray = new JSONArray(result);

				// init locations
				initLocation_Shop(jsonArray);

				// Show list view
				addDataForNewsPage();

				innitAdapterLv();

			} catch (JSONException e) {
				Toast.makeText(ListNearByCityActivity.this,
						getResources().getString(R.string.khongcocuahang),
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			progressDialog.dismiss();
		}
	}

	private void initCity(JSONArray jsonArray) {
		cities = new ArrayList<City>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject;
			try {
				jsonObject = jsonArray.getJSONObject(i);

				int mID = jsonObject.getInt("ID");
				String mName = jsonObject.getString("Name");

				cities.add(new City(mID, mName));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void initLocation_Shop(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		SearchNearBy.locations = new ArrayList<Location_Shop>();
		Log.d("TAG", "jsonArray size: " + jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject;
			try {
				jsonObject = jsonArray.getJSONObject(i);

				int mID = jsonObject.getInt("ID");
				int mBrandID = jsonObject.getInt("BrandID");
				int mCityID = jsonObject.getInt("CityID");
				int mValid = jsonObject.getInt("Valid");

				// double mDistance = jsonObject.getDouble("Distance");
				double mGeoX = Double.parseDouble(jsonObject.getString("GeoX"));
				double mGeoY = Double.parseDouble(jsonObject.getString("GeoY"));

				String mBrandName = jsonObject.getString("BrandName");
				String mStoreName = jsonObject.getString("StoreName");
				String mAddress = jsonObject.getString("Address");
				String mPhone = jsonObject.getString("Phone");
				String mCityName = jsonObject.getString("CityName");
				String mLogo = jsonObject.getString("Logo");

				SearchNearBy.locations.add(new Location_Shop(mID, mBrandID,
						mCityID, mValid, 0.0, mGeoX, mGeoY, mBrandName,
						mStoreName, mAddress, mPhone, mCityName, mLogo));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_retry:
			onResume();
			break;

		default:
			break;
		}
	}
}
