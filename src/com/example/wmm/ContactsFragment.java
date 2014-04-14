package com.example.wmm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import models.Global;
import models.ContactItem;
import models.Iou;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
 
@SuppressLint("NewApi")
public class ContactsFragment extends Fragment {
	private ArrayList<ContactItem> contacts_items;
	private ArrayList<String> contacts;
	private ContactListAdapter contacts_adapter;
	private ListView contacts_list;
	
    public ContactsFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        
        contacts_list = (ListView) rootView.findViewById(R.id.contact_list);
        update_contacts_list();
        
        return rootView;
    }
    
    private void update_contacts_list(){
		contacts_items = new ArrayList<ContactItem>();
		ContentResolver content_resolver;
		Uri uri = Data.CONTENT_URI;
		String[] projection = new String[]{PhoneLookup._ID};
		String selection = StructuredName.DISPLAY_NAME + " = ?";
		String[] selection_arguments = {""};
		String contact_id;
		Cursor cursor;
		ArrayList<Iou> outstanding_ious;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		// Load contact names from the database
		contacts = Global.iou_db_mgr.get_contacts();
		
		for (int curr_contact=0; curr_contact < contacts.size(); curr_contact++){
			// Query for the contact
			content_resolver = getActivity().getContentResolver();
			selection_arguments[0] = contacts.get(curr_contact);
			cursor = content_resolver.query(uri, projection, selection, selection_arguments, null);
			Log.d("Search Contact", selection_arguments[0]);
			
			if (cursor != null){
				Log.d("Contact Count", Integer.toString(cursor.getCount()));
				while (cursor.moveToNext()){
					contact_id = cursor.getString(0);
					Log.d("Contact ID", contact_id);
					
					// Query the user's loan information
					outstanding_ious = Global.iou_db_mgr.get_contact_ious_ordered_by_earliest_loan_date(selection_arguments[0]);
					
					/*contacts_items.add(new ContactItem(getActivity(),
							selection_arguments[0],
							Integer.toString(outstanding_ious.size()),
							df.format(outstanding_ious.get(outstanding_ious.size()-1).date_borrowed()),
							));*/
				}
			}
		}
		
		//String[][] dummy_contacts = new String [3][5];
		
		/*dummy_contacts[0][0] = "Galen Gong";
		dummy_contacts[0][1] = "4";
		dummy_contacts[0][2] = "Last on 4/2";
		dummy_contacts[0][3] = "$14.50";
		dummy_contacts[0][4] = "0";
		dummy_contacts[1][0] = "Vladimir Putin";
		dummy_contacts[1][1] = "1";
		dummy_contacts[1][2] = "Last on 3/20";
		dummy_contacts[1][3] = "$3.50";
		dummy_contacts[1][4] = "0";
		dummy_contacts[2][0] = "Connor Davidson";
		dummy_contacts[2][1] = "2";
		dummy_contacts[2][2] = "Last on 4/8";
		dummy_contacts[2][3] = "$7.50";
		dummy_contacts[2][4] = "3";*/
		
        contacts_adapter = new ContactListAdapter(contacts_items);
        contacts_list.setAdapter(contacts_adapter);
        
        contacts_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				((ContactListAdapter)parent.getAdapter()).setSelected(position);
			}
        });
    }
}