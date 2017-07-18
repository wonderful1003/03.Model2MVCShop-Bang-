package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class UpdateProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		Product product=new Product();
		
		
		product.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		System.out.println(product.getProdNo());
		product.setProdName(request.getParameter("prodName"));
		System.out.println(product.getProdName());
		product.setProdDetail(request.getParameter("prodDetail"));
		System.out.println(product.getProdDetail());
		product.setManuDate(request.getParameter("manuDate"));
		System.out.println(Integer.parseInt(request.getParameter("price")));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		System.out.println("šx´õ"+product);
		
		System.out.println("updateproduct");
		ProductService service=new ProductServiceImpl();
		service.updateProduct(product);
		
		request.setAttribute("product",product);
		return "forward:/getProduct.do?prodNo="+request.getParameter("prodNo");
	}
}