package com.example.wmm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

	private String mCurrentPhotoPath;
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_GALLERY_IMAGE = 2;
		
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
			
			mImg.setVisibility(View.INVISIBLE);
			mCurrentPhotoPath = null;
			pictureUrl = null;
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
		
		mImg = (ImageView) findViewById(R.id.addIouPictureViewer);
		 
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
				if (!pictureUrl.isEmpty()) {
					
				}
				
				if (position == 1) {
				    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				    
				    File photoFile = null;
			        try {
			            photoFile = createImageFile();
						mCurrentPhotoPath = photoFile.getAbsolutePath();
			        } catch (IOException ex) {
			            // Error occurred while creating the File
			        }
			        // Continue only if the File was successfully created
			        if (photoFile != null) {
			            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			                    Uri.fromFile(photoFile));
					    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
					    }
			        }
				}
				else if (position == 2) {
			      Intent intent = new Intent();
			      intent.setType("image/*");
			      intent.setAction(Intent.ACTION_GET_CONTENT);
			      if (intent.resolveActivity(getPackageManager()) != null) {
			    	  startActivityForResult(Intent.createChooser(intent,"Select Picture"), REQUEST_GALLERY_IMAGE);
			      }
				}
				
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
		
		
		Bundle b = intent.getExtras();
        if(b!=null) // an edit
        {
            editIou();
        }
	        
    }
	
	public void editIou(){
		
		Iou iou = Global.iou;
			
		if( iou == null) return;
		
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
		this.mPicture.setSelection(0);
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
        if (id == R.id.action_open_settings) {
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
		

	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        
			setPic();
			galleryAddPic();
			
			pictureUrl = mCurrentPhotoPath;
	    	
	        //Bundle extras = data.getExtras();
	        //Bitmap imageBitmap = (Bitmap) extras.get("data");
            
	        //ImageView imageView = (ImageView) findViewById(R.id.addIouPictureViewer);
	        //imageView.setImageBitmap(imageBitmap);
	    }

		
		if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == RESULT_OK) {
			
            Uri selectedImageUri = data.getData();
            pictureUrl = getPath(selectedImageUri);
            System.out.println("Image Path : " + pictureUrl);
            
            try {
	            ImageView imageView = (ImageView) findViewById(R.id.addIouPictureViewer);
	            
				Bitmap bm = Bitmap.createScaledBitmap(
						MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri), 
						imageView.getWidth(), imageView.getHeight(),  false);
				
	            imageView.setImageBitmap(bm);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
        }
	}

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "WMM_JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}

	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}
	
	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

        ImageView mImageView = (ImageView) findViewById(R.id.addIouPictureViewer);
		
		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		
		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
		mImageView.setVisibility(View.VISIBLE);
	}

    /**
     * helper to retrieve the path of an image URI
     */
    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}