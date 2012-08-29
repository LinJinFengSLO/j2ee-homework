package com.asafandben.server.backend.core_entities_managers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.User;
import com.asafandben.bl.core_entities.User.Permission;
import com.asafandben.dal.cache.GenericCache;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.StringUtilities;

public class UsersManager {

	private static UsersManager _instance;

	private static GenericCache<User, String> usersCache = new GenericCache<User, String>(User.class);

	private UsersManager() {
	}

	public static UsersManager getInstance() {
		if (_instance == null)
			_instance = new UsersManager();
		return _instance;
	}

	public List<User> getUsers(String currentUser, String[] requestUsersIds) {
		List<User> returnUsers = new ArrayList<User>();

		User requestingUser = getUserById(currentUser);
		
		if (requestingUser != null && requestUsersIds.length >= 1) {
			boolean isRequestingUserAdmin = isUserAdmin(requestingUser);
			
			// If all users are requested
			if (requestUsersIds[0].equals(FrontEndToBackEndConsts.ALL_ENTITIES_REQUESTED)) {
				returnUsers = isRequestingUserAdmin ? usersCache.getAll() : null;			
			} else {
			// If specific users are requested
				for (int i = 0; i < requestUsersIds.length; i++) {
					User requestedUser = getUserById(requestUsersIds[i]);
					if (requestedUser != null) {
						// Check if user is allowed to get this information:
						boolean isUserAllowedtoGetInformation = (isRequestingUserAdmin || requestingUser.getUsersIManage().contains(requestedUser) || requestedUser.equals(requestingUser));
	
						if (isUserAllowedtoGetInformation)
							returnUsers.add(requestedUser);
					}
				}
			}
		} else {
			returnUsers = null;
		}

		return returnUsers;
	}

	public static User getUserById(String userId) {
		return usersCache.find(userId);
	}
	
	public boolean isUserAdmin(User user) {
		return (user.getPermission() == Permission.ADMIN);
	}

	public boolean checkCredentials(String username, String password) {
		boolean isLoginValid = false;

		User loggedInUser = getUserById(username);

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
			String lastName, String nickName, String password1, String password2)
			throws UnsupportedEncodingException {

		boolean isRegistrationValid = true;

		if (getUserById(email) != null)
			throw new RuntimeException("User with email " + email
					+ " already exists in the system.");

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
			newRegisteredUser.setPassword(StringUtilities
					.getMD5StringfromString(password1));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		saveUser(newRegisteredUser);

		return isRegistrationValid;

	}

	// TODO: Remove this method. This only works if a user with email
	// ben@benbenedek.com already exists.
	public void createDummyInformation() {
		User asafRatzon = new User("asaf.ratzon@gmail.com");
		User benBenedek = usersCache.find("ben@benedek.com");

		User anarAzdalayav = new User("anara@gmail.com");

		if (benBenedek == null) {
			benBenedek = new User("ben@benedek.com");
			benBenedek.setFirstName("Ben");
			benBenedek.setLastName("Benedek");
			benBenedek.setNickname("Chucky");
			try {
				benBenedek.setPassword(StringUtilities.getMD5StringfromString("1234"));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		benBenedek.setPermission(User.Permission.ADMIN);
		
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

		List<Task> bensTasks = new ArrayList<Task>();
		bensTasks.add(newTask);

		benBenedek.setTasks(bensTasks);

		List<User> bensEmployees = new ArrayList<User>();
		List<User> asafEmployees = new ArrayList<User>();

		bensEmployees.add(asafRatzon);
		bensEmployees.add(anarAzdalayav);
		asafEmployees.add(anarAzdalayav);
		asafEmployees.add(benBenedek);

		benBenedek.setUsersIManage(bensEmployees);
		asafRatzon.setUsersIManage(asafEmployees);

		saveUser(benBenedek);
	}

	public void editUserProfile(String email, String firstName,	String lastName, String nickName) {
		
		User userToEdit = getUserById(email);
		
		if (userToEdit == null)
			throw new RuntimeException("User with email " + email
					+ " doesn't exists in the system.");
		
		if (!StringUtilities.isEmailValid(email))
			throw new RuntimeException("Invalid email address: " + email + ".");
		
		userToEdit.setFirstName(firstName);
		userToEdit.setLastName(lastName);
		userToEdit.setNickname(nickName);
		
		saveUser(userToEdit);

	}

	public void changeUserPassword(String email, String password1, String password2, String oldPassword) throws NoSuchAlgorithmException {
		User userToEdit = getUserById(email);
		
		if (userToEdit == null)
			throw new RuntimeException("User with email " + email
					+ " doesn't exists in the system.");
		
		if (!password1.equals(password2)) {
			throw new RuntimeException("Passwords do not match.");
		}
		
		if (checkCredentials(email, oldPassword)) {
			userToEdit.setPassword(StringUtilities.getMD5StringfromString(password1));
			saveUser(userToEdit);
		}
		else {
			throw new RuntimeException("Old password is incorrect.");
		}
		
	}

	private void saveUser(User userToSave) {
		usersCache.save(userToSave);
		
	}

	public void assignTaskToUser(String email, String employeeEmail, Long taskID) {
		Long[] taskIDs = { taskID };
		User boss = getUserById(email);
		User employee = getUserById(employeeEmail);
		
		if ((boss==null)||(employee==null)) {
			throw new RuntimeException("Missing information.");
		}
		
		if (isAssignedToUser(employee, boss)) {
			TasksManager taskManager = TasksManager.getInstance();
			taskManager.assignEmployeeToTask(taskID, employee);
			//taskManager.getTasks(boss, taskIDs);
		}
		
	}

	private boolean isAssignedToUser(User employee, User boss) {
		List<User> usersIManage = boss.getUsersIManage();
		
		if (boss.getPermission() == Permission.ADMIN)
			return true;
		
		for (User currentUser : usersIManage) {
			if (currentUser.equals(employee))
				return true;
		}
		return false;
	}

}
