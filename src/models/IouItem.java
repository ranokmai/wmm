package models;

import com.example.wmm.IouListFragment;
import com.example.wmm.R;

import models.Global;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * IOUItem represents one IOU which appears on the main screen
 * Contains information like name, date, description, amount, and image
 * @author Galen
 *
 */
@SuppressWarnings("unused")
public class IouItem {
	private RelativeLayout layout;
	private String contact, date, item_name;
	int amount;
	private Iou iou;
	private IouListFragment parentFragment;

	@SuppressLint("NewApi") public IouItem(Context context, String inName, String inDate, String inDesc, double inAmount, boolean money) {
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
			if (iou.pic_loc().isEmpty()) {
				((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setImageResource(R.drawable.ic_logo);
			}
			else {
				//load image
				((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setImageResource(R.drawable.ic_logo);
			}
			((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setVisibility(View.GONE);
		}

	}

	public IouItem(Context context, final Iou iou) {
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

		String temp = iou.item_type();

		if(temp.equals("Money")) {
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
			if (iou.pic_loc().length() == 0) {
				((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setImageResource(R.drawable.ic_logo);
			}
			else {
				Uri pic = Uri.parse(iou.pic_loc());
				((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setImageURI(pic);
			}
			((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setVisibility(View.GONE);
		}
	}
	
	public Iou getIou() { return this.iou; }

	public void toggleExpandIou(boolean toggleFlag) {
		if(toggleFlag) {
			layout.findViewById(R.id.archive_button).setVisibility(View.VISIBLE);
			layout.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
			layout.findViewById(R.id.edit_button).setVisibility(View.VISIBLE);
		}
		else {
			layout.findViewById(R.id.archive_button).setVisibility(View.GONE);
			layout.findViewById(R.id.delete_button).setVisibility(View.GONE);
			layout.findViewById(R.id.edit_button).setVisibility(View.GONE);
		}

	}

	public RelativeLayout getView() { return layout;	}
}
