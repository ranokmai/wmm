package com.example.wmm;

import models.NavigationItem;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class NavigationDrawerAdapter extends BaseAdapter {
     
    private Context context;
    private ArrayList<NavigationItem> navDrawerItems;
     
    public NavigationDrawerAdapter(Context context, ArrayList<NavigationItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {      
        return navDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.navigation_list_item, null);
        }
        
        NavigationItem temp_nav_item = (NavigationItem)this.getItem(position);
        //if (temp_nav_item.getType() == NavigationItem.ITEM_TYPE){
        	ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
              
            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());       
            txtTitle.setText(navDrawerItems.get(position).getTitle());
        //} else {
        	
        //}
         
        return convertView;
    }
 
}