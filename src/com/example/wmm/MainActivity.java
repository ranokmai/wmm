package com.example.wmm;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import models.Global;
import models.Iou;
import models.IouDBManager;
import models.IouItem;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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
		navigation_list.setOnItemClickListener(new SlideMenuClickListener());
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

		if (savedInstanceState == null) {
            // on first time display view for first nav item
            display_fragment(0);
        }
		
//		for(int i=0; i<5; i++) {
//			Iou test1 = new Iou("Drinks", "Louis", true, "Money", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,20).getTime(), 13.21, "", "night out");
//			
//			Global.iou_db_mgr.insertIou(test1);
//		}
	}
	
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// display view for selected nav drawer item
			display_fragment(position);
		}
	}
	
	private void display_fragment(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new IouListFragment();
            break;
        case 1:
            fragment = new ContactsFragment();
            break;
 
        default:
            break;
        }
 
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
 
            // Update the title and close the drawer
            navigation_list.setItemChecked(position, true);
            navigation_list.setSelection(position);
            setTitle(navigation_titles[position]);
            navigation_layout.closeDrawer(navigation_list);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error creating fragment from navigation drawer.");
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If nav drawer is opened, hide the action items
        boolean drawerOpen = navigation_layout.isDrawerOpen(navigation_list);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigation_toggle.syncState();
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
