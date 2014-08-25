package com.itheima.mobilesafe;

import java.io.File;

import com.itheima.mobilesafe.utils.SmsBackupUtils;
import com.itheima.mobilesafe.utils.SmsBackupUtils.SmsBackuCallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AToolsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	/**
	 * 点击事件--进入号码归属地查询的页面
	 * @param view
	 */
	public void numberAddressQuery(View view){
		
		Intent intent = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 点击事件--进入常用号码查查页面
	 * @param view
	 */
	public void commonNumberQuery(View view){
		
		Intent intent = new Intent(this,CommonNumberQueryActivity.class);
		startActivity(intent);
		
	}
	/**
	 * 点击事件--短信备份
	 * @param view
	 */
	public void smsBackup(View view){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			final File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "smsBackup.xml");
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("请稍候，正在备份短信中...");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.show();
				new Thread(){
					public void run() {
						try {
							SmsBackupUtils.smsBackup(AToolsActivity.this, file.getAbsolutePath(), new SmsBackuCallBack() {
								
								@Override
								public void onSmsbackupProgress(int progress) {
									dialog.setProgress(progress);
									
								}
								
								@Override
								public void beforeSmsbacup(int total) {
									dialog.setMax(total);
									
								}
							});
							dialog.dismiss();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}.start();
			
//			
		}else{
			Toast.makeText(this, "sd卡不可用", 1).show();
		}
		
		
	}

}
