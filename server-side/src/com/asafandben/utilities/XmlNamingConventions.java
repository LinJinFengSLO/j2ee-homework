package com.asafandben.utilities;

public abstract class XmlNamingConventions {
	
	//User Entity
	public static final String USER_ROOT_ELEMENT = "User";
	public static final String USER_MAIL_ELEMENT = "Email";
	public static final String USER_NICKNAME_ELEMENT = "Nickname";
	public static final String USER_FIRSTNAME_ELEMENT = "FirstName";
	public static final String USER_LASTNAME_ELEMENT = "FirstName";
	public static final String USER_TASKS_ELEMENT = "Tasks";
	public static final String USER_USERSIMANAGE_ELEMENT = "UsersIManage";
	public static final String USER_ID_ELEMENT = "Id";
	public static final String USER_PERMISSION_ELEMENT = "Permission";
	
	// Task Entity
	public static final String TASK_ROOT_ELEMENT = "Task";
	public static final String TASK_ID_ELEMENT = "Id";
	public static final String TASK_NAME_ELEMENT = "Name";
	public static final String TASK_DESCRIPTION_ELEMENT = "Description";
	public static final String TASK_CREATIONDATE_ELEMENT = "CreationDate";
	public static final String TASK_DUEDATE_ELEMENT = "DueDate";
	public static final String TASK_PRIORTASKS_ELEMENT = "PriorTasks";
	public static final String TASK_USERASSIGNED_ELEMENT = "UsersAssigned";
	public static final String TASK_STATUS_ELEMENT = "Status";
	
	// Project Entity
	public static final String PROJECT_ROOT_ELEMENT = "Project";
	public static final String PROJECT_ID_ELEMENT = "Id";
	public static final String PROJECT_NAME_ELEMENT = "Name";
	public static final String PROJECT_DESCRIPTION_ELEMENT = "Description";
	public static final String PROJECT_USERASSIGNED_ELEMENT = "UsersAssigned";
	public static final String PROJECT_TASKSASSIGNED_ELEMENT = "TasksAssigned";

	public static final String ROOT_NODE = "TaskManagement";
	public static final String ROOT_ACTIONS_LIST = "Actions";
	public static final String ROOT_ACTION = "Action";
	public static final String ELEMENT_NAME = "Name";
	public static final String ELEMENT_LINK = "Link";
	public static final String ELEMENT_METHOD_TYPE = "MethodType";
	public static final String ROOT_ARGUMENTS = "Arguments";
	public static final String ROOT_ARGUMENT = "Arg";
	public static final String ELEMENT_TYPE = "Type";
	public static final String ELEMENT_ID = "Id";
	public static final String ELEMENT_INFO = "Info";
	public static final String ELEMENT_TITLE = "Title";
	public static final String ELEMENT_TEXT = "Text";
	
	//Who Am I
	public static final String WHO_AM_I_TAG = "WhoAmI";
	public static final String LOGGED_IN_AS_TAG = "LoggedInAs";
	public static final String ROLE_AS_TAG = "Role";
}
