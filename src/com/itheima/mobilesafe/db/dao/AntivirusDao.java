package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	// assets资源目录下的address.db拷贝到data/data/com.itheima.mobilesafe/files/address.db
	private static String path = "/data/data/com.itheima.mobilesafe/files/antivirus.db";

	
	/**
	 * 检查一个应用程序是否是md5
	 * @param md5
	 * @return
	 */
	public static boolean isVirus(String md5){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		boolean result = false;
		Cursor cursor = db.rawQuery("select * from datable where md5=?", new String[]{md5});
		if(cursor.moveToFirst()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
}
