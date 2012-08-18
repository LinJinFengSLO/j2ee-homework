package com.asafandben.bl.core_entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.IndexColumn;

import com.asafandben.dal.cache.ICacheable;

public class Project implements ICacheable<Long> {
	
	// Members
	@Id
	@GeneratedValue
	@IndexColumn(name = "projectid")
	private Long projectID;
	@IndexColumn(name = "projectname")
	private String projectName;
	@ManyToMany(cascade={CascadeType.ALL})
	private List<Task> tasks = new ArrayList<Task>();
	@ManyToMany(cascade={CascadeType.ALL})
	private List<User> users = new ArrayList<User>();
	
	// Setters/Getters
	public Long getID() {
		return projectID;
	}
	public void setID(Long projectID) {
		this.projectID = projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
