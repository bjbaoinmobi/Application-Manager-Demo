package com.itheima.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
	}
	
	

	public void shownext() {
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
		//播放动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}




    //当前是第一个页面，不需要实现
	@Override
	public void showpre() {
		// TODO Auto-generated method stub
		
	}

}
