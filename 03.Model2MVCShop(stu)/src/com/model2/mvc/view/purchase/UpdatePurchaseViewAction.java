package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImp;

public class UpdatePurchaseViewAction extends Action {

	public UpdatePurchaseViewAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Purchase purchase = new Purchase();
		PurchaseService service = new PurchaseServiceImp();
		purchase = service.getPurchase(Integer.parseInt(request.getParameter("tranNo")));
		
		request.setAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}

}
