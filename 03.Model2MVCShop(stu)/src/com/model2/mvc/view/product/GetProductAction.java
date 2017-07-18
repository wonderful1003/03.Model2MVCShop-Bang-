package com.model2.mvc.view.product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class GetProductAction extends Action{

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		//String prodNo = request.getParameter("prodNo");

		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if(cookies != null && cookies.length > 0){
			for(int i = 0; i < cookies.length; i++){
				cookie = cookies[i];
				if(cookie.getName().equals("history")){
					cookie = new Cookie( "history", cookies[i].getValue()+","+prodNo);
				}else{
					cookie = new Cookie("history",String.valueOf(prodNo));
				}
			}
		}
		
		cookie.setMaxAge(60*5);
		response.addCookie(cookie);
	/*	내가 했던 안되는 예)	
		String history = null;
		
		Cookie c = new Cookie("history",String.valueOf(prodNo));
		Cookie[] cookies = request.getCookies();
			
		if (cookies!=null && cookies.length > 0) {
			
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				
				if (cookie.getName().equals("history")) {
					history = cookie.getValue();
					history = prodNo+",";
					
					response.addCookie(cookie);
				}
			}
		}
		*/
		ProductService service=new ProductServiceImpl();
		Product product=service.getProduct(prodNo);

		request.setAttribute("product", product);
		System.out.println("GetProduct 오류");
		
		if (request.getParameter("menu") != null) {
			if (request.getParameter("menu").equals("manage")) {
				return "forward:/product/updateProductView.jsp";
				
			} else {
				return "forward:/product/getProduct.jsp";
			}
		}else{
			return "forward:/product/getProduct.jsp";
		}
		
	
	}	

}
	
