package com.itheima.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {

	public static final String TAG = "CallSmsSafeService";
	private InnerCallSmsSafe receiver;

	private BlackNumberDao dao;
	/**
	 * 电话管理服务
	 */
	private TelephonyManager tm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// 接收短信--拦截
	class InnerCallSmsSafe extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "我是代码---内部类广播接收者收到短信了");
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");

			for (Object pdu : pdus) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
				String number = sms.getOriginatingAddress();// 根据电话号码这种方式拦截

				// 判断一下这个电话号码对应的短信改不改拦截
				if (dao.find(number)) {
					String mode = dao.findMode(number);
					if ("2".equals(mode) || "3".equals(mode)) {

						// 拦截短信
						Log.i(TAG, "要拦截的电话号码：" + number);
						abortBroadcast();// 拦截短信
					}
				}

				// 根据短信内容拦截 ---智能拦截
				String body = sms.getMessageBody();
				if (body.contains("fapiao")) {// 查询数据库//分词
					Log.i(TAG, "拦截到一个广告的信息：" + number + ",,body==" + body);
					abortBroadcast();// 拦截短信
				}

			}

		}

	}

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 有一个电话来电

				if (dao.find(incomingNumber)) {
					String mode = dao.findMode(incomingNumber);
					if ("1".equals(mode) || "3".equals(mode)) {
						// 电话拦截
						// 把这个电话给挂了

						// 清除呼叫记录
						// tm.endCall();//把电话挂断
						endCall();//立刻挂掉电话了，但是呼叫记录并没有立即生成；不同步导致了
						Uri url = Uri.parse("content://call_log/calls");
						//注册内容观察者
						getContentResolver().registerContentObserver(url, true, new MyContentObserver(new Handler(), incomingNumber));
//						deleteCallLog(incomingNumber);
					}
				}

				break;

			default:
				break;
			}

		}

	}
	
	private class MyContentObserver extends ContentObserver{
		private String number;

		public MyContentObserver(Handler handler,String number) {
			super(handler);
			this.number = number;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//取消注册内容观察者
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(number);
		}
		
		
		
	}





	private MyPhoneStateListener listener;

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BlackNumberDao(this);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 代码注册广播接收者 --拦截短信
		receiver = new InnerCallSmsSafe();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);

		// 监听来电
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	/**
	 * 删除呼叫记录
	 */
	public void deleteCallLog(String number) {
		ContentResolver resolver = getContentResolver();
		Uri url = Uri.parse("content://call_log/calls");
		resolver.delete(url, "number=?", new String[]{number});
		
	}

	/**
	 * 挂断电话
	 */
	public void endCall() {

		// 用反射的方式得到原生的API方法
		// ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass(
					"android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			// 执行我们的方法
			IBinder b = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

			ITelephony service = ITelephony.Stub.asInterface(b);
			// 得到了原生的没有包装的电话服务的接口
			
			service.endCall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 代码取消注册广播接收者
		unregisterReceiver(receiver);
		receiver = null;

		// 取消监听来电
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

}
