package models;

import com.example.wmm.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IouItem {
	private RelativeLayout layout;
	private String contact, date, item_name;
	int amount;
	private Iou iou;
	
	public IouItem(Context context, String inName, String inDate, String inDesc, double inAmount, boolean money) {
		contact = inName;
		date = inDate;
		item_name = inDesc;
		if(inAmount < 0)
			amount = (int) Math.floor(inAmount);
		else
			amount = (int) Math.ceil(inAmount);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (RelativeLayout) inflater.inflate(R.layout.iou_item, null);
		
		// Set the name, date, and desc
		((TextView) layout.findViewById(R.id.singleItemName)).setText(contact);
		((TextView) layout.findViewById(R.id.singleItemDate)).setText(date);
		((TextView) layout.findViewById(R.id.singleItemAmount)).setText(item_name);
		
		// If this IOU is money or not money, use an Image for not money, and text for money
		if(money) {
			((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setVisibility(View.GONE);
			
			// Set color to red if amount is negative
			if(amount < 0) {
				((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setTextColor(Color.parseColor("#FF0000"));
				String text = "($" + Integer.toString(amount*-1) + ")";
				((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setText(text);
			}
			else {
				String text = "$" + Integer.toString(amount);
				((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setText(text);
			}
		}
		// Set image to be the launcher and hide the MoneyThumbnail
		else {
			((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setImageResource(R.drawable.ic_logo);
			((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setVisibility(View.GONE);
		}
		
	}
	
	public IouItem(Context context, Iou iou) {
		contact = iou.contact_name();
		date = Global.date_to_str(iou.date_due());
		item_name = iou.item_name();
		
		this.iou = iou;
		
		if(iou.value() < 0)
			amount = (int) Math.floor(iou.value());
		else
			amount = (int) Math.ceil(iou.value());
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (RelativeLayout) inflater.inflate(R.layout.iou_item, null);
		
		// Set the name, date, and desc
		((TextView) layout.findViewById(R.id.singleItemName)).setText(contact);
		((TextView) layout.findViewById(R.id.singleItemDate)).setText(date);
		((TextView) layout.findViewById(R.id.singleItemAmount)).setText(item_name);
		
		// If this IOU is money or not money, use an Image for not money, and text for money
		if(iou.item_type().equals(Iou.ITEM_TYPES.get(Iou.item_types_e.MONEY.get_val()))) {
			((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setVisibility(View.GONE);
			
			// Set color to red if amount is negative
			if(amount < 0) {
				((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setTextColor(Color.parseColor("#FF0000"));
				String text = "($" + Integer.toString(amount*-1) + ")";
				((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setText(text);
			}
			else {
				String text = "$" + Integer.toString(amount);
				((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setText(text);
			}
		}
		// Set image to be the launcher and hide the MoneyThumbnail
		else {
			((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setImageResource(R.drawable.ic_bat);
			((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setVisibility(View.GONE);
		}
	}
	
	public RelativeLayout getView() { return layout;	}
}
