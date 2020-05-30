<%@ page contentType="text/html; charset=euc-kr" pageEncoding="euc-kr" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<html>
<head>
<title>상품 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
//검색 / page 두가지 경우 모두 Form 전송을 위해 JavaScrpt 이용  

function fncGetList(currentPage){
	$("#currentPage").val(currentPage)
	$("form").attr("mehtd", "POST").attr("action", "/product/listProduct?menu=${param.menu}").submit()
}

$(function(){
	$(".ct_btn01:contains('검색')").on("click", function(){
		javascript:fncGetList('1')
	})
})

$(function(){
	$(".stock div").css("text-align", "right")
	
	$(".stock span:contains('재고없는상품안보기')").on("click", function(){
		//alert(1)
		self.location = "/product/listProduct?menu=${param.menu}&stock=1"
	})
	
	$(".stock span:contains('재고없는상품보기')").on("click", function(){
		//alert(2)
		self.location = "/product/listProduct?menu=${param.menu}"
	})
	
})

$(function(){
	$(".prodName").css("color","green")
	$(".prodName").on("click", function(){
		var prodName = $(this).text().trim()
		alert(prodName)
		alert("${param.menu}")
		//self.location = "/product/getProduct?prodNo="+prodName+"&menu=${param.menu}"
	})
})

$(function(){
	$("span:contains('배송하기')").on("click", function(){
		var prodNo = $(this).text().trim()
		//alert(prodNo)
		self.location = "/purchase/updateTranCodeByProd?tranCode=2&prodNo="+prodNo
	})
})

</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">

<form name="detailForm">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					${param.menu=='manage'? "상품 관리" : "상품 목록조회" }
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		
		<td align="right">
			<select name="sortCondition" class="ct_input_g" style="width:100px" >
				<option value="0" ${!empty search.sortCondition && search.sortCondition ==0 ? "selected" : "" }>정렬순서</option>
				<option value="1" ${!empty search.sortCondition && search.sortCondition ==1 ? "selected" : "" }>가격높은순</option>
				<option value="2" ${!empty search.sortCondition && search.sortCondition ==2 ? "selected" : "" }>가격낮은순</option>
			</select>
			
			<select name="searchCondition" class="ct_input_g" style="width:80px">
			<c:if test="${param.menu=='manage' }">
				<option value="0" ${!empty search.searchCondition && search.searchCondition ==0 ? "selected" : "" }>상품번호</option>
			</c:if>
				<option value="1" ${!empty search.searchCondition && search.searchCondition ==1 ? "selected" : "" }>상품명</option>
				<option value="2" ${!empty search.searchCondition && search.searchCondition ==2 ? "selected" : "" }>상품가격</option>
			</select>
			<input type="text" name="searchKeyword" value="${search.searchKeyword}" class="ct_input_g"
						 style="width:200px; height:19px" />
		</td>
	
		
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						검색
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" class="stock">전체 ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage } 페이지
			<div>
			<span>재고없는상품안보기</span>
			<span>재고없는상품보기</span>
			</div>
			<input type="hidden" name="stock" value="${!empty search.stock ? search.stock : ""}" />
		</td>
		
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">등록일</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	
	<c:set var="i" value="0" />
	<c:forEach var="product" items="${list }">
		<c:set var="i" value="${i+1 }" />
	<tr class="ct_list_pop">
		<td align="center">${i }</td>
		<td></td>
		<c:if test="${empty product.proTranCode }">
			<td align="left" class="prodName">${product.prodName}</td>
		</c:if>
		<c:if test="${!empty product.proTranCode }">
			<td align="left">${product.prodName}</td>		
		</c:if>
		<td></td>
		<td align="left">${product.price }</td>
		<td></td>
		<td align="left">${product.regDate }</td>
		<td></td>
		<td align="left">
			<c:if test="${param.menu=='manage' }">
					<c:if test="${empty product.proTranCode }">판매중</c:if>	
					<c:if test="${product.proTranCode == '0  '}">구매완료
						 <span>배송하기</span><span id="prodNo" style="visibility: hidden;">${product.prodNo }</span>
					</c:if>
					<c:if test="${product.proTranCode == '1  '}"></c:if>
					<c:if test="${product.proTranCode == '2  '}">배송중</c:if>
					<c:if test="${product.proTranCode == '3  '}">배송완료</c:if>
			</c:if>
			<c:if test="${!(param.menu=='manage') }">
					${empty product.proTranCode ? "판매중" : "재고없음" }
			</c:if>
		
		</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	</c:forEach>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
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

