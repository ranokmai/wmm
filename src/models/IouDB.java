package models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//Class to create DB from createdb.sql
//calls onCreate if db does not exist (first time app is used)
//calls onUpgrade whenever database is updated (still need to write)
//nothing else should need to change, as IouDBManager handles queries/inserts/updates
public class IouDB extends SQLiteOpenHelper {
	
	final static int DB_VERSION = 1;
	final static String DB_NAME = "ioudb.s3db";
	Context context;
	
	public IouDB(Context context_) {
		super(context_, DB_NAME, null, DB_VERSION);
		context = context_;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		executeSQLScript(database, "createdb.sql");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	private void executeSQLScript(SQLiteDatabase database, String sql_file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    byte buf[] = new byte[1024];
	    int len;
	    AssetManager assetManager = context.getAssets();
	    InputStream inputStream = null;
	         
	    try{
	        inputStream = assetManager.open(sql_file);
	        while ((len = inputStream.read(buf)) != -1) {
	            outputStream.write(buf, 0, len);
	        }
	        outputStream.close();
	        inputStream.close();
	             
	        String[] createScript = outputStream.toString().split(";");
	        for (int i = 0; i < createScript.length; i++) {
	                String sqlStatement = createScript[i].trim();
	            // TODO You may want to parse out comments here
	            if (sqlStatement.length() > 0) {
	                    database.execSQL(sqlStatement + ";");
	                }
	        }
	    } catch (IOException e){
	        // TODO Handle Script Failed to Load
	    } catch (SQLException e) {
	        // TODO Handle Script Failed to Execute
	    }
	}

}
