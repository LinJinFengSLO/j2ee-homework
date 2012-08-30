package com.asafandben.server.backend.core_entities_managers;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.Task.Status;
import com.asafandben.bl.core_entities.User;
import com.asafandben.dal.cache.GenericCache;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.StringUtilities;

public class TasksManager {
	private static TasksManager _instance;
	private static UsersManager _usersManagerInstance;

	private static GenericCache<Task, Long> tasksCache = new GenericCache<Task, Long>(Task.class);

	private TasksManager() {
		initOtherManagers();
	}

	private void initOtherManagers() {
		_usersManagerInstance = UsersManager.getInstance();
	}

	public static TasksManager getInstance() {
		if (_instance == null)
			_instance = new TasksManager();
		
		return _instance;
	}

	public List<Task> getTasks(String currentUser, Long[] requestTasksIds) {
		List<Task> returnTasks = new ArrayList<Task>();

		User requestingUser = UsersManager.getUserById(currentUser);
		
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

	public void assignEmployeeToTask(Long taskID, User user) {
		Task taskToAdd = tasksCache.find(taskID);
		
		List<User> usersAssignedToTask = taskToAdd.getUsersAssigned();
		
		if (taskToAdd!=null) {
			if (user!=null) {
				if (!usersAssignedToTask.contains(user)) {
					usersAssignedToTask.add(user);
					taskToAdd.setUsersAssigned(usersAssignedToTask);
					tasksCache.save(taskToAdd);
				}
			}
		}
		
	}

	public void createOrEditTask(String taskID, String taskName,
			String taskDescription, String taskCreationDate,
			String taskDueDate, List<Long> priorTasks,
			List<String> assignedUsers, String taskStatus) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd"); 
		
		Task task = getTaskByID(Long.parseLong(taskID));
		
		

		
		if (task==null) {
			task = new Task();
			task.setID(Long.parseLong(taskID));
		}
		
		// TODO: check if assigne is assigned to user.

		validateAndEditTaskInformation(taskName, taskDescription, taskCreationDate, taskDueDate,
				taskStatus, dateFormat, task);

		assignPriorTasksToTask(priorTasks, task);
		
		assignUsersToTask(assignedUsers, task);
		
		tasksCache.save(task);
		
		
		
	}

	private List<Task> assignPriorTasksToTask(List<Long> priorTasks, Task task) {
		List<Task> currentPriorTasks = task.getPriorTasks();
		for (Long priorTask : priorTasks) {
			Task taskToAdd = getTaskByID(priorTask);
			if (taskToAdd!=null)
				if (!currentPriorTasks.contains(priorTask))
					currentPriorTasks.add(taskToAdd);
		}
		task.setPriorTasks(currentPriorTasks);
		return currentPriorTasks;
	}

	private void assignUsersToTask(List<String> assignedUsers, Task task) {
		List<User> currentAssignedUsers = task.getUsersAssigned();
		for (String currentUser : assignedUsers) {
			User user = UsersManager.getUserById(currentUser);
			
			if (user!=null)
				if (!currentAssignedUsers.contains(user)) {
					currentAssignedUsers.add(user);
					_usersManagerInstance.assignTaskToUserNoCheck(user, task);
				}
		}
		task.setUsersAssigned(currentAssignedUsers);
	}

	private void validateAndEditTaskInformation(String taskName, String taskDescription,
			String taskCreationDate, String taskDueDate, String taskStatus,
			SimpleDateFormat dateFormat, Task task) throws ParseException {
		if (taskName!=null) {
			task.setTaskName(taskName);
		}
		else {
			if (task.getTaskName()==null)
				throw new RuntimeException("Can't have a task without a name");
		}
		
		if (taskDescription!=null) {
			task.setTaskDescription(taskDescription);
		}
		else {
			if (task.getTaskDescription()==null)
				throw new RuntimeException("Can't have a task without description");
		}
		
		if (taskCreationDate!=null) {
			task.setCreationDate(dateFormat.parse(taskCreationDate));
		}
		else {
			if (task.getCreationDate()==null) {
				Date date = new Date();
				task.setCreationDate(date);
			}
		}
		
		if (taskDueDate!=null) {
			task.setDueDate(dateFormat.parse(taskDueDate));
		}
		else {
			if (task.getDueDate()==null) {
				throw new RuntimeException("Can't have a task without due date.");
			}
		}
		
		if (taskStatus!=null) {
			
			task.setDueDate(dateFormat.parse(taskDueDate));
		}
		else {
			if (task.getDueDate()==null) {
				throw new RuntimeException("Can't have a task without due date.");
			}
		}
		
		if (taskStatus!=null) {
			task.setStatus(Status.valueOf(taskStatus));
		}
		else {
			task.setStatus(Status.NOT_STARTED);
		}
	}

	
	
	private Task getTaskByID(Long taskID) {
		return tasksCache.find(taskID);
	}
	
}
