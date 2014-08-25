package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DragViewActivity extends Activity {

	protected static final String TAG = "DragViewActivity";
	private ImageView iv_drag_view;
	private SharedPreferences sp;

	private WindowManager wm;
	private int mWidth;
	private int mHeight;

	private TextView tv_top;
	private TextView tv_bottom;

	long[] mHits = new long[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		iv_drag_view = (ImageView) findViewById(R.id.iv_drag_view);
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		tv_top = (TextView) findViewById(R.id.tv_top);
		tv_bottom = (TextView) findViewById(R.id.tv_bottom);
		mWidth = wm.getDefaultDisplay().getWidth();
		mHeight = wm.getDefaultDisplay().getHeight();
		sp = getSharedPreferences("config", MODE_PRIVATE);

		int lastx = sp.getInt("lastx", 0);
		int lasty = sp.getInt("lasty", 0);

		if (lasty > (mHeight / 2)) {
			// 控件View对象在底部
			tv_top.setVisibility(View.VISIBLE);
			tv_bottom.setVisibility(View.INVISIBLE);
		} else {
			// 控件View对象在顶部
			tv_top.setVisibility(View.INVISIBLE);
			tv_bottom.setVisibility(View.VISIBLE);
		}
		Log.i(TAG,
				"控件的左下方的X=" + iv_drag_view.getLeft() + iv_drag_view.getWidth());

		// 这个方法是布局机制的第二阶段才能使用的方法，第一个阶段是测量
		// 这个方法在第一阶段，不起作用
		// iv_drag_view.layout(lastx, lasty, iv_drag_view.getLeft()
		// + iv_drag_view.getWidth(),
		// iv_drag_view.getTop()+iv_drag_view.getHeight());

		// 用在第一阶段就起作用的方法
		// 导包要导相对布局的包
		LayoutParams params = (LayoutParams) iv_drag_view.getLayoutParams();
		params.leftMargin = lastx;
		params.topMargin = lasty;
		iv_drag_view.setLayoutParams(params);

		iv_drag_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				// 1300 >2000 - 500 == 1500
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {

					// 双击事件的代码了

					iv_drag_view.layout(mWidth / 2 - iv_drag_view.getWidth()
							/ 2, iv_drag_view.getTop(), mWidth / 2
							+ iv_drag_view.getWidth() / 2,
							iv_drag_view.getBottom());
					
					
					Editor editor = sp.edit();
					// 保存X坐标
					editor.putInt("lastx", iv_drag_view.getLeft());
					// 保存Y坐标
					editor.putInt("lasty", iv_drag_view.getTop());

					editor.commit();
					
					
				}

			}
		});

		Log.i(TAG, "上次保存的坐标(" + lastx + "," + lasty + ")");
		iv_drag_view.setOnTouchListener(new OnTouchListener() {
			int startX = 0;
			int startY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 第一次触摸屏幕的时候触发
					// 1.第一次按下，记录坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.i(TAG, "按下");

					break;

				case MotionEvent.ACTION_MOVE:// 当手指在屏幕上移动的时候触发
					// 2.手指在屏幕上移动来到了一个新的点
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					// 3.计算偏移量
					int dX = newX - startX;
					int dY = newY - startY;
					Log.i(TAG, "移动");
					// 4.更新控件的位置
					int newl = iv_drag_view.getLeft() + dX;
					int newt = iv_drag_view.getTop() + dY;
					int newr = iv_drag_view.getRight() + dX;
					int newb = iv_drag_view.getBottom() + dY;

					if (newl < 0 || newt < 0 || newr > mWidth
							|| newb > (mHeight - 30)) {
						break;
					}

					if (newt > (mHeight / 2)) {
						// 控件View对象在底部
						tv_top.setVisibility(View.VISIBLE);
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						// 控件View对象在顶部
						tv_top.setVisibility(View.INVISIBLE);
						tv_bottom.setVisibility(View.VISIBLE);
					}

					iv_drag_view.layout(newl, newt, newr, newb);

					// 5.重新记录坐标点
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;

				case MotionEvent.ACTION_UP:// 当我们的手指离开屏幕的时候触发
					Log.i(TAG, "离开");
					Editor editor = sp.edit();
					// 保存X坐标
					editor.putInt("lastx", iv_drag_view.getLeft());
					// 保存Y坐标
					editor.putInt("lasty", iv_drag_view.getTop());

					editor.commit();

					break;
				}

				return false;
			}
		});
	}
}
