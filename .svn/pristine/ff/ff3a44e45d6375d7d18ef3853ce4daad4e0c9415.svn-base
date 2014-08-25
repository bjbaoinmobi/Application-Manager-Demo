package com.itheima.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplockDBOpenHelper extends SQLiteOpenHelper {

	public ApplockDBOpenHelper(Context context) {
		// 创建数据库了
		super(context, "applock.db", null, 1);
	}

	/**
	 * 当数据库创建好了后，就会回调该方法 --常用来创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//自增长的_id,锁定应用程序的包名。packname
		db.execSQL("create table applock(_id integer primary key autoincrement,packname varchar(20))");

	}

	
	/**
	 * 当数据库版本// 1-->2,就会回调这个方法
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	

}
