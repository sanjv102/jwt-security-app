package com.spring.jwt.model;

public class LoginForm {

	private String UserName;
	private String password;
	
	public LoginForm(String userName, String password) {
		super();
		UserName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return UserName;
	}
	public String getPassword() {
		return password;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
