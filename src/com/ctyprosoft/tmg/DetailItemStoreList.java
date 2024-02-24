package com.ctyprosoft.tmg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import object.BrandList;
import object.Location_Shop;

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
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DetailItemStoreList extends Activity implements
		OnNavigationListener, OnClickListener {
	// Google Map
	private GoogleMap googleMap;
	public static ArrayList<Location_Shop> locations;
	double lat = 10.771890, lng = 106.667448;
	private Marker marker;
	private Hashtable<String, String> markers;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ArrayList<BrandList> brandLists;
	Context context = this;
	String str_title;
	String str_phone;
	int pos;
	Resources res;
	ImageView guidebox;
	SharePreferance share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get array list from SearchNearBy Activity
		Intent i = getIntent();
		pos = Integer.parseInt(i.getStringExtra("pos"));
		locations = SearchNearBy.locations;
		// get Shop Name
		str_title = locations.get(pos).getmStoreName();
		initActionBar();
		res = getResources();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.detail_stores);

		// Loading map
		initilizeMap();

		// Setting map
		settingMap();

		View v = findViewById(R.id.llHeaderInfor);
		v.setVisibility(View.VISIBLE);

		// set Infor for header
		ImageView badge = (ImageView) findViewById(R.id.badge);
		TextView title = (TextView) findViewById(R.id.title);
		TextView phone = (TextView) findViewById(R.id.phone);
		TextView add = (TextView) findViewById(R.id.add);

		title.setText(locations.get(pos).getmStoreName());
		str_phone = locations.get(pos).getmPhone();
		phone.setText(locations.get(pos).getmPhone());
		phone.setOnClickListener(this);
		add.setText(locations.get(pos).getmAddress());

		lazyloadimagesfromurl.ImageLoader imageLoader = new lazyloadimagesfromurl.ImageLoader(
				this);
		imageLoader.DisplayImage(locations.get(pos).getmLogo(), badge);

		LatLng latLng = new LatLng(locations.get(pos).getmGeoY(), locations
				.get(pos).getmGeoX());
		MarkerOptions markerOptions;

		markerOptions = new MarkerOptions().position(latLng);
		markerOptions.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
		googleMap.addMarker(markerOptions);

		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
		// initGuideBox();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initilizeMap();
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
		guidebox_layoutparams.addRule(RelativeLayout.ALIGN_TOP,
				R.id.phone_linear);
		guidebox_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// guidebox_layoutparams.setMargins(-1, Dp2Px(10), Dp2Px(10), -1);

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
				// TODO Auto-generated method stub

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
		guidebox.clearAnimation();
		guidebox.setVisibility(View.INVISIBLE);
	}

	int Dp2Px(int pxvalue) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				pxvalue, r.getDisplayMetrics());
		return (int) px;
	}

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			initImageLoader();
			markers = new Hashtable<String, String>();
			imageLoader = ImageLoader.getInstance();

			options = new DisplayImageOptions.Builder()
					// .showStubImage(R.drawable.ic_launcher)
					// Display Stub Image
					// .showImageForEmptyUri(R.drawable.ic_launcher)
					// If Empty image found
					.cacheInMemory().cacheOnDisc()
					.bitmapConfig(Bitmap.Config.RGB_565).build();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * to init actionbar
	 */
	private void initActionBar() {
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
		tv_label.setText(str_title);

		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));

		actionBar.setCustomView(iconHome);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);
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

	/**
	 * To setting values for Map
	 */
	private void settingMap() {
		// // Changing map type
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

		// Showing / hiding your current location
		googleMap.setMyLocationEnabled(true);

		// Enable / Disable zooming controls
		googleMap.getUiSettings().setZoomControlsEnabled(false);

		// Enable / Disable my location button
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);

		// Enable / Disable Compass icon
		googleMap.getUiSettings().setCompassEnabled(true);

		// Enable / Disable Rotate gesture
		googleMap.getUiSettings().setRotateGesturesEnabled(true);

		// Enable / Disable zooming functionality
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
	}

	/**
	 * To get the last known Location
	 * 
	 * @return Location
	 */
	private Location getLocation() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		LocationManager mlocManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		Location mLoc = null;
		String locationprovider = mlocManager.getBestProvider(criteria, true);
		try {
			/* If User's phone don't have GoogleMap then this line is error */
			mLoc = mlocManager.getLastKnownLocation(locationprovider);
		} catch (Exception e) {
			return null;
		}

		if (mLoc != null) {
			return mLoc;
		} else {
			return null;
		}
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	class HttpRequestTask extends AsyncTask<String, String, String> {
		private int type; // 1 get location; 2: get Brand list

		// Show a Alert Dialog

		private Dialog waitingDialog;

		public HttpRequestTask(int type) {
			this.type = type;

			waitingDialog = new Dialog(context);
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			waitingDialog.show();
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
			waitingDialog.dismiss();
			// Do anything with response..
			// Log.d("TAG", "result: "+ result);
			if (type == 1) {
				try {
					JSONArray jsonArray = new JSONArray(result);

					// init locations
					initLocation_Shop(jsonArray);
					str_title = locations.get(pos).getmStoreName();

					// initMarker();
					initMarker();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (type == 2) {
				try {
					JSONArray jsonArray = new JSONArray(result);

					// init Brand List
					initBrand_List(jsonArray);

					initActionBar();
					/*
					 * Actionbar Init after got Shop Name
					 */

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private void initMarker() {
		// TODO Auto-generated method stub
		Log.d("TAG", "initMarker");
		Log.d("TAG", "Location size: " + locations.size());
		MarkerOptions markerOptions;
		for (int i = 0; i < locations.size(); i++) {
			markerOptions = new MarkerOptions().position(new LatLng(locations
					.get(i).getmGeoY(), locations.get(i).getmGeoX()));
			markerOptions.title(locations.get(i).getmStoreName());
			markerOptions.snippet(locations.get(i).getmPhone());
			markerOptions.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
			Marker kiel = googleMap.addMarker(markerOptions);
			String add = "add" + kiel.getId();
			markers.put(add, locations.get(i).getmAddress());
			markers.put(kiel.getId(), locations.get(i).getmLogo());
		}

	}

	private void initBrand_List(JSONArray jsonArray) {
		brandLists = new ArrayList<BrandList>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject;
			try {
				jsonObject = jsonArray.getJSONObject(i);

				int ID = jsonObject.getInt("ID");
				String Name = jsonObject.getString("Name");
				String FBPage = jsonObject.getString("FBPage");
				String Logo = jsonObject.getString("Logo");
				brandLists.add(new BrandList(ID, Name, FBPage, Logo));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void initLocation_Shop(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		locations = new ArrayList<Location_Shop>();
		Log.d("TAG", "jsonArray size: " + jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject;
			try {
				jsonObject = jsonArray.getJSONObject(i);

				int mID = jsonObject.getInt("ID");
				int mBrandID = jsonObject.getInt("BrandID");
				int mCityID = jsonObject.getInt("CityID");
				int mValid = jsonObject.getInt("Valid");

				double mDistance = jsonObject.getDouble("Distance");
				double mGeoX = Double.parseDouble(jsonObject.getString("GeoX"));
				double mGeoY = Double.parseDouble(jsonObject.getString("GeoY"));

				String mBrandName = jsonObject.getString("BrandName");
				String mStoreName = jsonObject.getString("StoreName");
				String mAddress = jsonObject.getString("Address");
				String mPhone = jsonObject.getString("Phone");
				String mCityName = jsonObject.getString("CityName");
				String mLogo = jsonObject.getString("Logo");

				locations.add(new Location_Shop(mID, mBrandID, mCityID, mValid,
						mDistance, mGeoX, mGeoY, mBrandName, mStoreName,
						mAddress, mPhone, mCityName, mLogo));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Log.d("TAG", "sss: " + locations.size());
		//
		// locations.add(new Location_Shop(10.771907, 106.657736,
		// "Ä�H BÃ¡ch Khoa",
		// "Há»“ ChÃ­ Minh"));

	}

	private class CustomInfoWindowAdapter implements InfoWindowAdapter {

		private View view;

		public CustomInfoWindowAdapter() {
			view = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
		}

		@Override
		public View getInfoContents(Marker marker) {

			if (DetailItemStoreList.this.marker != null
					&& DetailItemStoreList.this.marker.isInfoWindowShown()) {
				DetailItemStoreList.this.marker.hideInfoWindow();
				DetailItemStoreList.this.marker.showInfoWindow();
			}
			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			DetailItemStoreList.this.marker = marker;

			String url = null;
			String add = null;

			if (marker.getId() != null && markers != null && markers.size() > 0) {
				if (markers.get(marker.getId()) != null
						&& markers.get(marker.getId()) != null) {
					url = markers.get(marker.getId());
					add = markers.get("add" + marker.getId());
				}
			}
			// Log.d("TAG", "add: "+ add);
			final ImageView image = ((ImageView) view.findViewById(R.id.badge));

			if (url != null && !url.equalsIgnoreCase("null")
					&& !url.equalsIgnoreCase("")) {
				imageLoader.displayImage(url, image, options,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								super.onLoadingComplete(imageUri, view,
										loadedImage);
								getInfoContents(marker);
							}
						});
			} else {
				image.setImageResource(R.drawable.logo);
			}

			final String title = marker.getTitle();
			final TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				titleUi.setText(title);
			} else {
				titleUi.setText("");
			}

			final String phone = marker.getSnippet();
			final TextView snippetUi = ((TextView) view
					.findViewById(R.id.phone));
			if (phone != null) {
				snippetUi.setText(phone);
			} else {
				snippetUi.setText("");
			}

			final TextView address = ((TextView) view.findViewById(R.id.add));

			if (add != null) {
				address.setText(add);
			} else {
				address.setText("");
			}

			return view;
		}
	}

	private void initImageLoader() {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024;
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.threadPoolSize(5)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(memoryCacheSize)
				.memoryCache(
						new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		//
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.phone:
			if (prosoft.android.utility.Utility.canMakeCall(context))
				showDialogCall();
			break;

		default:
			break;
		}
	}

	private void showDialogCall() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(getResources().getString(
				R.string.CallConfirmationDesc).replace("%@", str_phone));
		builder.setTitle(getResources().getString(R.string.CallConfirmation));
		builder.setPositiveButton(res.getString(R.string.CallConfirmationYes),
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
				res.getString(R.string.CallConfirmationCancel),
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

}
