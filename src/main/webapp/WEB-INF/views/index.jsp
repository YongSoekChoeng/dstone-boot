<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    

%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<!-- Header 영역 -->
		<jsp:include page="common/header.jsp" flush="true"/>
	</head>
	<body class="is-preload">

		<!-- Wrapper -->
		<div id="wrapper">

			<!-- Main -->
			<div id="main">
			
				<div class="inner">

					<!-- Top 영역 -->
					<jsp:include page="common/top.jsp" flush="true"/>

					<section>
					
						<!-- =============================================== Content 영역 Start =============================================== -->
						<header class="main">
							<h1>Main </h1>
						</header>
						<!-- =============================================== Content 영역 End =============================================== -->
						
					</section>

				</div>
			
			</div>

			<!-- Menu 영역 -->
			<jsp:include page="common/left.jsp" flush="true"/>

		</div>

	</body>
</html>