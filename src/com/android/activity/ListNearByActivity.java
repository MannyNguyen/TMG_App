package com.android.activity;

import java.util.ArrayList;

import object.Location_Shop;
import object.Stores;
import prosoft.android.utility.Utility;
import adapter.AdapterNearBy;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctyprosoft.tmg.DetailItemStoreList;
import com.ctyprosoft.tmg.R;
import com.ctyprosoft.tmg.SearchNearBy;

import eu.erikw.PullToRefreshListView;

public class ListNearByActivity extends Activity {
	Context context = this;
	private static int currentPage = 1;
	private static int positionGridView = 1;
	int firstVisiblePosition;
	int visibleItem;
	int totalItem;
	private static int totalPage = 0;
	ArrayList<Stores> array;
	public static Resources res;
	private adapter.ListAsGridExampleAdapter mAdapter;
	AdapterNearBy adapter;
	PullToRefreshListView lv;
	public static int positionClick = -1;
	private ArrayList<Location_Shop> shops;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_nearby);

		// configure action bar
		initActionBar();

		shops = new ArrayList<Location_Shop>();
		shops = SearchNearBy.locations;
		if (shops.size() > 0)
			addDataForNewsPage();
		else
			Utility.NoInternet(context, null);
		innitAdapterLv();
	}

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
//		TextView tv_label = (TextView) iconHome.findViewById(R.id.tv_label);
//		tv_label.setText(getResources().getString(R.string.storelist));

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

	private void innitAdapterLv() {
		lv = (PullToRefreshListView) findViewById(R.id.listView);
		adapter = new AdapterNearBy(context, array);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				Log.d("TAG", "pos: " + pos);
				for (int a = 0; a < lv.getChildCount(); a++) {
					lv.getChildAt(a).setBackgroundColor(Color.WHITE);
				}

				v.setBackgroundColor(Color.GRAY);
				positionClick = pos;
				Intent i = new Intent(ListNearByActivity.this,
						DetailItemStoreList.class);
				i.putExtra("pos", pos + "");
				startActivity(i);

			}

		});

	}

	private void addDataForNewsPage() {
		// int[] images = { R.drawable.android_ainosofia,
		// R.drawable.android_johnhenry, R.drawable.farah_logo,
		// R.drawable.android_ainosofia, R.drawable.android_johnhenry,
		// R.drawable.farah_logo, R.drawable.android_ainosofia,
		// R.drawable.android_johnhenry, R.drawable.farah_logo };
		//
		// String[] storename = { "QuanFashion", "QuanSipVang", "Quankun",
		// "QuanMaster", "QuanFashion", "QuanSipVang", "Quankun",
		// "QuanMaster" };
		// String[] addr = { "CongHoa", "HoangHoaTham", "Quankun", "QuanMaster",
		// "QuanFashion", "QuanSipVang", "Quankun", "QuanMaster" };
		// String[] distance = { "0.5km", "0.9km", "1.3km", "2km", "5km",
		// "30km",
		// "45km", "53km" };
		// for (int i = 0; i < 8; i++) {
		//
		// Log.d("TAG", "j:" + i);
		// array.add(new Stores(storename[i], addr[i], distance[i], images[i]));
		// }

		array = new ArrayList<Stores>();
		for (int i = 0; i < shops.size(); i++) {
			array.add(new Stores(shops.get(i).getmStoreName(), shops.get(i)
					.getmAddress(), shops.get(i).getmDistance(), shops.get(i)
					.getmLogo()));
		}

	}
}
