package com.asafandben.bl.core_entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Task  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@IndexColumn(name = "taskid")
	private Long taskID;

	public Long getTaskID() {
		return taskID;
	}

	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}

}
