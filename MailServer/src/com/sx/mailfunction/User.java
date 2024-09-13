package com.sx.mailfunction;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userID;
	private String password;
	//判断有户是否在线
	private boolean loginStatus;
	
	public User(String userID) {
		this.userID=userID;
	}
	
	public User(String userID,String password) {
		this.userID=userID;
		this.password=password;
	}
	//有参构造器，封装完整信息
		public User(String userID, String password, Boolean loginStatus) {
			super();
			this.userID = userID;
			this.password = password;
			this.loginStatus = loginStatus;
		}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", password=" + password + ", loginStatus=" + loginStatus + "]";
	}
	
	
}
