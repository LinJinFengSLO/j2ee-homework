package com.asafandben.bl.core_entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Task  implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Status {
		NOT_STARTED, ON_GOING, COMPLETED;
	}
	
	
	//	******************	Members ******************
	@Id
	@GeneratedValue
	@IndexColumn(name = "taskid")
	private Long taskID;
	
	@IndexColumn(name = "name")
	private String taskName;
	
	@IndexColumn(name = "description")
	private String taskDescription;
	
	@IndexColumn(name = "creationdate")
	private Date creationDate;
	
	@IndexColumn(name = "duedate")
	private Date dueDate;
	
	
	@ManyToMany(cascade={CascadeType.ALL})
	private List<Task> priorTasks = new ArrayList<Task>();
	
	@ManyToMany(cascade={CascadeType.ALL})
	private List<User> usersAssigned = new ArrayList<User>();
	
	@IndexColumn(name = "status")
	private Status status;
	
	
	
	
	
	
	//	******************	Setters/Getters ******************
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<Task> getPriorTasks() {
		return priorTasks;
	}

	public void setPriorTasks(List<Task> priorTasks) {
		this.priorTasks = priorTasks;
	}

	public List<User> getUsersAssigned() {
		return usersAssigned;
	}

	public void setUsersAssigned(List<User> usersAssigned) {
		this.usersAssigned = usersAssigned;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getTaskID() {
		return taskID;
	}

	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}

}
