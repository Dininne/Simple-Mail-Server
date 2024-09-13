package com.sx.mailThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.sx.mailfunction.DataOpera;
import com.sx.mailfunction.Message;
import com.sx.mailserver.MailServer;

public class ServerConcatClientThread extends Thread {
	private Socket socket;
	private String userID;
	private static ConcurrentHashMap<String, Vector<Message>> olchm = new ConcurrentHashMap<String, Vector<Message>>();

	public ServerConcatClientThread(Socket socket, String userID) {
		this.socket = socket;
		this.userID = userID;
	}

	// 接收离线消息的ArrayList
	Vector<Message> al = new Vector<Message>();

	public void run() {

		// 持续读取
		while (true) {
			System.out.println("服务器读取中...");
			try {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Message message = (Message) ois.readObject();

				// 接收到客户端退出请求
				if (message.getMessType().equals("MESSAGE_CLIENT_EXIT")) {
					System.out.println("服务器线程退出中...");
					// 将该用户的登录状态置为false
					MailServer.getChm().get(message.getSender().getUserID()).setLoginStatus(false);
					// 线程集合中移除线程
					MangeServerConcatClientThread.removeThread(message.getSender().getUserID());
					// 回显
					System.out.println("服务器线程退出成功");
					// 关闭socket
					socket.close();
					// 跳出循环
					break;

				}
				synchronized (this) {
					// 接收客户端的文件邮件索引请求
					if (message.getMessType().equals("MESSAGE_INDEX")) {
						System.out.println("服务器遍历邮件中...");
						// 创建新的ArrayList对符合条件的内容进行保存发送
						Vector<Message> ial = new Vector<Message>();
						// 读取要求索引的用户名
						String sendUserID = message.getSender().getUserID();
						// 读取要索引的内容
						String indexStr = message.getConnect().getCon();
						//判断mal是否为空
						//if (MailServer.getMal().isEmpty()) {
						//	System.out.println("mal为空");
						//}

						for (Message m : DataOpera.readMess(MailServer.getMessagePath())) {

							// 如果用户的ID等于发送者的ID 或 接收者的ID
							if (m.getSender().getUserID().equals(sendUserID) || m.getReceiver().equals(sendUserID)) {

								//if (m.getReceiver().equals(sendUserID)) {
								//	System.out.println("存在");
								//}

								// 获得文件内容
								String fileCon = m.getConnect().getCon();
								// 内容包含索引内容
								if (fileCon.contains(indexStr)) {
									//设置消息为普通消息
									m.setMessType("MESSAGE_COMM_MAIL");
									// 添加到集合
									ial.add(m);
								}
							}

						}
						// 拿到发送者的线程，原路返回
						ObjectOutputStream oos = new ObjectOutputStream(MangeServerConcatClientThread
								.getThread(message.getSender().getUserID()).getSocket().getOutputStream());

						// 默认ial不为空，向客户端写入
						oos.writeObject(ial);
						oos.flush();

						continue;
					}
				}

				// 判断接收者是否存在
				if (MailServer.getChm().containsKey(message.getReceiver())) {
					// 读取后根据相应type执行操作
					// 接收客户端发送邮件请求并进行转发
					if (message.getMessType().equals("MESSAGE_COMM_MAIL")
							|| message.getMessType().equals("MESSAGE_FILE_MAIL")) {
						// 根据接收者的状态做相应的处理
						// 当receiver在线时
						if (MailServer.getChm().get(message.getReceiver()).getLoginStatus()) {
							// 向客户端写入
							ObjectOutputStream oos = new ObjectOutputStream(MangeServerConcatClientThread
									.getThread(message.getReceiver()).getSocket().getOutputStream());

							oos.writeObject(message);
							oos.flush();
							// 当receiver不在线时
						} else {
							System.out.println("接收用户不在线，登录时将接收到消息...");
							// 将message放入arrayList,再将arrayList放入olchm中
							al.add(message);
							olchm.put(message.getReceiver(), al);
						}

						MailServer.getMal().add(message);
						DataOpera.storeMess(MailServer.getMal(),MailServer.getMessagePath());

					}
				} else {
					Message noUserMessage = new Message();
					noUserMessage.setMessType("MESSAGE_NO_USER");
					// 拿到发送者的线程，原路返回
					ObjectOutputStream oos = new ObjectOutputStream(MangeServerConcatClientThread
							.getThread(message.getSender().getUserID()).getSocket().getOutputStream());
					// 写入消息，发送给客户端
					oos.writeObject(noUserMessage);
					oos.flush();

				}

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public Socket getSocket() {
		return socket;
	}

	public static ConcurrentHashMap<String, Vector<Message>> getOlchm() {
		return olchm;
	}

}
