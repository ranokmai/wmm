package models;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
//Iou test2 = new Iou("DB Textbook", "Louis", true, "Item", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,30).getTime(), (double)80, "", "borrowed for semester");
//Iou test3 = new Iou("Cash", "jimmy", true, "Money", true, new GregorianCalendar().getTime(), new GregorianCalendar(2014,Global.APR,15).getTime(), (double)20, "", "quick buck");
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
//Global.iou_db_mgr.updateIou(testX);
//test.print();

//Log.i("db_test", "first query");		
//ArrayList<Iou> ious = Global.iou_db_mgr.get_ious_ordered_by_closest_due_date();
//for (int i = 0; i < ious.size(); i++) {
//	ious.get(i).print();
//}
//
//Log.i("db_test", "second query");
//ious = Global.iou_db_mgr.get_ious_unordered();
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
//ArrayList<Iou> ious_of_contact = Global.iou_db_mgr.get_contact_ious_unordered(contacts.get(0));
//for (int i = 0; i < ious_of_contact.size(); i++) {
//	ious_of_contact.get(i).print();
//}

public class IouDBManager {
		
	private static IouDB db;
	private static SQLiteDatabase sqldb;
	private static String db_table_name = "ious";
	private static String archive_table_name = "archived_ious";
	
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
			
			String date_s = cursor.getString(6);
			Date b = Global.str_to_date(date_s);
			
			date_s = cursor.getString(7);
			Date d = Global.str_to_date(date_s);
			
			Double val = Double.parseDouble(cursor.getString(9));
			String pic = cursor.getString(10);
			String notes = cursor.getString(11);
			
			Iou temp = new Iou(i_name, c, is_c, i_type, is_o, b, d, val, pic, notes);
			temp.setDb_row_id(cursor.getLong(0));
			
			ious.add(temp);
		}
		
		cursor.close();
		
		return ious;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//BASIC DB QUERIES FOR ACTIVE IOUS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//retrieves all ious unordered
	public ArrayList<Iou> get_ious_unordered() {
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of the shortest time to due date
	public ArrayList<Iou> get_ious_ordered_by_closest_due_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious ORDER BY date_due ASC", null);
		
		return retrieve_ious(cursor);
	}	
	
	//retrieves all ious in order of chronological time
	public ArrayList<Iou> get_ious_ordered_by_earliest_loan_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious ORDER BY date_borrowed ASC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of highest to lowest value
	public ArrayList<Iou> get_ious_ordered_by_value_desc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious ORDER BY value DESC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of lowest to highest value
	public ArrayList<Iou> get_ious_ordered_by_value_asc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious ORDER BY value ASC", null);
		
		return retrieve_ious(cursor);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//BASIC DB QUERIES FOR ACTIVE IOUS OUTGOING
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//retrieves all ious unordered
	public ArrayList<Iou> get_outgoing_ious_unordered() {
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 1", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of the shortest time to due date
	public ArrayList<Iou> get_outgoing_ious_ordered_by_closest_due_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 1 ORDER BY date_due ASC", null);
		
		return retrieve_ious(cursor);
	}	
	
	//retrieves all ious in order of chronological time
	public ArrayList<Iou> get_outgoing_ious_ordered_by_earliest_loan_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 1 ORDER BY date_borrowed ASC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of highest to lowest value
	public ArrayList<Iou> get_outgoing_ious_ordered_by_value_desc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 1 ORDER BY value DESC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of lowest to highest value
	public ArrayList<Iou> get_outgoing_ious_ordered_by_value_asc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 1 ORDER BY value ASC", null);
		
		return retrieve_ious(cursor);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//BASIC DB QUERIES FOR ACTIVE IOUS INCOMING
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//retrieves all ious unordered
	public ArrayList<Iou> get_incoming_ious_unordered() {
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 0", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of the shortest time to due date
	public ArrayList<Iou> get_incoming_ious_ordered_by_closest_due_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 0 ORDER BY date_due ASC", null);
		
		return retrieve_ious(cursor);
	}	
	
	//retrieves all ious in order of chronological time
	public ArrayList<Iou> get_incoming_ious_ordered_by_earliest_loan_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 0 ORDER BY date_borrowed ASC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of highest to lowest value
	public ArrayList<Iou> get_incoming_ious_ordered_by_value_desc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 0 ORDER BY value DESC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of lowest to highest value
	public ArrayList<Iou> get_incoming_ious_ordered_by_value_asc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE outbound = 0 ORDER BY value ASC", null);
		
		return retrieve_ious(cursor);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//DB QUERIES FOR SPECIFIC CONTACT IOUS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//retrieves all ious, unordered from a specific contact
	public ArrayList<Iou> get_contact_ious_unordered (String contact) {
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE contact = ?", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of the shortest time to due date
	public ArrayList<Iou> get_contact_ious_ordered_by_closest_due_date(String contact) {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE contact = ? ORDER BY date_due ASC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}	
	
	//retrieves all ious in order of chronological time
	public ArrayList<Iou> get_contact_ious_ordered_by_earliest_loan_date(String contact) {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE contact = ? ORDER BY date_borrowed ASC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of highest to lowest value
	public ArrayList<Iou> get_contact_ious_ordered_by_value_desc(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE contact = ? ORDER BY value DESC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all ious in order of lowest to highest value
	public ArrayList<Iou> get_contact_ious_ordered_by_value_asc(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM ious WHERE contact = ? ORDER BY value ASC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//get number of active loans between contact
	public Integer get_contact_num_active_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM ious WHERE contact = ?", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		return num;
	}
	
	//get number of arhcived loans between contact
	public Integer get_contact_num_archived_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM archived_ious WHERE contact = ?", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		return num;
	}
	
	//get number of active ious to contact
	public Integer get_contact_num_outbound_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM ious WHERE contact = ? AND outbound = 1", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		return num;
	}
	
	//get number of active ious from contact
	public Integer get_contact_num_inbound_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM ious WHERE contact = ? AND outbound = 0", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		return num;
	}
	
	//get number of money loans to contact
	public Integer get_contact_num_outbound_money_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM ious WHERE contact = ? AND outbound = 1 AND item_type = 'Money'", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		return num;
	}
	
	//get number of money loans from contact
	public Integer get_contact_num_inbound_money_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM ious WHERE contact = ? AND outbound = 0 AND item_type = 'Money'", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		return num;
	}
	
	//get number of non-money loans to contact
	public Integer get_contact_num_outbound_item_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM ious WHERE contact = ? AND outbound = 1 AND item_type = 'Item'", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		return num;
	}
	
	//get number of non-money loans from contact
	public Integer get_contact_num_inbound_item_ious(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT COUNT(*) FROM ious WHERE contact = ? AND outbound = 0 AND item_type = 'Item'", new String[] {contact});
		cursor.moveToNext();
		int num = cursor.getInt(0);
		
		return num;
	}
	
	//get total money lent out
	public Double get_contact_total_value_lent_out(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT value FROM ious WHERE contact = ? AND outbound = 1", new String[] {contact});
		double num = 0;
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		return num;
	}
	
	//get total money that is owed to contact
	public Double get_contact_total_value_owed_to(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT value FROM ious WHERE contact = ? AND outbound = 0", new String[] {contact});
		double num = 0;
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		return num;
	}
	
	//get total value of money loans lent out
	public Double get_contact_total_money_value_lent(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT value FROM ious WHERE contact = ? AND outbound = 1 AND item_type = 'Money'", new String[] {contact});
		double num = 0;
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		return num;
	}
	
	//get total value of money owed to contact
	public Double get_contact_total_money_value_owed_to(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT value FROM ious WHERE contact = ? AND outbound = 0 AND item_type = 'Money'", new String[] {contact});
		double num = 0;
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		return num;
	}
	
	//get total value of item loans lent out
	public Double get_contact_total_item_value_lent(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT value FROM ious WHERE contact = ? AND outbound = 1 AND item_type = 'Item'", new String[] {contact});
		double num = 0;
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		return num;
	}
	
	//get total value of item owed to contact
	public Double get_contact_total_item_value_owed_to(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT value FROM ious WHERE contact = ? AND outbound = 0 AND item_type = 'Item'", new String[] {contact});
		double num = 0;
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		return num;
	}
	
	//get total value traded between contacts across time
	public Double get_contact_historical_total_value(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT value FROM ious WHERE contact = ?", new String[] {contact});
		double num = 0;
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		cursor = sqldb.rawQuery("SELECT value FROM archived_ious WHERE contact = ?", new String[] {contact});
		
		while (cursor.moveToNext()) {
			num += Double.parseDouble(cursor.getString(0));
		}
		
		cursor.close();
		
		return num;
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//OTHER DB QUERIES
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//retrieves all contacts as strings
	public ArrayList<String> get_contacts() {

		Cursor cursor = sqldb.rawQuery("SELECT DISTINCT contact FROM ious", null);
		
		ArrayList<String> contacts = new ArrayList<String>();
		
		while (cursor.moveToNext()) {
			contacts.add(cursor.getString(0));
		}
		
		cursor.close();
		
		return contacts;
	}
	
	//retrieves ContactSummary for each contact
	public ArrayList<ContactSummary> get_contact_summaries() {

		Cursor cursor = sqldb.rawQuery("SELECT DISTINCT contact, COUNT(*) as total_items, SUM(value) as total_val "
				+ "FROM ious GROUP BY contact ORDER BY contact DESC", null);
		
		ArrayList<ContactSummary> contact_summaries = new ArrayList<ContactSummary>();
		
		while (cursor.moveToNext()) {
			contact_summaries.add(new ContactSummary(cursor.getString(0), cursor.getInt(1), cursor.getDouble(2)));
		}
		
		cursor.close();
		
		return contact_summaries;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//MOVE IOU BETWEEN ACTIVE AND ARCHIVED TABLES
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void add_iou_to_archive(Iou iou) {
				
		sqldb.delete(db_table_name, "item_id = ?", new String[] {iou.getDb_row_id().toString()});
		
		iou.update_date_completed(new Date());
		
		iou.setDb_row_id(sqldb.insert(archive_table_name, null, iou.iou));
		
	}
	
	public void remove_iou_from_archive(Iou iou) {
		
		sqldb.delete(archive_table_name, "item_id = ?", new String[] {iou.getDb_row_id().toString()});
		
		iou.update_date_completed(Global.DATE_MAX);
		
		iou.setDb_row_id(sqldb.insert(db_table_name, null, iou.iou));
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//BASIC DB QUERIES FOR ALL ARCHIVE IOUS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//retrieves archrived ious unordered
	public ArrayList<Iou> get_archived_ious_unordered() {
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves archived ious in chronological order of completion
	public ArrayList<Iou> get_archived_ious_ordered_by_completion() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious ORDER BY date_completed ASC", null);
		
		return retrieve_ious(cursor);
		
	}
	
	//retrieves archived ious in order of the loan date
	public ArrayList<Iou> get_archived_ious_ordered_by_loan_date() {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious ORDER BY date_borrowed ASC", null);
		
		return retrieve_ious(cursor);
	}	
	
	//retrieves archived ious in order of highest to lowest value
	public ArrayList<Iou> get_archived_ious_ordered_by_value_desc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious ORDER BY value DESC", null);
		
		return retrieve_ious(cursor);
	}
	
	//retrieves all archived in order of lowest to highest value
	public ArrayList<Iou> get_archived_ious_ordered_by_value_asc() {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious ORDER BY value ASC", null);
		
		return retrieve_ious(cursor);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//BASIC DB QUERIES FOR CONTACT ARCHIVE IOUS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//retrieves archive ious, unordered from a specific contact
	public ArrayList<Iou> get_archived_contact_ious_unordered (String contact) {
		
		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious WHERE contact = ?", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves archive ious in order of the shortest time to due date
	public ArrayList<Iou> get_archived_contact_ious_ordered_by_completion_date(String contact) {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious WHERE contact = ? ORDER BY date_due ASC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}	
	
	//retrieves archive ious in order of chronological time
	public ArrayList<Iou> get_archived_contact_ious_ordered_by_earliest_loan_date(String contact) {

		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious WHERE contact = ? ORDER BY date_borrowed ASC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves archive ious in order of highest to lowest value
	public ArrayList<Iou> get_archived_contact_ious_ordered_by_value_desc(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious WHERE contact = ? ORDER BY value DESC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	//retrieves archive ious in order of lowest to highest value
	public ArrayList<Iou> get_archived_contact_ious_ordered_by_value_asc(String contact) {
		Cursor cursor = sqldb.rawQuery("SELECT * FROM archived_ious WHERE contact = ? ORDER BY value ASC", new String[] {contact});
		
		return retrieve_ious(cursor);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//IOU INSERT UPDATE DELETE
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

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
	
	public void deleteIou(Iou iou) {
		sqldb.delete(db_table_name, "iou_id = ?", new String[] {iou.getDb_row_id().toString()});
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//ARCHIVE DELETE
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	//deletes all archived ious of a specific contact
	public void delete_archived_contact_ious(String contact) {
		sqldb.delete(archive_table_name, "contact = ?", new String[] {contact});
	}
	
	//deletes all archived ious
	public void delete_archive() {
		db.reset_archive(sqldb);
	}
	
	
}
