package com.example.wmm;

import java.util.ArrayList;

import models.Global;
import models.Global.Filters;
import models.Iou;
import models.IouItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
 
@SuppressLint("NewApi")
public class IouListFragment extends Fragment{
	private ArrayList<IouItem> iouItems;
	private ArrayList<Iou> ious;
	private ListView iou_list;
	private IouListAdapter adapter;
	
    public IouListFragment(){}
    
    private boolean incoming;
    private boolean outgoing;
    
    private Spinner filters;
    private boolean ascending; 
    
    private Iou selectedIou;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	
    	incoming = outgoing = true;
    	ascending = true; 
    			
    	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		
		// Add current iou items
		iou_list = (ListView) rootView.findViewById(R.id.iou_list);
		
		filters = (Spinner) rootView.findViewById(R.id.filter_by);
		ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource( (Context)getActivity() ,
		         R.array.filter_options, android.R.layout.simple_spinner_item);
		spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filters.setAdapter(spinneradapter);
		
		Filters toint = Filters.DATEDUE;
		filters.setSelection( toint.getint() );
		
		filters.setOnItemSelectedListener( new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				updateListView( );
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		final RadioButton rboutgoing = (RadioButton) rootView.findViewById(R.id.outgoing);
		final RadioButton rbincoming = (RadioButton) rootView.findViewById(R.id.incoming);
		rboutgoing.setChecked(true);
		rbincoming.setChecked(true);
		rboutgoing.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton parent, boolean checked) {
				outgoing = checked;
				updateListView( );
				
			}
			
		});
		rbincoming.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton parent, boolean checked) {
				incoming = checked;
				updateListView( );
			}
			
		});
		
		updateListView();
		
        return rootView;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case 0:
        	updateListView();
        }
    }

	public void deleteSelectedIOU() {
		//Iou iou = ious.get(selected);
		models.Global.iou_db_mgr.deleteIou(selectedIou);
		updateListView();
	}
    
    // Update the main view with the Items in iouItems
 	public void updateListView( ) {
 		//make sure initialized
		iouItems = new ArrayList<IouItem>();
		
		populateIous();
		
		// Fill iouItems with db data
		for (int i = 0; i < ious.size(); i++) {
			iouItems.add(new IouItem(getActivity(), ious.get(i)));
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
 			//add current iou items according to filter
 			if( this.filters.getSelectedItemPosition() == 0 ) { // loaned
 				ious = Global.iou_db_mgr.get_ious_ordered_by_earliest_loan_date();
 			}
 			else if ( this.filters.getSelectedItemPosition() == 1 ) {// due
 				ious = Global.iou_db_mgr.get_ious_ordered_by_closest_due_date();
 			}
 			else if( this.filters.getSelectedItemPosition() == 2 ) {// title
 				ious = Global.iou_db_mgr.get_ious_ordered_by_name();
 			}
 			else if( this.filters.getSelectedItemPosition() == 3 ) {// value
 				ious = Global.iou_db_mgr.get_ious_ordered_by_value_desc();
 			}
 	 		else {
 	 			ious = Global.iou_db_mgr.get_ious_unordered();
 	 		}
 		}
 		else if( this.incoming){
 			if( this.filters.getSelectedItemPosition() == 0 ) { // loaned
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_earliest_loan_date();
 			}
 			else if ( this.filters.getSelectedItemPosition() == 1 ) {// due
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_closest_due_date();
 			}
 			else if( this.filters.getSelectedItemPosition() == 2 ) {// title
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_name();
 			}
 			else if( this.filters.getSelectedItemPosition() == 3 ) {// value
 				ious = Global.iou_db_mgr.get_incoming_ious_ordered_by_value_desc();
 			}
 	 		else {
 	 			ious = Global.iou_db_mgr.get_incoming_ious_unordered();
 	 		}
 		}
 		else if (this.outgoing){
 			if( this.filters.getSelectedItemPosition() == 0 ) { // loaned
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_earliest_loan_date();
 			}
 			else if ( this.filters.getSelectedItemPosition() == 1 ) {// due
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_closest_due_date();
 			}
 			else if( this.filters.getSelectedItemPosition() == 2 ) {// title
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_name();
 			}
 			else if( this.filters.getSelectedItemPosition() == 3 ) {// value
 				ious = Global.iou_db_mgr.get_outgoing_ious_ordered_by_value_desc();
 			}
 	 		else {
 	 			ious = Global.iou_db_mgr.get_outgoing_ious_unordered();
 	 		}
 		}
 		else {
 			ious = Global.iou_db_mgr.get_ious_unordered();
 		}
 		
 		System.out.println( this.filters.getSelectedItemPosition() + " LOUIS");
 		
 		// flip if they want the other direction
 		if( !ascending ) ious = flipArray(ious);
 		
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