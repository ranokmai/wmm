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
    
    private Filters mFilter;
    private Iou selectedIou;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	
    	incoming = outgoing = true;
    	mFilter = Filters.DATEDUE;
    			
    	View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		
		// Add current iou items
		iou_list = (ListView) rootView.findViewById(R.id.iou_list);
		
		Button b = (Button)(rootView.findViewById(R.id.add_new_button));
		b.setOnClickListener( new OnClickListener() {

			public void onClick(View view) {
				
				Global.iou = selectedIou;
				System.out.println("LOUIS: pre");
				
				
				 // Display the fragment as the main content.
				Intent intent = new Intent( getActivity(), NewIouActivity.class);
				startActivityForResult(intent, 0);			
			    
			}

		});
		
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
				mFilter = Filters.values()[position];
				updateListView( mFilter, outgoing, incoming);
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
				updateListView( mFilter, outgoing, incoming);
				
			}
			
		});
		rbincoming.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton parent, boolean checked) {
				incoming = checked;
				updateListView( mFilter, outgoing, incoming);
			}
			
		});
		
		updateListView( mFilter, outgoing, incoming);
		
        return rootView;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case 0:
        	updateListView( mFilter, outgoing, incoming);
        }
    }
    
    
    // Update the main view with the Items in iouItems
 	public void updateListView( Filters filter, boolean showOut, boolean showIn) {
 		//make sure initialized
		iouItems = new ArrayList<IouItem>();
		
		//add current iou items
		// use correct query here or just sort manually...
		ious = Global.iou_db_mgr.get_ious_ordered_by_closest_due_date();
		
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
}