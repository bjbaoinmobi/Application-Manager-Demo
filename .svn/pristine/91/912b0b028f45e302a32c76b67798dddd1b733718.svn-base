package com.itheima.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {
	
	private EditText et_setup3_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_setup3_phone = (EditText) findViewById(R.id.et_setup3_phone);
		//把之前保存的取出来
		et_setup3_phone.setText(sp.getString("safenumber", ""));
	}
	
	
	


	@Override
	public void shownext() {
		String phone = et_setup3_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "还没有设置安全号码", 1).show();
			return;
		}
		
		Editor editor = sp.edit();
		editor.putString("safenumber", phone);
		editor.commit();
		
		Intent intent = new Intent(this,Setup4Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}


	@Override
	public void showpre() {
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
	}
	/**
	 * 点击事件
	 * @param view
	 */
	public void selectContact(View view){
		Intent intent = new Intent(this,SelectContactActivity.class);
		startActivityForResult(intent, 0);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			String phone = data.getStringExtra("phone").replace("-", "");
			et_setup3_phone.setText(phone);
		}
	}


}
