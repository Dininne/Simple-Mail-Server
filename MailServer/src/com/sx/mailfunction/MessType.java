package com.sx.mailfunction;

public interface MessType {
	String MESSAGE_REG = "0"; // 0表示注册请求
	String MESSAGE_REG_SUCCEED = "1"; // 1表示注册成功
	String MESSAGE_REG_FAIL = "2"; // 2表示注册失败
	String MESSAGE_LOGIN = "3"; // 3表示登录请求
	String MESSAGE_LOGIN_SUCCEED = "4"; // 4表示登录成功
	String MESSAGE_LOGIN_FAIL = "5"; // 5表示登录失败
	String MESSAGE_COMM_MAIL = "6"; // 6表示发送普通邮件
	String MESSAGE_CLIENT_EXIT = "7"; // 客户端请求退出 服务端接收
	String MESSAGE_FILE_MAIL = "8";  // 表示发送含文件的邮件
	String MESSAGE_INDEX="9"; // 表示客户端请求返回邮件列表
	String MESSAGE_NO_USER="10"; // 表示发送消息时服务端表示无此用户

}
