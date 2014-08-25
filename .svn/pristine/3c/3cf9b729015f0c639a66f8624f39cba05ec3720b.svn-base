package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.db.dao.NumberAddressQueryDao;

public class NumberAddressQueryActivity extends Activity {
	
	private static final String TAG = "NumberAddressQueryActivity";
	private EditText et_number_query;
	
	//振动设备服务
	
	private Vibrator vibrator;
	
	private TextView result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_address_query);
		et_number_query = (EditText) findViewById(R.id.et_number_query);
		result = (TextView) findViewById(R.id.result);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		et_number_query.addTextChangedListener(new TextWatcher() {
			/**
			 * 文件变化时回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s!= null&&s.length()>=3){
					//得到我们要查询的电话号，
					//去数据库查询
					String address = NumberAddressQueryDao.getAddresss(s.toString());
					result.setText(address);
					
					
				}
				
			}
			/**
			 * 文件变化前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * 文件变化后回调
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 按钮的点击事件--查询号码归属地
	 * @param view
	 */
	public void query(View view){
		//要查询的电话号码
		String phone = et_number_query.getText().toString().trim();
		
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "您输入的电话号码为空", 1).show();
			
			
			 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 
			 et_number_query.startAnimation(shake);
		}else{
			//查询号码归属地
			//1.联网查询---需要有网络
			//2.到本地数据库去查询--返回号码归属地
			//号码归属地查询的原理--查询数据库或者网络查询，返回归属地
			//抖动效果
			String address= NumberAddressQueryDao.getAddresss(phone);
			result.setText(address);
			
			//振动效果
//			vibrator.vibrate(2000);//振动两秒钟
			long []pattern = {200,200,300,300,1000,1000};
			//-1不重复 0 重复 1
			vibrator.vibrate(pattern, -1);
			//眼部按摩棒
			Log.i(TAG, "你要查询的号码是："+phone);
		}
	}

}
