package com.asafandben.server.backend.core_entities_managers;

import java.util.List;

import com.asafandben.bl.core_entities.User;

public class UsersManager {

	private static UsersManager _instance;
	
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

}
