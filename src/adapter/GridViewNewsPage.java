package adapter;

import java.util.ArrayList;

import object.News;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctyprosoft.tmg.R;


public class GridViewNewsPage extends ArrayAdapter<News> {

	public GridViewNewsPage(Context context, int textViewResourceId,
			ArrayList<News> news) {
		super(context, textViewResourceId, news);
		this.context = context;
		this.news = news;
		this.textViewResourceId = textViewResourceId;
	}

	private Context context;
	private ArrayList<News> news;
	private int textViewResourceId;

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		Log.d("TAG", "getView");

		Log.d("TAG", "GridViewNewsPage getView: " + pos);

		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(this.textViewResourceId, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.news_image);
			holder.tvNews_title = (TextView) convertView
					.findViewById(R.id.news_title);
			holder.tvNews_content = (TextView) convertView
					.findViewById(R.id.news_subtitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvNews_title.setText(news.get(pos).getNews_title());
		holder.tvNews_content.setText(news.get(pos).getNews_contents());

		return convertView;
	}

	class ViewHolder {
		ImageView image;
		TextView tvNews_title;
		TextView tvNews_content;
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
