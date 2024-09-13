package com.sx.mailfunction;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 此类用于演示关于流的读写方法
 *
 */
public class StreamUtils {
	/**
	 * 功能：将输入流转换成byte[]
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] streamToByteArray(InputStream is) throws Exception{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();//创建输出流对象
		byte[] b = new byte[1024];
		int len;
		while((len=is.read(b))!=-1){
			bos.write(b, 0, len);	
		}
		byte[] array = bos.toByteArray();
		bos.close();
		return array;
	}
	/**
	 * 功能：将InputStream转换成String
	 * @param is
	 * @return
	 * @throws Exception
	 */
	
	public static String streamToString(InputStream is) throws Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder builder= new StringBuilder();
		String line;
		while((line=reader.readLine())!=null){
			builder.append(line+"\r\n");
		}
		return builder.toString();
		
	}
	

	/** 
	* 将字节流转换成文件 
	* @param filename 
	* @param data 
	* @throws Exception 
	*/  
	public static void saveFile(String filename,byte [] data)throws Exception{   
	    if(data != null){   
	      String filepath ="D:\\A_简易邮件服务器测试文件\\服务端保存文件目录\\" + filename;   
	      File file  = new File(filepath);   
	      if(file.exists()){   
	         file.delete();   
	      }   
	      FileOutputStream fos = new FileOutputStream(file);   
	      fos.write(data,0,data.length);   
	      fos.flush();   
	      fos.close();   
	    }   
	}  

}
