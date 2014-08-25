package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.Md5Utils;
import com.startapp.android.publish.StartAppAd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	protected static final String TAG = "HomeActivity";
	private GridView list_home;
	private MyAdapter adapter;

	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", "缓存清理", "高级工具", "设置中心"

	};

	private static int[] ids = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };

	private SharedPreferences sp;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppAd.init(this, "105756043", "204514114");
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		list_home = (GridView) findViewById(R.id.list_home);
		adapter = new MyAdapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				switch (position) {
				case 0:// 进入手机防盗页面
					showlostFindDialog();
					break;
				case 1://进入通讯卫士
					intent = new Intent(HomeActivity.this,CallSmsSafeActivity.class);
					startActivity(intent);
					break;
				case 2://进入软件管理页面
					intent = new Intent(HomeActivity.this,AppManagerActivity.class);
					startActivity(intent);
					break;	
				case 3://进入进程管理页面
					intent = new Intent(HomeActivity.this,TaskManagerActivity.class);
					startActivity(intent);
					break;	
				case 4://进入流量统计页面
					intent = new Intent(HomeActivity.this,TrafficManagerActivity.class);
					startActivity(intent);
					break;
				case 5://手机杀毒页面
					intent = new Intent(HomeActivity.this,AntiVirusActivity.class);
					startActivity(intent);
					break;
				case 6://缓存清理页面
					intent = new Intent(HomeActivity.this,CleanCacheActivity.class);
					startActivity(intent);
					break;
				case 7://进入高级工具
					intent = new Intent(HomeActivity.this,AToolsActivity.class);
					startActivity(intent);
					break;
				case 8:// 进入设置中心
					 intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);

					break;

				default:
					break;
				}

			}
		});
	}

	protected void showlostFindDialog() {
		// 判断是否设置过密码
		if (isSetupPwd()) {
			// 已经设置密码了
			showEnterDialog();
		} else {
			// 还没有设置密码
			showSetupPwdDialog();
		}
	}

	private EditText et_setup_pwd;
	private EditText et_setup_pwd_confirm;
	private Button ok;
	private Button cancel;
	
	private AlertDialog dialog;

	/**
	 * 弹出设置密码的对话框
	 */
	private void showSetupPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(this, R.layout.dialog_setup_password, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_pwd_confirm = (EditText) view
				.findViewById(R.id.et_setup_pwd_confirm);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//取消对话框
				dialog.dismiss();
				
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//得到密码 ，保存密码 消掉对话框
				String password = et_setup_pwd.getText().toString().trim();
				String password_confirm = et_setup_pwd_confirm.getText().toString().trim();
				
				if(TextUtils.isEmpty(password)||TextUtils.isEmpty(password_confirm)){
					Toast.makeText(HomeActivity.this, "密码不能为空，哥们", 0).show();
					return ;
				}
				
				if(password.equals(password_confirm)){
					//密码一致 保存 ，消掉对话框
					Editor editor = sp.edit();
					editor.putString("password", Md5Utils.encode(password));//保存加密后的密码
					editor.commit();
					
					dialog.dismiss();
					Log.i(TAG, "密码设置成功，进入主页面");
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(HomeActivity.this, "密码不一致，姐们", 0).show();
					return;
				}
				
			}
		});

//		builder.setView(view);
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	}

	/**
	 * 弹出输入密码的对话框
	 */
	private void showEnterDialog() {

		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(this, R.layout.dialog_enter_password, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//取消对话框
				dialog.dismiss();
				
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//得到密码 ，保存密码 消掉对话框
				String password = et_setup_pwd.getText().toString().trim();//1
				String savePassword = sp.getString("password", "");//加密后的密文，保存的
				if(TextUtils.isEmpty(password)){
					Toast.makeText(HomeActivity.this, "密码不能为空，哥们", 0).show();
					return ;
				}
				
			 
				//保存的adjdj
				//判断密码是否正确，取消对话框/adjdj
				if(Md5Utils.encode(password).equals(savePassword)){
					//密码正确，进入主页面
					Log.i(TAG, "密码正确，进入主页面");
					dialog.dismiss();
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				}else{
					//密码错误 
					Toast.makeText(HomeActivity.this, "密码出错了", 0).show();
					return;
				}
				
				
				
			}
		});

		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();

	

	}

	/**
	 * 判断是否设置了密码
	 * 
	 */

	private boolean isSetupPwd() {
		String password = sp.getString("password", null);
		// if(TextUtils.isEmpty(password)){
		// return false;
		// }else{
		// return true;
		// }
		return !TextUtils.isEmpty(password);
		//

	}

	private class MyAdapter extends BaseAdapter {

		// 得到总条数
		@Override
		public int getCount() {
			return names.length;
		}

		/**
		 * 返回某一条的View对象
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.list_home_item, null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			tv_item.setText(names[position]);
			iv_item.setImageResource(ids[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

}
