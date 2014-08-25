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
			// �ؼ�View�����ڵײ�
			tv_top.setVisibility(View.VISIBLE);
			tv_bottom.setVisibility(View.INVISIBLE);
		} else {
			// �ؼ�View�����ڶ���
			tv_top.setVisibility(View.INVISIBLE);
			tv_bottom.setVisibility(View.VISIBLE);
		}
		Log.i(TAG,
				"�ؼ������·���X=" + iv_drag_view.getLeft() + iv_drag_view.getWidth());

		// ��������ǲ��ֻ��Ƶĵڶ��׶β���ʹ�õķ�������һ���׶��ǲ���
		// ��������ڵ�һ�׶Σ���������
		// iv_drag_view.layout(lastx, lasty, iv_drag_view.getLeft()
		// + iv_drag_view.getWidth(),
		// iv_drag_view.getTop()+iv_drag_view.getHeight());

		// ���ڵ�һ�׶ξ������õķ���
		// ����Ҫ����Բ��ֵİ�
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

					// ˫���¼��Ĵ�����

					iv_drag_view.layout(mWidth / 2 - iv_drag_view.getWidth()
							/ 2, iv_drag_view.getTop(), mWidth / 2
							+ iv_drag_view.getWidth() / 2,
							iv_drag_view.getBottom());
					
					
					Editor editor = sp.edit();
					// ����X����
					editor.putInt("lastx", iv_drag_view.getLeft());
					// ����Y����
					editor.putInt("lasty", iv_drag_view.getTop());

					editor.commit();
					
					
				}

			}
		});

		Log.i(TAG, "�ϴα��������(" + lastx + "," + lasty + ")");
		iv_drag_view.setOnTouchListener(new OnTouchListener() {
			int startX = 0;
			int startY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// ��һ�δ�����Ļ��ʱ�򴥷�
					// 1.��һ�ΰ��£���¼����
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.i(TAG, "����");

					break;

				case MotionEvent.ACTION_MOVE:// ����ָ����Ļ���ƶ���ʱ�򴥷�
					// 2.��ָ����Ļ���ƶ�������һ���µĵ�
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					// 3.����ƫ����
					int dX = newX - startX;
					int dY = newY - startY;
					Log.i(TAG, "�ƶ�");
					// 4.���¿ؼ���λ��
					int newl = iv_drag_view.getLeft() + dX;
					int newt = iv_drag_view.getTop() + dY;
					int newr = iv_drag_view.getRight() + dX;
					int newb = iv_drag_view.getBottom() + dY;

					if (newl < 0 || newt < 0 || newr > mWidth
							|| newb > (mHeight - 30)) {
						break;
					}

					if (newt > (mHeight / 2)) {
						// �ؼ�View�����ڵײ�
						tv_top.setVisibility(View.VISIBLE);
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						// �ؼ�View�����ڶ���
						tv_top.setVisibility(View.INVISIBLE);
						tv_bottom.setVisibility(View.VISIBLE);
					}

					iv_drag_view.layout(newl, newt, newr, newb);

					// 5.���¼�¼�����
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;

				case MotionEvent.ACTION_UP:// �����ǵ���ָ�뿪��Ļ��ʱ�򴥷�
					Log.i(TAG, "�뿪");
					Editor editor = sp.edit();
					// ����X����
					editor.putInt("lastx", iv_drag_view.getLeft());
					// ����Y����
					editor.putInt("lasty", iv_drag_view.getTop());

					editor.commit();

					break;
				}

				return false;
			}
		});
	}
}
