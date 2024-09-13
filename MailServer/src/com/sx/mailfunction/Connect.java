package com.sx.mailfunction;

import java.io.File;
import java.io.Serializable;

public class Connect implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//文件
	private File file;
	//字串内容
	private String connect;
	//存放文件的数组
	private byte[] byteFile;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getCon() {
		return connect;
	}
	public void setCon(String connect) {
		this.connect = connect;
	}
	public byte[] getByteFile() {
		return byteFile;
	}
	public void setByteFile(byte[] byteFile) {
		this.byteFile = byteFile;
	}
	@Override
	public String toString() {
		return "Connect [fileName=" + file.getName() + ", file=" + file + ", connect=" + connect + "]";
	}
	
	
}
