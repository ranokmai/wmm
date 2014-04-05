package models;

import java.util.ArrayList;
import java.util.Date;

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
 * TODO: add one instance of ctor in program
 * 
 * TODO: inserts
 * TODO: updates
 * 
 */

public class IouDBManager {
	
	private static IouDB db;
	private static SQLiteDatabase sqldb;
	private String db_table_name = "ious";
	
	public IouDBManager(Context context) {
		db = new IouDB(context);
		sqldb = db.getWritableDatabase();
	}
	
	//Class that returns an ArrayList<Iou> of ious from cursor
	//used by some queries
	private static ArrayList<Iou> retrieve_ious(Cursor cursor) {
		ArrayList<Iou> ious = new ArrayList<Iou>();
		
		while (cursor.moveToNext()) {
			
			//i'm not sure if these are off by one or not
			//setting at not off by one because sqlplus isn't
			//minus one if run into error
			String i_name = cursor.getString(2);
			String c = cursor.getString(3);
			
			int is_c_i = cursor.getInt(4);
			boolean is_c = false;
			if (is_c_i == 1) {
				is_c = true;
			}
			
			String i_type = cursor.getString(5);
			
			int is_o_i = cursor.getInt(6);
			boolean is_o = false;
			if (is_o_i == 1) {
				is_o = true;
			}
			
			int date_i = cursor.getInt(7);
			Date b = new Date(date_i);
			date_i = cursor.getInt(8);
			Date d = new Date(date_i);
			
			double val = cursor.getDouble(9);
			String pic = cursor.getString(10);
			String notes = cursor.getString(11);
			
			ious.add(new Iou(i_name, c, is_c, i_type, is_o, b, d, val, pic, notes));
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

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE contact = '?'", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all contacts as strings
	public static ArrayList<String> get_contacts() {

		Cursor cursor = sqldb.rawQuery("SELECT DISTINCT contact FROM ious", null);
		
		ArrayList<String> contacts = new ArrayList<String>();
		
		while (cursor.moveToNext()) {
			contacts.add(cursor.getString(1));
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
