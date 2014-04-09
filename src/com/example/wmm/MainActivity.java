package com.example.wmm;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import models.Global;
import models.Iou;
import models.IouItem;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private CharSequence app_title;
	private ArrayList<IouItem> iouItems;
	private ArrayList<Iou> ious;
	private ListView mainListView;
	private IouListAdapter adapter;
	
	// Navigation drawer variables
	private CharSequence navigation_title;
	private String[] navigation_titles;
	private TypedArray navigation_icons;
	private DrawerLayout navigation_layout;
	private ListView navigation_list;
	private NavigationDrawerAdapter navigation_adapter;
	private ArrayList<models.NavigationItem> navigation_items;
	private ActionBarDrawerToggle navigation_toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer);
		app_title = "Where's My Money";
		navigation_title = app_title;
				
		// Setup the navigation drawer
		navigation_titles = getResources().getStringArray(R.array.nav_drawer_items);
		navigation_icons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		navigation_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
		navigation_list = (ListView)findViewById(R.id.nav_drawer);
		navigation_items = new ArrayList<models.NavigationItem>();
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		
		// Add navigation links
		navigation_items.add(new models.NavigationItem(navigation_titles[0],
			navigation_icons.getResourceId(0, -1)));
		navigation_items.add(new models.NavigationItem(navigation_titles[1],
			navigation_icons.getResourceId(1, -1)));
		navigation_items.add(new models.NavigationItem(navigation_titles[2],
			navigation_icons.getResourceId(2, -1)));
		navigation_items.add(new models.NavigationItem(navigation_titles[3],
			navigation_icons.getResourceId(3, -1)));
		navigation_items.add(new models.NavigationItem(navigation_titles[4],
			navigation_icons.getResourceId(4, -1)));
		navigation_items.add(new models.NavigationItem(navigation_titles[5],
			navigation_icons.getResourceId(5, -1)));
		navigation_icons.recycle();
		
		// Populate navigation drawer
		navigation_adapter = new NavigationDrawerAdapter(getApplicationContext(),
			navigation_items);
		navigation_list.setAdapter(navigation_adapter);
        navigation_toggle = new ActionBarDrawerToggle(this, navigation_layout,
        		R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name
        		){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(app_title);
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(navigation_title);
                invalidateOptionsMenu();
            }
        };
        navigation_layout.setDrawerListener(navigation_toggle);
		
		// Database setup
        
		Global.setup_db_mgr(getApplicationContext());
		Iou.init_item_types();
		
		Global.iou_db_mgr.reset_db();
		
		for(int i=0; i<5; i++) {
			Iou test1 = new Iou("Drinks", "Louis", true, "Money", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,20).getTime(), 13.21, "", "night out");
			
			Global.iou_db_mgr.insertIou(test1);
		}
		
		mainListView = (ListView) findViewById(R.id.main_list_view);
		
		updateListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = navigation_layout.isDrawerOpen(navigation_list);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
	public void open_contacts(View view){
		
		getFragmentManager().beginTransaction().replace( android.R.id.content, new ContactsFragment()).commit();
		
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigation_toggle.syncState();
    }

	// Update the main view with the Items in iouItems
	private void updateListView() {
		
		//make sure initialized
		iouItems = new ArrayList<IouItem>();
		
		//add current iou items
		ious = Global.iou_db_mgr.get_ious_ordered_by_closest_due_date();
		
		// Fill iouItems with db data
		for (int i = 0; i < ious.size(); i++) {
			iouItems.add(new IouItem(this, ious.get(i)));
		}
		
        adapter = new IouListAdapter(iouItems);
        mainListView.setAdapter(adapter);
        
        mainListView.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				((IouListAdapter)parent.getAdapter()).setSelected(position);
				
			}
        });
        
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navigation_toggle.onConfigurationChanged(newConfig);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (navigation_toggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		// Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}

	@Override
    public void setTitle(CharSequence title) {
        app_title = title;
        getActionBar().setTitle(app_title);
    }
}
