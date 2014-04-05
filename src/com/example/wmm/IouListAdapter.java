package com.example.wmm;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import models.IouItem;
import models.Global;
import models.Iou;

 
public class IouListAdapter extends BaseAdapter {
	private ArrayList<IouItem> data;
 
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
}
