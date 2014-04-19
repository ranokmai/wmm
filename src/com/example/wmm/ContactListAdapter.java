package com.example.wmm;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import models.Global;
import models.ContactItem;

 
public class ContactListAdapter extends BaseAdapter {
	private ArrayList<ContactItem> data;
 
    public ContactListAdapter(ArrayList<ContactItem> inArrayList) {
        data = inArrayList;
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactItem temp = data.get(position);
		return temp.getView();
	}
	
	public ContactItem getContactItem(int position){
		ContactItem temp = data.get(position);
		return temp;
	}
}