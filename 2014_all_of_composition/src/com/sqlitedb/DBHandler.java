package com.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBHandler {

	private DBHelper helper;
	private SQLiteDatabase db;

	private DBHandler(Context ctx) {

		this.helper = new DBHelper(ctx);
		this.db = helper.getWritableDatabase();
	}

	public static DBHandler open(Context ctx) throws SQLException {

		DBHandler handler = new DBHandler(ctx);

		return handler;
	}

	public void close() {
		helper.close();
	}

	public long insert(String _name, String _contents) {
		ContentValues values = new ContentValues();
		values.put("_name", _name);
		values.put("_contents", _contents);

		return db.insert("composition", null, values);
	}

	public Cursor selectAll() throws SQLException {
		Cursor cursor = db.query(true, "composition", new String[] { "_id",
				"_name", "_contents" }, null, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	
	public void re_insert(int index, String contents) { 
		String sql = "update composition set _contents = '" +  contents +"' where _id = " + index + ";";
		db.execSQL(sql);
	}	
	
	public void delete(int index) { 
		String sql = "delete from composition where _id = "+index+";";
		db.execSQL(sql);
	}


	public void deleteAll() {
		db = helper.getReadableDatabase();
		db.delete("composition", null, null);// table name
	}

}
