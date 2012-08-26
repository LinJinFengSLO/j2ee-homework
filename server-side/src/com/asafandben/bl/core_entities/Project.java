package com.asafandben.bl.core_entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.IndexColumn;

import com.asafandben.dal.cache.ICacheable;
@XmlRootElement(name = "Project") // TODO: use naming convention for the whole class!!!
public class Project implements ICacheable<Long> {
	
	// Members
	@Id
	@GeneratedValue
	@IndexColumn(name = "projectid")
	private Long projectID;
	@IndexColumn(name = "projectname")
	private String projectName;
	@IndexColumn(name = "description")
	private String projectDescription;
	@ManyToMany(cascade={CascadeType.ALL})
	private List<Task> tasks = new ArrayList<Task>();
	@ManyToMany(cascade={CascadeType.ALL})
	private List<User> users = new ArrayList<User>();
	
	// Setters/Getters
	@XmlElement(name = "Id")
	public Long getID() {
		return projectID;
	}
	public void setID(Long projectID) {
		this.projectID = projectID;
	}
	@XmlElement(name = "Name")
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@XmlElement(name = "Description")
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	@XmlElement(name = "Tasks")
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	@XmlElement(name = "Users")
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
