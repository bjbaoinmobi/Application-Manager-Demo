package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.TaskInfoProvider;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

public class TaskManagerActivity extends Activity {
	private TextView tv_process_count;
	private TextView tv_mem_info;
	private ListView list_task_item;
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	private TextView tv_status;
	private TaskManagerAdapter adapter;

	private int processCount;
	private long totalRam;
	private long availRam;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		processCount = SystemInfoUtils.getRunningProcessCount(this);
		tv_process_count.setText("�����н��̣�" + processCount + "��");
		totalRam = SystemInfoUtils.getTotalRam(this);
		availRam = SystemInfoUtils.getAvailRam(this);
		tv_mem_info.setText("����/���ڴ棺"
				+ Formatter.formatFileSize(this, availRam) + "/"
				+ Formatter.formatFileSize(this, totalRam));
		list_task_item = (ListView) findViewById(R.id.list_task_item);
		fillData();
		list_task_item.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			// ����һ�����͵��õķ���
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > userTaskInfos.size()) {
					tv_status.setText("ϵͳ����(" + systemTaskInfos.size() + ")");
				} else {
					tv_status.setText("�û�����(" + userTaskInfos.size() + ")");
				}
			}
		});

		list_task_item.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("���������ұ����ˡ���");
				TaskInfo taskInfo = null;
				if (position == 0) {// �û�����ı�ǩ
					return;
				} else if (position == (userTaskInfos.size() + 1)) {// ϵͳ�����ǩ
					return;
				} else if (position <= userTaskInfos.size()) {// �û�����
					taskInfo = userTaskInfos.get(position - 1);
				} else {// ϵͳ����
					taskInfo = systemTaskInfos.get(position - 1
							- userTaskInfos.size() - 1);
				}
				if (getPackageName().equals(taskInfo.getPackname())) {
					return;
				}
				if (taskInfo.isChecked()) {
					taskInfo.setChecked(false);
				} else {
					taskInfo.setChecked(true);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void fillData() {
		// ��ȡ��ȫ���Ľ�����Ϣ
		taskInfos = TaskInfoProvider.getTaskInfos(this);
		userTaskInfos = new ArrayList<TaskInfo>();
		systemTaskInfos = new ArrayList<TaskInfo>();
		for (TaskInfo taskInfo : taskInfos) {
			if (taskInfo.isUserTask()) {
				userTaskInfos.add(taskInfo);
			} else {
				systemTaskInfos.add(taskInfo);
			}
		}
		adapter = new TaskManagerAdapter();
		list_task_item.setAdapter(adapter);
	}

	private class TaskManagerAdapter extends BaseAdapter {
		// ���ؼ����ж��ٸ���Ŀ
		@Override
		public int getCount() {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			boolean showsystem = sp.getBoolean("showsystem", true);
			if (showsystem) {
				return userTaskInfos.size() + 1 + systemTaskInfos.size() + 1;
			}else{
				return userTaskInfos.size() + 1 ;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TaskInfo taskInfo;
			if (position == 0) {// �û�����ı�ǩ
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(0xff888888);
				tv.setText("�û�����(" + userTaskInfos.size() + ")");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {// ϵͳ�����ǩ
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(0xff888888);
				tv.setText("ϵͳ����(" + systemTaskInfos.size() + ")");
				return tv;
			} else if (position <= userTaskInfos.size()) {// �û�����
				taskInfo = userTaskInfos.get(position - 1);
			} else {// ϵͳ����
				taskInfo = systemTaskInfos.get(position - 1
						- userTaskInfos.size() - 1);
			}
			View view = null;
			ViewHolder holder = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_task_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_memsize = (TextView) view
						.findViewById(R.id.tv_memsize);
				holder.cb = (CheckBox) view.findViewById(R.id.cb_status);
				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(taskInfo.getIcon());
			holder.tv_name.setText(taskInfo.getName());
			holder.tv_memsize.setText("�ڴ�ռ�ã�"
					+ Formatter.formatFileSize(getApplicationContext(),
							taskInfo.getMemsize()));
			// //����ҵ��bean�������Ϣ����checkbox�Ĺ�ѡ״̬
			// holder.cb.setOnCheckedChangeListener(new
			// OnCheckedChangeListener() {
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView, boolean
			// isChecked) {
			// taskInfo.setChecked(isChecked);
			// }
			// });
			holder.cb.setChecked(taskInfo.isChecked());
			if (getPackageName().equals(taskInfo.getPackname())) {
				holder.cb.setVisibility(View.INVISIBLE);
			} else {
				holder.cb.setVisibility(View.VISIBLE);
			}
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

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memsize;
		CheckBox cb;
	}

	/**
	 * ȫѡ
	 * 
	 * @param view
	 */
	public void selectAll(View view) {
		for (TaskInfo info : userTaskInfos) {
			if (getPackageName().equals(info.getPackname())) {
				continue;
			}
			info.setChecked(true);
		}
		for (TaskInfo info : systemTaskInfos) {
			info.setChecked(true);
		}
		// ֪ͨ������¡�
		adapter.notifyDataSetChanged();
	}

	/**
	 * ���
	 * 
	 * @param view
	 */
	public void unselectAll(View view) {
		for (TaskInfo info : userTaskInfos) {
			info.setChecked(false);
		}
		for (TaskInfo info : systemTaskInfos) {
			info.setChecked(false);
		}
		// ֪ͨ������¡�
		adapter.notifyDataSetChanged();
	}

	/**
	 * �����̨�Ľ���
	 * 
	 * @param view
	 */
	public void cleanProcess(View view) {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		long savedMem = 0;
		int count = 0;
		// ��ű�ɱ���Ľ���
		List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();
		for (TaskInfo info : userTaskInfos) {
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
				count++;
				savedMem += info.getMemsize();
				killedTaskInfos.add(info);
			}
		}
		for (TaskInfo info : systemTaskInfos) {
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
				count++;
				savedMem += info.getMemsize();
				killedTaskInfos.add(info);
			}
		}
		for (TaskInfo info : killedTaskInfos) {
			if (info.isUserTask()) {
				userTaskInfos.remove(info);
			} else {
				systemTaskInfos.remove(info);
			}
		}
		// ˢ�½���
		// fillData();
		adapter.notifyDataSetChanged();
		processCount -= count;
		tv_process_count.setText("�����н��̣�" + processCount + "��");
		availRam += savedMem;
		tv_mem_info.setText("����/���ڴ棺"
				+ Formatter.formatFileSize(this, availRam) + "/"
				+ Formatter.formatFileSize(this, totalRam));
		Toast.makeText(
				this,
				"ɱ����" + count + "�����̣��ͷ���"
						+ Formatter.formatFileSize(this, savedMem) + "���ڴ�", 1)
				.show();
	}

	/**
	 * �������ý���
	 * 
	 * @param view
	 */
	public void enterSetting(View view) {
		Intent intent = new Intent(this, TaskManagerSettingActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
