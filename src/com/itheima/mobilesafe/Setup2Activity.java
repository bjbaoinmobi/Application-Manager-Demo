package com.itheima.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.itheima.mobilesafe.ui.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {
	
	private SettingItemView siv_setup2_bind;
	//电话管理服务
	private TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		siv_setup2_bind  = (SettingItemView) findViewById(R.id.siv_setup2_bind);
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			//设置为非选中状态
			siv_setup2_bind.setChecked(false);
		}else{
			//设置为选中状态
			siv_setup2_bind.setChecked(true);
		}
		siv_setup2_bind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String sim = tm.getSimSerialNumber();
				Editor editor = sp.edit();
				if(siv_setup2_bind.isChecked()){
					//设置为非选中
					siv_setup2_bind.setChecked(false);
					editor.putString("sim", null);
				}else{
					//设置为选中状态
					siv_setup2_bind.setChecked(true);
					editor.putString("sim", sim);
				}
//				tm.getLine1Number();//当前sim电话号码
				//读取sim的序列号
				
				editor.commit();
				
			}
		});
		
		
		
	}
	
	


	@Override
	public void shownext() {
		//判断是否已经保存了Sim序列号
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(this, "sim卡还没有绑定", 0).show();
			return;
		}
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}


	@Override
	public void showpre() {
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
	}

}
