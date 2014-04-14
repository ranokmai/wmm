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
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
				}
				//TODO: code to add a contact here...
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
				
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 1);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		 
		Calendar c = Calendar.getInstance(); 
		mDateLoaned = (DatePicker) findViewById(R.id.addIouDatePicker1); 		 
		mDateLoaned.init(c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH), new OnDateChangedListener() {
		
			@Override
			public void onDateChanged(DatePicker parent, int year, int month,
					int day) {
				dateChanged = true;
			} 
			 
		});
		 
		mDateDue = (DatePicker) findViewById(R.id.addIouDatePicker2); 
		mDateDue.init(c.get(c.YEAR), c.get(c.MONTH), c.get(c.DAY_OF_MONTH), new OnDateChangedListener() {
		
			@Override
			public void onDateChanged(DatePicker parent, int year, int month,
					int day) {
				dateChanged = true;
			} 
			 
		});
		 
		mIsDateDue = (CheckBox) findViewById(R.id.addIouDateDue);		 
		mIsDateDue.setOnCheckedChangeListener( new OnCheckedChangeListener() {
		
			@Override
			public void onCheckedChanged(CompoundButton parent, boolean checked) {
				mDateDue.setEnabled(checked);
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
    }
	
	private boolean addNewIou(){
	
		String name = this.mTitle.getText().toString();
		String contact = this.mContacts.getSelectedItem().toString();
		boolean isContact = this.realContact;
		String type = this.mTypes.getSelectedItem().toString();
		boolean direction = (this.mDirections.getSelectedItemPosition() == 0);
		GregorianCalendar loanedDate = new GregorianCalendar( 
											this.mDateLoaned.getYear(), 
											this.mDateLoaned.getMonth(), 
											this.mDateLoaned.getDayOfMonth());
		GregorianCalendar dueDate = new GregorianCalendar( 
											this.mDateDue.getYear(), 
											this.mDateDue.getMonth(), 
											this.mDateDue.getDayOfMonth());
		Double value = Double.parseDouble(this.mValue.getText().toString());
		String picture = this.pictureUrl; 
		String notes = this.mNotes.getText().toString();
	
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
				.setMessage("Leaving will discard your changes. Continue without adding IOU?")
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
    

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pictureUrl = cursor.getString(columnIndex);
            cursor.close();
             
            ImageView imageView = (ImageView) findViewById(R.id.addIouPictureViewer);
            imageView.setImageBitmap(BitmapFactory.decodeFile(pictureUrl));
         
        }
	}
}