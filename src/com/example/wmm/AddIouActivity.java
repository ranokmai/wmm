package com.example.wmm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class AddIouActivity extends Activity {

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_iou_item);
        getActionBar().setTitle(R.string.app_add_iou);
     
         //Initialize the page with values now
        
		 Spinner spinner = (Spinner) findViewById(R.id.addIouTypes);
		 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		         R.array.new_iou_types, android.R.layout.simple_spinner_item);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner.setAdapter(adapter);
        
		 Spinner spinner2 = (Spinner) findViewById(R.id.addIouContacts);
		 adapter = ArrayAdapter.createFromResource(this,
		         R.array.new_iou_subjects, android.R.layout.simple_spinner_item);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner2.setAdapter(adapter);
		 
		 Button b = (Button) findViewById(R.id.addIouDirections);
		 b.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if( ((TextView)view).getText() == "@string/new_iou_direction_to" )
					((TextView)view).setText( "@string/new_iou_date_direction_from");
				else
					((TextView)view).setText( "@string/new_iou_date_direction_to");
			}
		 });
		 
		 final Spinner spinner3 = (Spinner) findViewById(R.id.addIouPicture);
		 adapter = ArrayAdapter.createFromResource(this,
		         R.array.new_iou_pictures, android.R.layout.simple_spinner_item);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner3.setAdapter(adapter);
		 
		 ImageView iv = (ImageView) findViewById(R.id.addIouPictureViewer);
		 iv.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if( spinner3.getSelectedItemId() == 0 ){
					// code to get picture from lib
				}
				else {
					// code to take new picture
				}
			}
			 
		 });
		 
		 //set dates
		 
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
              View rootView = inflater.inflate(R.layout.fragment_display_contacts,
                      container, false);
              return rootView;
        }
    }
}