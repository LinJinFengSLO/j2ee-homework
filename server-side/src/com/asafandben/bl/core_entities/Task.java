package com.asafandben.bl.core_entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.IndexColumn;

import com.asafandben.dal.cache.ICacheable;
import com.asafandben.utilities.XmlNamingConventions;

@Entity
@XmlRootElement(name = XmlNamingConventions.TASK_ROOT_ELEMENT) // TODO: use naming convention for the whole class!!!
public class Task  implements Serializable, ICacheable<Long> {

	private static final long serialVersionUID = 1L;

	public enum Status {
		NOT_STARTED, ON_GOING, COMPLETED;
	}
	
	// Members
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
	
	// Setters/Getters
	@XmlElement(name = XmlNamingConventions.TASK_STATUS_ELEMENT)
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@XmlElement(name = XmlNamingConventions.TASK_NAME_ELEMENT)
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	@XmlElement(name = XmlNamingConventions.TASK_DESCRIPTION_ELEMENT)
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	@XmlElement(name = XmlNamingConventions.TASK_CREATIONDATE_ELEMENT)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@XmlElement(name = XmlNamingConventions.TASK_DUEDATE_ELEMENT)
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	@XmlIDREF
	@XmlElementWrapper(name = "AssignedUsers")
	@XmlElement(name = XmlNamingConventions.TASK_PRIORTASKS_ELEMENT)
	public List<Task> getPriorTasks() {
		return priorTasks;
	}
	public void setPriorTasks(List<Task> priorTasks) {
		this.priorTasks = priorTasks;
	}
	@XmlIDREF
	@XmlElementWrapper(name = "AssignedUsers")
	@XmlElement(name = XmlNamingConventions.TASK_USERASSIGNED_ELEMENT)
	public List<User> getUsersAssigned() {
		return usersAssigned;
	}
	public void setUsersAssigned(List<User> usersAssigned) {
		this.usersAssigned = usersAssigned;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@XmlElement(name = XmlNamingConventions.TASK_ID_ELEMENT)
	@XmlID
	@XmlJavaTypeAdapter(LongAdapter.class)
	public Long getID() {
		return taskID;
	}
	public void setID(Long taskID) {
		this.taskID = taskID;
	}
	
    public boolean equals(Object obj) {
        if (obj instanceof Task)
            return getID().equals(((Task)obj).getID()); 
        else
            return false;
    }
}
