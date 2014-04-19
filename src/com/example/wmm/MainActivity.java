package com.example.wmm;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import models.ContactSummary;
import preferences.SettingsFragment;
import models.Global;
import models.Iou;
import models.IouDBManager;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private CharSequence app_title;
	
	// Navigation drawer variables
	private CharSequence navigation_title;
	private String[] navigation_titles;
	private TypedArray navigation_icons;
	private DrawerLayout navigation_layout;
	private ListView navigation_list;
	private NavigationDrawerAdapter navigation_adapter;
	private ArrayList<models.NavigationItem> navigation_items;
	private ActionBarDrawerToggle navigation_toggle;
	public Fragment listFrag;
	private IouListFragment iouListFragment = null;

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
		
		/*IouDBManager.reset_db();
		for(int i=0; i<5; i++) {
			
			Iou test1 = new Iou("Drinks", "Louis", true, "Money", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,20).getTime(), 13.21, "", "night out");
			Iou test2 = new Iou("Drinks", "Jimmy Blanchard", true, "Money", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,20).getTime(), 15.00, "", "night out");
			Iou test3 = new Iou("GTA V", "Jimmy Blanchard", true, "Item", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,25).getTime(), 60.00, "", "GTA 5");
			Iou test4 = new Iou("Great American Challenge", "Galen Gong", true, "Item", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,25).getTime(), 50.00, "", ":D");
			
			Global.iou_db_mgr.insertIou(test1);
			Global.iou_db_mgr.insertIou(test2);
			Global.iou_db_mgr.insertIou(test3);
			Global.iou_db_mgr.insertIou(test4);
		}*/
		
		ArrayList<ContactSummary> cs = Global.iou_db_mgr.get_contact_summaries();
		
		for (int i = 0; i < cs.size(); i++) {
			cs.get(i).print();
		}

	}
	
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// display view for selected nav drawer item
			display_fragment(position);
		}
	}
	
	public void display_fragment(int position) {
		navigation_icons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		
        // update the main content by replacing fragments
        switch (position) {
        case 0:
        	listFrag = new IouListFragment();
        	iouListFragment = (IouListFragment) listFrag;
            break;
        case 1:
        	listFrag = new ContactsFragment();
        	iouListFragment = null;
            break;
        case 4:
        	listFrag = new SettingsFragment();
        	iouListFragment = null;
        	break;
        case 5:
        	listFrag = new AboutFragment();
        	iouListFragment = null;
        	break;
        	
        default:
            break;
        }    
        
    	// preference fragment functions differently than fragment 
        if (listFrag != null || position == 4) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, listFrag).commit();
 
            // Update the title and close the drawer
            navigation_list.setItemChecked(position, true);
            navigation_list.setSelection(position);
            setTitle(navigation_titles[position]);
            if (position == 0){
            	getActionBar().setIcon(R.drawable.ic_logo);
            } else {
            	getActionBar().setIcon(navigation_icons.getResourceId(position, -1));
            }
            navigation_layout.closeDrawer(navigation_list);
            
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error creating fragment from navigation drawer.");
        }
    }
	
	public void deleteIouButtonListener(View v) {
		if(iouListFragment != null)
			iouListFragment.deleteSelectedIOU();
	}
	
	public void editIouButtonListener(View v){		
		if(iouListFragment != null)
			iouListFragment.editSelectedIOU();
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
        if( item.getItemId() == R.id.action_settings ) {
            return true;
        }
        else { 
            return super.onOptionsItemSelected(item);
        }
	}
	
	@Override
    public void setTitle(CharSequence title) {
        app_title = title;
        getActionBar().setTitle(app_title);
    }
}
