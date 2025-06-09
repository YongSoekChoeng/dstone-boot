<%@page import="net.dstone.common.utils.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    
	String SUCCESS_YN = StringUtil.nullCheck(response.getHeader("SUCCESS_YN"), "");
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
		<!-- Header 영역 -->
		<jsp:include page="../common/header.jsp" flush="true"/>
		
		<script type="text/javascript">
			
			function doTestAjax(){ 
				$.ajax({ 
					type:"POST", 
					url:"/test/doTestAjax.do", 
					data:encodeURIComponent(JSON.stringify($(document.AJAX_FORM).serializeObject())), 
					dataType:"json", 
					success:function(data, status, request){
						var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN');
						var ERR_CD = data['ERR_CD'];
						var ERR_MSG = data['ERR_MSG'];
						if( 'Y' == SUCCESS_YN ){
							console.log('success ===>>> data:' + (JSON.stringify(data)));
							alert("success");
							$("#SUCCESS_YN").text("성공");
						}else{
							console.log('failure ===>>> data:' + (JSON.stringify(data)));
							alert("failure");
							$("#SUCCESS_YN").text("실패");
						}
						
					}, 
					error : function(data, status, e) { 
						console.log('system error ===>>> data:' + (JSON.stringify(data))); 
						alert("system error");
						$("#SUCCESS_YN").text("에러]");
					} 
				}); 
			} 
		
			function doTestSubmit(){ 
				document.SUBMIT_FORM.action="/test/doTestSubmit.do";
				document.SUBMIT_FORM.submit();
			} 

			function doTest(){ 
				alert('doTest');
			} 
		 
		</script>

	</head>
	<body class="is-preload">

		<!-- Wrapper -->
		<div id="wrapper">

			<!-- Main -->
			<div id="main">
			
				<div class="inner">

					<!-- Top 영역 -->
					<jsp:include page="../common/top.jsp" flush="true"/>
					
					<section>
						<!-- =============================================== Content 영역 Start =============================================== -->
						   
						폼서밋 테스트<%=("Y".equals(SUCCESS_YN)?"성공":("N".equals(SUCCESS_YN)?"실패":"")) %><br>
						<form name="SUBMIT_FORM" method="post" action="">
						TEST_PARAM1:<input type="text" name="MY_NAME" value="TEST..." >
						<br>
						FORCE_EXCEPTION_YN:<select name="FORCE_EXCEPTION_YN" ><option value="N">성공</option><option value="Y">실패</option></select>
						<br>
						<table border=1>
							<tr>
								<td colspan="2"> <input type="button" value="GO" onclick="javascript:doTestSubmit();" > </td>
							</tr>
						</table>
						</form>
						
						<br>
						<br>
						
						AJAX 테스트<span id="SUCCESS_YN"></span><br>
						<form name="AJAX_FORM" method="post" action="">
						TEST_PARAM1:<input type="text" name="MY_NAME" value="TEST..." >
						<br>
						FORCE_EXCEPTION_YN:<select name="FORCE_EXCEPTION_YN" ><option value="N">성공</option><option value="Y">실패</option></select>
						<br>
						<table border=1>
							<tr>
								<td colspan="2"> <input type="button" value="GO" onclick="javascript:doTestAjax();" > </td>
							</tr>
						</table>
						</form>
						        
						<br>
						<br>
						
						<!-- =============================================== Content 영역 End =============================================== -->
					</section>

				</div>
			
			</div>

			<!-- Menu 영역 -->
			<jsp:include page="../common/left.jsp" flush="true"/>

		</div>

	</body>
</html>
