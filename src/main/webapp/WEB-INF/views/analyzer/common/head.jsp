<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    
/******************************************* 변수 선언 시작 *******************************************/              
net.dstone.common.utils.RequestUtil requestUtil = new net.dstone.common.utils.RequestUtil(request, response);

/******************************************* 변수 선언 끝 *********************************************/           
%>  
	<head>
		<title>Application Analyzer</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<link rel="stylesheet" href="<%=requestUtil.getStrContextPath()%>/analyzer/assets/css/main.css" />
		<!-- Scripts -->
		<script src="<%=requestUtil.getStrContextPath()%>/js/jquery-1.7.2.js"></script>
		<script src="<%=requestUtil.getStrContextPath()%>/js/jquery.json-2.4.js" ></script> 
		<script src="<%=requestUtil.getStrContextPath()%>/js/common.js" ></script> 
		<script src="<%=requestUtil.getStrContextPath()%>/analyzer/assets/js/browser.min.js"></script>
		<script src="<%=requestUtil.getStrContextPath()%>/analyzer/assets/js/breakpoints.min.js"></script>
		<script src="<%=requestUtil.getStrContextPath()%>/analyzer/assets/js/util.js"></script>
		<script src="<%=requestUtil.getStrContextPath()%>/analyzer/assets/js/main.js"></script>		
	</head>