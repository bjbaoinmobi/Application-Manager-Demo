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
		//�ж��Ƿ��Ѿ����ù������򵼣����û�����þ���ת�������򵼵ĵ�һ��ҳ�棬
		//��������ˣ��;��ڵ�ǰҳ�棨�ֻ�����ҳ�棩
		
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_find);
			
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
			
			//��ȡ��ȫ����
			String safenumber= sp.getString("safenumber", "");
			tv_safenumber.setText(safenumber);
			
			//��ȡ���������Ƿ��Ѿ�����
			boolean protecting = sp.getBoolean("protecting", false);
			if(protecting){
				//���������Ѿ�������
				iv_protecting.setImageResource(R.drawable.lock);
			}else{
				//��������û�п���
				iv_protecting.setImageResource(R.drawable.unlock);
			}
			
			
		}else{
			//��ת�������򵼵�һ��ҳ��
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			//�ѵ�ǰҳ��ر�
			finish();
			
		}
		
		
		
		
	}
	/**
	 *   ���½���������ҳ��
	 * @param view
	 */
	public void reEnterSetup(View view){
		//��ת�������򵼵�һ��ҳ��
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		//�ѵ�ǰҳ��ر�
		finish();
		
	}

}
