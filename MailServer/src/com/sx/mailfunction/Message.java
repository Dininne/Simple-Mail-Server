package com.sx.mailfunction;

import java.io.Serializable;

public class Message implements Serializable{


	private static final long serialVersionUID = 1L;
	
	//发送者
	private User sender;
	//接收者
	private String receiver;
	//内容
	private Connect connect;
	//发送时间
	private String sendTime;
	//是否成功
	private String messType;

	
	//get和set
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMessType() {
		return messType;
	}
	public void setMessType(String messType) {
		this.messType = messType;
	}
	public Connect getConnect() {
		return connect;
	}
	public void setConnect(Connect connect) {
		this.connect = connect;
	}
	@Override
	public String toString() {
		return "Message [sender=" + sender + ", receiver=" + receiver + ", connect=" + connect + ", sendTime="
				+ sendTime + ", messType=" + messType + "]";
	}

	
}