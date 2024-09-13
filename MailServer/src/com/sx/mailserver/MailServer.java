package com.sx.mailserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import com.sx.mailThread.MangeServerConcatClientThread;
import com.sx.mailThread.ServerConcatClientThread;
import com.sx.mailfunction.DataOpera;
import com.sx.mailfunction.MailServerServe;
import com.sx.mailfunction.Message;
import com.sx.mailfunction.User;


public class MailServer {
	
	ServerSocket serSocket = new ServerSocket(9999);
	//存放用户信息路径
	private static String userInfoPath="D:\\A_简易邮件服务器测试文件\\user.txt"; 
	//存放历史邮件路径
	private static String messagePath="D:\\A_简易邮件服务器测试文件\\message.txt";
	//存放用户信息集合
	private static ConcurrentHashMap<String,User> chm=new ConcurrentHashMap<String,User>();

	//接收所有要转发的消息
	private static Vector<Message> mal=new Vector<Message>();
	//默认用户
	static {
		chm.put("100",new User( "100","123456",false));
		chm.put("200",new User( "200","123456",false));
		chm.put("300",new User( "300","123456",false));
	}
	//服务端服务
	public MailServer() throws IOException, ClassNotFoundException {
		// 将默认用户追加到用户集合中
		DataOpera.storeData(chm, userInfoPath);
		// 从用户文件中读取所有有户数据
		chm=DataOpera.readData(userInfoPath);
		//服务端持续监听
		while(true) {
			Socket socket= serSocket.accept();
			String userIDStr;
			String passwordStr;
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Message regMessage=new Message();
			regMessage=(Message)ois.readObject();
			System.out.println(regMessage);
			
			
			switch(regMessage.getMessType()) {
			case "MESSAGE_REG":
				System.out.println("服务端开启注册服务");
				User regUser=regMessage.getSender();
				
				//回显
				userIDStr=regUser.getUserID();
				passwordStr=regUser.getPassword();
				System.out.println("注册用户："+userIDStr);
				System.out.println("注册密码："+passwordStr);
				
				oos.writeObject(MailServerServe.regCheck(chm,regUser));
				oos.flush();
				
				
				break;
				
			case "MESSAGE_LOGIN":
				System.out.println("服务端开启登录服务");
				User logUser=regMessage.getSender();
				//回显
				userIDStr=logUser.getUserID();
				passwordStr=logUser.getPassword();
				System.out.println("登录用户："+userIDStr);
				System.out.println("登录密码："+passwordStr);
				
				Message message=new Message();
				
				
				chm=DataOpera.readData(userInfoPath);
				
				if (MailServerServe.logCheck(chm,logUser)) {
					message.setMessType("MESSAGE_LOGIN_SUCCEED");
					//将message对象回送给客户端
					oos.writeObject(message);
					oos.flush();
					System.out.println("登录成功");
					//登录成功，创建线程
					//创建一个与客户端相连的线程
					ServerConcatClientThread scct=
							new ServerConcatClientThread(socket,logUser.getUserID());
					//添加到相应集合中
					MangeServerConcatClientThread.addThread(logUser.getUserID(),scct);
					System.out.println("启动服务端线程");	
					//开启线程
					scct.start();
					
					//发送离线消息
					MailServerServe.sendOfflineMessage(userIDStr, ServerConcatClientThread.getOlchm());
					
					
				}else {
					message.setMessType("MESSAGE_LOGIN_FAIL");
					oos.writeObject(message);
					oos.flush();
					System.out.println("登录失败");
				}
				
				break;
				
			}
		}
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		new MailServer();
	}
	
	//其他类获得用户集合方法
	public static ConcurrentHashMap<String, User> getChm() {
        return chm;
    }

	public static Vector<Message> getMal() {
		return mal;
	}

	public static String getMessagePath() {
		return messagePath;
	}
	
	public static String getUserInfoPath() {
		return userInfoPath;
	}


}
