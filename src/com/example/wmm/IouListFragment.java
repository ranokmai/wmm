package com.example.wmm;

import java.util.ArrayList;

import models.Global;
import models.Global.Filters;
import models.Iou;
import models.IouDB_Error;
import models.IouItem;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
 
@SuppressLint("NewApi")
public class IouListFragment extends Fragment{
	private ArrayList<IouItem> iouItems;
	private ArrayList<Iou> ious;
	private ListView iou_list;
	private IouListAdapter adapter;
	private OnNavigationListener filter_callback;
	
    public IouListFragment(){}
    
    private boolean incoming;
    private boolean outgoing;
    
    private int selected_sort = 0;
    private Spinner filters;
    private boolean ascending;
    
    public View rootView;
    
    private Iou selectedIou;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	incoming = outgoing = true;
    	ascending = true; 
    			
    	rootView = inflater.inflate(R.layout.fragment_main, container, false);
    	
    	filter_callback = new OnNavigationListener() {
		  // Get the dropdown string list
		  String[] strings = getResources().getStringArray(R.array.filter_options);

		  @Override
		  public boolean onNavigationItemSelected(int position, long itemId) {
		    //Log.d("Selected Sort Option", String.valueOf(itemId));
		    selected_sort = position;
		    updateListView(null);
			  
		    return true;
		  }
		};
		
		// Add current iou items
		iou_list = (ListView) rootView.findViewById(R.id.iou_list);
		
		ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource((Context)getActivity(),
		         R.array.filter_options, android.R.layout.simple_spinner_item);
		spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		getActivity().getActionBar().setListNavigationCallbacks(spinneradapter, filter_callback);
		
		final ToggleButton rboutgoing = (ToggleButton) rootView.findViewById(R.id.outgoing);
		final ToggleButton rbincoming = (ToggleButton) rootView.findViewById(R.id.incoming);
		final Switch sort_switch = (Switch) rootView.findViewById(R.id.sort_switch);
		rboutgoing.setChecked(true);
		rbincoming.setChecked(true);
		
		sort_switch.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				ascending = !ascending;
				updateListView(null);
			}
		});
		
		rboutgoing.setOnClickListener( new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
		 	    outgoing = !outgoing;
		 	    updateListView(null);
			}
		});
		
		rbincoming.setOnClickListener( new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
		 	    incoming = !incoming;
		 	    updateListView(null);
			}
		});
		
		updateListView(rootView);
		
        return rootView;
    }

	public void deleteSelectedIOU() {
		
		try {
			models.Global.iou_db_mgr.deleteIou(selectedIou);
		}
		catch (IouDB_Error e) {
			Log.i("db_error", e.error);
		}
		
		updateListView(null);
		selectedIou = null;
		
		((IouListAdapter)iou_list.getAdapter()).clearSelection();
		
	}
	
	public void editSelectedIOU() {
		
		Global.iou = selectedIou;
			
		Intent intent = new Intent( getActivity(), NewIouActivity.class);
		intent.putExtra( "isEdit", true);
		startActivityForResult(intent, 1);	// 1 for edit
	}
	
	public void archiveSelectedIOU() {
		models.Global.iou_db_mgr.add_iou_to_archive(selectedIou);
		updateListView(null);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
    		
	    	case 1: // edit         
	    		//deleteSelectedIOU();       		
	    	case 0: // add
	        	updateListView(null);
	        	Global.newIouAct = null;
        }
    }
    
	public void addNewIou( ) {
		
		Intent intent = new Intent( getActivity(), NewIouActivity.class);
	    startActivityForResult(intent, 0);
	}
	
    // Update the main view with the Items in iouItems
 	public void updateListView(View rootView) {
 		
 		//make sure initialized
		iouItems = new ArrayList<IouItem>();
		View fragment_view;
		
		populateIous();
		
		if (rootView == null){
			fragment_view = getView();
		} else {
			fragment_view = rootView;
		}
		
		// Detect no IOUs 
		if (ious.size() == 0){
			TextView no_iou_text = (TextView) fragment_view.findViewById(R.id.no_iou_text);
			no_iou_text.setVisibility(View.VISIBLE);
			RelativeLayout filter_panel = (RelativeLayout) fragment_view.findViewById(R.id.filter_region);
			//filter_panel.setVisibility(View.GONE);
			//View seperator = (View) fragment_view.findViewById(R.id.separator);
			//seperator.setVisibility(View.GONE);
		} else {
			TextView no_iou_text = (TextView) fragment_view.findViewById(R.id.no_iou_text);
			no_iou_text.setVisibility(View.GONE);
			RelativeLayout filter_panel = (RelativeLayout) fragment_view.findViewById(R.id.filter_region);
			//filter_panel.setVisibility(View.VISIBLE);
			//View seperator = (View) fragment_view.findViewById(R.id.separator);
			//seperator.setVisibility(View.VISIBLE);
		}
		
		// Fill iouItems with db data
		for (int i = 0; i < ious.size(); i++) {
			iouItems.add(new IouItem(getActivity(), ious.get(i), true));
		}
		
        adapter = new IouListAdapter(iouItems);
        iou_list.setAdapter(adapter);
        
        iou_list.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				((IouListAdapter)parent.getAdapter()).setSelected(position);
				selectedIou = ious.get(position);
				
			}
        });
 	}
 	
 	private void populateIous(){
 		if( this.incoming && this.outgoing ){
 			if(selected_sort == 0) { // loaned
 				ious = Global.iou_db_mgr.get_ious_ordered_by_earliest_loan_date();
 			}
 			else if (selected_sort == 1) {// due
 				ious = Global.iou_db_mgr.get_ious_ordered_by_closest_due_date();
 			}
 			else if (selected_sort == 2) {// title
 				ious = Global.iou_db_mgr.get_ious_ordered_by_name();
 			}
 			else if (selected_sort == 3) {// value
 				ious = Global.iou_db_mgr.get_ious_ordered_by_value_desc();
 			}
 			else if (selected_sort == 4) {// Archived
 				ious = Global.iou_db_mgr.get_archived_ious_ordered_by_loan_date();
 				
 			}
 	 		else {
 	 			ious = Global.iou_db_mgr.get_ious_unordered();
 	 		}
 		}
 		else if(this.incoming){
 			if (selected_sort == 0) { // loaned
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_earliest_loan_date();
 			}
 			else if (selected_sort == 1) {// due
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_closest_due_date();
 			}
 			else if(selected_sort == 2) {// title
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_name();
 			}
 			else if(selected_sort == 3) {// value
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_value_desc();
 			}
 			else if(selected_sort == 4) {
 				ious = Global.iou_db_mgr.get_inbound_archived_ious_ordered_by_loan_date();
 			}
 	 		else {
 	 			ious = Global.iou_db_mgr.get_incoming_ious_unordered();
 	 		}
 		}
 		else if (this.outgoing){
 			if(selected_sort == 0) { // loaned
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_earliest_loan_date();
 			}
 			else if (selected_sort == 1) {// due
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_closest_due_date();
 			}
 			else if (selected_sort == 2) {// title
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_name();
 			}
 			else if (selected_sort == 3) {// value
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_value_desc();
 			}
 			else if (selected_sort == 4){
 				ious = Global.iou_db_mgr.get_outbound_archived_ious_ordered_by_loan_date();
 			}
 	 		else {
 	 			ious = Global.iou_db_mgr.get_outgoing_ious_unordered();
 	 		}
 		}
 		else {
 			// Clear the list
 			ious.clear();
 		}
 		
 		// flip if they want the other direction
 		if(!ascending) ious = flipArray(ious);
 		
 		return;
 	}
 	
 	private ArrayList<Iou> flipArray( ArrayList<Iou> reverse){
		
 		for(int i = 0; i < reverse.size() / 2; i++)
 		{
 		    Iou temp = reverse.get(i);
 		    reverse.set(i, reverse.get(reverse.size() - i - 1));
 		   reverse.set(reverse.size() - i - 1, temp);
 		}
 		
 		return reverse;
 	}
}