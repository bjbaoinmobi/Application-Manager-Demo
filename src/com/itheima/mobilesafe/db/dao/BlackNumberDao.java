package com.itheima.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

public class BlackNumberDao {

	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * 增加一条黑名单数据
	 * 
	 * @param number
	 *            黑名单号码
	 * @param mode
	 *            1：电话拦截 2：短信拦截 3：全部拦截
	 */
	public void add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
		db.close();
	}

	/**
	 * 删除一条黑名单数据
	 * 
	 * @param number
	 *            黑名单号码
	 */
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber", "number=?", new String[] { number });
		db.close();
	}

	/**
	 * 修改黑名单数据库的一条信息
	 * 
	 * @param number
	 *            黑名单号码
	 * @param newmode
	 *            新的拦截模式
	 */
	public void update(String number, String newmode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values, "number=?", new String[] { number });
		db.close();

	}

	/**
	 * 查询某天黑名单号码是否存在
	 * 
	 * @param number
	 * @return
	 */
	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", null, "number=?",
				new String[] { number }, null, null, null);
		while (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * 查询某条黑名单的拦截模式
	 * 
	 * @param number
	 *            要查询的黑名单号码
	 * @return 1：电话拦截 2：短信拦截 3：全部拦截
	 */
	public String findMode(String number) {
		String mode = "3";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		while (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	/**
	 * 得到黑名单数据库的所有数据
	 * 
	 * @return
	 */
	public List<BlackNumberInfo> findAll() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db
				.query("blacknumber", new String[] { "number", "mode" }, null,
						null, null, null, null);
		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setNumber(number);
			info.setMode(mode);
			infos.add(info);

		}
		cursor.close();
		db.close();
		return infos;

	}

	/**
	 * 得到黑名单数据库的部分数据
	 * 
	 * @return
	 */
	public List<BlackNumberInfo> findPart(int startIndex) {
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select number,mode from blacknumber order by _id desc limit 20 offset ?",
				new String[] { startIndex + "" });
		while (cursor.moveToNext()) {
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setNumber(number);
			info.setMode(mode);
			infos.add(info);

		}
		cursor.close();
		db.close();
		return infos;

	}

	/**
	 * 得到黑名单数据库的总条数
	 * 
	 * @return
	 */
	public int getTotatCount() {
		int totalCount = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		if (cursor.moveToNext()) {
			totalCount = cursor.getInt(0);

		}
		cursor.close();
		db.close();
		return totalCount;
	}

}
