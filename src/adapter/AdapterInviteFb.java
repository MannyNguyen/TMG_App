package adapter;

import java.util.ArrayList;

import object.Coupon;
import object.InviteFB;
import prosoft.android.utility.ImageDownloaderMainBanners;

import com.android.activity.CouponActivity;
import com.android.activity.InviteFBActivity;
import com.ctyprosoft.tmg.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterInviteFb extends BaseAdapter {

	Context mContext;
	ArrayList<InviteFB> array ;
	ImageDownloaderMainBanners loader;
	View view;
	public AdapterInviteFb(Context mContext, ArrayList<InviteFB> array) {
		this.mContext = mContext;
		this.array = array;
		this.view = view;
		loader = new ImageDownloaderMainBanners();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
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
		public TextView name;
		public ImageView img_invite;
		public ImageView img_avarta;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		System.out.println("pos="+position);
		if(convertView == null)
		{
			convertView = View.inflate(mContext, R.layout.item_fb_list, null);
			
			holder.name = (TextView) convertView.findViewById(R.id.tv_fb_name);
			holder.img_invite = (ImageView) convertView.findViewById(R.id.img_invite);
			holder.img_avarta = (ImageView) convertView.findViewById(R.id.img_avarta_fb);
			convertView.setTag(holder);

		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.name.setText(array.get(position).getFb_name());
//		loader.download(array.get(position)., holder.img_invite);
		holder.img_invite.setBackgroundDrawable(array.get(position).isCheck() 
				?mContext.getResources().getDrawable(R.drawable.icontick)
				:mContext.getResources().getDrawable(R.drawable.iconadd));
		loader.download(array.get(position).getImg_avarta(), holder.img_avarta);
		
		return convertView;
	}

}
