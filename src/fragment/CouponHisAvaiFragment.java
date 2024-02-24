package fragment;

import java.util.ArrayList;

import object.Coupon;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.SharePreferance;
import prosoft.android.utility.Utility;
import adapter.AdapterCouponHis;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.activity.CouponDetailActivity;
import com.android.activity.MainActivity;
import com.android.activity.NewsDetailActivity;
import com.ctyprosoft.tmg.R;
import com.google.android.gms.internal.ar;

import fragment.JonHenryFragment.BG_getNews;

public class CouponHisAvaiFragment extends Fragment implements
		OnItemClickListener, OnScrollListener, OnClickListener {
	AdapterCouponHis adapter;
	ListView lv;
	ArrayList<Coupon> array;
	public Dialog waitingDialog;
	SharePreferance share;
	String email;
	View rootView;

	private int currentPage = 1;
	private int positionGridView = 0;
	int firstVisiblePosition;
	int visibleItem;
	int totalItem;
	private int totalPage = 0;
	int flag_refreshPull = 0;

	RelativeLayout retry;
	Button btn_retry;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);
		rootView = inflater.inflate(R.layout.activity_coupon, container, false);
		share = new SharePreferance(getActivity());
		 email = share.getEmail();
		initButtonRetry();
		return rootView;
	}
	private void initButtonRetry()
	{
		retry = (RelativeLayout) rootView.findViewById(R.id.rela_retry);
		retry.setOnClickListener(this);
		rootView.findViewById(R.id.btn_retry).setOnClickListener(this);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			if (!Utility.isInternet(getActivity()) && array == null)
				Utility.NoInternet(getActivity(), "");
			else
			{
				retry.setVisibility(View.INVISIBLE);
				new backgroundGetCpList().execute();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	class backgroundGetCpList extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			waitingDialog = new Dialog(getActivity());
			waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			waitingDialog.setContentView(R.layout.waitingdialog);
			waitingDialog.setCancelable(false);
			if (!waitingDialog.isShowing()  && array == null)
			{
				waitingDialog.show();
				array = new ArrayList<Coupon>();
				array.removeAll(array);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				if(array.size() == 0)
				{
					array = GetJsonFromAPI.getCpList(API.getCPListString(email,
							String.valueOf(currentPage)));
					totalPage = GetJsonFromAPI.getTotalPage(API.getCPListString(
							email, String.valueOf(currentPage)));
				}
				
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
				retry.setVisibility(View.VISIBLE);
			} else {
				try {
				retry.setVisibility(View.INVISIBLE);
				waitingDialog.cancel();
				lv = (ListView) rootView.findViewById(R.id.lv_coupon);
				adapter = new AdapterCouponHis(getActivity(), array);
				lv.setAdapter(adapter);
				lv.setOnScrollListener(CouponHisAvaiFragment.this);
				lv.setSelection(positionGridView);
				lv.setOnItemClickListener(CouponHisAvaiFragment.this);
				waitingDialog.cancel();
				}
				catch (Exception e) {
					// TODO: handle exception
					waitingDialog.cancel();
					onResume();
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		for (int a = 0; a < lv.getChildCount(); a++) {
			lv.getChildAt(a).setBackgroundColor(Color.WHITE);
		}

		arg1.setBackgroundColor(Color.GRAY);
		Intent it = new Intent(getActivity(), CouponDetailActivity.class);
		it.putExtra(Utility.IdCouponDetail,
				String.valueOf(lv.getItemIdAtPosition(arg2)));
		startActivity(it);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_retry:
			array = null;
			onResume();
			break;
		default:
			break;
		}
	}
}
