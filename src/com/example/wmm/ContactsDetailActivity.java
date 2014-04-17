package com.example.wmm;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import models.Global;
import models.ContactItem;
import models.Iou;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
 
@SuppressLint("NewApi")
public class ContactsDetailActivity extends Activity {
	private String contact_name;
	private TextView contact_title;
	
	public ContactsDetailActivity() {
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragment_contacts_detail);
	    
	    contact_name = getIntent().getExtras().getString("ContactName");
	    setTitle(contact_name);
	    
	    contact_title = (TextView) findViewById(R.id.contact_title);
	    contact_title.setText(contact_name);
	}
	
}