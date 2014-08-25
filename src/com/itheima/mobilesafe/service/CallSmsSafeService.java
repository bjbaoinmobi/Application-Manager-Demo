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
	 * �绰�������
	 */
	private TelephonyManager tm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// ���ն���--����
	class InnerCallSmsSafe extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "���Ǵ���---�ڲ���㲥�������յ�������");
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");

			for (Object pdu : pdus) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
				String number = sms.getOriginatingAddress();// ���ݵ绰�������ַ�ʽ����

				// �ж�һ������绰�����Ӧ�Ķ��ŸĲ�������
				if (dao.find(number)) {
					String mode = dao.findMode(number);
					if ("2".equals(mode) || "3".equals(mode)) {

						// ���ض���
						Log.i(TAG, "Ҫ���صĵ绰���룺" + number);
						abortBroadcast();// ���ض���
					}
				}

				// ���ݶ����������� ---��������
				String body = sms.getMessageBody();
				if (body.contains("fapiao")) {// ��ѯ���ݿ�//�ִ�
					Log.i(TAG, "���ص�һ��������Ϣ��" + number + ",,body==" + body);
					abortBroadcast();// ���ض���
				}

			}

		}

	}

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ��һ���绰����

				if (dao.find(incomingNumber)) {
					String mode = dao.findMode(incomingNumber);
					if ("1".equals(mode) || "3".equals(mode)) {
						// �绰����
						// ������绰������

						// ������м�¼
						// tm.endCall();//�ѵ绰�Ҷ�
						endCall();//���̹ҵ��绰�ˣ����Ǻ��м�¼��û���������ɣ���ͬ��������
						Uri url = Uri.parse("content://call_log/calls");
						//ע�����ݹ۲���
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
			//ȡ��ע�����ݹ۲���
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
		// ����ע��㲥������ --���ض���
		receiver = new InnerCallSmsSafe();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);

		// ��������
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	/**
	 * ɾ�����м�¼
	 */
	public void deleteCallLog(String number) {
		ContentResolver resolver = getContentResolver();
		Uri url = Uri.parse("content://call_log/calls");
		resolver.delete(url, "number=?", new String[]{number});
		
	}

	/**
	 * �Ҷϵ绰
	 */
	public void endCall() {

		// �÷���ķ�ʽ�õ�ԭ����API����
		// ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass(
					"android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			// ִ�����ǵķ���
			IBinder b = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

			ITelephony service = ITelephony.Stub.asInterface(b);
			// �õ���ԭ����û�а�װ�ĵ绰����Ľӿ�
			
			service.endCall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ����ȡ��ע��㲥������
		unregisterReceiver(receiver);
		receiver = null;

		// ȡ����������
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

}
