package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.util.Log;

/*Iou class
 * contains basic information about an exchange 
 * 
 * required attributes are always:
 * the name of the item 
 * the name of the person exchanged
 * and if the person is actually a contact in the person's phone
 * 
 * nothing is controlled here except if the iou can itself be inserted into the db
 * 
 * values are all private, but access functions are made available for everything
 * as are update functions
 * 
 * three constructors:
 * empty
 * minimum (required attributes)
 * everything
 * 
 * TODO: create a way for new item_types to be added to options (from file -- xml?)
 * 
 */
public class Iou {
	
	public static ArrayList<String> ITEM_TYPES;
	
	/**
	 * @return the db_row_id
	 */
	public Long getDb_row_id() {
		return db_row_id;
	}

	/**
	 * @param db_row_id the db_row_id to set
	 */
	public void setDb_row_id(long db_row_id) {
		this.db_row_id = db_row_id;
	}

	public static void init_item_types() {
		ITEM_TYPES = new ArrayList<String>();
		ITEM_TYPES.add("None");
		ITEM_TYPES.add("Money");
		ITEM_TYPES.add("Item");
	}
	
	public ContentValues iou;
	private long db_row_id = -1;
	
	//keys for attributes:
	
	//required attributes
	private static String item_name = "item_name";
	private static String contact_name = "contact";
	private static String is_a_contact = "is_contact";
	
	//optional attributes
	private static String item_type = "item_type";
	private static String outbound = "outbound";
	private static String date_borrowed = "date_borrowed";
	private static String date_due = "date_due";
	private static String date_completed = "date_completed";
	private static String value = "value";
	private static String pic_loc = "picture_loc";
	private static String notes = "notes";
	
	//ctors
	public Iou() {
		Iou_blank_init();
	}
	
	public Iou(String item_name_, String contact_name_, boolean is_a_contact_) {
		Iou_blank_init();
		
		iou.put(item_name, item_name_);
		iou.put(contact_name,contact_name_);
		iou.put(is_a_contact,is_a_contact_);
	}
	
	public Iou(String item_name_, String contact_name_, boolean is_a_contact_, 
			String item_type_, boolean outbound_, Date date_borrowed_, Date date_due_,
			double value_, String pic_loc_, String notes_) {
		
		iou = new ContentValues();
		
		iou.put(item_name, item_name_);
		iou.put(contact_name,contact_name_);
		iou.put(is_a_contact,is_a_contact_);
		
		if (ITEM_TYPES.contains(item_type_)) {
			iou.put(item_type, ITEM_TYPES.indexOf(item_type_));
		}
		else {
			iou.put(item_type, 0);
		}
		
		iou.put(outbound, outbound_);
		iou.put(date_borrowed, Global.date_to_str(date_borrowed_));
		iou.put(date_due, Global.date_to_str(date_due_));
		iou.put(date_completed, Global.date_to_str(Global.DATE_MAX));
		iou.put(value, value_);
		iou.put(pic_loc, pic_loc_);
		iou.put(notes, notes_);
		
	}
	
	//ctor helper
	private void Iou_blank_init() {
		
		iou = new ContentValues();
		
		iou.put(item_name, new String());
		iou.put(contact_name, new String());
		iou.put(is_a_contact, false);
		iou.put(item_type, 0);
		iou.put(outbound, false);
		iou.put(date_borrowed, Global.date_to_str(new Date()));
		iou.put(date_due, Global.date_to_str(Global.DATE_MAX));
		iou.put(date_completed, Global.date_to_str(Global.DATE_MAX));
		iou.put(value, 0.0);
		iou.put(pic_loc, new String());
		iou.put(notes, new String());
		
	}
	
	//actually contains required variables
	//currently only needed if iou is created empty and does not obtain required variables
	public boolean can_insert_into_db() {
		if (!(iou.getAsString(item_name).length() == 0) 
				&& !(iou.getAsString(contact_name).length() == 0) 
				&& (iou.getAsBoolean(is_a_contact) == false || iou.getAsBoolean(is_a_contact) == true) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//access functions
	public String item_name() {return iou.getAsString(item_name);}
	public String contact_name() {return iou.getAsString(contact_name);}
	public Boolean is_a_contact() {return iou.getAsBoolean(is_a_contact);}
	
	public String item_type() {return ITEM_TYPES.get(iou.getAsInteger(item_type));}
	public Boolean outbound() {return iou.getAsBoolean(outbound);}
	
	public Date date_borrowed() {
		
		DateFormat df = new SimpleDateFormat(Global.date_format, Locale.US);
		Date r = new Date();
		
		try {
			r = df.parse(iou.getAsString(date_borrowed));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	public Date date_due()  {
		
		DateFormat df = new SimpleDateFormat(Global.date_format, Locale.US);
		Date r = Global.DATE_MAX;
		
		try {
			r = df.parse(iou.getAsString(date_due));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return r;
	}
	
	public Date date_completed() {
		
		DateFormat df = new SimpleDateFormat(Global.date_format, Locale.US);
		Date r = Global.DATE_MAX;
		
		try {
			r = df.parse(iou.getAsString(date_completed));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return r;
		
	}
	
	public Double value() {return iou.getAsDouble(value);}
	public String pic_loc() {return iou.getAsString(pic_loc);}
	public String notes() {return iou.getAsString(notes);}
	
	//update functions
	public void update_item_name(String item_name_) {iou.remove(item_name); iou.put(item_name, item_name_);}
	public void update_contact_name(String contact_name_) {iou.remove(contact_name); iou.put(contact_name, contact_name_);}
	public void update_is_a_contact(boolean is_a_contact_) {iou.remove(is_a_contact); iou.put(is_a_contact, is_a_contact_);}
	
	public void update_item_type(String item_type_) {
		
		iou.remove(item_type);
		
		if (ITEM_TYPES.contains(item_type_)) {
			iou.put(item_type, ITEM_TYPES.indexOf(item_type_));
		}
		else {
			iou.put(item_type, 0);
		}
	
	}
	public void update_outbound(boolean outbound_) {iou.remove(outbound); iou.put(outbound, outbound_);}
	public void update_date_borrowed(Date date_borrowed_) {iou.remove(date_borrowed); iou.put(date_borrowed, Global.date_to_str(date_borrowed_));}
	public void update_date_due(Date date_due_) {iou.remove(date_due); iou.put(date_due, Global.date_to_str(date_due_));}
	public void update_date_completed(Date date_completed_) {iou.remove(date_completed); iou.put(date_completed, Global.date_to_str(date_completed_));}
	public void update_value(double value_) {iou.remove(value); iou.put(value, value_);}
	public void update_pic_loc(String pic_loc_) {iou.remove(pic_loc); iou.put(pic_loc, pic_loc_);}
	public void update_notes(String notes_) {iou.remove(notes); iou.put(notes, notes_);}
	
	public void print() {
		
		String s = new String();
		
		s += "Item Id: " + getDb_row_id().toString() + ",  ";
		s += "Item name: " + item_name() + ", ";
		s += "Contact name: " + contact_name() + ",  ";
		s += "Is a contact?: " + is_a_contact() + ",  ";
		s += "Item type: " + item_type() + ",  ";
		s += "Outbound?: " + outbound() + ",  ";
		s += "Date borrowed: " + Global.date_to_str(date_borrowed()) + ",  ";
		s += "Date due: " + Global.date_to_str(date_due()) + ",  ";
		s += "Date completed: " + Global.date_to_str(date_completed()) + ",  ";
		s += "Value: " + value() + ",  ";
		s += "Picture location: " + pic_loc() + ",  ";
		s += "Notes: " + notes();
		
		Log.i("iou_output", s);
	}
	
}