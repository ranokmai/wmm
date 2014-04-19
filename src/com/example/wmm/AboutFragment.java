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
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
 
@SuppressLint("NewApi")
public class AboutFragment extends Fragment {
	
    public AboutFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.wheres_my_money);
        
        // Create the Github link
        TextView link_text = (TextView) rootView.findViewById(R.id.github_link);
        link_text.setText(Html.fromHtml("<a href=\"https://github.com/ranokmai/wmm\">Github Project</a> "));
        link_text.setMovementMethod(LinkMovementMethod.getInstance());
        
        // Setup audio clip
        ImageButton logo_button = (ImageButton) rootView.findViewById(R.id.logo_button);
        logo_button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
            	mp.start();
            }
        });
        
        return rootView;
    }
}