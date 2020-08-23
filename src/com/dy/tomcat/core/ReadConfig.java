package com.dy.tomcat.core;

import java.io.InputStream;
import java.util.Properties;

/**
 * 采用单例模式读取配置文件
 * @author 董仪
 *
 */
public class ReadConfig extends Properties{
	private static final long serialVersionUID = 1L;
	
	private static ReadConfig instance = new ReadConfig();
	
	private ReadConfig() {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("web.properties");
			load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ReadConfig getInstance() {
		return instance;
	}
	
}
