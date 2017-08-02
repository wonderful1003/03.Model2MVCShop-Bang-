package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImp;

public class AddPurchaseAction extends Action {

	public AddPurchaseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Purchase purchase = new Purchase();
		User buyer = new User();
		Product product = new Product();
		
		buyer = (User)request.getSession().getAttribute("user");
		
		product = new ProductServiceImpl().getProduct
					(Integer.parseInt(request.getParameter("prodNo")));
		
		
		purchase.setBuyer(buyer);
		purchase.setPurchaseProd(product);
		
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("receiverDate"));
		
		PurchaseService service = new PurchaseServiceImp();
		service.addPurchase(purchase);
		
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}

}
