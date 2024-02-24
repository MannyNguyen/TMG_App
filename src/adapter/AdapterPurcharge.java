package adapter;

import java.util.ArrayList;

import object.Purchage;

import com.android.activity.PurchaseActivity;
import com.ctyprosoft.tmg.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class AdapterPurcharge extends BaseAdapter {

	Context mContext;
	ArrayList<Purchage> array ;
	public AdapterPurcharge(Context mContext, ArrayList<Purchage> array) {
		this.mContext = mContext;
		this.array = array;
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
		public TextView tvReceipt;
		public TextView tvValue;
		public TextView tvDay;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
//		if(convertView == null)
//		{
			convertView = View.inflate(mContext, R.layout.item_purchase_list, null);
			holder.tvReceipt = (TextView) convertView.findViewById(R.id.tv_receipt_value);
			holder.tvValue = (TextView) convertView.findViewById(R.id.tv_receipt_price_value);
			holder.tvDay = (TextView) convertView.findViewById(R.id.tv_receipt_day);
			convertView.setTag(holder);

//		}
//		else
//			holder = (ViewHolder) convertView.getTag();
		
		holder.tvReceipt.setText(array.get(position).getReceiptNo());
		holder.tvValue.setText(array.get(position).getPrice());
		
		holder.tvDay.setText(array.get(position).getDay());
		
		if(position == PurchaseActivity.positionClick)
			convertView.setBackgroundColor(Color.GRAY);
		return convertView;
	}

}
