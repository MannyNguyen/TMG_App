package adapter;


import java.util.ArrayList;

import object.News;
import prosoft.android.utility.ImageDownloaderMainBanners;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.activity.MainActivity;
import com.ctyprosoft.tmg.R;
import com.plattysoft.ui.ListAsGridBaseAdapter;



/**
 * Created by shalafi on 6/6/13.
 */
public class ListAsGridExampleAdapter extends ListAsGridBaseAdapter {
	private Context context;
	private ArrayList<News> news;
	private int textViewResourceId;
	ImageDownloaderMainBanners downloader;
	public ListAsGridExampleAdapter(Context context, int textViewResourceId,
			ArrayList<News> newss) {
		super(context);
		this.context = context;
		this.news = newss;
		this.textViewResourceId = textViewResourceId;
		downloader = new ImageDownloaderMainBanners();

	}

	@Override
	public News getItem(int position) {
		return news.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return news.size() - 1;
	}

	@Override
	protected View getItemView(int pos, View convertView, ViewGroup parent) {
		Log.d("TAG", "getView");
		pos = pos + 1;
		Log.d("TAG", "GridViewNewsPage getView: " + pos);

		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(this.textViewResourceId, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.news_image);
			holder.image.getLayoutParams().height = MainActivity.width*2/5;
			holder.tvNews_title = (TextView) convertView
					.findViewById(R.id.news_title);
			holder.tvNews_subtitle = (TextView) convertView
					.findViewById(R.id.news_subtitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
//			holder.image.setImageBitmap(decodeSampledBitmapFromResource(
//					JonHenryFragment.res, news.get(pos).getNews_image_url(), 100,
//					100));

			// holder.image.setImageResource(news.get(pos).getNews_image_url());
			downloader.download(news.get(pos).getNews_image_url(), holder.image);
			holder.tvNews_title.setText(news.get(pos).getNews_title());
			holder.tvNews_subtitle.setText(news.get(pos).getNews_subtitle());
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		return convertView;
	}
	class ViewHolder {
		ImageView image;
		TextView tvNews_title;
		TextView tvNews_subtitle;
	}
	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
