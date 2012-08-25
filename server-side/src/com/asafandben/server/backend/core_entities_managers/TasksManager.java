package com.asafandben.server.backend.core_entities_managers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.User;
import com.asafandben.dal.cache.GenericCache;
import com.asafandben.utilities.StringUtilities;

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

	public List<Task> getTasks(String[] requestTasks) {
		List<Task> returnTasks = new ArrayList<Task>();

		for (int i = 0; i < requestTasks.length; i++) {
			returnTasks.add(tasksCache.find(Long.parseLong(requestTasks[i], 10)));
		}

		return returnTasks;
	}
	
	public void createDummyInformation() {
		Task task1 = new Task();
		task1.setID(123L);
		task1.setCreationDate(new Date());
		User benBenedek = usersCache.find("ben@benedek.com");

		User anarAzdalayav = new User("anara@gmail.com");

		if (benBenedek == null) {
			benBenedek = new User("ben@benedek.com");
			benBenedek.setFirstName("Ben");
			benBenedek.setLastName("Benedek");
			benBenedek.setNickname("Chucky");
			try {
				benBenedek.setPassword(StringUtilities
						.getMD5StringfromString("1234"));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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

		Task newTask = new Task();
		newTask.setTaskName("First Task");

		List bensTasks = new ArrayList<Task>();
		bensTasks.add(newTask);

		benBenedek.setTasks(bensTasks);

		List bensEmployees = new ArrayList<User>();
		List asafEmployees = new ArrayList<User>();
		
		bensEmployees.add(asafRatzon);
		asafEmployees.add(anarAzdalayav);
		asafEmployees.add(benBenedek);

		
		benBenedek.setUsersIManage(bensEmployees);
		asafRatzon.setUsersIManage(asafEmployees);

		usersCache.save(benBenedek);
	}
	
}
