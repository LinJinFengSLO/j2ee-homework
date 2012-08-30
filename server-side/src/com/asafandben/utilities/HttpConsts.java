package com.asafandben.utilities;

public final class HttpConsts {
	
	// Do not start this.
	private HttpConsts() {};

	public static final String LOGIN_COOKIE_NAME = "TaskManagementLogin";
	public static final int LOGIN_COOKIE_AGE = (60 * 60); // One Hour
	public static final String COOKIE_SEPERATOR = ":";
	public static final String GET_URL_SEPEARTOR = ",";
	public static final String PROJECT_PATH = "/TaskManagement";
	public static final String USER_PATH = PROJECT_PATH + "/" + "user";
	public static final String TASK_PATH = PROJECT_PATH + "/" + "task";
	
	// User Information:
	public static final String USERNAME_PARAMETER_PASSWORD = "password";
	public static final String USERNAME_PARAMETER_EMAIL = "email";
	public static final String USERNAME_PARAMETER_NICKNAME = "nickname";
	public static final String USERNAME_PARAMETER_FIRSTNAME = "firstname";
	public static final String USERNAME_PARAMETER_LASTNAME = "lastname";
	public static final String USERNAME_PARAMETER_FIRSTPASSWORD = "password1";
	public static final String USERNAME_PARAMETER_SECONDPASSWORD = "password2";
	public static final String USERNAME_PARAMETER_OLDPASSWORD = "oldpassword";
	public static final String USERNAME_PARAMETER_EMPLOYEE_EMAIL = "employeemail";

	// User Action Information:
	public static final String SUCCESSFUL_LOGIN_REDIRECT_URL = "";
	public static final String XML_CONTENT_TYPE = "text/xml";
	public static final String USER_PARAMETER_NAME = "usersIds";
	
	
	// Task Information:
	public static final String TASK_PARAMETER_NAME = "tasksIds";
	public static final String TASK_PARAMETER_NAME_ID = "tasksId";

	// Project Information
	public static final String PROJECT_PARAMETER_NAME = "projectsIds";
	
	
	// Action Parameters
	public static final String ACTION_PARAMETER_NAME = "ActionName";
	public static final String EDIT_USER_PROFILE_ACTION_NAME = "EditProfile";
	public static final String EDIT_USER_PASSWORD_ACTION_NAME = "EditPassword";
	public static final String ASSIGN_TASK_TO_USER_ACTION_NAME = "AssignTask";
	
}
