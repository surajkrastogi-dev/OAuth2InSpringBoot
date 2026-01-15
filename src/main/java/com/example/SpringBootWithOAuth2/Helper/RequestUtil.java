package com.example.SpringBootWithOAuth2.Helper;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class RequestUtil {
	
	public static String generateIpAddress(HttpServletRequest request){
		try {
			
			System.out.println("Headers Parts User-Agent:"+request.getHeader("User-Agent")
				+"X-Forwarded-For:"+request.getHeader("X-Forwarded-For")+"Remote Address:"+request.getRemoteAddr());
			
			String xf = request.getHeader("X-Forwarded-For");
			return xf !=null ? xf.split(",")[0] : request.getRemoteAddr() ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static String generateUserAgent(HttpServletRequest request) {
		try {
			
			String userAgent = request.getHeader("User-Agent");
			return userAgent;
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
