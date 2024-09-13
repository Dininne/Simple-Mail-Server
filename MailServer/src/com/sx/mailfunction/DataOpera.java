package com.sx.mailfunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.sx.mailserver.MailServer;

public class DataOpera {
	
	//从本地文件中读取数据
	 public static ConcurrentHashMap<String, User> readData(String filePath) {
	        ConcurrentHashMap<String, User> dataMap = new ConcurrentHashMap<>();

	        try (FileInputStream fis = new FileInputStream(filePath);
	             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
	             BufferedReader reader = new BufferedReader(isr)) {

	            String line;
	            while ((line = reader.readLine()) != null) {
	                String[] parts = line.split("=");
	                if (parts.length == 2) {
	                    String key = parts[0];
	                    String[] userParts = parts[1].split(", ");
	                    if (userParts.length == 3) {
	                        String userID = userParts[0].substring(userParts[0].indexOf('=') + 1);
	                        String password = userParts[1].substring(userParts[1].indexOf('=') + 1);
	                        boolean loginStatus = Boolean.parseBoolean(userParts[2].substring(userParts[2].indexOf('=') + 1, userParts[2].length() - 1));
	                        User user = new User(userID, password, loginStatus);
	                        dataMap.put(key, user);
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return dataMap;
	    }

	 
	 //将数据写入本地
	 public static void storeData(ConcurrentHashMap<String, User> dataMap,String filePath) {
	        try (FileWriter fw = new FileWriter(filePath, true);
	             BufferedWriter writer = new BufferedWriter(fw)) {

	            for (String key : dataMap.keySet()) {
	                User user = dataMap.get(key);
	                String userData = key + "=" + user.getUserID() + ", " + user.getPassword() + ", " + user.getLoginStatus();
	                if (!isDataAlreadyExists(filePath, userData)) {
	                    writer.write(userData + "\n");
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	 	//判断用户是否在文件中已存在，存在则跳过
	    public static boolean isDataAlreadyExists(String filePath, String userData) {
	        try (FileReader fr = new FileReader(filePath);
	             BufferedReader reader = new BufferedReader(fr)) {

	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (line.equals(userData)) {
	                    return true;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return false;
	    }
	    
	    //判断该消息在文件中是否存在
	    public static boolean isMessageExists(Vector<Message> vec,String sender, String receiver, String content) {
	        for (Message message : vec) {
	            if (message.getSender().getUserID().equals(sender) && message.getReceiver().equals(receiver) && message.getConnect().getCon().equals(content)) {
	            	return true;
	            }
	        }
	        return false;
	    }

	    //将消息写入文件中
	    public static void storeMess(Vector<Message> vec,String filePath) {
	        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,true), StandardCharsets.UTF_8))) {

	            for (Message message : vec) {
	                if (!isMessageExists(readMess(filePath),message.getSender().getUserID(), message.getReceiver(), message.getConnect().getCon())) {
	                    writer.write("发送者: " + message.getSender().getUserID() + ", 接收者: " + message.getReceiver() + ", 消息内容: " + message.getConnect().getCon());
	                    writer.newLine();
	                    System.out.println("message写入文件成功");
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	    }
	    
	    //从文件中读取消息
	    public static Vector<Message> readMess(String filePath) {
	        Vector<Message> vec = new Vector<>();
	        
	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                String[] parts = line.split(", ");
	                String sender = parts[0].substring(parts[0].indexOf(": ") + 2);
	                String receiver = parts[1].substring(parts[1].indexOf(": ") + 2);
	                String content = parts[2].substring(parts[2].indexOf(": ") + 2);
	                
	                Message message = new Message();
	                Connect con=new Connect();
	                con.setCon(content);
	                
	                message.setSender(new User(sender));
	                message.setReceiver(receiver);
	                message.setConnect(con);
	                
	                vec.add(message);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        return vec;
	    }
	
	
}
