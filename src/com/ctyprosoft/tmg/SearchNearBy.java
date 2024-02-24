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
import android.Vietnalyze;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.activity.ListNearByActivity;
import com.android.activity.MainActivity;
import com.android.activity.Vinalyze;
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

public class SearchNearBy extends Vinalyze implements OnNavigationListener,
		OnClickListener {

	// Google Map
	private GoogleMap googleMap;
	public static ArrayList<Location_Shop> locations;
	double lat = 10.771890, lng = 106.667448;
	private Marker marker;
	private Hashtable<String, String> markers;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ArrayList<BrandList> brandLists;
	// public static ArrayList<Location_Shop> currentLocations;
	Context context = this;
	private boolean isFirst = true;
	ImageView guidebox;
	SharePreferance share;
	int i = 0;
	Button btn_retry;
	RelativeLayout rela_cover;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vietnalyze.logEvent("searchNearBy");
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setContentView(R.layout.activity_google_map);

		// Search Event
		// Getting reference to btn_find of the layout activity_main
		// Button btn_find = (Button) findViewById(R.id.btn_find);
		rela_cover = (RelativeLayout) findViewById(R.id.rela_retry);
		Button btn_retry = (Button) findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(this);

		if (prosoft.android.utility.Utility.isInternet(this)) {
			new HttpRequestTask(2)
					.execute("http://tmg.ctyprosoft.com:81/API/Brand/GetBrandList.aspx");

			// Loading map
			initilizeMap();

			// Setting map
			settingMap();

			Intent i = getIntent();
			try {
				int pos = Integer.parseInt(i.getStringExtra("pos"));
				View v = findViewById(R.id.llHeaderInfor);
				v.setVisibility(View.VISIBLE);

				// set Infor for header
				ImageView badge = (ImageView) findViewById(R.id.badge);
				TextView title = (TextView) findViewById(R.id.title);
				TextView phone = (TextView) findViewById(R.id.phone);
				TextView add = (TextView) findViewById(R.id.add);

				title.setText(locations.get(pos).getmStoreName());
				phone.setText(locations.get(pos).getmPhone());
				add.setText(locations.get(pos).getmAddress());

				lazyloadimagesfromurl.ImageLoader imageLoader = new lazyloadimagesfromurl.ImageLoader(
						this);
				imageLoader.DisplayImage(locations.get(pos).getmLogo(), badge);

				LatLng latLng = new LatLng(locations.get(pos).getmGeoY(),
						locations.get(pos).getmGeoX());
				MarkerOptions markerOptions;

				markerOptions = new MarkerOptions().position(latLng);
				markerOptions.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
				googleMap.addMarker(markerOptions);

				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						latLng, 13));

			} catch (Exception e) {
				Location lc = getLocation();
				LatLng latLng = null;
				if (lc != null)
					latLng = new LatLng(lat = lc.getLatitude(),
							lng = lc.getLongitude());
				else
					latLng = new LatLng(lat, lng);

				googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						latLng, 13));

				Log.d("TAG", " ok ");

				// HttpRequest
				if (prosoft.android.utility.Utility.isInternet(this))
					new HttpRequestTask(1)
							.execute("http://tmg.ctyprosoft.com:81/api/Stores/SearchNearbyStore.aspx?x="
									+ lng + "&y=" + lat);
			}
		} else
			prosoft.android.utility.Utility.NoInternet(context, "");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// get BrandList

	}

	private void initGuideBox() {

		guidebox = (ImageView) findViewById(R.id.guide_box);
		if (share.getValueLanguage().equals("en")) {
			guidebox.setImageResource(R.drawable.choosebrand_en);
		} else {
			guidebox.setImageResource(R.drawable.choosebrand_vi);
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
		// guidebox_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
		// R.id.btn_mix);
		guidebox_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		guidebox_layoutparams.setMargins(-1, Dp2Px(10), Dp2Px(width / 10), -1);

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
		try {
			guidebox.clearAnimation();
			guidebox.setVisibility(View.INVISIBLE);
		} catch (Exception e) {
			// TODO: handle exception
		}

		share.setStatusfirstBrand(true);
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
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// add icon Back
		View iconHome = View.inflate(context, R.layout.custom_actionbar_nearby,
				null);
		ImageView img = (ImageView) iconHome.findViewById(R.id.image2);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//		TextView tv_label = (TextView) iconHome.findViewById(R.id.tv_label);
//		tv_label.setText(getResources().getString(R.string.nearbySearch));
		Spinner spinner = (Spinner) iconHome.findViewById(R.id.spinner);
		ImageView btnList = (ImageView) iconHome.findViewById(R.id.img_list);
		// add icon Back
		int n = brandLists.size() + 1;
		final String[] entries = new String[n];
		entries[0] = "-- " + getResources().getString(R.string.all) + " --";
		for (int i = 0; i < brandLists.size(); i++) {
			entries[i + 1] = brandLists.get(i).getName();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, entries);

		// adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		// create ICS spinner
		// IcsSpinner spinner = new IcsSpinner(this, null,
		// R.attr.actionDropDownStyle);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				i = i + 1;
				if (i == 2)
					stopGuidebox();
				if (pos != 0) {
					googleMap.clear();
					new HttpRequestBrandId()
							.execute("http://tmg.ctyprosoft.com:81/api/Stores/SearchNearbyStore.aspx?BrandID="
									+ brandLists.get(pos - 1).getID()
									+ "&x="
									+ lng + "&y=" + lat);
				}
				if (pos == 0 && !isFirst) {
					googleMap.clear();
					new HttpRequestBrandId()
							.execute("http://tmg.ctyprosoft.com:81/api/Stores/SearchNearbyStore.aspx?&x="
									+ lng + "&y=" + lat);
				}
				isFirst = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// List Image

		btnList.setImageResource(R.drawable.whitebackline64);
		btnList.setBackgroundResource(R.drawable.img_bg);
		btnList.setClickable(true);
		btnList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (locations.size() == 0) {
					prosoft.android.utility.Utility.showDialog(context,
							getResources().getString(R.string.noShop));
				} else {
					Intent i = new Intent(SearchNearBy.this,
							ListNearByActivity.class);
					startActivity(i);
				}

			}
		});

		// configure custom view
		LinearLayout listNavLayout = new LinearLayout(this);

		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.gravity = Gravity.LEFT;

		listNavLayout.addView(iconHome, params1);
		ab.setCustomView(listNavLayout);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
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

		private Dialog progressDialog;

		public HttpRequestTask(int type) {
			this.type = type;

			progressDialog = new Dialog(SearchNearBy.this);
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
				progressDialog.dismiss();
				// Do anything with response..
				// Log.d("TAG", "result: "+ result);
				if (type == 1) {
					try {
						JSONArray jsonArray = new JSONArray(result);

						// init locations
						initLocation_Shop(jsonArray);

						// initMarker();
						initMarker();

						// init ActionBar
						initActionBar();
						share = new SharePreferance(context);
						if (!share.getStatusfirstBrand()) {
							initGuideBox();

						}

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

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

			if (SearchNearBy.this.marker != null
					&& SearchNearBy.this.marker.isInfoWindowShown()) {
				SearchNearBy.this.marker.hideInfoWindow();
				SearchNearBy.this.marker.showInfoWindow();
			}
			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			SearchNearBy.this.marker = marker;

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

		ImageLoader.getInstance().init(config);
	}

	/**
	 * To get Location by Brand Id
	 * 
	 * @author My PC
	 * 
	 */
	private class HttpRequestBrandId extends AsyncTask<String, String, String> {
		private Dialog progressDialog;

		public HttpRequestBrandId() {
			progressDialog = new Dialog(SearchNearBy.this);
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

			progressDialog.dismiss();
			try {
				JSONArray jsonArray = new JSONArray(result);

				// init locations
				initLocation_Shop(jsonArray);

				// initMarker();
				initMarker();

				if (locations.size() > 0) {
					LatLng latLng = new LatLng(locations.get(0).getmGeoY(),
							locations.get(0).getmGeoX());

					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							latLng, 13));

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

}
