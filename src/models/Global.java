package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

public class Global {
	public static IouDBManager iou_db_mgr;
	
	public static Date DATE_MAX = new Date(Long.MAX_VALUE);
	
	public static int JAN = 0;
	public static int FEB = 1;
	public static int MAR = 2;
	public static int APR = 3;
	public static int MAY = 4;
	public static int JUN = 5;
	public static int JUL = 6;
	public static int AUG = 7;
	public static int SEP = 8;
	public static int OCT = 9;
	public static int NOV = 10;
	public static int DEC = 11;
	
	public static Iou iou;
	
    public enum Filters {
    	DATEDUE, 
    	DATELOANED,
 	    TITLE,
 	    VALUE,
 	    CONTACT,
 	    NUMFILTER;
 	    
    	private Filters() {
	        this.value = -1;
	    }
    	
    	// use this value/hack to get int back
    	private int value;
    	private Filters(int value) {
	        this.value = value;
	    }

	    public int getint() {
	        return value;
	    }
    }
	
	public static void setup_db_mgr(Context context) {
		iou_db_mgr = new IouDBManager(context);
	}

	//for date formatting:
	public static String date_format = "yyyy-MM-dd";
	
	public static Date str_to_date(String s)  {
		
		DateFormat df = new SimpleDateFormat(date_format, Locale.US);
		Date r = new Date();
		
		try {
			r = df.parse(s);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	public static String date_to_str(Date d) {
		return new SimpleDateFormat(date_format, Locale.US).format(d);
	}
	
}
