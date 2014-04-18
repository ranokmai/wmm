package com.example.wmm;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import models.Global;
import models.ContactItem;
import models.Iou;
import models.IouItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
 
@SuppressLint("NewApi")
public class ContactsDetailActivity extends Activity {
	private TypedArray navigation_icons;
	private String contact_name;
	private TextView contact_title;
	private TextView last_loan;
	private TextView loan_count;
	private TextView loan_amount;
	private TextView items_lent;
	private ListView iou_list;
	
	public ContactsDetailActivity() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		double total_loan_value = 0;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat af = new DecimalFormat("#.##");
		contact_name = getIntent().getExtras().getString("ContactName");
		navigation_icons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragment_contacts_detail);
	    setTitle("Contact Details");
	    getActionBar().setIcon(navigation_icons.getResourceId(1, -1));
	    
	    // Query the user's loan information
	    ArrayList<Iou> outstanding_ious = Global.iou_db_mgr.get_contact_ious_ordered_by_earliest_loan_date(contact_name);
	 			
	 	for (int curr_iou = 0; curr_iou < outstanding_ious.size(); curr_iou++){
	 		if (outstanding_ious.get(curr_iou).item_type().equals("Money")){
	 			total_loan_value += outstanding_ious.get(curr_iou).value();
	 		}
	 	}
	 			
	 	// Update the detailed view
	 	iou_list = (ListView) findViewById(R.id.detail_iou_list);
	 	contact_title = (TextView) findViewById(R.id.detail_contact_name);
	 	contact_title.setText(contact_name);
	 	last_loan = (TextView) findViewById(R.id.detail_last_loan);
	 	last_loan.setText("Last loan made on "+df.format(outstanding_ious.get(outstanding_ious.size()-1).date_borrowed()));
	 	loan_count = (TextView) findViewById(R.id.detail_loan_count);
	 	loan_count.setText(Integer.toString(outstanding_ious.size())+" outstanding loans");
	 	loan_amount = (TextView) findViewById(R.id.detail_loan_amount);
	 	loan_amount.setText("$"+af.format(total_loan_value));
	 	items_lent = (TextView) findViewById(R.id.detail_items_lent);
	 	items_lent.setText("+ "+Integer.toString(Global.iou_db_mgr.get_contact_num_outbound_item_ious(contact_name))+" items");
	
	 	// Load the contact's IOUs
	 	ArrayList<IouItem> iouItems = new ArrayList<IouItem>();
	 	ArrayList<Iou> ious = Global.iou_db_mgr.get_contact_ious_ordered_by_earliest_loan_date(contact_name);
		
		// Populate the IOU list
		for (int i = 0; i < ious.size(); i++) {
			iouItems.add(new IouItem(this, ious.get(i)));
		}
		
		IouListAdapter adapter = new IouListAdapter(iouItems);
        iou_list.setAdapter(adapter);
        iou_list.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				((IouListAdapter)parent.getAdapter()).setSelected(position);
			}
        });
	}
}