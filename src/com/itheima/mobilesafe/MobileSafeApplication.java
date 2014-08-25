package com.itheima.mobilesafe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;
import android.util.Printer;
/**
 * 自定义一个类 代表手机卫士的应用程序。
 * @author Administrator
 *
 */
public class MobileSafeApplication extends Application {

	//在应用程序其他的任何对象创建之前调用的方法。开天地。
	@Override
	public void onCreate() {
		super.onCreate();
		//设置了一个自定义的异常处理器
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
	}

	
	private class MyExceptionHandler implements UncaughtExceptionHandler{
		//参数了未捕获的异常就执行下面的代码
		//不能让应用程序原地复活。 这个方法只是在应用程序挂掉之前 留下来遗嘱
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			try {
				System.out.println("产生了一个异常，但是被哥给捕获了。");
				File file = new File(Environment.getExternalStorageDirectory(),"/error.log");
				PrintWriter err = new PrintWriter(file);
				ex.printStackTrace(err);
				err.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//早死早超生
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
