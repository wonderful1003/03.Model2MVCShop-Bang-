package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class PurchaseDAO {

	public PurchaseDAO() {
		// TODO Auto-generated constructor stub
	}

	public Purchase findPurchase(int tranNo) throws Exception{
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE tran_no = ?";
			
		PreparedStatement pstmt = con.prepareStatement(sql);//프리컴파일된 SQL 문을 나타내는 객체 
		
		pstmt.setInt(1, tranNo);
		
		ResultSet rs = pstmt.executeQuery();
		//데이터베이스 결과 집합을 나타내는 데이터 테이블
		Purchase purchase = new Purchase();
		
		while(rs.next()){
			
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));
			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setOrderDate(rs.getDate("order_data"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
			
			Product product = new ProductServiceImpl().getProduct(rs.getInt("prod_no"));
			User user = new UserServiceImpl().getUser(rs.getString("buyer_id"));
			
			purchase.setBuyer(user);
			purchase.setPurchaseProd(product);
			
		}
		
		return purchase;
	}
	
	public HashMap<String,Object> getPurchaseList(Search search,String buyerId) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT ts.tran_no, ts.buyer_id, ts.receiver_name, ts.receiver_phone, ts.tran_status_code "
					+ "from transaction ts "
					+ "WHERE ts.buyer_id='"+buyerId+"'";
		
		sql += " order by ts.PROD_NO";
		int totalCount = this.getTotalCount(sql);
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		ArrayList<Purchase> purchaseList = new ArrayList<Purchase>();
		
		for (int i = 0; i < search.getPageUnit(); i++) {
			while (rs.next()) {
				Purchase purchase = new Purchase();
				User user = new UserServiceImpl().getUser(rs.getString("buyer_id"));

				purchase.setTranNo(rs.getInt("tran_no"));
				purchase.setReceiverName(rs.getString("receiver_name"));
				purchase.setReceiverPhone(rs.getString("receiver_phone"));
				purchase.setTranCode(rs.getString("tran_status_code").trim());
				purchase.setBuyer(user);
				purchaseList.add(purchase);
			}
		}
		map.put("totalCount", new Integer(totalCount));
		map.put("purchaseList", purchaseList);
		return map;
	}
	
	public HashMap<String,Object> getSaleList(Search search){
		return null;
	}
	
	public void insertPurchase(Purchase purchase) throws Exception{
		Connection con = DBUtil.getConnection();
		String sql = "INSERT INTO transaction VALUES(seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,sysdate,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		User buyer = purchase.getBuyer();
		Product product = purchase.getPurchaseProd();
		
		pstmt.setInt(1,product.getProdNo());
		pstmt.setString(2, buyer.getUserId());
		pstmt.setString(3, purchase.getPaymentOption());
		pstmt.setString(4, purchase.getReceiverName());
		pstmt.setString(5, purchase.getReceiverPhone());
		pstmt.setString(6, purchase.getDivyAddr());
		pstmt.setString(7, purchase.getDivyRequest());
		pstmt.setInt(8,Integer.parseInt(purchase.getPaymentOption()));
		pstmt.setString(9, purchase.getDivyDate());
		
		pstmt.executeQuery();

		con.close();
	
	}
	
	public void updatePurchase(Purchase purchase) throws Exception{
		Connection con = DBUtil.getConnection();
		String sql = "UPDATE transaction SET payment_option=?, "
				+ "receiver_name=?, receiver_phone=?, demailaddr=?, "
				+ "dlvy_request=?, dlvy_date=?"
				+ "WHERE tran_no = ? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		
		pstmt.setString(1, purchase.getPaymentOption());
		pstmt.setString(2, purchase.getReceiverName());
		pstmt.setString(3, purchase.getReceiverPhone());
		pstmt.setString(4, purchase.getDivyAddr());
		pstmt.setString(5, purchase.getDivyRequest());
		pstmt.setString(6, purchase.getDivyDate());
		pstmt.setInt(7, purchase.getTranNo());
		
		pstmt.executeQuery();
	}
	
	public void updateTranCode(Purchase purchase) throws Exception{
		Connection con = DBUtil.getConnection();
		String sql = "UPDATE transaction SET tran_status_code=? "
					+ "WHERE tran_no=(SELECT tran_no "
									+ "FROM product p, transaction t "
									+ "WHERE p.prod_no = t.prod_no AND p.prod_no=?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, purchase.getTranCode());
		System.out.println(purchase.getTranCode());
		pstmt.setInt(2, purchase.getPurchaseProd().getProdNo());
		System.out.println(purchase.getTranNo());
		pstmt.executeQuery();
	}
	
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
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
			
		sql = 	"SELECT * "+ 
				"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
								" 	FROM (	"+sql+" ) inner_table "+
								"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageUnit()+" ) " +
				"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageUnit()+1) +" AND "+search.getCurrentPage()*search.getPageUnit();
	
		System.out.println("PurchaseDAO :: make SQL :: "+ sql);	
			
			return sql;
	}
}
