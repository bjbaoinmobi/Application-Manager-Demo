package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_safenumber;
	private ImageView iv_protecting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//判断是否已经设置过设置向导，如果没有设置就跳转到设置向导的第一个页面，
		//如果设置了，就就在当前页面（手机防盗页面）
		
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_find);
			
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
			
			//读取安全号码
			String safenumber= sp.getString("safenumber", "");
			tv_safenumber.setText(safenumber);
			
			//读取防盗保护是否已经开启
			boolean protecting = sp.getBoolean("protecting", false);
			if(protecting){
				//防盗保护已经开启了
				iv_protecting.setImageResource(R.drawable.lock);
			}else{
				//防盗保护没有开启
				iv_protecting.setImageResource(R.drawable.unlock);
			}
			
			
		}else{
			//跳转到设置向导第一个页面
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			//把当前页面关闭
			finish();
			
		}
		
		
		
		
	}
	/**
	 *   重新进入设置向导页面
	 * @param view
	 */
	public void reEnterSetup(View view){
		//跳转到设置向导第一个页面
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//把当前页面关闭
		finish();
		
	}

}
