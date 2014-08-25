package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterPwdActivity extends Activity {
	private ImageView iv_icon;
	private TextView tv_name;
	private EditText et_password;
	private String packname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pwd);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_name = (TextView) findViewById(R.id.tv_name);
		Intent intent = getIntent();
		packname = intent.getStringExtra("packname");
		et_password = (EditText) findViewById(R.id.et_password);
		try {
			PackageManager pm = getPackageManager();
			iv_icon.setImageDrawable(pm.getPackageInfo(packname, 0).applicationInfo.loadIcon(pm));
			tv_name.setText(pm.getPackageInfo(packname, 0).applicationInfo.loadLabel(pm));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		//ֱ�� �����档
//        <action android:name="android.intent.action.MAIN" />
//        <category android:name="android.intent.category.HOME" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <category android:name="android.intent.category.MONKEY"/>
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
	}
	
	//ֻҪ��������Ľ����û����ɼ� ����Ҫ�������رյ���
	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}
	/**
	 * ȷ����ť�ĵ���¼�
	 * @param view
	 */
	public void click(View view){
		String password = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(password)){
			Toast.makeText(this, "�Բ��𰡣����벻��Ϊ��", 0).show();
			return;
		}
		if("123".equals(password)){
			//������ȷ
			//���Ź�ʵ�����Ƿ����������ѭ���� �����Ź������񣩷���Ϣ����ʱ��ֹͣ������
			//���� �� ���õ�Ӧ�ó��� ���� ��ͬ�����֮��ͬ����Ϣ�� �Զ���Ĺ㲥��
			Intent intent = new Intent();
			intent.setAction("com.itheima.mobilesafe.stopprotect");
			intent.putExtra("packname", packname);
			sendBroadcast(intent);
			//�����Զ���㲥���߿��Ź� ��ʱ��ֹͣ������
			finish();//�رյ�ǰ��activity������Ӧ�ó�����档
		}else{
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			iv_icon.startAnimation(shake);
		}
	}
	
	
}
