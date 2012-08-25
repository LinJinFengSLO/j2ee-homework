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
	public static final String SUCCESSFUL_LOGIN_REDIRECT_URL = "";	
}
