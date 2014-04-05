package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * A class that queries/inserts/updates for the db
 * 
 * db is static because there is only one database that should be used
 * IouDBManager ctor should be called once and then queries can be used as needed
 * may change to only create a writeable db once
 * 
 * Example use below:
 */

//Iou test1 = new Iou("Drinks", "Louis", true, "Money", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,20).getTime(), 13.21, "", "night out");
//Iou test2 = new Iou("DB Textbook", "Louis", true, "Item", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,30).getTime(), 80, "", "borrowed for semester");
//Iou test3 = new Iou("Cash", "jimmy", true, "Money", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,15).getTime(), 20, "", "quick buck");
//
////test.print();		
//
//Global.iou_db_mgr.insertIou(test1);
//Global.iou_db_mgr.insertIou(test2);
//Global.iou_db_mgr.insertIou(test3);
//test.print();

//test3.update_contact_name("Jimmy");
//test3.update_value(25.0);
//test.print();		
//Global.iou_db_mgr.updateIou(test);
//test.print();

//Log.i("db_test", "first query");		
//ArrayList<Iou> ious = Global.iou_db_mgr.get_ious_ordered_by_closest_due_date();
//for (int i = 0; i < ious.size(); i++) {
//	ious.get(i).print();
//}
//
//Log.i("db_test", "second query");
//ious = Global.iou_db_mgr.get_unordered_ious();
//for (int i = 0; i < ious.size(); i++) {
//	ious.get(i).print();
//}
//
//Log.i("db_test", "third query");
//ArrayList<String> contacts = Global.iou_db_mgr.get_contacts();
//for (int i = 0; i < contacts.size(); i++) {
//	Log.i("db_test", contacts.get(i));
//}
//
//Log.i("db_test", "fourth query");
//ArrayList<Iou> ious_of_contact = Global.iou_db_mgr.get_ious_from_contact(contacts.get(0));
//for (int i = 0; i < ious_of_contact.size(); i++) {
//	ious_of_contact.get(i).print();
//}

public class IouDBManager {
	
	private static IouDB db;
	private static SQLiteDatabase sqldb;
	private static String db_table_name = "ious";
	
	public IouDBManager(Context context) {
		db = new IouDB(context);
		sqldb = db.getWritableDatabase();
	}
	
	public static void reset_db() {
		db.clear_db(sqldb);
	}
	
	//Class that returns an ArrayList<Iou> of ious from cursor
	//used by some queries
	private static ArrayList<Iou> retrieve_ious(Cursor cursor) {
		ArrayList<Iou> ious = new ArrayList<Iou>();
		
		while (cursor.moveToNext()) {
			
			//i'm not sure if these are off by one or not
			//setting at not off by one because sqlplus isn't
			//minus one if run into error
			String i_name = cursor.getString(1);
			String c = cursor.getString(2);
			
			int is_c_i = cursor.getInt(3);
			boolean is_c = false;
			if (is_c_i == 1) {
				is_c = true;
			}
			
			String i_type = cursor.getString(4);
			
			int is_o_i = cursor.getInt(5);
			boolean is_o = false;
			if (is_o_i == 1) {
				is_o = true;
			}
			

			DateFormat df = new SimpleDateFormat(Global.date_format);
			
			String date_s = cursor.getString(6);
			Date b = Global.str_to_date(date_s);
			
			date_s = cursor.getString(7);
			Date d = Global.str_to_date(date_s);
			
			double val = cursor.getDouble(8);
			String pic = cursor.getString(9);
			String notes = cursor.getString(10);
			
			Iou temp = new Iou(i_name, c, is_c, i_type, is_o, b, d, val, pic, notes);
			temp.setDb_row_id(cursor.getLong(0));
			
			ious.add(temp);
		}
		
		cursor.close();
		
		return ious;
	}
	
	//retrieves all ious unordered
	public static ArrayList<Iou> get_unordered_ious() {
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of the shortest time to due date
	public static ArrayList<Iou> get_ious_ordered_by_closest_due_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious ORDER BY date_due ASC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious from a specific contact
	public static ArrayList<Iou> get_ious_from_contact(String contact) {

		//Cursor cursor = sqldb.query(true, db_table_name, null, "contact = " + contact, null, groupBy, having, orderBy, limit, cancellationSignal)
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE contact = ?", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all contacts as strings
	public static ArrayList<String> get_contacts() {

		Cursor cursor = sqldb.rawQuery("SELECT DISTINCT contact FROM ious", null);
		
		ArrayList<String> contacts = new ArrayList<String>();
		
		while (cursor.moveToNext()) {
			contacts.add(cursor.getString(0));
		}
		
		cursor.close();
		
		return contacts;
	}
	
	//retrieves ContactSummary for each contact
	public static ArrayList<ContactSummary> get_contact_summaries() {

		Cursor cursor = sqldb.rawQuery("SELECT DISTINCT contact, COUNT(*) as total_items, SUM(value) as total_val "
				+ "FROM ious ORDER BY contact DESC", null);
		
		ArrayList<ContactSummary> contact_summaries = new ArrayList<ContactSummary>();
		
		while (cursor.moveToNext()) {
			contact_summaries.add(new ContactSummary(cursor.getString(1), cursor.getInt(2), cursor.getDouble(3)));
		}
		
		cursor.close();
		
		return contact_summaries;
	}

	public void insertIou(Iou iou) {
		if (iou.can_insert_into_db()) {
			
			iou.setDb_row_id(sqldb.insert(db_table_name, null, iou.iou));
			
		}
		else {
			throw new IouDB_Error("Insertion Error");
		}
	}
	
	public void updateIou(Iou iou) {
		if (iou.getDb_row_id() > 0) {
			
			sqldb.update(db_table_name, iou.iou, "iou_id = ?", new String[] {iou.getDb_row_id().toString()});
			
		}
		else {
			throw new IouDB_Error("Update Error: not in db");
		}
	}
	
}
