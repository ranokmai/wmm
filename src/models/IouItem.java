package models;

import com.example.wmm.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IouItem {
	private RelativeLayout layout;
	private String name, date, amount;
	
	public IouItem(Context context, String inName, String inDate, String inAmount, boolean money) {
		name = inName;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		layout = (RelativeLayout) inflater.inflate(R.layout.iou_item, null);
		
		((TextView) layout.findViewById(R.id.singleItemName)).setText(name);
		((TextView) layout.findViewById(R.id.singleItemDate)).setText(inDate);
		((TextView) layout.findViewById(R.id.singleItemAmount)).setText(inAmount);
		
		// If this IOU is money or not money, use an Image for not money, and text for money
		if(money) {
			((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setVisibility(View.GONE);
		}
		else {
			((ImageView) layout.findViewById(R.id.singleItemThumbnail)).setImageResource(R.drawable.ic_bat);
			((TextView) layout.findViewById(R.id.singleItemMoneyThumbnail)).setVisibility(View.GONE);
		}
		
	}
	
	public RelativeLayout getView() { return layout;	}
}
