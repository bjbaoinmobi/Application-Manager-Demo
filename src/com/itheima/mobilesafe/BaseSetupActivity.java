package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	
	protected SharedPreferences sp;
	// 1.定义手势识别器
	private GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 2. 初始化手势识别器
		detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				
				//处理滑动很慢这种情形 像数/每秒
				if(Math.abs(velocityX)< 200){
					Toast.makeText(BaseSetupActivity.this, "哥们滑动快一点嘛", 0).show();
					return true;
				}
				
				//屏蔽竖直方向滑动
				if(Math.abs((e2.getRawY() - e1.getRawY())) > 100){
					
					//不合法的滑动
					Toast.makeText(BaseSetupActivity.this, "不可以这样滑，姐", 0).show();
					return true;
				}
				

				if ((e2.getRawX() - e1.getRawX()) > 200) {
					// 显示上一个页面
					System.out.println("显示上一个页面");
					showpre();
					return true;
				}

				if ((e1.getRawX() - e2.getRawX()) > 200) {
					// 显示下一个页面；
					shownext();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});
	}
	
	public abstract void shownext();
	public abstract void showpre();
	
	/**
	 * 处理点击事件--显示下一个页面
	 * 
	 */
	
	public void next(View view){
		shownext();
	}
	
	/**
	 * 处理点击事件-显示上一个页面
	 * 
	 */
	
	public void pre(View view){
		showpre();
	}
	

	//3.使用滑动事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	

}
