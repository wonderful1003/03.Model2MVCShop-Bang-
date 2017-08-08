<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 

<%@page import="com.model2.mvc.service.domain.Purchase"%>
<%@page import="com.model2.mvc.common.Search"%>
<%@page import="com.model2.mvc.service.domain.Product"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.model2.mvc.service.domain.User"%>

<%
	User user = (User)session.getAttribute("user");
	HashMap<String,Object> map = (HashMap<String,Object>)request.getAttribute("map");
	Search search = (Search)request.getAttribute("search");
	
	int total=0;
	ArrayList<Product> productList=null;
	ArrayList<Purchase> purchaseList=null;
	if(map != null){
		total=((Integer)map.get("count")).intValue();
		productList=(ArrayList<Product>)map.get("productList");
		purchaseList=(ArrayList<Purchase>)map.get("purchaseList");
	}else{
		System.out.println("map is null");
	}
	
	int currentPage=search.getCurrentPage();
	
	int totalPage=0;
	if(total > 0) {
		totalPage= total / search.getPageUnit() ;
		if(total%search.getPageUnit() >0)
			totalPage += 1;
	}
%> --%>


<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncGetList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
		document.detailForm.submit();
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/listPurchase.do" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">구매 목록조회</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<%-- <td colspan="11">전체 <%=total %> 건수, 현재 <%=currentPage %> 페이지</td> --%>
		<td colspan="11">전체 ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage } 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">전화번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">정보수정</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

	
	
	<%-- <%	int no = productList.size(); 
		for(int i = 0; i<productList.size(); i++){ %>
	<tr class="ct_list_pop">
		<td align="center">
			<a href="/getPurchase.do?tranNo=<%=purchaseList.get(i).getTranNo()%>"><%=no--%></a>
		<%-- </td>
		<td></td>
		<td align="left">
			<a href="/getUser.do?userId=<%=user.getUserId()%>"><%=user.getUserId()%></a>
		</td>
		<td></td>
		<td align="left"><%=user.getUserName() %></td>
		<td></td>
		<td align="left"><%=user.getPhone() %></td>
		<td></td>
		<td align="left">현재
					<%=purchaseList.get(i).getTranCode() %>
				상태 입니다.</td>
		<td></td>
		<td align="left">
			
		</td>
	</tr> 
	<%} %> --%>
	
	
	<c:set var="i" value="0"/>
		<c:forEach var="purchase" items="${purchaselist}">
			<c:set var="i" value="${ i+1 }"/>
			<tr class="ct_list_pop">
			<td align="center">
				<c:if test="${purchase.tranCode eq 1 }">
				<a href="/getPurchase.do?tranNo=${purchase.tranNo }"> ${i}</a>
				</c:if>
				<c:if test="${purchase.tranCode ne 1 }">
				${i}
				</c:if>
			</td>
			<td></td>
			<td align="left">
				<a href="/getUser.do?userId=${user.userId }"> ${user.userId }</a>
			</td>
			<td></td>
			<td align="left">${user.userName }</td>
			<td></td>
			<td align="left">${user.phone }</td>
			<td></td>
			<td align="left">현재
						<c:choose>
						<c:when test="${! empty purchase.tranCode && purchase.tranCode eq 1}">
							구매완료
						</c:when>
						<c:when test="${! empty purchase.tranCode && purchase.tranCode eq 2}">
							배송중
						</c:when>
						<c:when test="${! empty purchase.tranCode && purchase.tranCode eq 3}">
							배송완료
						</c:when>
					</c:choose>
						<!-- ${purchase.tranCode} -->
					상태 입니다.</td>
			<td></td>
			<td align="left">	
			<c:if test="${! empty purchase.tranCode && purchase.tranCode eq 2}">
			<a href="updateTranCode.do?tranNo=${purchase.tranNo }&tranCode=3">물건도착</a>
			</c:if>
		</td>
	</tr>
	</c:forEach>
	
	
	
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
	<tr>
		<td align="center">
		
		<input type="hidden" id="currentPage" name="currentPage" value=""/>
		
		 	<jsp:include page="../common/pageNavigator.jsp"/>		
		
		</td>
	</tr>
</table>

<!--  페이지 Navigator 끝 -->
</form>

</div>

</body>
</html>