package com.ctyprosoft.tmg;

import com.ctyprosoft.tmg.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GoogleMapBrandList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map_brand_list);
		
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		
		//Intent intent = getIntent();
		//int id = intent.getExtras().getInt("id");
		
		TextView v = (TextView) findViewById(R.id.textView1);
		 
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(GoogleMapBrandList.this,SearchNearBy.class);
				i.putExtra("pos", "2");
				startActivity(i);
			}
		});
		
		//v.setText("Name: -"+ "- currentBrandLists: "+ GoogleMapActivity.currentLocations.get(1).getmName());
		
		
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

}
