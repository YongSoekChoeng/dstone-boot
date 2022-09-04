<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    

String memoryInfo = SystemUtil.getMemoryInfo("<br>");
String sysInfo = SystemUtil.getAllSystemProperties("<br>");

%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<h2 style="color: blue">【Spring Boot】</h2>
<h3 style="color: blue">◈시스템메모리정보</h3>
<%=memoryInfo %>
<h3 style="color: blue">◈환경변수정보</h3>
<%=sysInfo %>
</body>
</html>