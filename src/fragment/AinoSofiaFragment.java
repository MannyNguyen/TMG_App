package fragment;

import java.util.ArrayList;

import object.News;
import prosoft.android.utility.API;
import prosoft.android.utility.GetJsonFromAPI;
import prosoft.android.utility.ImageDownloaderMainBanners;
import prosoft.android.utility.Utility;
import adapter.ListAsGridExampleAdapter;
import android.Vietnalyze;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.activity.MainActivity;
import com.android.activity.NewsDetailActivity;
import com.ctyprosoft.tmg.R;
import com.plattysoft.ui.GridItemClickListener;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class AinoSofiaFragment extends Fragment implements OnScrollListener,
		GridItemClickListener, OnRefreshListener, OnClickListener {

	// parse pages.
	private int currentPage = 1;
	private int positionGridView = 0;
	int firstVisiblePosition;
	int visibleItem;
	int totalItem;
	private static int totalPage = 0;
	// Variable create List
	public static ArrayList<News> news;
	private adapter.ListAsGridExampleAdapter mAdapter;
	PullToRefreshListView gridView;
	// Variable View
	static View rootView;
	private Dialog waitingDialog;
	View header;
	TextView tvTitle, tvSubTitle;
	RelativeLayout retry;
	ImageView headerImage;
	Button btn_retry;
	
	ImageDownloaderMainBanners downloader;
	int flag_refreshPull = 0;
	public static Resources res;
	Context context = getActivity();

	String AinoSofia = "2";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_top_rated, container,
				false);
		initVariable();
		return rootView;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			//If no Internet & array not fill.If array has data.Display cache
			if (!Utility.isInternet(getActivity()) && news == null)
				Utility.NoInternet(getActivity(), "");
			else
				new BG_getNews().execute();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void initVariable() {
		res = getResources();
		downloader = new ImageDownloaderMainBanners();
		
		initButtonRetry();
		
		initWaitingDialog();
		
		initGridView();

		initHeaderGridView();

	}

	class BG_getNews extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (!waitingDialog.isShowing() && flag_refreshPull == 0
					&& news == null) {
				waitingDialog.show();
				news = new ArrayList<News>();
				news.removeAll(news);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				if (news.size() == 0) {
					totalPage = GetJsonFromAPI.getTotalPage(API
							.getNewListBandById(String.valueOf(currentPage),
									AinoSofia));
					news.addAll(GetJsonFromAPI.getNewList(API
							.getNewListBandById(String.valueOf(currentPage),
									AinoSofia)));
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
					waitingDialog.cancel();
					retry.setVisibility(View.INVISIBLE);
					
					gridView.removeHeaderView(header);
					
					mAdapter = new ListAsGridExampleAdapter(getActivity()
							.getApplicationContext(),
							R.layout.grid_view_news_page_item, news);
					mAdapter.setNumColumns(2);
					mAdapter.setOnGridClickListener(AinoSofiaFragment.this);
					gridView.setAdapter(mAdapter);
//					gridView.setSelection(currentPage);
					gridView.setSelection(positionGridView);
					initHeaderGridView();
					tvTitle.setText(news.get(0).getNews_title());
					tvSubTitle.setText(news.get(0).getNews_subtitle());
					downloader.download(news.get(0).getNews_image_url(),
							headerImage);
					
					gridView.addHeaderView(header);
				} catch (Exception e) {
					// TODO: handle exception
					waitingDialog.cancel();
					onResume();
				}
			}
			if (flag_refreshPull == 1) {
				gridView.onRefreshComplete();
				flag_refreshPull = 0;
			}
			waitingDialog.cancel();
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
			new BG_getNews().execute();
		}
	}

	@Override
	public void onGridItemClicked(View v, int position, long itemId) {
		try {
			Vietnalyze.logEvent("newDetailClick ");
			Intent it = new Intent(getActivity(), NewsDetailActivity.class);
			it.putExtra("title", news.get(position + 1).getNews_title());
			it.putExtra("subtitle", news.get(position + 1).getNews_subtitle());
			it.putExtra("content", news.get(position + 1).getNews_contents());
			it.putExtra("img", news.get(position + 1).getNews_image_url());
			startActivity(it);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		flag_refreshPull = 1;
		news = new ArrayList<News>();
		news.removeAll(news);
		positionGridView = 0;
		new BG_getNews().execute();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image2:
			try {
				Vietnalyze.logEvent("newDetailClick ");
				Intent it = new Intent(getActivity(), NewsDetailActivity.class);
				it.putExtra("title", news.get(0).getNews_title());
				it.putExtra("subtitle", news.get(0).getNews_subtitle());
				it.putExtra("content", news.get(0).getNews_contents());
				it.putExtra("img", news.get(0).getNews_image_url());
				startActivity(it);
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		case R.id.btn_retry:
			news = null;
			onResume();
			break;
		default:
			break;
		}
	}

	private void toast(String sms) {
		Toast.makeText(getActivity(), sms, Toast.LENGTH_SHORT).show();
	}
	private void initWaitingDialog()
	{
		waitingDialog = new Dialog(getActivity());
		waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		waitingDialog.setContentView(R.layout.waitingdialog);
		waitingDialog.setCancelable(false);
	}
	private void initHeaderGridView()
	{
		header = View.inflate(getActivity(), R.layout.item_new_header, null);
		ImageView headerButton = (ImageView) header.findViewById(R.id.image2);
		headerImage = (ImageView) header.findViewById(R.id.image);
		headerImage.getLayoutParams().height = MainActivity.height * 3 / 10;
		headerButton.getLayoutParams().height = MainActivity.height * 3 / 10;
		headerButton.setOnClickListener(AinoSofiaFragment.this);
		tvTitle = (TextView) header.findViewById(R.id.news_head_title);
		tvSubTitle = (TextView) header.findViewById(R.id.news_head_subtitle);
	}
	private void initGridView()
	{
		gridView = (PullToRefreshListView) rootView.findViewById(R.id.listView);
		gridView.setOnRefreshListener(AinoSofiaFragment.this);
		gridView.setOnScrollListener(AinoSofiaFragment.this);
	}
	private void initButtonRetry()
	{
		retry = (RelativeLayout) rootView.findViewById(R.id.rela_retry);
		retry.setOnClickListener(this);
		rootView.findViewById(R.id.btn_retry).setOnClickListener(this);
	}
}
