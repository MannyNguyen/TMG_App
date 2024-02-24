package com.android.activity;

import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;

import com.ctyprosoft.tmg.R;

import android.Vietnalyze;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PurchaseDetailActivity extends Vinalyze{
	Context context = this;
	Resources res;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Vietnalyze.logEvent("hisPurchage");
		initVariable();
		initActionBar();
		
		
		setContentView(R.layout.activity_purchage_detail);
		customLayout();
	}
	private void initActionBar()
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
		tv_label.setText(res.getString(R.string.purchase));
		
		actionBar.setLogo(new ColorDrawable(Color.TRANSPARENT));
		
		actionBar.setCustomView(iconHome);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
	}
	private void initVariable()
	
	{
		res = getResources();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
	}
	private void customLayout()
	{
		Bundle it = getIntent().getExtras();
		String pos = it.getString(Utility.IdPurchaseDetail);
		TextView tv_storename = (TextView)	findViewById(R.id.tv_storename);
		TextView tv_no = (TextView)	findViewById(R.id.tv_receiptno);
		TextView tv_ucp = (TextView)	findViewById(R.id.tv_ucp);
		TextView tv_des = (TextView)	findViewById(R.id.tv_des);
		TextView tv_attr = (TextView)	findViewById(R.id.tv_attr);
		TextView tv_size = (TextView)	findViewById(R.id.tv_size);
		TextView tv_quan = (TextView)	findViewById(R.id.tv_quantity);
		TextView tv_price = (TextView)	findViewById(R.id.tv_price);
		TextView tv_day = (TextView)	findViewById(R.id.tv_day);
		
		tv_storename.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getStoreName());
		tv_no.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getReceiptNo());
		tv_ucp.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getUPC());
		tv_des.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getDesc());
		tv_attr.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getAttr());
		tv_size.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getSize());
		tv_quan.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getQty());
		tv_price.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getPrice());
		tv_day.setText(PurchaseActivity.array.get(Integer.parseInt(pos)).getDay());
		
	}

}
