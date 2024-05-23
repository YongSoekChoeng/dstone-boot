<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    
/******************************************* 변수 선언 시작 *******************************************/              
net.dstone.common.utils.RequestUtil requestUtil = new net.dstone.common.utils.RequestUtil(request, response);

/******************************************* 변수 선언 끝 *********************************************/           
%>   

			<!-- Footer -->
				<div id="footer-wrapper">
					<div class="container">
						<div class="row">
							<div class="col-8 col-12-medium">

								<section>
									<h2>Application Analyzer</h2>
									<div>
										<div class="row">
											<div class="col-3 col-6-medium col-12-small">
												<ul class="link-list">
													<li>
														<a href="<%=requestUtil.getStrContextPath()%>/defaultLink.do?defaultLink=analyzer/configuration/configuration" >Configuration</a>
													</li>
												</ul>
											</div>
											<div class="col-3 col-6-medium col-12-small">
												<ul class="link-list">
													<li>
														<a href="<%=requestUtil.getStrContextPath()%>/defaultLink.do?defaultLink=analyzer/analyzation/analyzation" >Analyzation</a>
													</li>
												</ul>
											</div>
											<div class="col-3 col-6-medium col-12-small">
												<ul class="link-list">
													<li>
														<a href="<%=requestUtil.getStrContextPath()%>/defaultLink.do?defaultLink=analyzer/report/main" >Report</a>
													</li>
												</ul>
											</div>
										</div>
									</div>
								</section>

							</div>
						</div>
						<div class="row">
							<div class="col-12">

								<div id="copyright">
									&copy; Untitled. All rights reserved. | Design: <a href="http://html5up.net">HTML5 UP</a>
								</div>

							</div>
						</div>
					</div>
				</div>
