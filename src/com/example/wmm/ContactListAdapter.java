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
	int selected = -1;
 
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
	
	public void setSelected(int select) {
		// Toggle selection
		if(select == selected) {
			data.get(selected).getView().setBackgroundColor(Color.TRANSPARENT);
			selected = -1; 
		} else {
			if(selected != -1) 
				data.get(selected).getView().setBackgroundColor(Color.TRANSPARENT);
			
			data.get(select).getView().setBackgroundColor(Color.parseColor("#4c4c4c"));
			selected = select; 
		}	
	}
	
	public int getSelected() {
		return selected;
	}
}