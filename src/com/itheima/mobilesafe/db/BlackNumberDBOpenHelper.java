package com.itheima.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {

	public BlackNumberDBOpenHelper(Context context) {
		// �������ݿ���
		super(context, "blacknumber.db", null, 1);
	}

	/**
	 * �����ݿⴴ�����˺󣬾ͻ�ص��÷��� --������������
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//��������_id,number���������룬mode 1���绰���� 2���������� 3��ȫ������
		db.execSQL("create table blacknumber(_id integer primary key autoincrement,number varchar(20),mode varchar(2))");

	}

	
	/**
	 * �����ݿ�汾// 1-->2,�ͻ�ص��������
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	

}
