package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class AddPurchaseViewAction extends Action {

	public AddPurchaseViewAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

		int prod_no = Integer.parseInt(request.getParameter("prod_no"));
		
		Purchase purchase = new Purchase();
		
		purchase.setBuyer((User)request.getSession().getAttribute("user"));
		purchase.setPurchaseProd(new ProductServiceImpl().getProduct(prod_no));
		
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}

}
