package com.itheima.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.TaskInfoProvider;

public class TestTaskInfoProvider extends AndroidTestCase {
	public void testGetTaskInfos() throws Exception{
		List<TaskInfo> infos = TaskInfoProvider.getTaskInfos(getContext());
		for(TaskInfo info: infos){
			System.out.println(info.toString());
		}
	}
}
