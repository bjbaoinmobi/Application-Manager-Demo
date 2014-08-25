package com.itheima.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {
	protected static final String TAG = "SplashActivity";

	protected static final int ENTER_HOME = 0;

	protected static final int SHOW_UPDATE_DIALOG = 1;

	protected static final int URL_ERROR = 2;

	protected static final int NETWORK_ERROR = 3;

	protected static final int JSON_ERROR = 4;

	private TextView tv_splash_version;

	/**
	 * �汾��������Ϣ
	 */
	private String description;
	/**
	 * apk���ص�ַ
	 */
	private String apkurl;

	private TextView tv_updateinfo;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("�汾�ţ�" + getVersionName());
		tv_updateinfo = (TextView) findViewById(R.id.tv_updateinfo);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("update", false);
		// ���ݵĳ�ʼ��--�������ݿ�
		installShortcut();
		copyDB("address.db");
		copyDB("commonnum.db");
		copyDB("antivirus.db");
		if (update) {
			// �����Ѿ��򿪣�����˵�����Ѿ���ѡ
			checkUpdate();
		} else {
			// ����û�д򿪣�������ҳ�� ͣ��2��
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					enterHome();// ������ҳ��
				}
			}, 2000);

		}

		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(600);
		findViewById(R.id.rl_splash_root).startAnimation(aa);
	}

	/**
	 * ������ݼ�
	 */
	private void installShortcut() {
		if(sp.getBoolean("installedshortcut", false)){
			return;
		}
		
		//�����ݼ�����ͼ
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�ֻ���ʿ");//��ݼ�������
		//��ݼ���ͼ��
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(getResources(), R.drawable.callmsgsafe));
		
		//��Ҫ�Ҹ�ʲôѽ
		//���������ҳ��
		Intent intentHome = new Intent();
		intentHome.setAction("com.itheima.mobilesafe.home");
		intentHome.addCategory("android.intent.category.DEFAULT");
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentHome);
		
		sendBroadcast(intent);
		
		Editor editor = sp.edit();
		editor.putBoolean("installedshortcut", true);
		editor.commit();

	}

	/**
	 * 
	 * assets��ԴĿ¼�µ�address.db������data/data/com.itheima.mobilesafe/files/address
	 * .db
	 */
	private void copyDB(String dbName) {

		try {

			File file = new File(getFilesDir(), dbName);
			if (file.exists() && file.length() > 0) {
				// �����ɹ��ˣ�����Ҫ������
				Log.i(TAG, "�����ɹ��ˣ�����Ҫ������");
			} else {
				InputStream is = getAssets().open(dbName);

				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				is.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ENTER_HOME:// ������ҳ��
				enterHome();
				break;
			case SHOW_UPDATE_DIALOG:// ���������Ի���
				Log.i(TAG, "���°汾�ˣ����������Ի���");
				showUpdateDialog();
				break;
			case URL_ERROR:// URL����
				Toast.makeText(getApplicationContext(), "URL����", 0).show();
				enterHome();
				break;

			case NETWORK_ERROR:// �����쳣
				enterHome();
				Toast.makeText(SplashActivity.this, "�����쳣", 0).show();
				break;

			case JSON_ERROR:// JSON�����쳣
				enterHome();
				Toast.makeText(SplashActivity.this, "JSON�����쳣", 0).show();
				break;

			}

		};
	};

	/**
	 * �������
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				long startTime = System.currentTimeMillis();
				// URL http://192.168.1.100:8080/updateinfo.html
				Message message = Message.obtain();
				try {
					URL url = new URL(getResources().getString(
							R.string.serverurl));
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(4000);
					int code = connection.getResponseCode();// ��Ӧ��
					if (code == 200) {
						// �����ɹ�
						InputStream is = connection.getInputStream();
						// ����������
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "���ݣ�" + result);

						// JSON����
						JSONObject jsonObject = new JSONObject(result);
						// �����������°汾
						String version = (String) jsonObject.get("version");
						description = (String) jsonObject.get("description");
						apkurl = (String) jsonObject.get("apkurl");

						// �ж��Ƿ����µİ汾
						if (getVersionName().equals(version)) {
							// �汾һ�£�����Ҫ���� --������ҳ��
							message.what = ENTER_HOME;
						} else {
							// ���°汾�������Ի������û�ѡ���Ƿ�����
							message.what = SHOW_UPDATE_DIALOG;
						}
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
					message.what = URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					message.what = NETWORK_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					message.what = JSON_ERROR;
				} finally {

					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendMessage(message);
				}
			};
		}.start();

	}

	protected void showUpdateDialog() {
		// this �ȼ���SplashActivity.this
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("��������");
		// builder.setCancelable(false);//ǿ������

		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// ���������ص�ʱ��
				enterHome();

			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �����µ�APK �滻��װ
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// aFinal
					FinalHttp http = new FinalHttp();
					http.download(apkurl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/mobilesafe2.0.apk", new AjaxCallBack<File>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							t.printStackTrace();
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(getApplicationContext(), "����ʧ��", 0)
									.show();
						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							tv_updateinfo.setVisibility(View.VISIBLE);
							int progress = (int) (current * 100 / count);

							tv_updateinfo.setText("���ؽ��ȣ�" + progress + "%");
						}

						@Override
						public void onSuccess(File t) {
							super.onSuccess(t);
							installAPK(t);
						}

						/**
						 * ��װAPK
						 * 
						 * @param t
						 */
						private void installAPK(File t) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setDataAndType(Uri.fromFile(t),
									"application/vnd.android.package-archive");
							startActivity(intent);

						}

					});

				} else {
					Toast.makeText(SplashActivity.this, "sd��������", 0).show();
				}

			}
		});
		builder.setNegativeButton("�´���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();
			}
		});
		// ��ʾ�Ի���
		builder.show();

	}

	/**
	 * ������ҳ��
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		// �ѵ�ǰҳ��رյ�
		finish();

	}

	/**
	 * �õ���ǰ�����İ汾����
	 */

	private String getVersionName() {
		// ���ĵĹ�����
		PackageManager pm = getPackageManager();
		// �õ������嵥�ļ�
		try {
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

}