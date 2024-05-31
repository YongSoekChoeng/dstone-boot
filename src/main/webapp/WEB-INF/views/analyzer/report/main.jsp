<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    

/******************************************* 변수 선언 시작 *******************************************/              
net.dstone.common.utils.RequestUtil requestUtil = new net.dstone.common.utils.RequestUtil(request, response);

/******************************************* 변수 선언 끝 *********************************************/           
    

%> 
<!DOCTYPE HTML>
<!--
	Minimaxing by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>

	<!-- Head -->
	<jsp:include page="../common/head.jsp"></jsp:include>
		
	<body>
		<div id="page-wrapper">

			<!-- Header -->
			<jsp:include page="../common/header.jsp"></jsp:include>
			
			<!-- Main -->
				<div id="main">
					<div class="container">
						<div class="row main-row">
							<div class="col-2 col-12-medium">

								<section>
									<h2>Compelling links</h2>
									<ul class="link-list">
										<li><a href="#">Quis accumsan lorem</a></li>
										<li><a href="#">Sed neque nisi consequat</a></li>
										<li><a href="#">Eget et amet consequat</a></li>
										<li><a href="#">Dapibus sed mattis blandit</a></li>
										<li><a href="#">Vitae magna sed dolore</a></li>
										<li><a href="#">Eget et amet consequat</a></li>
										<li><a href="<%=requestUtil.getStrContextPath()%>/defaultLink.do?defaultLink=analyzer/report/overall">종합결과</a></li>
									</ul>
								</section>

							</div>
							<div class="col-10 col-12-medium imp-medium">

								<section class="middle-content">
									<h2 align="center" style="font-weight: bold;" >Three Column (two sidebars)</h2>
								</section>

								<section>
									<h2>Something profound</h2>
									<p>Duis neque sed nisi, dapibus sed mattis rutrum accumsan sed.
									Suspendisse eu varius amet nibh. Suspendisse vitae magna eget odio amet
									mollis justo facilisis quis. Sed sagittis mauris amet tellus gravida
									lorem ipsum dolor consequat blandit tempus ipsum dolor lorem sit amet.</p>
								</section>
								
							</div>

						</div>
					</div>
				</div>

			<!-- Footer -->
			<jsp:include page="../common/footer.jsp"></jsp:include>

		</div>

	</body>
</html>