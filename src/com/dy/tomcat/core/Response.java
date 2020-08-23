package com.dy.tomcat.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
	private OutputStream os = null;
	private String basePath = null; // 服务器读取路径
	
	public Response(OutputStream os) {
		this.os = os;
		basePath = ReadConfig.getInstance().getProperty("path");
		//System.out.println("basePath=" + basePath);
	}

	/**
	 * 重定向
	 * @param url
	 */
	public void sendRedirect(String url) {
		if (url == null || "".equals(url)) {
			error404(url); // 找不到资源
			return;
		}
		
	//	System.out.println("url=>" + url);
	//	System.out.println(url.substring(url.lastIndexOf(".") + 1).toLowerCase());
		
		// 请求资源的分类处理    
		// 排除/情况
		if (url.indexOf("/") == url.lastIndexOf("/") && (url.indexOf("/") + 1) < url.length()) { // /snacknet 
			// 需要加/处理
			send302(url);
		} else {
			if (url.endsWith("/")) { // /snacknet/ /snacknet/back/
				// 此时从配置文件读取默认访问路径
				String defaultPath = ReadConfig.getInstance().getProperty("default");
				
				// 读取默认资源
				File file = new File(basePath, url.substring(1).replace("/", "\\") + defaultPath);
				//System.out.println("file=" + file);
				//System.out.println(url);
				//System.out.println(url.substring(1).replace("/", "\\"));
				
				if (!file.exists()) {
					error404(url);
					return;
				}
				send200(readFile(file),defaultPath.substring(defaultPath.lastIndexOf(".") + 1).toLowerCase());
			} else { // 无特殊的情况，即默认路径
				File file = new File(basePath,url.substring(1).replace("/", "\\"));
				if (!file.exists() || !file.isFile()) {
					error404(url);
					return;
				}
				send200(readFile(file),url.substring(url.lastIndexOf(".") + 1).toLowerCase());
			}
		}
	}

	/**
	 * 读取指定资源
	 * @param file
	 * @return
	 */
	private byte[] readFile(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] bt = new byte[fis.available()];
			fis.read(bt);
			return bt;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 后补/
	private void send302(String url) {
		try {
			String msg = "HTTP/1.1 302 Moved Temporarily\r\nContent-Type:text/html;charset=utf-8\r\nLocation:" + url + "/\r\n\r\n";
			os.write(msg.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 成功响应
	 * @param bt  存放请求资源的数组
	 * @param url 请求资源
	 */
	private void send200(byte[] bt, String extension) {
		try {
			//String contentType = "text/html;charset=utf-8";
			String contentType = ParseXml.getCintentType(extension);
			String msg = "HTTP/1.1 200 OK\r\nContent-Type:" + contentType + "\r\nContent-Length:" + bt.length + "\r\n\r\n";
			os.write(msg.getBytes());
			os.write(bt);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 找不到资源
	 * @param url 请求资源
	 */
	private void error404(String url) {
		try {
			String errInfo = "<h1>HTTP Status 404 -" + url + "</h1>";
			String msg = "HTTP/1.1 404 File Not Found\r\nContent-Type:text/html;charset=utf-8\r\nContent-Length:" + errInfo.length() + "\r\n\r\n" + errInfo;
			os.write(msg.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
}
