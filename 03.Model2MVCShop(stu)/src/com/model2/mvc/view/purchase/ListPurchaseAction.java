package com.model2.mvc.view.purchase;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImp;

public class ListPurchaseAction extends Action {

	public ListPurchaseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Search search = new Search();
		
		int currentPage=1;
		if(request.getParameter("currentPage") != null && !(request.getParameter("currentPage")).equals("")){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
			System.out.println("이번에 눌린 커런트 페이지"+currentPage);
		}
		
		search.setCurrentPage(currentPage);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
		search.setPageUnit(pageUnit);

		
		String buyerId = ((User)(request.getSession(true).getAttribute("user"))).getUserId();
		PurchaseService service = new PurchaseServiceImp();
		
		HashMap<String, Object> map = service.getPurchaseList(search, buyerId);
		
		Page resultPage	= 
				new Page( currentPage, ((Integer)map.get("totalCount")).intValue(),
						pageUnit, pageSize);
		System.out.println("ListPurchaseAction ::"+resultPage);
		
		request.setAttribute("resultPage", resultPage);
		request.setAttribute("purchaseList", map.get("purchaseList"));
		request.setAttribute("search",search);
		
		return "forward:/purchase/listPurchase.jsp";
	}

}