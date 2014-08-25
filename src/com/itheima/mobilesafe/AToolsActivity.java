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
	 * ����¼�--�����������ز�ѯ��ҳ��
	 * @param view
	 */
	public void numberAddressQuery(View view){
		
		Intent intent = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
	}
	
	/**
	 * ����¼�--���볣�ú�����ҳ��
	 * @param view
	 */
	public void commonNumberQuery(View view){
		
		Intent intent = new Intent(this,CommonNumberQueryActivity.class);
		startActivity(intent);
		
	}
	/**
	 * ����¼�--���ű���
	 * @param view
	 */
	public void smsBackup(View view){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			final File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "smsBackup.xml");
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("���Ժ����ڱ��ݶ�����...");
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
			Toast.makeText(this, "sd��������", 1).show();
		}
		
		
	}

}
