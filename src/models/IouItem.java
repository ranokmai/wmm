package models;

import com.example.wmm.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IouItem {
	private RelativeLayout layout;
	private String name;
	
	public IouItem(Context context, String inName) {
		name = inName;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		layout = (RelativeLayout) inflater.inflate(R.layout.iou_item, null);
		
		((TextView) layout.findViewById(R.id.singleItemTitle)).setText(name);
	}
	
	public RelativeLayout getView() { return layout;	}
}
