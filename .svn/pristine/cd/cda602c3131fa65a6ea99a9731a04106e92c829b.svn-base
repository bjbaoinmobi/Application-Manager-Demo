package com.itheima.mobilesafe.test;

import java.util.Random;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {

	public void testCreateDB() {
		BlackNumberDBOpenHelper blackNumberDBOpenHelper = new BlackNumberDBOpenHelper(
				getContext());
		blackNumberDBOpenHelper.getReadableDatabase();

	}

	public void testadd() {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		//13512340000--13512340099
		Random random = new Random();
		for(int i=0;i<100;i++){
			dao.add("1351234000"+i, ""+(random.nextInt(3)+1));
		}
	}

	public void tesdelete() {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("119");

	}

	public void testupdate() {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update("119", "2");

	}

	public void testfind() {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean  rusult = dao.find("119");
		assertEquals(true, rusult);

	}

	public void testfindMode() {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		String mode = dao.findMode("119");
		System.out.println("À¹½ØÄ£Ê½==="+mode);

	}

}
