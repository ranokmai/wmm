package com.example.wmm;

import java.util.ArrayList;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import models.IouItem;

 
public class IouListAdapter extends BaseAdapter {
	private ArrayList<IouItem> data;
	
	int selected = -1;
 
    public IouListAdapter(ArrayList<IouItem> inArrayList) {
        data=inArrayList;
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
		IouItem temp = data.get(position);
		return temp.getView();
	}
	
	public void setSelected( int select ) {
		//toggle
		if( select == selected ) {
			data.get(selected).toggleExpandIou(false);
			data.get(selected).getView().setBackgroundColor( Color.TRANSPARENT);
			selected = -1;
		}	
		// new item
		else {
			if( selected != -1 ) {
				data.get(selected).getView().setBackgroundColor( Color.TRANSPARENT);
				data.get(selected).toggleExpandIou(false);
			}
				
			data.get(select).getView().setBackgroundColor( Color.parseColor( "#4c4c4c") );
			data.get(select).toggleExpandIou(true);
			selected = select; 
		}		
	}
	
	public int getSelected() { return selected; }
	
}