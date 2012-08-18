package com.asafandben.server.backend.core_entities_managers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

	public List<User> getUsers(String attribute, String[] requestUsers) {
		// TODO STUB - LOGIC
		return null;

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
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
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
		newRegisteredUser.setTasks(null);
		newRegisteredUser.setUsersIManage(null);
		
		try {
			newRegisteredUser.setPassword(StringUtilities.getMD5StringfromString(password1));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		usersCache.save(newRegisteredUser);
		
		return isRegistrationValid;
		
	}
	
	

}
