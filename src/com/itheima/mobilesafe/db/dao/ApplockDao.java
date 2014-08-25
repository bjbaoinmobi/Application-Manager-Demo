package com.itheima.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.db.ApplockDBOpenHelper;

public class ApplockDao {

	private ApplockDBOpenHelper helper;

	public ApplockDao(Context context) {
		helper = new ApplockDBOpenHelper(context);
	}
	/**
	 * 添加一条锁定的包名
	 * @param packname
	 */
	public void add(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		db.insert("applock", null, values);
		db.close();
	}
	/**
	 * 删除一条锁定的包名
	 * @param packname
	 */
	public void delete(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("applock", "packname=?", new String[]{packname});
		db.close();
	}
	/**
	 * 查询一个包名是否被锁定
	 * @param packname
	 * @return
	 */
	public boolean find(String packname){
		SQLiteDatabase db = helper.getReadableDatabase();
		boolean result = false;
		Cursor cursor = db.query("applock", null, "packname=?", new String[]{packname}, null, null, null);
		if(cursor.moveToFirst()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	
	/**
	 * 查询全部的锁定包名
	 * @return
	 */
	public List<String> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<String> packnames = new ArrayList<String>();
		Cursor cursor = db.query("applock", new String[]{"packname"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			String packname = cursor.getString(0);
			packnames.add(packname);
		}
		cursor.close();
		db.close();
		return packnames;
	}
}
