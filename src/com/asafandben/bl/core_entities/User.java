package com.asafandben.bl.core_entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.Serializable;



@Entity
public class User implements Serializable {
	@Id
	@Column
	private String email;
//	private String nickname;
//	private String firstName;
//	private String lastName;
//	private String password;
	//private List<Task> tasks;
	//private List<User> usersIManage;
	
	public User() {
	}
	public User(String email) {
		this.email = email;
	}
	// TODO: Permissions Enum (should be accessable by filters also).
	
	
	
}
