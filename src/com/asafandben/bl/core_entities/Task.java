package com.asafandben.bl.core_entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Task  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@IndexColumn(name = "taskid")
	private Long taskID;
	
	@IndexColumn(name = "name")
	private String taskName;
	@IndexColumn(name = "description")
	private String taskDescription;
	
	@ManyToMany(cascade={CascadeType.ALL})
	private List<Task> priorTasks = new ArrayList<Task>();
	
	@ManyToMany(cascade={CascadeType.ALL})
	private List<User> usersAssigned = new ArrayList<User>();
	
	//TODO: Status enum.

	public Long getTaskID() {
		return taskID;
	}

	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}

}
