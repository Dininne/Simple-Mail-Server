package com.sx.mailView;

import com.sx.mailfunction.UserClientServer;
import com.sx.mailfunction.Utility;


public class MailView {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new MailView().Menu();
	}
	
	private char key;
	private boolean outLoop = true;
	UserClientServer ucs = new UserClientServer();
	
	//mail菜单
	public void Menu() throws Exception {
		while(outLoop) {
			//操作菜单
			System.out.println("==============欢迎登录简易邮件服务==============");
			System.out.println("\t\t1. 用户注册");
			System.out.println("\t\t2. 用户登录");
			System.out.println("\t\t3. 退出服务");
			// 提示操作
			System.out.print("请输入你的选择：");
			// 读取输入的一个字符
			key = Utility.readMenuSelection_3();
			//根据读取到的内容执行操作
			switch(key) {
			//用户注册
				case '1':
					//接收用户输入
					System.out.print("请输入用户名：");
					String regUserID = Utility.readString(10);
					System.out.print("请输入密 码：");
					String regPassword = Utility.readString(10);
					
					if(ucs.regCheck(regUserID,regPassword)) {
						System.out.println("注册成功");
					}else {
						System.out.println("用户已存在");
					}
					break;
					
				case '2':
					//接收用户输入
					System.out.print("请输入用户名：");
					String logUserID = Utility.readString(10);
					System.out.print("请输入密 码：");
					String logPassword = Utility.readString(10);
					
					if (ucs.logCheck(logUserID,logPassword)) {
						System.out.println("=================欢迎" + logUserID + "用户" + "=================");
						boolean inLoop=true;
						while(inLoop) {
							System.out.println("\t\t1. 发送邮件");
							System.out.println("\t\t2. 邮件索引");
							System.out.println("\t\t3. 退出服务");
							// 提示操作
							System.out.print("请输入你的选择：");
							// 读取输入的一个字符
							key = Utility.readMenuSelection_3();
							
							switch(key) {
							case '1':
								//System.out.println("发送邮件操作请求");
								
								//读取接收方用户名和内容
								System.out.print("请输入接收方用户名：");
								String recUser=Utility.readString(20);
								
								System.out.print("请输入要发送的内容:");
								String sendStr=Utility.readString(100);
								//确认是否要发送邮件
								char fileFlag=Utility.readConfirmSelection();
								//发送邮件方法
								System.out.println("发送邮件中...");
								ucs.sendMail(logUserID, recUser, sendStr,fileFlag);
								
								
								break;
							case '2':
								//System.out.println("发送邮件索引请求");
								System.out.print("请输入要查找的邮件内容：");
								String indexString=Utility.readString(20);
								System.out.println();
								//查找方法
								ucs.mailIndex(logUserID,indexString);
								

								break;
							case '3':
								System.out.println("退出服务");
								//退出二级菜单方法
								ucs.logOut(logUserID);
								//退出二级菜单
								inLoop=false;
								break;
							}
							
						}
					}
					break;
				case '3':
					//退出
					outLoop=false;
					System.out.println("已退出服务");
					break;
			}
			
		}
	}

	
}
