<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    

String memoryInfo = SystemUtil.getMemoryInfo("\r\n");
String sysInfo = SystemUtil.getAllSystemProperties("\r\n");

%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="common/header.jsp" flush="true"/>
		<script type="text/javascript">
			function goLoginCheckAjax(){ 
				$.ajax({ 
					type:"POST", 
					url:"/com/login/loginCheck.do", 
					data:{}, 
					dataType:"json", 
					success:function(data, status, request){
						var dmIsLogin = data.dmIsLogin; 
						if( 'Y' == dmIsLogin.isLogin ){
							$("#LOGOUT_SPAN").show();
						}else{
							$("#LOGOUT_SPAN").hide();
						}
					}, 
					error : function(data, status, e) { 
						$("#LOGOUT_SPAN").hide();
					} 
				}); 
			}
			function goLogoutAjax(){ 
				$.ajax({ 
					type:"POST", 
					url:"/com/login/logout.do", 
					data:{}, 
					dataType:"json", 
					success:function(data, status, request){
						var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN');
						var ERR_CD = data['ERR_CD'];
						var ERR_MSG = data['ERR_MSG'];
						if( 'Y' == SUCCESS_YN ){
							alert("로그아웃되었습니다.");
							location.href = "/index.html";
						}
					}, 
					error : function(data, status, e) { 
						
					} 
				}); 
			} 
			function doLogoutSubmit(){ 
				document.SUBMIT_FORM.action="/com/login/logout.do";
				document.SUBMIT_FORM.submit();
			} 
		</script>
	</head>
	<body class="is-preload"  onload="javascript:goLoginCheckAjax();" >

		<!-- Wrapper -->
		<div id="wrapper">

			<!-- Main -->
			<div id="main">
			
				<div class="inner">

					<!-- Top -->
					<jsp:include page="common/top.jsp" flush="true"/>

					<section>
						<!-- Content Start  -->
						
						<span id="LOGOUT_SPAN" style="display: none">
						AJAX 로그아웃
						<form name="AJAX_FORM" method="post" action="">
						<input type="button" value="GO" onclick="javascript:goLogoutAjax();" >
						</form>
						<br>
						폼서밋 로그아웃
						<form name="SUBMIT_FORM" method="post" action="">
						<input type="button" value="GO" onclick="javascript:doLogoutSubmit();" >
						</form>
						</span>
						<br>
						<br>
						
						<h2 style="color: blue">【Spring Boot】</h2>
						<h3 style="color: blue">◈시스템메모리정보</h3>
						<textarea rows="10" cols="20" readonly ><%=memoryInfo %></textarea>
						
						<h3 style="color: blue">◈환경변수정보</h3>
						<textarea rows="10" cols="20" readonly ><%=sysInfo %></textarea>
						
						<!-- Content End  -->
					</section>

				</div>
			
			</div>

			<!-- Menu -->
			<jsp:include page="common/left.jsp" flush="true"/>

		</div>

	</body>
</html>