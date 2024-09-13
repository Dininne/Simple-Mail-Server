package com.sx.mailfunction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import com.sx.mailThread.MangeServerConcatClientThread;
import com.sx.mailserver.MailServer;

//提供服务端服务类
public class MailServerServe {
	
	//检查注册信息函数
	public static Message regCheck(ConcurrentHashMap<String,User> hm,User user) throws FileNotFoundException, IOException, ClassNotFoundException {
		Message message=new Message();
		
		//containsKey如果存在，则返回ture；
		if(hm.containsKey(user.getUserID())) {
			message.setMessType("MESSAGE_REG_FAIL");
			System.out.println("用户已存在");
		}else {
			message.setMessType("MESSAGE_REG_SUCCEED");
			System.out.println("用户创建成功");
			hm.put(user.getUserID(), user);
			System.out.println(hm);
			
			DataOpera.storeData(hm, MailServer.getUserInfoPath());
		}
		return message;
	}
	
	//检查登录用户函数,是否是合法用户
		public static boolean logCheck(ConcurrentHashMap<String,User> hm,User user) {
			//判断这个用户是否存在
			if(hm.containsKey(user.getUserID())) {
				
				//获得这个密码的密码
				String str=hm.get(user.getUserID()).getPassword();
				//判断获得的密码与接收的密码是否相等
				if(str.equals(user.getPassword())) {
					//判断用户是否在线
					if(hm.get(user.getUserID()).getLoginStatus()!=true) {
						//不在线，登录成功，将该用户状态变为在线
						hm.get(user.getUserID()).setLoginStatus(true);
						return true;
					}
				}
			}
			user.setLoginStatus(false);
			return false;
		}
		
		//判断登录有户是否在olchm中,并做出相应操作
		public static void sendOfflineMessage(String userID,ConcurrentHashMap<String, Vector<Message>> olchm) throws IOException{
			//如果登录有户ID存在
			if(olchm.containsKey(userID)) {
				//则得到相应的arrayList
				Vector<Message> al=(Vector<Message>)olchm.get(userID);
				//得到该用户线程
				ObjectOutputStream oos=new ObjectOutputStream(
						MangeServerConcatClientThread.getThread(userID).getSocket().getOutputStream());
				//发送
				oos.writeObject(al);
				oos.flush();
				System.out.println("离线消息列表发送成功");
				//删除olchm中发送的消息
				olchm.remove(userID);
				System.out.println("发送列表已删除");
				
			}
		}
		
		
		
		

}
