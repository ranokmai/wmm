package com.example.wmm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import models.ContactSummary;
import preferences.SettingsFragment;
import models.Global;
import models.Iou;
import models.IouDBManager;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
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
	public Fragment listFrag = null;
	private IouListFragment iouListFragment = null;
	private StatisticsFragment statisticsFragment = null;
	private int selected_fragment = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer);
		app_title = "Where's My Money?!";
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
			public void onDrawerClosed(View view){
				if (selected_fragment == 0){
					getActionBar().setDisplayShowTitleEnabled(false);
					getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
					invalidateOptionsMenu();
				} else {
					getActionBar().setDisplayShowTitleEnabled(true);
					getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					setTitle(navigation_titles[selected_fragment]);
					invalidateOptionsMenu();
				}
			}

			public void onDrawerOpened(View drawerView){
				getActionBar().setDisplayShowTitleEnabled(true);
				getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				getActionBar().setTitle(navigation_title);
				invalidateOptionsMenu();
			}
		};
		navigation_layout.setDrawerListener(navigation_toggle);

		Global.main_context = getApplicationContext();
		
		// Database setup
		Global.setup_db_mgr(getApplicationContext());
		Iou.init_item_types();
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			display_fragment(0);
		}

		IouDBManager.reset_db();
		for(int i=0; i<1; i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
			String dateInString = "01-04-2014";
			Date date1 = null;
			Date date2 = null;
			Date date3 = null;
			Date date4 = null;
			try {
				date1 = sdf.parse(dateInString);
				dateInString = "10-04-2014";
				date2 = sdf.parse(dateInString);
				dateInString = "15-04-2014";
				date3 = sdf.parse(dateInString);
				dateInString = "21-04-2014";
				date4 = sdf.parse(dateInString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iou test1 = new Iou("Drinks", "Louis", true, "Money", true, date1, new GregorianCalendar(2014,Global.APR,20).getTime(), 10.21, "", "night out", new GregorianCalendar(2014,Global.APR,21).getTime());
			Iou test5 = new Iou("Camera", "Louis", true, "Item", true, date1, new GregorianCalendar(2014,Global.APR,20).getTime(), 5.00, "", "photo shoot", new GregorianCalendar(2014,Global.APR,21).getTime());
			Iou test2 = new Iou("Drinks", "Jimmy Blanchard", true, "Money", true, date2, new GregorianCalendar(2014,Global.APR,20).getTime(), 15.00, "", "night out", new GregorianCalendar(2014,Global.APR,21).getTime());
			Iou test3 = new Iou("GTA V", "Connor Lawrence", true, "Money", true, date3, new GregorianCalendar(2014,Global.APR,25).getTime(), 60.00, "", "GTA 5", new GregorianCalendar(2014,Global.APR,21).getTime());
			Iou test4 = new Iou("Bidet", "Galen Gong", true, "Money", true, date4, new GregorianCalendar(2014,Global.APR,25).getTime(), -50.00, "", ":D", new GregorianCalendar(2014,Global.APR,21).getTime());
	
			Global.iou_db_mgr.insertIou(test1);
			Global.iou_db_mgr.insertIou(test2);
			Global.iou_db_mgr.insertIou(test3);
			Global.iou_db_mgr.insertIou(test4);
			Global.iou_db_mgr.insertIou(test5);
		}

		ArrayList<ContactSummary> cs = Global.iou_db_mgr.get_contact_summaries();

		for (int i = 0; i < cs.size(); i++) {
			cs.get(i).print();
		}
		
		ArrayList<Iou> to_be_reminded = Global.iou_db_mgr.get_ious_with_reminders_before_and_of_date();
		
		for (int i = 0; i < to_be_reminded.size(); i++) {
			String text_content = Global.text_content(to_be_reminded.get(i));

			String contact_number = Global.contact_number(to_be_reminded.get(i).contact_name(), getApplicationContext());
			
			if (contact_number.equals("Unsaved")) {
				
			}
			else {
			    //popup dialog to ask if want to send sms reminder
				
				//if yes to send reminder
				Uri uri = Uri.parse("smsto:" + contact_number); 
			    Intent it = new Intent(Intent.ACTION_SENDTO, uri); 
			    it.putExtra("sms_body", text_content); 
			    startActivity(it); 
			}
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
		selected_fragment = position;
		switch (position) {
		case 0:
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			invalidateOptionsMenu();
			listFrag = new IouListFragment();
			iouListFragment = (IouListFragment) listFrag;
			break;
		case 1:
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			invalidateOptionsMenu();
			listFrag = new ContactsFragment();
			break;
		case 3:
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			invalidateOptionsMenu();
			listFrag = new StatisticsFragment();
			statisticsFragment = (StatisticsFragment) listFrag;
			break;
		case 4:
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			invalidateOptionsMenu();
			listFrag = new SettingsFragment();
			break;
		case 5:
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			invalidateOptionsMenu();
			listFrag = new AboutFragment();
			break;

		default:
			break;
		}    

		// preference fragment functions differently than fragment 
		if (listFrag != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, listFrag).commit();

			// Update the title and close the drawer
			navigation_list.setItemChecked(position, true);
			navigation_list.setSelection(position);

			if (position == 0){
				getActionBar().setIcon(R.drawable.ic_logo);
			} else {
				setTitle(navigation_titles[position]);
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

	public void lastMonthListener(View v){		
		if(statisticsFragment != null)
			statisticsFragment.setMonthChart();
	}

	public void perContactListener(View v){		
		if(statisticsFragment != null)
			statisticsFragment.setContactChart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If nav drawer is opened, hide the action items
		boolean drawerOpen = navigation_layout.isDrawerOpen(navigation_list);

		menu.findItem(R.id.action_open_settings).setVisible(!drawerOpen);
		if (selected_fragment == 0 && !drawerOpen){
			menu.findItem(R.id.action_new_iou).setVisible(true);
		} else {
			menu.findItem(R.id.action_new_iou).setVisible(false);
		}

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
	protected void onPostResume() {
		super.onPostResume();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (navigation_toggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle action bar actions click
		int itemId = item.getItemId();
		if(itemId == R.id.action_new_iou) {
			iouListFragment.addNewIou();
			return true;
		}
		else if(itemId == R.id.action_open_settings) {
			Fragment fragment = new SettingsFragment();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

			// Update the title and close the drawer
			navigation_list.setItemChecked(4, true);
			navigation_list.setSelection(4);
			setTitle(navigation_titles[4]);
			getActionBar().setIcon(navigation_icons.getResourceId(4, -1));
			navigation_layout.closeDrawer(navigation_list);

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
