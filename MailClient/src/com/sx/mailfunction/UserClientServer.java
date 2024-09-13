package com.sx.mailfunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import com.sx.mailThread.ClientConcatServerThread;
import com.sx.mailThread.MangeClientConcatServerThread;

public class UserClientServer {
	// 创建user对象接收客户端发送来的用户名和密码
	Message regMessage=new Message();

	private Socket socket;

	public boolean regCheck(String regUserID,String regPassword) throws IOException, ClassNotFoundException{
		User regUser = new User();
		//将用户名和密码封装到user对象
		regUser.setUserID(regUserID);
		regUser.setPassword(regPassword);
		//将regUser对象放入regMessage对象
		regMessage.setMessType("MESSAGE_REG");
		regMessage.setSender(regUser);
		
		//建立socket建立与服务端的通信
		socket=new Socket(InetAddress.getLocalHost(), 9999);
		//向服务器写入
		ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
		
		// 写入RegMessage对象
		oos.writeObject(regMessage);
		// 刷新
		oos.flush();
		// 回显
		//System.out.println("发送完毕");
		//System.out.println(regMessage);

		//读取服务器
		ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
		Message message = (Message) ois.readObject();
		//System.out.println("读取Message完毕");
		//System.out.println(message.getMessType());

		//oos.close();
		//ois.close();
		socket.close();
		// 读服务器返回message对象
		if (message.getMessType().equals("MESSAGE_REG_FAIL")) {
			//有户已存在
			return false;
		} else {
			//注册成功
			return true;
		}
	}

	// 检查发送来的用户名和密码是否正确
	public boolean logCheck(String logUserID,String logPassword) throws IOException, ClassNotFoundException {
		
		User logUser=new User();
		//将用户名和密码封装到logUser对象
		logUser.setUserID(logUserID);
		logUser.setPassword(logPassword);
		//建立socket建立与服务端的通信
		socket=new Socket(InetAddress.getLocalHost(), 9999);
		//向服务器写入
		ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
		//将regUser对象放入regMessage对象
		regMessage.setMessType("MESSAGE_LOGIN");
		regMessage.setSender(logUser);
		// 写入RegMessage对象
		oos.writeObject(regMessage);
		// 刷新
		oos.flush();
		// 回显
		//System.out.println("发送完毕");
		//System.out.println(regMessage);
		
		//读服务器
		ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
		Message message = (Message) ois.readObject();
		//System.out.println("读取Message完毕");
		//System.out.println(message.getMessType());

		if(message.getMessType().equals("MESSAGE_LOGIN_SUCCEED")) {
			//登录成功
			//创建线程
			ClientConcatServerThread ccst=new ClientConcatServerThread(socket);
			//添加到线程集合
			MangeClientConcatServerThread.addThread(logUser.getUserID(),ccst);
			//启动线程
			new Thread(ccst).start();
			//System.out.println("登录成功,线程已启动");
			System.out.println("登录成功");
			
			return true;
		}else {
			//登录失败
			//关闭socket
			socket.close();
			//System.out.println("登录失败,线程已关闭");
			System.out.println("登录失败");
			return false;
		}

	}
	

	
	//发送邮件方法
	public void sendMail(String sender,String receiver ,String concat,char fileFlag) throws Exception {
		User user=new User();
		Connect con=new Connect();
		Message mail=new Message();
		
		
		
		//发送者 private User sender;;
		user.setUserID(sender);
		mail.setSender(user);
		//接收者 private String receiver;
		mail.setReceiver(receiver);
		//发送时间 private String sendTime;
		mail.setSendTime(new java.util.Date().toString());
		//内容 private Connect connect;
		//将字串内容先写入con中
		con.setCon(concat);
		//将con放入mail中
		mail.setConnect(con);
		
		//确认执行的请求 private String messType;
		if(fileFlag=='Y') {
			mail.setMessType("MESSAGE_FILE_MAIL");
			
			while (true) {
				System.out.print("请输入要发送文件的路径：");
				String path = Utility.readString(60);
				System.out.println();
				File file = new File(path);
				//如果文件存在
				if (file.isFile()) {
					//将这个文件对象传给con对象
					con.setFile(file);
					break;
				}
				if(!file.exists()) {
					System.out.println("文件不存在");
				}
				if(file.isDirectory()) {
					System.out.println("这是一个目录");
				}
				if(!file.canRead()) {
					System.out.println("文件不可读取");
				}
				System.out.println("路径错误");
			}
			//将文件存放于fileByte数组
			byte[] fileByte=StreamUtils.streamToByteArray(new FileInputStream(con.getFile()));
			//写入到con对象中，以供读取
			con.setByteFile(fileByte);
			
			
			
		}else {
			mail.setMessType("MESSAGE_COMM_MAIL");
		}
		
		
		//回显
		//System.out.println(sender+"发送"+concat+"给"+receiver);
		//向服务器写入
		ObjectOutputStream oos=new ObjectOutputStream(
				MangeClientConcatServerThread.getThread(sender).getSocket().getOutputStream());
		//写服务器
		oos.writeObject(mail);
		oos.flush();
		
		//System.out.println("邮件发送完毕");
		

		
	}
	
	//邮件内容查找方法
	public void mailIndex(String logUser,String indexString) throws IOException {
		User user=new User();
		Connect con=new Connect();
		Message message=new Message();
		
		//设置发送用户
		user.setUserID(logUser);
		message.setSender(user);
		//设置消息类型
		message.setMessType("MESSAGE_INDEX");
		// 将字串内容先写入con中
		con.setCon(indexString);
		// 将con放入mail中
		message.setConnect(con);
		
		ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(message);
		oos.flush();
		//System.out.println("成功向服务器写入数据"+message.getConnect().getCon());
	}
	
	
	
	//退出方法
	public void logOut(String logUser) throws IOException {
		User user=new User();
		Message message=new Message();
		//填充内容
		user.setUserID(logUser);
		message.setSender(user);
		message.setMessType("MESSAGE_CLIENT_EXIT");
		
		//向服务器写入
		ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(message);
		oos.flush();
		//System.out.println("成功向服务器写入数据："+message);
		//System.out.println("客户端退出成功");
		System.out.println("退出成功");
		//结束该进程
		System.exit(0);
		//关闭socket
		socket.close();
		
		
	}

}
