<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    

%>  
<div id="sidebar">
	<div class="inner">
		<nav id="menu">
			<header class="major">
				<h2>Dstone</h2>
			</header>
			<ul>
				<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=main">main</a></li>
				<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=login">로그인</a></li>
				<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=sample/user/listUser">멤버조회</a></li>
				<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=sample/admin/manageUser">멤머입력</a></li>
				<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=analyzer/index">분석툴</a></li>
				<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=sample/google/maps/main">구글맵</a></li>
				<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=test/test">테스트1</a></li>
				<!--  
				<li>
					<span class="opener">멤버관리</span>
					<ul>
						<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=sample/user/listUser">멤버조회</a></li>
						<li><a href="<%=request.getContextPath()%>/defaultLink.do?defaultLink=sample/admin/manageUser">멤머입력</a></li>
					</ul>
				</li>
				-->
			</ul>
		</nav>
	</div>
</div>