package com.sx.mailThread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MangeServerConcatClientThread {
	//使用map管理线程
		//String接收userID,根据userID来决定要发送的对象
		public static HashMap<String,ServerConcatClientThread> hm=new HashMap<>();
		
		//向集合中添加userID对于的线程
		public static void addThread(String userID,ServerConcatClientThread ccst) {
			hm.put(userID,ccst);
		}
		
		//判断userID对于的线程是否在运行
		public static boolean containsKeyThread(String userID) {
			return hm.containsKey(userID);
		}
		
		//得到userID对于的线程
		public static ServerConcatClientThread getThread(String userID) {
			return hm.get(userID);
		}
		
		//删除userID对于的线程
		public static boolean removeThread(String userID) {
			if(containsKeyThread(userID)) {
				hm.remove(userID);
				return true;
			}
			return false;
		}
		
		//遍历集合所有的线程
		public static void traverseThread() {
			Set entrySet=hm.entrySet();
			Iterator it=entrySet.iterator();
			while(it.hasNext()) {
				Map.Entry m=(Map.Entry)it.next();
				//Object m.getKey(); 
				//Object m.getValue();
				System.out.println("key:"+m.getKey()+"\t\t"+"value:"+m.getValue());
			}
		}
		
}
