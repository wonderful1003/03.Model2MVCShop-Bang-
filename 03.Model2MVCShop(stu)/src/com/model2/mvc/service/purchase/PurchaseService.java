package com.model2.mvc.service.purchase;

import java.util.HashMap;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseService {
	
	public  Purchase getPurchase(int tranNo) throws Exception; 
	
	public  Purchase getPurchaseByProd(int prodNo) throws Exception; 
	
	public  HashMap<String, Object> getPurchaseList(Search search, String buyerId) throws Exception;
	
	public  HashMap<String, Object> getSaleList(Search search) throws Exception; 
	
	public void addPurchase(Purchase purchase) throws Exception; 
	
	public  void updatePurchase(Purchase purchase)  throws Exception; 
	
	public  void updateTranCode(Purchase purchase)  throws Exception; 
}
