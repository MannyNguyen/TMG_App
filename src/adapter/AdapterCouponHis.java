package adapter;

import java.util.ArrayList;

import object.Coupon;
import prosoft.android.utility.ImageDownloaderMainBanners;

import com.ctyprosoft.tmg.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import fragment.CouponHisAvaiFragment;

public class AdapterCouponHis extends BaseAdapter {

	Context mContext;
	ArrayList<Coupon> array ;
	ImageDownloaderMainBanners loader;
	public AdapterCouponHis(Context mContext, ArrayList<Coupon> array) {
		this.mContext = mContext;
		this.array = array;
		loader = new ImageDownloaderMainBanners();
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
		return Integer.parseInt(array.get(position).getId());
	}

	public class ViewHolder {
		public TextView tvExpi;
		public TextView tvname;
		public TextView tvvalue;
		public ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
//		if(convertView == null)
//		{
			convertView = View.inflate(mContext, R.layout.item_coupon_list, null);
			holder.tvExpi = (TextView) convertView.findViewById(R.id.cpExpireValue);
			holder.tvname = (TextView) convertView.findViewById(R.id.cp_type);
			holder.tvvalue = (TextView) convertView.findViewById(R.id.cpvalue);
			holder.img = (ImageView) convertView.findViewById(R.id.img_cp);
			convertView.setTag(holder);

//		}
//		else
//			holder = (ViewHolder) convertView.getTag();
		
		String[] Expiration = array.get(position).getCp_expired().split(" ");
		holder.tvExpi.setText(Expiration[0]);
		holder.tvname.setText(array.get(position).getCp_type());
		holder.tvvalue.setText(array.get(position).getCp_value());
		loader.download(array.get(position).getImg_path(), holder.img);
		
		return convertView;
	}

}
