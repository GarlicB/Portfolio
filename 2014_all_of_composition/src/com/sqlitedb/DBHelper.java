package com.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public DBHelper(Context context){
		
		super(context,"composition.db",null,1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		
		String table = 
				"CREATE TABLE composition (" +
			    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		        "_name TEXT NOT NULL," +
			    "_contents TEXT NOT NULL);";
		
				db.execSQL(table);
	}
	
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
				db.execSQL("DROP TABLE IF EXISTS composition");
				onCreate(db);
	}	
}		
