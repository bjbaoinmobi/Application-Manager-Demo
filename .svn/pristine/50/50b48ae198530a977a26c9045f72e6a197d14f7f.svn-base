package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryDao {

	// assets资源目录下的address.db拷贝到data/data/com.itheima.mobilesafe/files/address.db
	private static String path = "/data/data/com.itheima.mobilesafe/files/address.db";

	/**
	 * 号码归属地的查询
	 * 
	 * @param number
	 *            要查询的电话号码
	 * @return 归属地
	 */

	public static String getAddresss(String number) {
		String address = number;
		// 不识别
		// 设识别assets资源目录呢
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		// 判断是否是手机号码--总共11位
		// 13 14 15 17 18
		// 正则表达式:^1[34578]\d{9}$

		if (number.matches("^1[34578]\\d{9}$")) {
			Cursor cursor = db
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				String location = cursor.getString(0);
				address = location;
			}
		} else {
			// 长度 3 110 119
			switch (number.length()) {
			case 3:// 匪警号码 120 119 
				address = "匪警号码";
				break;

			case 4:// 5556 模拟器
				address = "模拟器";
				break;

			case 5:// 10086 客服号码
				address = "客服号码";
				break;

			case 7://
				address = "本地号码";
				break;

			case 8://
				address = "本地号码";
				break;

			default:
				if (number.length() >= 10 && number.startsWith("0")) {
					Cursor cursor = db.rawQuery(
							"select location from data2 where  area = ?",
							new String[] { number.substring(1, 3) });
					
					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length()-2);
					}
					
					cursor.close();
					
					
				    cursor = db.rawQuery(
							"select location from data2 where  area = ?",
							new String[] { number.substring(1, 4) });
					
					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length()-2);
					}
					
					cursor.close();
				}

				break;
			}
		}

		return address;
	}

}
