package com.asafandben.server.backend.core_entities_managers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.User;
import com.asafandben.bl.core_entities.User.Permission;
import com.asafandben.dal.cache.GenericCache;
import com.asafandben.utilities.StringUtilities;

public class UsersManager {

	private static UsersManager _instance;

	private static GenericCache<User, String> usersCache = new GenericCache<User, String>(
			User.class);

	private UsersManager() {
	}

	public static UsersManager getInstance() {
		if (_instance == null)
			_instance = new UsersManager();
		return _instance;
	}

	public List<User> getUsers(String currentUser, String[] requestUsers) {
		List<User> returnUsers = new ArrayList<User>();
		
		returnUsers.add(usersCache.find(currentUser));
		for (int i = 0; i < requestUsers.length; i++) {
			returnUsers.add(usersCache.find(requestUsers[i]));
		}
		
		return returnUsers;

	}

	public boolean checkCredentials(String username, String password) {
		boolean isLoginValid = false;

		User loggedInUser = usersCache.find(username);

		if (loggedInUser != null) {
			try {
				String realPassword = loggedInUser.getPassword();
				String tryPassword = StringUtilities
						.getMD5StringfromString(password);
				if (realPassword.equals(tryPassword))
					isLoginValid = true;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return isLoginValid;

	}

	public boolean createNewUser(String email, String firstName,
			String lastName, String nickName, String password1, String password2) throws UnsupportedEncodingException {
		
		boolean isRegistrationValid = true;
		
		if (usersCache.find(email)!=null)
			throw new RuntimeException("User with email " + email + " already exists in the system.");
		
		if (!StringUtilities.isEmailValid(email))
			throw new RuntimeException("Invalid email address: " + email + ".");
		
		if (!password1.equals(password2))
			throw new RuntimeException("Both passwords supplied do not match.");
		
		User newRegisteredUser = new User(email);
		newRegisteredUser.setFirstName(firstName);
		newRegisteredUser.setLastName(lastName);
		newRegisteredUser.setNickname(nickName);
		newRegisteredUser.setPermission(Permission.USER);
		newRegisteredUser.setTasks(new ArrayList<Task>());
		newRegisteredUser.setUsersIManage(new ArrayList<User>());
		
		try {
			newRegisteredUser.setPassword(StringUtilities.getMD5StringfromString(password1));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		usersCache.save(newRegisteredUser);
		
		return isRegistrationValid;
		
	}
	
	// TODO: Remove this method. This only works if a user with email ben@benbenedek.com already exists.
	public void createDummyInformation() {
		User asafRatzon = new User("asaf.ratzon@gmail.com");
		User benBenedek = usersCache.find("ben@benedek.com");
		
		
		asafRatzon.setEmail("asaf.ratzon@gmail.com");
		asafRatzon.setFirstName("Asaf");
		asafRatzon.setLastName("Ratzon");
		asafRatzon.setNickname("Chicky");
		asafRatzon.setPassword("12345");
		
		Task newTask = new Task();
		newTask.setTaskName("First Task");
		
		List bensTasks = new ArrayList<Task>();
		bensTasks.add(newTask);
		
		benBenedek.setTasks(bensTasks);
		
		List bensEmployees = new ArrayList<User>();
		bensEmployees.add(asafRatzon);
		
		benBenedek.setUsersIManage(bensEmployees);
		
		usersCache.save(benBenedek);
	}
	
	

}
