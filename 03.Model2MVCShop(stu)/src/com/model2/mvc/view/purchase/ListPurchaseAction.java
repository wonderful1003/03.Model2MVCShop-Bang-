package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class ListPurchaseAction extends Action {

	public ListPurchaseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Search search = new Search();
				
		int currentPage=1;
	
		if(request.getParameter("currentPage")!=null && !(request.getParameter("currentPage").equals(""))){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
	
		search.setCurrentPage(currentPage);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		int pageSize = Integer.parseInt(request.getServletContext().getInitParameter("pageSize"));
		int pageUnit = Integer.parseInt(request.getServletContext().getInitParameter("pageUnit"));
		search.setPageSize(pageSize);
		
		
		String buyerId = ((User)(request.getSession(true).getAttribute("user"))).getUserId();
		PurchaseService service = new PurchaseServiceImpl();
		HashMap<String, Object> map = service.getPurchaseList(search, buyerId);
		
		Page resultPage = 
				new Page( currentPage, ((Integer)map.get("totalCount")).intValue(), 
						pageUnit, pageSize);
		
		request.setAttribute("resultPage", resultPage);
		request.setAttribute("purchaseList", map.get("purchaseList"));
		request.setAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}

}
