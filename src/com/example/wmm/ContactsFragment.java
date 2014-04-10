package com.example.wmm;

import java.util.ArrayList;

import models.Global;
import models.ContactItem;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
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
		
		// Load contact names from the database
		//contacts = Global.iou_db_mgr.get_contacts();
		
		String[][] dummy_contacts = new String [3][5];
		
		dummy_contacts[0][0] = "Galen Gong";
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
		dummy_contacts[2][4] = "3";
		
		// Generate contact list items
		for (int curr_contact = 0; curr_contact < dummy_contacts.length; curr_contact++){
			contacts_items.add(new ContactItem(getActivity(),
											   dummy_contacts[curr_contact][0],
											   dummy_contacts[curr_contact][1],
											   dummy_contacts[curr_contact][2],
											   dummy_contacts[curr_contact][3],
											   dummy_contacts[curr_contact][4]));
		}
		
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