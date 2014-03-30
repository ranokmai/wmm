package models;

import java.util.ArrayList;
import java.util.Date;

public class Iou {

	public static ArrayList<String> ITEM_TYPES;
	
	public static void init_item_types() {
		ITEM_TYPES = new ArrayList<String>();
		ITEM_TYPES.add("None");
		ITEM_TYPES.add("Money");
		ITEM_TYPES.add("Item");
	}
	
	//required attributes
	String item_name;
	String contact_name;
	boolean is_a_contact;
	
	//optional attributes
	int item_type;
	boolean outbound;
	Date date_borrowed;
	Date date_due;
	double value;
	String pic_loc;
	String notes;
	
	//ctors
	public Iou() {
		Iou_blank_init();
	}
	
	public Iou(String item_name_, String contact_name_, boolean is_a_contact_) {
		Iou_blank_init();
		
		item_name = item_name_;
		contact_name = contact_name_;
		is_a_contact = is_a_contact_;
	}
	
	public Iou(String item_name_, String contact_name_, boolean is_a_contact_, 
			String item_type_, boolean outbound_, Date date_borrowed_, Date date_due_,
			double value_, String pic_loc_, String notes_) {
		
		item_name = item_name_;
		contact_name = contact_name_;
		is_a_contact = is_a_contact_;
		
		if (ITEM_TYPES.contains(item_type_)) {
			item_type = ITEM_TYPES.indexOf(item_type_);
		}
		else {
			item_type = 0;
		}
		
		outbound = outbound_;
		date_borrowed = date_borrowed_;
		date_due = date_due_;
		value = value_;
		pic_loc = pic_loc_;
		notes = notes_;
		
	}
	
	//ctor helper
	private void Iou_blank_init() {
		item_name = new String();
		contact_name = new String();
		is_a_contact = false;
		item_type = 0;
		outbound = false;
		date_borrowed = new Date();
		date_due = new Date(Long.MAX_VALUE);
		value = 0.0;
		pic_loc = new String();
		notes = new String();
	}
	
	boolean can_insert_into_db() {
		if (!(item_name.length() == 0) && !(contact_name.length() == 0) && (is_a_contact == false || is_a_contact == true) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//access functions
	String item_name() {return item_name;}
	String contact_name() {return contact_name;}
	boolean is_a_contact() {return is_a_contact;}
	
	String item_type() {return ITEM_TYPES.get(item_type);}
	boolean outbound() {return outbound;}
	Date date_borrowed() {return date_borrowed;}
	Date date_due() {return date_due;}
	double value() {return value;}
	String pic_loc() {return pic_loc;}
	String notes() {return notes;}
	
}
