package com.itheima.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {
	private static final String TAG = "CallSmsSafeActivity";

	private ListView list_callsms_safe;
	private BlackNumberDao dao;
	private LinearLayout ll_loading;

	private int startIndex = 0;

	private MyAdapter adapter;

	private boolean isLoading = false;
	/**
	 * 黑名单所有数据
	 */
	private List<BlackNumberInfo> infos;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			isLoading = false;
			if (adapter == null) {
				adapter = new MyAdapter();
				list_callsms_safe.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();// 通知我们适配器更新UI
			}

			ll_loading.setVisibility(View.INVISIBLE);
			// list_callsms_safe.setSelection(startIndex);//不推荐
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms_safe);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		list_callsms_safe = (ListView) findViewById(R.id.list_callsms_safe);
		dao = new BlackNumberDao(this);

		fillDate();

		list_callsms_safe.setOnScrollListener(new OnScrollListener() {

			// 当状态变化的时候回调该方法
			// 静止--->>滚动
			// 滚动---->>静止
			// 手指滑动--->>惯性滚动
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 静止状态

					int totalCount = infos.size();// 20

					int position = list_callsms_safe.getLastVisiblePosition();// 19

					if (isLoading) {
						Toast.makeText(CallSmsSafeActivity.this, "正在加载中...", 0)
								.show();
						return;
					}

					int TotatCount = dao.getTotatCount();
					if (position >= (TotatCount - 1)) {
						Toast.makeText(CallSmsSafeActivity.this, "已经没有数据了...",
								0).show();

						return;
					}

					if (position == (totalCount - 1)) {

						Toast.makeText(CallSmsSafeActivity.this, "加载更多数据", 0)
								.show();
						startIndex += 20;
						// 再次去请求
						fillDate();
						isLoading = true;
					}

					break;

				case OnScrollListener.SCROLL_STATE_FLING:// 滚动状态

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 手指滑动状态

					break;
				}

			}

			/**
			 * 当ListView滚动的时候回调该方法
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

	}

	private void fillDate() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (infos == null) {
					infos = dao.findPart(startIndex);
				} else {
					infos.addAll(dao.findPart(startIndex));
				}

				// 发一个消息更新界面
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view = null;
			ViewHolder holder = null;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
				Log.i(TAG, "利用历史缓存的的View对象==" + position);
			} else {
				Log.i(TAG, "创建新的View对象==" + position);
				view = View.inflate(CallSmsSafeActivity.this,
						R.layout.list_callsms_safe_item, null);

				// 当我们的View对象一创建，我就去查找并且放在容器里面（类对象）
				holder = new ViewHolder();
				// 该方法也是比较消耗资源的，或者说也是耗时
				holder.tv_number = (TextView) view.findViewById(R.id.tv_number);
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

				view.setTag(holder);// 保存我们对应关系或者说层次关系
			}
			BlackNumberInfo info = infos.get(position);

			holder.tv_number.setText(info.getNumber());

			if ("1".equals(info.getMode())) {
				// 拦截电话
				holder.tv_mode.setText("拦截电话");
			} else if ("2".equals(info.getMode())) {
				// 拦截短信
				holder.tv_mode.setText("拦截短信");
			} else {
				// 拦截所有的
				holder.tv_mode.setText("短信+电话拦截");
			}
			
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("小图标被点击了"+position);
					BlackNumberInfo  info = infos.get(position);
					
					//在数据库里面删除这条数据
					dao.delete(info.getNumber());
					
					//在当前列表也要移除这条信息
					infos.remove(info);
					
					//通知适配器刷新页面
					adapter.notifyDataSetChanged();
					
				}
			});

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}

	/**
	 * 点击事件--添加一条黑名单数据
	 * 
	 * @param view
	 */
	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.dialog_add_black_number,
				null);
		final EditText et_phone = (EditText) contentView
				.findViewById(R.id.et_blacknumber);
		final RadioGroup rg_mode = (RadioGroup) contentView
				.findViewById(R.id.rg_mode);
		Button ok = (Button) contentView.findViewById(R.id.ok);
		Button cancel = (Button) contentView.findViewById(R.id.cancel);
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
				//  得到黑名单号码 拦截模式
				String phone = et_phone.getText().toString().trim();
				//判断空
				if(TextUtils.isEmpty(phone)){
					Toast.makeText(CallSmsSafeActivity.this, "号码为空", 1).show();
					return;
				}
				//取一下拦截模式
				int id = rg_mode.getCheckedRadioButtonId();
				String mode = "3";
				switch (id) {
				case R.id.rb_all:
					mode = "3";//全部拦截
					break;

				case R.id.rb_phone:
					mode = "1";//电话
					break;
					
				case R.id.rb_sms:
					mode = "2";//短信
					break;
				}
				dialog.dismiss();
				BlackNumberInfo info = new BlackNumberInfo();
				//保存数据库里面
				dao.add(phone, mode);//把我们的数据添加到数据库里面去了
				//在我们的当前页面并没有把这条信息添加到当前列表
				info.setMode(mode);
				info.setNumber(phone);
				infos.add(0, info);
				//刷新页面
				adapter.notifyDataSetChanged();// 通知我们适配器更新UI
				
			}
		});
		
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();

	}

}
