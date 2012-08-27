package com.asafandben.server.backend.core_entities_managers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.User;
import com.asafandben.dal.cache.GenericCache;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.StringUtilities;

public class TasksManager {
	private static TasksManager _instance;

	private static GenericCache<Task, Long> tasksCache = new GenericCache<Task, Long>(Task.class);

	private TasksManager() {
	}

	public static TasksManager getInstance() {
		if (_instance == null)
			_instance = new TasksManager();
		return _instance;
	}

	public List<Task> getTasks(String currentUser, Long[] requestTasksIds) {
		List<Task> returnTasks = new ArrayList<Task>();

		User requestingUser = UsersManager.getInstance().getUserById(currentUser);
		
		if (requestingUser != null && requestTasksIds.length >= 1) {
			boolean isRequestingUserAdmin = UsersManager.getInstance().isUserAdmin(requestingUser);
			
			// If all tasks are requested
			if (requestTasksIds[0].equals(FrontEndToBackEndConsts.ALL_TASKS_REQUESTED)) {
				returnTasks = isRequestingUserAdmin ? tasksCache.getAll() : null;			
			} else {
			// If specific tasks are requested
				for (int i = 0; i < requestTasksIds.length; i++) {
					Task requestedTask = tasksCache.find(requestTasksIds[i]);
					if (requestedTask != null) {
						// Check if user is allowed to get this information:
						boolean isUserAllowedtoGetInformation = (isRequestingUserAdmin || requestingUser.getTasks().contains(requestedTask));
	
						if (isUserAllowedtoGetInformation)
							returnTasks.add(requestedTask);
					}
				}
			}
		} else {
			returnTasks = null;
		}

		return returnTasks;
	}
	
	public void createDummyInformation() {
		Task task1 = new Task();
		task1.setID(123L);
		task1.setTaskName("DummyTask1");
		task1.setTaskDescription("DummyTask1 description");
		task1.setCreationDate(new Date());
		task1.setDueDate(new Date());
		task1.setStatus(Task.Status.NOT_STARTED);

		User anarAzdalayav = new User("anara@gmail.com");
		anarAzdalayav.setEmail("anara@gmail.com");
		anarAzdalayav.setFirstName("Anar");
		anarAzdalayav.setLastName("Azdalayav");
		anarAzdalayav.setNickname("Kushi");
		try {
			anarAzdalayav.setPassword(StringUtilities
					.getMD5StringfromString("123456"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User asafRatzon = new User("asaf.ratzon@gmail.com");
		asafRatzon.setEmail("asaf.ratzon@gmail.com");
		asafRatzon.setFirstName("Asaf");
		asafRatzon.setLastName("Ratzon");
		asafRatzon.setNickname("Chicky");
		try {
			asafRatzon.setPassword(StringUtilities
					.getMD5StringfromString("12345"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		task1.getUsersAssigned().add(anarAzdalayav);
		task1.getUsersAssigned().add(asafRatzon);

		asafRatzon.getTasks().add(task1);
		anarAzdalayav.getTasks().add(task1);
		
		tasksCache.save(task1);
	}
	
}
