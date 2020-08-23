package com.dy.tomcat.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Request {
	private String url; // 请求资源地址
	private String method; // 请求方式
	private String protocolVersion; // 协议版本
	private InputStream is = null;
	private Map<String, String> parameters = new HashMap<String, String>(); // 放置地址栏请求参数
	
	public Request(InputStream is) {
		this.is = is;
		parse(); // 解析请求
	}

	/**
	 * 解析请求
	 */
	private void parse() {
		try {
			BufferedReader read = new BufferedReader(new InputStreamReader(is));
			String line = null;
			int flag = 0; // 默认请求第一行
			
			
			while ( (line = read.readLine()) != null && !"".equals(line)) {
				System.out.println(flag + "-" + line);
				if (flag == 0) { // 起始行
					String[] strs = line.split(" "); // 获取请求方式，资源地址，版本等
					this.method = strs[0];
					this.protocolVersion = strs[2];
					
					System.out.println(strs[1]);
					
					if ("GET".equals(method)) {
						doGet(strs[1]); // 处理get请求
					}
				}
				
				flag++;
			}
			System.out.println("end...");
			
			/*
			if ( (line = read.readLine()) != null ) {
					String[] strs = line.split(" "); // 获取请求方式，资源地址，版本等
					this.method = strs[0];
					this.protocolVersion = strs[2];
					
				//	System.out.println(strs[1]);
					
					if ("GET".equals(method)) {
						doGet(strs[1]); // 处理get请求
					}
				
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 处理get请求
	 * @param str 请求资源
	 */
	private void doGet(String str) {
		// 需要判断地址栏是否携带请求参数
		if (!str.contains("?")) {
			// 没有参数
			this.url = str;
			return;
		}
		
		// 有参数时的处理
		String paramStr = str.substring(str.indexOf("?") + 1);
		// 参数为键值对形式
		String[] params = paramStr.split("&");
		String[] param = null;
		
		for (String strs : params) {
			param = strs.split("=");
			this.parameters.put(param[0], param[1]);
		}
		
		// 获取请求地址
		this.url = str.substring(0, str.indexOf("?"));
		//System.out.println(url);
	}

	public String getUrl() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public InputStream getIs() {
		return is;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	
}
