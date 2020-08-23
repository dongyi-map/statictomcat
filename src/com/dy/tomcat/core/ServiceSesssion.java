package com.dy.tomcat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServiceSesssion implements Runnable{
	private Socket sk = null;
	private OutputStream os = null;
	public ServiceSesssion(Socket sk) {
		this.sk = sk;
	}
	
	@Override
	public void run() {
		try (InputStream is = sk.getInputStream()){
			
			// 处理请求
			Request request = new Request(is);
			// 获取请求资源地址
			String url = request.getUrl();
			
			//System.out.println("==>" + url);
			
			this.os = sk.getOutputStream();
			
			// 服务器响应
			new Response(os).sendRedirect(url);
		} catch (IOException e) {
			send500(e);
			e.printStackTrace();
		}
		
	}

	/**
	 * 发送500错误信息
	 * @param e
	 */
	private void send500(IOException e) {
		
	}

}
