package com.example.wmm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import models.Global;
import models.ContactItem;
import models.Iou;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
 
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
        update_contacts_list(rootView);
        
        return rootView;
    }
    
    private void update_contacts_list(View rootView){
		contacts_items = new ArrayList<ContactItem>();
		ContentResolver content_resolver;
		String[] projection = new String[]{ContactsContract.Data.DISPLAY_NAME, PhoneLookup._ID, ContactsContract.Data.PHOTO_ID};
		String selection = ContactsContract.Data.DISPLAY_NAME + " like ?";
		String[] selection_arguments = {""};
		String full_contact_name, contact_id, photo_id;
		InputStream photo_stream;
		Cursor cursor;
		ArrayList<Iou> outstanding_ious;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		DecimalFormat af = new DecimalFormat("#.##");
		double total_loan_value;
		
		// Load contact names from the database
		contacts = Global.iou_db_mgr.get_contacts();
		
		if (contacts.size() == 0){
			TextView no_contacts_text = (TextView)rootView.findViewById(R.id.no_contacts_text);
			no_contacts_text.setVisibility(View.VISIBLE);
		} else {
			TextView no_contacts_text = (TextView)rootView.findViewById(R.id.no_contacts_text);
			no_contacts_text.setVisibility(View.GONE);
		}
		
		for (int curr_contact=0; curr_contact < contacts.size(); curr_contact++){
			// Query for the contact
			total_loan_value = 0;
			Bitmap photo = null;
			content_resolver = getActivity().getContentResolver();
			selection_arguments[0] = "%"+contacts.get(curr_contact)+"%";
			
			cursor = content_resolver.query(ContactsContract.Contacts.CONTENT_URI, projection, selection, selection_arguments, null);
			
			if (cursor != null){
				while (cursor.moveToNext()){
					full_contact_name = cursor.getString(0);
					contact_id = cursor.getString(1);
					photo_id = cursor.getString(2);
					
					if (photo_id != null){
						// Load the user's photo
						photo = loadContactPhoto(content_resolver, Long.parseLong(contact_id), Long.parseLong(photo_id));
					}
				
					break;
				}
			}
			
			// Query the user's loan information
			outstanding_ious = Global.iou_db_mgr.get_contact_ious_ordered_by_earliest_loan_date(contacts.get(curr_contact));
			
			for (int curr_iou = 0; curr_iou < outstanding_ious.size(); curr_iou++){
				if (outstanding_ious.get(curr_iou).item_type().equals("Money")){
					total_loan_value += outstanding_ious.get(curr_iou).value();
				}
			}
			
			int test = Global.iou_db_mgr.get_contact_num_outbound_item_ious(contacts.get(curr_contact));
			Log.d("IOU Test", Integer.toString(test));
			
			contacts_items.add(new ContactItem(getActivity(),
				contacts.get(curr_contact),
				Integer.toString(outstanding_ious.size()),
				df.format(outstanding_ious.get(outstanding_ious.size()-1).date_borrowed()),
				af.format(total_loan_value),
				Integer.toString(Global.iou_db_mgr.get_contact_num_outbound_item_ious(contacts.get(curr_contact))),
				photo));
		}
		
        contacts_adapter = new ContactListAdapter(contacts_items);
        contacts_list.setAdapter(contacts_adapter);
        
        contacts_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContactItem selected_item = ((ContactListAdapter)parent.getAdapter()).getContactItem(position);
				
				if(Global.fromnew == false){
					// Switch to the selected activity
					
					Intent intent = new Intent();
					intent.setClassName("com.example.wmm", "com.example.wmm.ContactsDetailActivity");
					intent.putExtra("ContactName", selected_item.name);
				    startActivity(intent);
				}
				else {
					Global.newIouAct.returnFromContacts( selected_item.name );
				}
			}
        });
    }
    
    public static Bitmap loadContactPhoto(ContentResolver cr, long id, long photo_id){
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input != null) 
        {
            return BitmapFactory.decodeStream(input);
        }
        else
        {
            Log.d("PHOTO","Failed to load photo from contact ID");

        }

        byte[] photoBytes = null;

        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);

        Cursor c = cr.query(photoUri, new String[] {ContactsContract.CommonDataKinds.Photo.PHOTO}, null, null, null);

        try {
            if (c.moveToFirst()){ 
                photoBytes = c.getBlob(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }           

        if (photoBytes != null){
            return BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.length);
        } else {
            Log.d("PHOTO","Failed to load photo from photo ID");
        }
        
        return null;
    }
}