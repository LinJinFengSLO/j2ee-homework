package com.asafandben.server.backend.core_entities_managers;

import java.util.List;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.dal.cache.GenericCache;

public class TasksManager {
	private static TasksManager _instance;


	private static GenericCache<Task, Long> tasksCache = new GenericCache<Task, Long>(
			Task.class);

	private TasksManager() {
	}

	public static TasksManager getInstance() {
		if (_instance == null)
			_instance = new TasksManager();
		return _instance;
	}

	
	
}
