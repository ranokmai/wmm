package com.example.wmm;

import java.util.Calendar;
import java.util.GregorianCalendar;

import models.Global;
import models.Iou;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

@SuppressLint("NewApi")
public class NewIouActivity extends Activity {

	private boolean dateChanged;
	private boolean realContact;
	private String pictureUrl;
	
	private EditText mTitle;
	private EditText mValue; 
	private Spinner mTypes;
	private Spinner mContacts;
	private EditText mNamedContact;
	private Button mRemove;
	private Spinner mDirections;
	private Spinner mPicture;
	private Button mRemovePicture;
	private ImageView mImg;
	private DatePicker mDateLoaned;
	private DatePicker mDateDue; 
	private Button mAccept;
	private Button mCancel;
	private CheckBox mIsDateDue;  
	private EditText mNotes;
		
	private OnClickListener removeContact = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mRemove.setVisibility(View.GONE);
			mNamedContact.setVisibility(View.GONE);
			mContacts.setVisibility(View.VISIBLE);
			
			mContacts.setSelection(0);
	   }
	};
	
	private OnClickListener removePicture = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			mRemovePicture.setVisibility(View.GONE);
			mPicture.setVisibility(View.VISIBLE);
			
			mPicture.setSelection(0);
	   }
	};
	
	private OnDateChangedListener changeDate = new OnDateChangedListener() {
		
		@Override
		public void onDateChanged(DatePicker parent, int year, int month,
				int day) {
			dateChanged = true;
		}  
	};
	
	public NewIouActivity() {
		// required empty constructor
	}
	
    public NewIouActivity(MainActivity parent,
			Class<NewIouActivity> class1) {
    	//required constructor
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dateChanged = false;
        realContact = true; 
        pictureUrl = "";
        
        Intent intent = getIntent();
                
        setContentView(R.layout.activity_new_iou);
        getActionBar().setDisplayHomeAsUpEnabled(true);

		///////////////////////////////////////////////////////
		// Initialize all private memers in class and on screen
		///////////////////////////////////////////////////////
		mTitle = (EditText) findViewById(R.id.addIouTitleField);
		mValue = (EditText) findViewById(R.id.addIouValueField);
		mNotes = (EditText) findViewById(R.id.addIouNotes);
		
		mTypes = (Spinner) findViewById(R.id.addIouTypes);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		         R.array.new_iou_types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mTypes.setAdapter(adapter);
		
		mContacts = (Spinner) findViewById(R.id.addIouContacts);
		adapter = ArrayAdapter.createFromResource(this,
		         R.array.new_iou_subjects, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mContacts.setAdapter(adapter);
		
		mContacts.setOnItemSelectedListener( new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if( id != 0 ){
					mContacts.setVisibility(View.GONE);
					mNamedContact.setVisibility(View.VISIBLE);
					mRemove.setVisibility(View.VISIBLE);
					
					if( id == 1){
						
					}
					else if( id == 2){
						
					}
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		mNamedContact = ((EditText)findViewById(R.id.addIouContactNamed)); 
		mRemove = (Button)findViewById(R.id.addIouChangeContact);
		mRemove.setOnClickListener( this.removeContact );
		this.removeContact.onClick(mRemove);
		
		mDirections = (Spinner) findViewById(R.id.addIouDirections);
		adapter = ArrayAdapter.createFromResource(this,
		         R.array.new_iou_direction, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDirections.setAdapter(adapter);
		 
		mPicture = (Spinner) findViewById(R.id.addIouPicture);
		adapter = ArrayAdapter.createFromResource(this,
		         R.array.new_iou_pictures, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mPicture.setAdapter(adapter);
		
		mRemovePicture = (Button) findViewById(R.id.addIouRemovePicture);
		mRemovePicture.setVisibility(View.GONE);
		mRemovePicture.setOnClickListener( this.removePicture );
		this.removePicture.onClick(mRemovePicture);
		
		mPicture.setOnItemSelectedListener( new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if( id > 0 ) {
					mRemovePicture.setVisibility(View.VISIBLE);
					mPicture.setVisibility(View.INVISIBLE);
				}
				//TODO: Code to add a picture here...
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		 
		Calendar c = Calendar.getInstance(); 
		mDateLoaned = (DatePicker) findViewById(R.id.addIouDatePicker1); 		 
		mDateLoaned.init(c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH), changeDate);
		 
		mDateDue = (DatePicker) findViewById(R.id.addIouDatePicker2); 
		mDateDue.init(c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH), changeDate);
		mDateDue.setVisibility(View.INVISIBLE);
		
		mIsDateDue = (CheckBox) findViewById(R.id.addIouDateDue);		 
		mIsDateDue.setOnCheckedChangeListener( new OnCheckedChangeListener() {
		
			@Override
			public void onCheckedChanged(CompoundButton parent, boolean checked) {
				mDateDue.setEnabled(checked);
				if( checked ) mDateDue.setVisibility(View.VISIBLE);
				else mDateDue.setVisibility(View.INVISIBLE);
				
				dateChanged = true;
			}
		 
		});		 
		mDateDue.setEnabled( mIsDateDue.isChecked() );
		
		mAccept = (Button)findViewById(R.id.addIouAccept);
		mAccept.setOnClickListener( new OnClickListener() {
		
			@Override
			public void onClick(View view) {
				
				addNewIou();
			} 
		});
		 
		mCancel = (Button)findViewById(R.id.addIouDecline);
		mCancel.setOnClickListener( new OnClickListener() {
		
			@Override
			public void onClick(View view) {
				
				NewIouActivity.super.onBackPressed();
			} 
		});
		
		editIou(null);
		
    }
	
	public void editIou( Iou iou){
		
		iou = Global.iou;
		System.out.println("LOUIS: post");
		
		if( iou == null) return;
		
		//requests- change things to enums where possible
		// 			change date to gregorian calendar
		
		this.mTitle.setText( iou.item_name() ); 
		if( iou.is_a_contact() )
			this.mContacts.setSelection(1);
		else 
			this.mContacts.setSelection(2);
		this.mNamedContact.setText( iou.contact_name() );
		if( iou.item_type().compareTo("Money") == 0 )
			this.mTypes.setSelection(0);
		else 
			this.mTypes.setSelection(1);
		this.mValue.setText( iou.value().toString() );
		if( iou.outbound() )
			this.mDirections.setSelection(0);
		else 
			this.mDirections.setSelection(1);
		this.mPicture.setSelection(2);
		this.pictureUrl = iou.pic_loc();
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(iou.date_borrowed());
		this.mDateLoaned.init(	gc.get(gc.YEAR), gc.get(gc.MONTH),
								gc.get(gc.DAY_OF_MONTH), changeDate);
		
		gc.setTime(iou.date_due());
		if( iou.date_due().compareTo(Global.DATE_MAX) == 0 )
			this.mIsDateDue.setChecked(false);
		else 
			this.mDateDue.init(	gc.get(gc.YEAR), gc.get(gc.MONTH),
								gc.get(gc.DAY_OF_MONTH), changeDate);
		this.mNotes.setText(iou.notes());
		
		
		getActionBar().setTitle("Where\'s My Money: Edit Transaction");
	}
	
	private boolean addNewIou(){

		String name = this.mTitle.getText().toString();
		String contact = this.mContacts.getSelectedItem().toString();
		boolean isContact = this.realContact;
		String type = this.mTypes.getSelectedItem().toString();
		boolean direction = (this.mDirections.getSelectedItemPosition() == 0);
		String picture = this.pictureUrl; 
		GregorianCalendar loanedDate = new GregorianCalendar( 
											this.mDateLoaned.getYear(), 
											this.mDateLoaned.getMonth(), 
											this.mDateLoaned.getDayOfMonth());
		GregorianCalendar dueDate = new GregorianCalendar( 
											this.mDateDue.getYear(), 
											this.mDateDue.getMonth(), 
											this.mDateDue.getDayOfMonth());
		Double value;
		if( this.mValue.getText().toString().compareTo("") == 0) value = (double)0; 
		else value = Double.parseDouble(this.mValue.getText().toString());
		String notes = this.mNotes.getText().toString();
		
		// three required fields for an IOU
		if( name.compareTo("") == 0 || contact.compareTo("") == 0 || 
									mContacts.getSelectedItemId() == 0  ){
			
			new AlertDialog.Builder(this)
			    .setTitle("Invalid IOU!")
				.setMessage("A title and contact must be set for a new IOU")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
				        
					}
				
				}).create().show();
			
			return false; 
		}
		
		
		Iou iou= new Iou(name, contact, isContact, type, direction, 
				loanedDate.getTime(), dueDate.getTime(), value, picture, notes);
		
		Global.iou_db_mgr.insertIou(iou);
		NewIouActivity.super.onBackPressed();
		
		return false;
	}
	
	private boolean changesMade() {
	
		boolean title, type, value, subject, picture, notes, reminders;
		title = type = value = subject = picture = notes = reminders = false;
	
		if( mTitle.getText().toString().compareTo("") != 0 )
			title = true;
		
		if( mTypes.getSelectedItemId() > 0 )
			type = true;
				
		if( mValue.getText().toString().compareTo("") != 0 )
			value = true;
		
		if( mContacts.getSelectedItemId() > 0 )
			subject = true;
		
		if( mPicture.getSelectedItemId() > 0 )
			picture = true;
		
		if( mNotes.getText().toString().compareTo("") != 0 )
			notes = true;
		
		//if( ((EditText)findViewById(R.id.addIouNotes)).getText().toString() != "")
		//	reminders = true;
		
		return (title || type || value || subject || picture || 
						this.dateChanged || notes || reminders);
	}
	
	@Override
	public void onBackPressed() {
		// intercept the back button and check to see if the user wants to discard the data 
		
		if( changesMade() )
			new AlertDialog.Builder(this)
			    .setTitle("Continue?")
				.setMessage("Leaving will discard your changes. Continue without adding?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
			        NewIouActivity.super.onBackPressed();
				}
			
			}).create().show();
		
		else 
			NewIouActivity.super.onBackPressed();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if( id == android.R.id.home) {
        	onBackPressed(); 
        	return true;
	    }
    
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    @SuppressLint("NewApi")
	public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
              View rootView = inflater.inflate(R.layout.fragment_contacts,
                      container, false);
              return rootView;
        }
    }
}