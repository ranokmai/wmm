package models;

//A small data class for things to be displayed on contact summary page
//has the name, the total number of items exchanged, and the total value of all items
//can be split up into outbound/inbound trades later
public class ContactSummary {

	private String contact;
	private int items_exchanged;
	private double total_val;
	
	public ContactSummary(String contact_, int items_exchanged_, double total_val_) {
		contact = contact_;
		items_exchanged = items_exchanged_;
		total_val = total_val_;
	}
	
	public String contact() {return contact;}
	public int items_exchanged() {return items_exchanged;}
	public double total_val() {return total_val;}
	
}
