package adapter;

import java.util.ArrayList;

import lazyloadimagesfromurl.ImageLoader;

import object.Stores;


import com.android.activity.CouponActivity;
import com.android.activity.ListNearByActivity;
import com.ctyprosoft.tmg.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class AdapterNearBy extends BaseAdapter {

	Context mContext;
	ArrayList<Stores> array ;
	public ImageLoader imageLoader; 
	//ImageDownloaderMainBanners loader;
	public AdapterNearBy(Context mContext, ArrayList<Stores> array) {
		this.mContext = mContext;
		this.array = array;
		imageLoader=new ImageLoader(mContext);
		//loader = new ImageDownloaderMainBanners();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		public TextView distance;
		public TextView tvname;
		public TextView addr;
		public ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
//		if(convertView == null)
//		{
			convertView = View.inflate(mContext, R.layout.list_item_nearby, null);
			holder.distance = (TextView) convertView.findViewById(R.id.tv_distance);
			holder.tvname = (TextView) convertView.findViewById(R.id.tv_store_name);
			holder.addr = (TextView) convertView.findViewById(R.id.tv_addr);
			holder.img = (ImageView) convertView.findViewById(R.id.img_store);
			convertView.setTag(holder);

//		}
//		else
//			holder = (ViewHolder) convertView.getTag();
		
		
		double d = array.get(position).getDistance();
		double newKB = Math.round(d*1000.0)/10.0;
		
		holder.tvname.setText(array.get(position).getName());
		holder.distance.setText("" + newKB+" Km");
		holder.addr.setText(array.get(position).getAddr());
		//holder.img.setImageResource(array.get(position).getImage_link());
		imageLoader.DisplayImage(array.get(position).getLogo(), holder.img);
		
//		loader.download(array.get(position).getImg_path(), holder.img);
		if(position == ListNearByActivity.positionClick)
			convertView.setBackgroundColor(Color.GRAY);
		return convertView;
	}

}
