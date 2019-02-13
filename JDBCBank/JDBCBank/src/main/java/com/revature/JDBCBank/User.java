package com.revature.JDBCBank;

import java.io.Serializable;

import com.revature.JDBCBank.App.Permissions;

public class User implements Serializable{
	//basic user functionality
	private Permissions userLevel;
	private String userName;
	private Integer userID;
	private Double funds;
	transient private String password; 
	
	public User() {
		this("temp", "temp", Permissions.UNREGISTERED, 1);
	}
	
	public User(String name, String pass, Permissions level, Integer id) {
		userName = name;
		password = pass;
		userLevel = level;
		userID = id;
		funds = 0.0;
	}
	
	public User(String name, String pass, Integer level, Integer id) {
		this (name, pass, Permissions.UNREGISTERED, id);
		
		if (level == 2) {
			userLevel = Permissions.SUPERUSER;
		} else if (level == 1) {
			userLevel = Permissions.REGISTERED;
		} else {
			userLevel = Permissions.UNREGISTERED;
		}
	}
	
	public User(String name, String pass, Integer level, Integer id, Double funds) {
		this (name, pass, Permissions.UNREGISTERED, id);
		
		if (level == 2) {
			userLevel = Permissions.SUPERUSER;
		} else if (level == 1) {
			userLevel = Permissions.REGISTERED;
		} else {
			userLevel = Permissions.UNREGISTERED;
		}
		
		this.funds = funds;
	}
	
	public String getUsername() {
		return userName;
	}
	
	public Permissions getPermissionLevel() {
		return userLevel;
	}
	
	public Integer getUserID() {
		return userID;
	}
	
	public Double getFunds() {
		return funds;
	}
	
	public String getPass() {
		return password;
	}
}
