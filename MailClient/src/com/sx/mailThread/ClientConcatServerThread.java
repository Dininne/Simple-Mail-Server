package com.sx.mailThread;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;
import com.sx.mailfunction.Message;
import com.sx.mailfunction.StreamUtils;

public class ClientConcatServerThread implements Runnable {

	private Socket socket;
	public static boolean loop = true;

	public ClientConcatServerThread(Socket socket) {
		this.socket = socket;

	}

	public void run() {
		// while持续与服务端连接
		while (loop) {
			// System.out.println("客户端线程运行中...");
			try {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

				Object o = ois.readObject();
				if (o instanceof Vector) {
					Vector<Message> al = (Vector<Message>) o;
					System.out.println("以下是您的消息：");
					// 遍历离线消息及索引信息
					for (Message message : al) {

						System.out.println(message.getSender().getUserID() + "发送内容： " + message.getConnect().getCon());

						if (message.getMessType().equals("MESSAGE_FILE_MAIL")) {

							StreamUtils.saveFile(message.getConnect().getFile().getName(),
									message.getConnect().getByteFile());

						}
					}

				} else {
					Message message = (Message) o;

					// 如果用户不存在，得到相应的type，输出用户不存在
					if (message.getMessType().equals("MESSAGE_NO_USER")) {
						System.out.println("接收方未注册");
					}

					// 显示服务端线程返回的消息
					if (message.getMessType().equals("MESSAGE_COMM_MAIL")
							|| message.getMessType().equals("MESSAGE_FILE_MAIL")) {
						// System.out.println(message);
						System.out.print(message.getSender().getUserID() + "于" + message.getSendTime() + "发送:"
								+ message.getConnect().getCon());

						if (message.getMessType().equals("MESSAGE_FILE_MAIL")) {
							System.out.println("及" + message.getConnect().getFile().getName());
							StreamUtils.saveFile(message.getConnect().getFile().getName(),
									message.getConnect().getByteFile());

						}
					}
				}
				System.out.println();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// System.out.println("退出线程");
		System.out.println("退出");
	}

	public Socket getSocket() {
		return socket;
	}

}
