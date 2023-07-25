<%@page import="net.dstone.common.utils.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String ERR_CD 			= StringUtil.nullCheck(request.getAttribute("ERR_CD"), "");
	String ERR_MSG 			= StringUtil.nullCheck(request.getAttribute("ERR_MSG"), "");
	String ERR_MSG_DETAIL 	= StringUtil.nullCheck(request.getAttribute("ERR_MSG_DETAIL"), "");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error Page</title>
</head>
<body>
	에러코드 : <%=ERR_CD %>
	<br>
	에러메세지 : <%=ERR_MSG %>
	<br>
	에러메세지상세
	<br>
	<textarea rows="10" cols="100"><%=ERR_MSG_DETAIL %></textarea>
</body>
</html>