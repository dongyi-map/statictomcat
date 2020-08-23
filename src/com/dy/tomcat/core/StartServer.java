package com.dy.tomcat.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {
	public static void main(String[] args) {
		try {
			new StartServer().start();  // 启动服务器
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() throws IOException {
		// 读取端口号
		int port = Integer.parseInt(ReadConfig.getInstance().getProperty("port"));
		// 启动服务器
		ServerSocket ssk = new ServerSocket(port);
		
		System.out.println("服务器启动成功，端口号为" + port);
		
		new ParseXml();
		
		// 创建线程池
		ExecutorService serviceThread = Executors.newFixedThreadPool(20);
		
		// 监听客服端请求
		Socket sk = null;
		while(true) {
			sk = ssk.accept();
			
			// 线程池处理
			serviceThread.submit(new ServiceSesssion(sk));
		}
	}
}
