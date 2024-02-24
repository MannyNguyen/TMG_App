package adapter;

import java.util.ArrayList;

import lazyloadimagesfromurl.ImageLoader;

import object.RowItem;
import prosoft.android.utility.BadgeView;
import prosoft.android.utility.BadgeViewNoNumber;
import prosoft.android.utility.ImageDownloaderMainBanners;
import prosoft.android.utility.SharePreferance;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.activity.MainActivity;
import com.ctyprosoft.tmg.R;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

	Context context;
	BadgeView badge;
	BadgeViewNoNumber badgeNoNumber;
	ArrayList<RowItem> items;
	ImageDownloaderMainBanners downloader;
	SharePreferance share;
	int indexHightLightItem;
	public CustomListViewAdapter(Context context, int resourceId,
			ArrayList<RowItem> items,int index) {
		super(context, resourceId, items);
		this.context = context;
		this.items = items;
		this.indexHightLightItem = index;
		downloader = new ImageDownloaderMainBanners();
		share = new SharePreferance(context);
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		if (!items.get(position).getIsHeader())
			return true;
		else
			return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = null;
		RowItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (position == 0)
		{
			convertView = mInflater.inflate(R.layout.list_item_fist, null);
			TextView username = (TextView)convertView.findViewById(R.id.username);
			username.setText(share.getUserName());
			TextView cardNO = (TextView)convertView.findViewById(R.id.card_number);
			cardNO.setText(context.getResources().getString(R.string.cardNo)+ 
					share.getCardNo());
			String link = share.getimgAvarta();
			try {
				prosoft.android.utility.RoundedImageView icon_avarta = 
						(prosoft.android.utility.RoundedImageView)convertView.findViewById(R.id.icon_avarta);
				
//				String link = "http://profile.ak.fbcdn.net/hprofile-ak-frc3/t1.0-1/c170.50.621.621/s200x200/970322_662073603822035_1054363487_n.jpg";
				int a  = link.length();
				if(!share.getimgAvarta().equals("") && a > 5)
					downloader.download(link, icon_avarta);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		else {
			convertView = mInflater.inflate(R.layout.list_item, null);
			TextView content = (TextView) convertView
					.findViewById(R.id.content);
			// txtHot = (TextView) convertView.findViewById(R.id.tv_hot);
			imageView = (ImageView) convertView.findViewById(R.id.icon);

			content.setText(rowItem.getContent());

			if (rowItem.getImageId() == 1) { // Mobile Ad list
				try {
					ImageLoader imageLoader = new ImageLoader(context);
					imageLoader.DisplayImage(
							MainActivity.mobileADs.get(position - 6)
									.getThumbImg(), imageView);

				} catch (Exception e) {
					imageView.setImageResource(R.drawable.monila_campaign);
				}
			} else
				imageView.setImageResource(rowItem.getImageId());

			if (items.get(position).getIsHeader()) { // Set for Header Item
				if (position != 1)
					convertView.findViewById(R.id.divider_item).setVisibility(
							View.VISIBLE);
				convertView.findViewById(R.id.icon).setVisibility(View.GONE);
				content.setTypeface(null, Typeface.NORMAL);
				int color = Color.parseColor("#7C7C7C");
				content.setTextColor(color);
			}
		}

		// if(position == 8)
		// holder.txtHot.setVisibility(View.VISIBLE);
		if(position == MainActivity.positionClick)
			convertView.setBackgroundColor(Color.GRAY);
		if (position == 4) {
			badgeNoNumber = new BadgeViewNoNumber(context, imageView);
			badgeNoNumber.setText("✓");
			badgeNoNumber.setBadgePosition(0);
			badgeNoNumber.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));//Red color
			Animation fadeIn = new AlphaAnimation(0, 1);
			fadeIn.setInterpolator(new DecelerateInterpolator());
			fadeIn.setDuration(1500);
			fadeIn.setRepeatCount(Animation.INFINITE);
			badgeNoNumber.show();
			badgeNoNumber.setAnimation(fadeIn);
		}

		if (position == 5) {
			badgeNoNumber = new BadgeViewNoNumber(context, imageView);
			badgeNoNumber.setText("✓");
			badgeNoNumber.setBadgePosition(0);
			badgeNoNumber.setBadgeBackgroundColor(Color.parseColor("#4bc1d2"));
			Animation fadeIn = new AlphaAnimation(0, 1);
			fadeIn.setInterpolator(new DecelerateInterpolator());
			fadeIn.setDuration(2000);
			fadeIn.setRepeatCount(Animation.INFINITE);
			badgeNoNumber.show();
			badgeNoNumber.setAnimation(fadeIn);
		}
		if(indexHightLightItem == 1)
		{
			if(position == 6)
			{
				badgeNoNumber = new BadgeViewNoNumber(context, imageView);
				badgeNoNumber.setText("✓");
				badgeNoNumber.setBadgePosition(0);
				badgeNoNumber.setBadgeBackgroundColor(Color.parseColor("#4bc1d2"));
				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setInterpolator(new DecelerateInterpolator());
				fadeIn.setDuration(2000);
				fadeIn.setRepeatCount(Animation.INFINITE);
				badgeNoNumber.show();
				badgeNoNumber.setAnimation(fadeIn);
			}
		}
		else if(indexHightLightItem == 2)
		{
			if(position == 6 || position == 7)
			{
				badgeNoNumber = new BadgeViewNoNumber(context, imageView);
				badgeNoNumber.setText("✓");
				badgeNoNumber.setBadgePosition(0);
				badgeNoNumber.setBadgeBackgroundColor(Color.parseColor("#4bc1d2"));
				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setInterpolator(new DecelerateInterpolator());
				fadeIn.setDuration(2000);
				fadeIn.setRepeatCount(Animation.INFINITE);
				badgeNoNumber.show();
				badgeNoNumber.setAnimation(fadeIn);
			}
		}
		else if(indexHightLightItem == 3)
		{
			if(position == 6 || position == 7|| position == 8)
			{
				badgeNoNumber = new BadgeViewNoNumber(context, imageView);
				badgeNoNumber.setText("✓");
				badgeNoNumber.setBadgePosition(0);
				badgeNoNumber.setBadgeBackgroundColor(Color.parseColor("#4bc1d2"));
				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setInterpolator(new DecelerateInterpolator());
				fadeIn.setDuration(2000);
				fadeIn.setRepeatCount(Animation.INFINITE);
				badgeNoNumber.show();
				badgeNoNumber.setAnimation(fadeIn);
			}
		}
		else if(indexHightLightItem == 4)
		{
			if(position == 6 || position == 7|| position == 8|| position == 9)
			{
				badgeNoNumber = new BadgeViewNoNumber(context, imageView);
				badgeNoNumber.setText("✓");
				badgeNoNumber.setBadgePosition(0);
				badgeNoNumber.setBadgeBackgroundColor(Color.parseColor("#4bc1d2"));
				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setInterpolator(new DecelerateInterpolator());
				fadeIn.setDuration(2000);
				fadeIn.setRepeatCount(Animation.INFINITE);
				badgeNoNumber.show();
				badgeNoNumber.setAnimation(fadeIn);
			}
		}
		else if(indexHightLightItem == 5)
		{
			if(position == 6 || position == 7|| position == 8|| position == 9|| position == 10)
			{
				badgeNoNumber = new BadgeViewNoNumber(context, imageView);
				badgeNoNumber.setText("✓");
				badgeNoNumber.setBadgePosition(0);
				badgeNoNumber.setBadgeBackgroundColor(Color.parseColor("#4bc1d2"));
				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setInterpolator(new DecelerateInterpolator());
				fadeIn.setDuration(2000);
				fadeIn.setRepeatCount(Animation.INFINITE);
				badgeNoNumber.show();
				badgeNoNumber.setAnimation(fadeIn);
			}
		}

		// Class<?> c = MainActivity.class;

		return convertView;
	}

}
