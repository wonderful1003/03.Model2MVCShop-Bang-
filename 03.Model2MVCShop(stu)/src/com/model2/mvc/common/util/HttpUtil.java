package com.model2.mvc.common.util;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HttpUtil {
	
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path){
		try{
			System.out.println("http������:"+path);
			RequestDispatcher dispatcher = request.getRequestDispatcher(path);
			System.out.println("����:"+response);
			dispatcher.forward(request, response);
		
		}catch(Exception ex){
			System.out.println("forward ���� : " + ex);
			throw new RuntimeException("forward ���� : " + ex);
		}
	}
	
	public static void redirect(HttpServletResponse response, String path){
		try{
			response.sendRedirect(path);
		}catch(Exception ex){
			System.out.println("redirect ���� : " + ex);
			throw new RuntimeException("redirect ����  : " + ex);
		}
	}
}