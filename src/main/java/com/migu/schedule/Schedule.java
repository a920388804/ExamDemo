package com.migu.schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.TaskInfo;

/*
*类名和方法不能修改
 */
public class Schedule {
	List<Task> queue = new LinkedList<Task>();
	List<Server> pool = new ArrayList<Server>();
	List<Task> allTask = new ArrayList<Task>();

	public int init() {
		pool = new ArrayList<Server>();
		return ReturnCodeKeys.E001;
	}

	public int registerNode(int nodeId) {
		if (nodeId <= 0) {
			return ReturnCodeKeys.E004;
		}
		if (pool.size() > 0) {
			for (int i = 0; i < pool.size(); i++) {
				if (pool.get(i).nodeId == nodeId) {
					return ReturnCodeKeys.E005;
				}
			}
		}
		Server server = new Server(nodeId);
		pool.add(server);
		return ReturnCodeKeys.E003;
	}

	public int unregisterNode(int nodeId) {
		if (nodeId <= 0) {
			return ReturnCodeKeys.E004;
		}
		if (pool.size() > 0) {
			for (int i = 0; i < pool.size(); i++) {
				if (pool.get(i).nodeId == nodeId) {
					if (pool.get(i).tasks.size() > 0) {
						List<Task> tasks = pool.get(i).tasks;
						for (int j = 0; j < tasks.size(); j++) {
							tasks.get(j).nodeId = -1;
							queue.add(tasks.get(j));
						}
					}
					pool.remove(i);
					return ReturnCodeKeys.E006;
				}
			}
		}
		return ReturnCodeKeys.E007;
	}

	public int addTask(int taskId, int consumption) {
		if (taskId <= 0) {
			return ReturnCodeKeys.E009;
		}
		for (int i = 0; i < allTask.size(); i++) {
			if (allTask.get(i).taskId == taskId) {
				return ReturnCodeKeys.E010;
			}
		}
		Task task = new Task(taskId, consumption);
		queue.add(task);
		allTask.add(task);
		return ReturnCodeKeys.E008;
	}

	public int deleteTask(int taskId) {
		if (taskId <= 0) {
			return ReturnCodeKeys.E009;
		}
		for (int i = 0; i < allTask.size(); i++) {
			if (allTask.get(i).taskId == taskId) {
				allTask.remove(i);
				if (allTask.get(i).nodeId == -1) {
					for (int j = 0; j < pool.size(); j++) {
						if (queue.get(j).taskId == taskId) {
							queue.remove(j);
							return ReturnCodeKeys.E011;
						}
					}
				} else {
					for (int j = 0; j < pool.size(); j++) {
						if (pool.get(j).nodeId == allTask.get(i).nodeId) {
							List<Task> tasks = pool.get(j).tasks;
							for (int k = 0; k < tasks.size(); k++) {
								if (tasks.get(k).taskId == taskId) {
									tasks.remove(k);
									return ReturnCodeKeys.E011;
								}
							}
						}
					}
				}
			}
		}
		return ReturnCodeKeys.E012;
	}

	public int scheduleTask(int threshold) {
		if(threshold<=0) {
			return ReturnCodeKeys.E002;
		}
		for(int i=0;i<allTask.size();i++) {
			if(allTask.get(i).nodeId != -1) {
				if(i==allTask.size()-1) {
					return ReturnCodeKeys.E014;
				}
			}
		}
		
		
		int num = pool.size();
		int tasknum = allTask.size()/num;
		for(int i=0;i<num;i++) {
			for(int j=i*tasknum;j<(i+1)*tasknum;j++) {
				allTask.get(j).nodeId=pool.get(i).nodeId;
				System.out.println(pool.get(i).nodeId);
			}
		}
		return ReturnCodeKeys.E013;
	}

	public int queryTaskStatus(List<TaskInfo> tasks) {
		if (null == tasks) {
			return ReturnCodeKeys.E016;
		}
		for (int i = 0; i < allTask.size(); i++) {
			TaskInfo ti = new TaskInfo();
			ti.setNodeId(allTask.get(i).nodeId);
			ti.setTaskId(allTask.get(i).taskId);
			tasks.add(ti);
		}
		return ReturnCodeKeys.E015;
	}

}
