package adapter;

import java.util.ArrayList;

import object.Notification;
import prosoft.android.utility.ImageDownloaderMainBanners;

import com.android.activity.NotificationActivity;
import com.ctyprosoft.tmg.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterNotification extends BaseAdapter {

	Context mContext;
	ArrayList<Notification> array ;
	ImageDownloaderMainBanners loader;
	public AdapterNotification(Context mContext, ArrayList<Notification> array) {
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
		return position;
	}

	public class ViewHolder {
		public TextView Email;
		public TextView message;
		public TextView createday;
		public TextView click;
		public ImageView icon_email;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
//		if(convertView == null)
//		{
			convertView = View.inflate(mContext, R.layout.item_notification_list, null);
			holder.message = (TextView) convertView.findViewById(R.id.tv_sms);
			holder.createday = (TextView) convertView.findViewById(R.id.tv_day_noti);
			holder.icon_email = (ImageView) convertView.findViewById(R.id.img_cp);
			convertView.setTag(holder);

//		}
//		else
//			holder = (ViewHolder) convertView.getTag();
		
		try {
			holder.message.setText(array.get(position).getMessage());
			holder.createday.setText(array.get(position).getCreateDate());
		} catch (Exception e) {
			// TODO: handle exception
		}
		String test = array.get(position).getClicked();
		System.out.println("click="+ test);
		if(!array.get(position).getClicked().equals(""))
		{
//			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
			holder.icon_email.setBackgroundResource(R.drawable.mail_open);
		}
		
		return convertView;
	}

}
