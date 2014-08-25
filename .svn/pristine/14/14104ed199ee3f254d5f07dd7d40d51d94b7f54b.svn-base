package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.NumberAddressQueryDao;

public class AddressService extends Service {

	// 电话管理服务
	private TelephonyManager tm;

	private MyPphoneStateListener listener;

	private OutCallReceiver receiver;
	/**
	 * 窗体管理者 ，它是一个服务
	 */
	private WindowManager wm;
	private View view;

	private SharedPreferences sp;

	private WindowManager.LayoutParams params;
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// 监听电话状态
	class MyPphoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state电话状态 ，incomingNumber来电电话号码
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 铃声响起 --来电状态
				// 根据来电电话号，查询数据库 ，并且显示
				String address = NumberAddressQueryDao
						.getAddresss(incomingNumber);
				// Toast.makeText(getApplicationContext(), address, 1).show();
				myToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 挂断电话的状态--空闲状态

				// 把屏蔽的View移除
				if (view != null) {
					wm.removeView(view);
					view = null;
				}

				break;

			default:
				break;
			}
		}

	}

	// 服务里面的内部类
	// 用来监听电话呼出
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 得到打出去的电话号码
			String phone = getResultData();
			// 查询数据库--号码归属地
			String address = NumberAddressQueryDao.getAddresss(phone);
			// Toast.makeText(context, address, 1).show();
			myToast(address);

		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPphoneStateListener();
		// 代码监听来电
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		// 代码注册广播接收者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		// 我只关心电话呼出的Action
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
	}

	public void myToast(String address) {
		// view = new TextView(this);
		// view.setText(address);
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		view = View.inflate(this, R.layout.show_address_toast, null);
		TextView tv_addrress = (TextView) view.findViewById(R.id.address);
		tv_addrress.setText(address);
		// { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" }
		int which = sp.getInt("which", 0);
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		view.setBackgroundResource(ids[which]);

		view.setOnTouchListener(new OnTouchListener() {
			int startX = 0;
			int startY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("触摸事件");
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下
					// 第一次按下的坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;

				case MotionEvent.ACTION_MOVE:// 移动
					// 移动后来到新的坐标点
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();

					// 计算偏移量
					int dX = newX - startX;
					int dY = newY - startY;

					// 更新界面
//					params.x = params.x + dX;
//					params.y = params.y + dY;
					params.x  += dX;
					params.y  += dY;
					if(params.x < 0){
						params.x = 0;
					}
					
					if(params.y < 0){
						params.y = 0;
					}
					
					if(params.x > wm.getDefaultDisplay().getWidth()-view.getWidth()){
						params.x = wm.getDefaultDisplay().getWidth()-view.getWidth();
					}
					
					if(params.y > wm.getDefaultDisplay().getHeight()-view.getHeight()){
						params.y = wm.getDefaultDisplay().getHeight()-view.getHeight();
					}
					
					wm.updateViewLayout(view, params);
					
					//重新记录
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:// 离开
					
					Editor editor = sp.edit();
					editor.putInt("lastx", params.x);
					editor.putInt("lasty", params.y);
					editor.commit();

					break;
				}
				return true;
			}
		});
		params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP + Gravity.LEFT;// 设置在那个区域显示
		params.x = sp.getInt("lastx", 0);
		params.y = sp.getInt("lasty", 0);

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		/* | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE */
		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 代码取消注册广播接收者
		unregisterReceiver(receiver);
		receiver = null;
		// 代码取消监听来电
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

}
