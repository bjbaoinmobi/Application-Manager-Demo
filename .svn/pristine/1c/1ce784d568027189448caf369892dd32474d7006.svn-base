package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberQueryDao {

	
	/**
	 * 查询分组的总数
	 */

	public static int getGroupCount(SQLiteDatabase db) {

//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
//				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select count(*) from classlist", null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
//		db.close();
		return count;
	}
	
	

	/**
	 * 得到每个分组孩子的总数
	 */

	public static int getChildCount(SQLiteDatabase db,int groupPosition) {
		
		int newGroupPosition = groupPosition + 1;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
//				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select count(*) from table"+newGroupPosition, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
//		db.close();
		return count;
	}

	/**
	 * 得到分组的名称
	 */

	public static String getGroupName(SQLiteDatabase db,int groupPosition) {
		int newGroupPosition = groupPosition + 1;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
//				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select name from classlist where idx = ?",
				new String[] { newGroupPosition + "" });
		cursor.moveToFirst();
		String name = cursor.getString(0);
		cursor.close();
//		db.close();
		return name;

	}
	
	
	/**
	 * 得到某个分组孩子的名称
	 */

	public static String getChildName(SQLiteDatabase db,int groupPosition, int childPosition) {
		int newGroupPosition = groupPosition + 1;
		int newChildPosition = childPosition + 1;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
//				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select name,number from table"+newGroupPosition+" where _id = ?",
				new String[] { newChildPosition + "" });
		cursor.moveToFirst();
		String name = cursor.getString(0);
		String number = cursor.getString(1);
		cursor.close();
//		db.close();
		return name+"\n"+number;

	}

}
