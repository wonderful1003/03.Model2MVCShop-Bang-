package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImp;

public class UpdateTranCodeByProdAction extends Action {

	public UpdateTranCodeByProdAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PurchaseService service = new PurchaseServiceImp();
		Purchase purchase = service.getPurchase2(Integer.parseInt(request.getParameter("prodNo")));
		purchase.setTranCode(request.getParameter("tranCode"));
		service.updateTranCode(purchase);
				
		return "forward:/listProduct.do?menu=manage";
	}

}
