package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;

/*Iou class
 * contains basic information about an exchange 
 * 
 * PROGRAM SHOULD CALL init_item_types() ONCE AT THE BEGINNING OF THE PROGRAM
 * THIS WILL ALLOW IOU TO FUNCTION PROPERLY
 * 
 * TODO: above all-caps item
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
	
	public static void init_item_types() {
		ITEM_TYPES = new ArrayList<String>();
		ITEM_TYPES.add("None");
		ITEM_TYPES.add("Money");
		ITEM_TYPES.add("Item");
	}
	
	private ContentValues iou;
	
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
	private static String value = "value";
	private static String pic_loc = "picture_loc";
	private static String notes = "notes";
	
	//for date formatting:
	private static String date_format = "EEE MMM dd kk:mm:ss zzz yyyy";
	
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
		iou.put(date_borrowed, date_borrowed_.toString());
		iou.put(date_due, date_due_.toString());
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
		iou.put(date_borrowed, new Date().toString());
		iou.put(date_due, new Date(Long.MAX_VALUE).toString());
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
	public 	boolean is_a_contact() {return iou.getAsBoolean(is_a_contact);}
	
	public String item_type() {return ITEM_TYPES.get(iou.getAsInteger(item_type));}
	public boolean outbound() {return iou.getAsBoolean(outbound);}
	
	public Date date_borrowed() {
		
		DateFormat df = new SimpleDateFormat(date_format);
		Date r = new Date();
		
		try {
			r = df.parse(iou.getAsString(date_borrowed));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	public Date date_due()  {
		
		DateFormat df = new SimpleDateFormat(date_format);
		Date r = new Date(Long.MAX_VALUE);
		
		try {
			r = df.parse(iou.getAsString(date_due));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	public double value() {return iou.getAsDouble(value);}
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
	public void update_date_borrowed(Date date_borrowed_) {iou.remove(date_borrowed); iou.put(date_borrowed, date_borrowed_.toString());}
	public void update_date_due(Date date_due_) {iou.remove(date_due); iou.put(date_due, date_due_.toString());}
	public void update_value(double value_) {iou.remove(value); iou.put(value, value_);}
	public void update_pic_loc(String pic_loc_) {iou.remove(pic_loc); iou.put(pic_loc, pic_loc_);}
	public void update_notes(String notes_) {iou.remove(notes); iou.put(notes, notes_);}
}
