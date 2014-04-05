package models;

import com.example.wmm.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IouItem {
	private RelativeLayout layout;
	private String name, date, amount;
	
	public IouItem(Context context, String inName, String inDate, String inAmount) {
		name = inName;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		layout = (RelativeLayout) inflater.inflate(R.layout.iou_item, null);
		
		((TextView) layout.findViewById(R.id.singleItemName)).setText(name);
		((TextView) layout.findViewById(R.id.singleItemDate)).setText(inDate);
		((TextView) layout.findViewById(R.id.singleItemAmount)).setText(inAmount);
		
	}
	
	public RelativeLayout getView() { return layout;	}
}
