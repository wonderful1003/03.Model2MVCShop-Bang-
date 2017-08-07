package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.jsp.jstl.sql.Result;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class PurchaseDAO {
	
	public PurchaseDAO() {
		// TODO Auto-generated constructor stub
	}

	public Purchase findPurchase(int tranNo) throws Exception {
		Connection conn = DBUtil.getConnection();
		
		String sql = "SELECT *FROM transaction WHERE tran_no = ?";
		
		PreparedStatement pstmt= conn.prepareStatement(sql);
		
		pstmt.setInt(1,  tranNo);
		
		ResultSet rs = pstmt.executeQuery();
		
		Purchase purchase = new Purchase();
		
		while (rs.next()) {
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setOrderDate(rs.getDate("order_data"));
			purchase.setPaymentOption(rs.getString("demailaddr"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			
			Product product = new ProductServiceImpl().getProduct(rs.getInt("prod_no"));
			User user = new UserServiceImpl().getUser(rs.getString("buyer_id"));
		
			purchase.setBuyer(user);
			purchase.setPurchaseProd(product);
				
		}
		
		rs.close();
		pstmt.close();
		conn.close();
				
		return purchase;
	}
	
	public HashMap<String, Object> getPurchaseList(Search search, String buyerId) throws Exception{
		
		Connection conn = DBUtil.getConnection();
		
		String sql = "SELECT ts.tran_no, ts.buyer_id, ts.receiver_name, ts.receiver_phone, ts.tran_status_code"
						+" FROM transaction ts"
						+" WHERE ts.buyer_id='"+buyerId+"'";
		
		sql += "ORDER BY ts.prod_no";
		
		int totalCount = this.getTotalCount(sql);
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		ArrayList<Purchase> purchaselist = new ArrayList<Purchase>();
		
		for(int i = 0 ; i < search.getPageUnit() ; i++ ){
			while(rs.next()){
				Purchase purchase = new Purchase();
				User user = new UserServiceImpl().getUser(rs.getString("buyer_id"));
				
				purchase.setTranNo(rs.getInt("tran_no"));
				purchase.setBuyer(user);
				purchase.setReceiverName(rs.getString("receiver_name"));
				purchase.setReceiverPhone(rs.getString("receiver_phone"));
				purchase.setTranCode(rs.getString("tran_status_code").trim());
				purchase.setTranNo(rs.getInt("tran_no"));
				purchaselist.add(purchase);
			}
		}
		
		map.put("totalCount", new Integer(totalCount));
		map.put("purchaselist", purchaselist);
		return map;
	}
	
	public HashMap<String, Object> getSaleList(Search search) {
		return null;
	}
	
	public void insertPurchase(Purchase purchase) throws Exception {

		Connection conn = DBUtil.getConnection();
		
		String sql = "INSERT INTO transaction VALUES(seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,sysdate,?)";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		User buyer = purchase.getBuyer();
		Product product = purchase.getPurchaseProd();
		
		pstmt.setInt(1, product.getProdNo());
		pstmt.setString(2, buyer.getUserId());
		pstmt.setString(3, purchase.getPaymentOption());
		pstmt.setString(4, purchase.getReceiverName());
		pstmt.setString(5, purchase.getReceiverPhone());
		pstmt.setString(6, purchase.getDivyAddr());
		pstmt.setString(7, purchase.getDivyRequest());
		pstmt.setInt(8, Integer.parseInt(purchase.getPaymentOption()));
		pstmt.setString(9, purchase.getDivyDate());
		
		pstmt.executeQuery();
		
		
		pstmt.close();
		conn.close();
				
	}
	
	public void updatePurchase(Purchase purchase) throws Exception {
		 
		Connection conn = DBUtil.getConnection();
		
		String sql = "UPDATE transaction SET payment_option=?, "
							+" receiver_name=?, receiver_phone=?, demailaddr=?"
							+" divy_request=?, divy_date=?"
							+ " WHERE tran_no=(SELECT tran_no "
							+ " WHERE tran_no=? ";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, purchase.getPaymentOption());
		pstmt.setString(2, purchase.getReceiverName());
		pstmt.setString(3, purchase.getReceiverPhone());
		pstmt.setString(4, purchase.getDivyAddr());
		pstmt.setString(5, purchase.getDivyRequest());
		pstmt.setString(6, purchase.getDivyDate());
		pstmt.setInt(7, purchase.getTranNo());
		
		pstmt.executeQuery();
		
		pstmt.close();
		conn.close();
	}
	
	public void updateTranCode(Purchase purchase) throws Exception {
		
		Connection conn = DBUtil.getConnection();
		
		String sql = " UPDATE transaction "+"SET tran_status_code = ? "
						+ " WHERE tran_no=(SELECT tran_no"
														+ " FROM product p, transaction t"
														+ " WHERE p.prod_no = t.prod_no AND p.prod_no=?)";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, purchase.getTranCode());
		pstmt.setInt(2, purchase.getPurchaseProd().getProdNo());
		
		pstmt.executeQuery();
		
		pstmt.close();
		conn.close();
	}
	
	private int getTotalCount(String sql) throws Exception {
			
		sql = " SELECT COUNT(*) "+
		          " FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
		
	private String makeCurrentPageSql(String sql , Search search){
			
		sql = 	" SELECT * "+ 
						" FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
										" 	FROM (	"+sql+" ) inner_table "+
										"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageUnit()+" ) " +
						" WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageUnit()+1) +" AND "+search.getCurrentPage()*search.getPageUnit();
			
		System.out.println("ProductDAO :: make SQL :: "+ sql);	
			
			return sql;
	}
	
}
