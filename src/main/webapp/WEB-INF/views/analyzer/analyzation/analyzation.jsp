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
	
	<!-- Script -->
	<script type="text/javascript"> 
	
		$(document).ready(function(){
			$("#btnAnalyze").on("click", btnAnalyzeOnclick);
		});
		
	    function goAppSelectList(){ 
	        $.ajax({  
	            type:"POST",  
	            url:"/analyzer/configuration/listSys.do", 
	            data:"{}",  
	            dataType:"json",  
	            success:function(data, status, request){ 
	                var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN'); 
	                var ERR_CD = request.getResponseHeader('ERR_CD'); 
	                var ERR_MSG = decodeURIComponent(request.getResponseHeader('ERR_MSG')); 
	                if( 'Y' == SUCCESS_YN ){ 
	                    var FORCED_TO_URL = request.getResponseHeader('FORCED_TO_URL'); 
	                    if(FORCED_TO_URL && "" != FORCED_TO_URL){ 
	                        location.href = "/defaultLink.do?defaultLink=" + FORCED_TO_URL; 
	                    }else{ 
	                        //console.log('success ===>>> data:' + (JSON.stringify(data))); 
	                        var appSel = $("#SYS_ID"); 
	                        appSel.empty(); 
	                        var returnList = data.returnObj.returnObj; 
	                        var lineStr = ""; 
	                        for(var i=0; i<returnList.length; i++){ 
	                            lineStr = ""; 
	                            lineStr = lineStr + "<option value='"+returnList[i].SYS_ID+"' >" + returnList[i].SYS_NM + "</option>"; 
	                            appSel.append(lineStr); 
	                        } 
	                        appSel.on("change", appSelOnChange);
	                        
	                        if( returnList.length > 0 ){
	                        	goSelectDetail(returnList[0].SYS_ID);
	                        }
	                    } 
	                }else{ 
	                    console.log('failure ===>>> data:' + (JSON.stringify(data))); 
	                    alert("failure ERR_MSG:" + ERR_MSG); 
	                } 
	            }, 
				error : function(data, status, e) { 
					alert(e); 
				} 
			}); 
		} 
			
		function appSelOnChange(e){
			var appSel = $(e.target);
			var SYS_ID = appSel.val();
			goSelectDetail(SYS_ID);
		}
	
	    function goSelectDetail(SYS_ID){
	        $.ajax({ 
	            type:"POST", 
	            url:"/analyzer/configuration/getAppOption.do", 
	            data:'{"SYS_ID":"'+SYS_ID+'"}', 
	            dataType:"json", 
	            success:function(data, status, request){ 
	                var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN'); 
	                var ERR_CD = request.getResponseHeader('ERR_CD'); 
	                var ERR_MSG = decodeURIComponent(request.getResponseHeader('ERR_MSG')); 
	                if( 'Y' == SUCCESS_YN ){ 
	                	var FORCED_TO_URL = request.getResponseHeader('FORCED_TO_URL'); 
	                	if(FORCED_TO_URL && "" != FORCED_TO_URL){ 
	                		location.href = "/defaultLink.do?defaultLink=" + FORCED_TO_URL; 
	                	}else{ 
	                		var returnObj = data.returnObj;

	                		
						}	
	                } 
	            }, 
	            error : function(data, status, e) { 
	                alert(e); 
	            } 
	        }); 
	    }
	    
	    function btnAnalyzeOnclick(){
	    	if(isValid()){
	    		goAnalyze();
	    	}
	    }
	    
	    function isValid(){
	    	var isValid = true;
	    	var formObj = document.FORM_ANALYSIS; 
	    	
	    	return isValid;
	    }

		function goAnalyze(){
	        $.ajax({ 
	            type:"POST", 
	            url:"/analyzer/analysis/doAnalyzing.do", 
	            data:encodeURIComponent(JSON.stringify($(document.FORM_ANALYSIS).serializeObject())), 
	            dataType:"json", 
	            success:function(data, status, request){ 
	                var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN'); 
	                var ERR_CD = request.getResponseHeader('ERR_CD'); 
	                var ERR_MSG = decodeURIComponent(request.getResponseHeader('ERR_MSG')); 
	                if( 'Y' == SUCCESS_YN ){ 
	                	var FORCED_TO_URL = request.getResponseHeader('FORCED_TO_URL'); 
	                	if(FORCED_TO_URL && "" != FORCED_TO_URL){ 
	                		location.href = "/defaultLink.do?defaultLink=" + FORCED_TO_URL; 
	                	}
	                	doMonitoring();
	                }else{ 
	                    console.log('failure ===>>> data:' + (JSON.stringify(data))); 
	                    alert("failure ERR_MSG:" + ERR_MSG); 
	                }
	                $("#btnAnalyze").attr("disabled",true); 
	            }, 
	            error : function(data, status, e) { 
	                console.log('system error ===>>> data:' + (JSON.stringify(data))); 
	                alert("system error"); 
	            } 
	        });
		}
		
		function doMonitoring(){
			console.log('doMonitoring ===>>> line 148' ); 
	        $.ajax({ 
	            type:"POST", 
	            url:"/analyzer/analysis/doMonitoring.do", 
	            data:encodeURIComponent(JSON.stringify($(document.FORM_ANALYSIS).serializeObject())), 
	            dataType:"json", 
	            success:function(data, status, request){ 
	                var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN'); 
	                var ERR_CD = request.getResponseHeader('ERR_CD'); 
	                var ERR_MSG = decodeURIComponent(request.getResponseHeader('ERR_MSG')); 
	                if( 'Y' == SUCCESS_YN ){ 
	                	var FORCED_TO_URL = request.getResponseHeader('FORCED_TO_URL'); 
	                	if(FORCED_TO_URL && "" != FORCED_TO_URL){ 
	                		location.href = "/defaultLink.do?defaultLink=" + FORCED_TO_URL; 
	                	}
	                }else{ 
	                    console.log('failure ===>>> data:' + (JSON.stringify(data))); 
	                    alert("failure ERR_MSG:" + ERR_MSG); 
	                }
	                console.log('success ===>>> data:' + (JSON.stringify(data))); 
	            }, 
	            error : function(data, status, e) { 
	                console.log('system error ===>>> data:' + (JSON.stringify(data))); 
	                alert("system error"); 
	            } 
	        });			
		}

	</script> 

	<body  onLoad="javascript:goAppSelectList();" >
		<div id="page-wrapper">

			<!-- Header -->
			<jsp:include page="../common/header.jsp"></jsp:include>
			
			<!-- Main -->
				<div id="main">
					<div class="container">
						<div class="row main-row">
							<div class="col-12">

								<section>
									<h2 align="center" style="font-weight: bold;">Application Analysis</h2>
									<p>
									Application 을 분석한다.
									</p>
								</section>

								<!--폼 시작--> 
								<form name="FORM_ANALYSIS" id="FORM_ANALYSIS" method="post" >
									
								<section>
									<h2>Application To Be Analyzed</h2>
									
									<ul class="small-image-list">
										<li>
											<h4>1. Application</h4>
											<p>
												<select name="SYS_ID" id="SYS_ID" >
												</select>
											</p>
										</li>
									</ul>
								</section>
								
								
								<section>
									<h2>Analysis Job Kind</h2>
									
									<table width="100%" border="1">
										<thead>
											<tr>
												<th width="10%">선택</th>
												<th width="20%">작업</th>
												<th width="70%">진행</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_31_ANALYZE_MTD%>" checked />
												</td>
												<td>메소드내 타 호출메소드 목록 추출</td>
												<td>
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_31_ANALYZE_MTD%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 10%;" />
												</td>
											</tr>
										</tbody>
									</table>

									<h2 align="right">
										<button type="button" id="btnAnalyze"  class="mini_button" >분석</button>
									</h2>
								</section>

								</form> 
							    <!--폼 끝--> 									
								
							</div>
						</div>
					</div>
				</div>

			<!-- Footer -->
			<jsp:include page="../common/footer.jsp"></jsp:include>

		</div>

	</body>
</html>