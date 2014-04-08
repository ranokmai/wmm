package com.example.wmm;
 
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
 
@SuppressLint("NewApi")
public class ContactsFragment extends Fragment {
	ImageView ivIcon;
	TextView tvItemName;
 
	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";
 
	public ContactsFragment() {
	}
 
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
            Bundle savedInstanceState) {
 
		View view = inflater.inflate(R.layout.contacts_list, container,
                        			 false);
 
		ivIcon = (ImageView) view.findViewById(R.id.icon_contacts);
		tvItemName = (TextView) view.findViewById(R.id.contacts_text);
 
		tvItemName.setText(getArguments().getString(ITEM_NAME));
		ivIcon.setImageDrawable(view.getResources().getDrawable(
								getArguments().getInt(IMAGE_RESOURCE_ID)));
		return view;
      }
 
}