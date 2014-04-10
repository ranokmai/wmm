package models;

import com.example.wmm.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Represents a contact in the application's contact list. 
 * 
 * @author Jim Blanchard
 */
public class ContactItem {
	private String name, total_value;
	private int item_count, loan_count;
	private LinearLayout layout;
	
	public ContactItem(Context context, String contact_name, String outstanding_loans, String date, String amount, String items){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.contact_item, null);
		
		// Populate list item layout
		((TextView) layout.findViewById(R.id.contact_name)).setText(contact_name);
		((TextView) layout.findViewById(R.id.contact_outstanding_loans)).setText(outstanding_loans+" outstanding loans");
		((TextView) layout.findViewById(R.id.contact_last_loan_date)).setText(date);
		((TextView) layout.findViewById(R.id.contact_lent_amount)).setText(amount);
		((TextView) layout.findViewById(R.id.contact_lent_items)).setText("+ "+items+" items");
		((ImageView) layout.findViewById(R.id.contact_image)).setImageResource(R.drawable.ic_logo);
		
	}
	
	public LinearLayout getView() {return layout;}
}
