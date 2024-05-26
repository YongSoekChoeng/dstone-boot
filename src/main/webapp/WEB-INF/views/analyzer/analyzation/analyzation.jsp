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
		var timeOutObj;
		$(document).ready(function(){
			// 전체체크/해제
			$("#checkAll").click(function() {
				if($("#checkAll").is(":checked")){
					$("input[name=ANALYZE_JOB_KIND]").prop("checked", true);
				} else {
					$("input[name=ANALYZE_JOB_KIND]").prop("checked", false);
				}
			});
			
			$("input[name=ANALYZE_JOB_KIND]").click(function() {
				var total = $("input[name=ANALYZE_JOB_KIND]").length;
				var checked = $("input[name=ANALYZE_JOB_KIND]:checked").length;
				if(total != checked){
					$("#checkAll").prop("checked", false);
				}else{
					$("#checkAll").prop("checked", true); 
				}
			});			
			// 분석버튼 클릭
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
	                        
	                        if( returnList.length > 0 ){
	                        	//startMonitoring();
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
	
	    function btnAnalyzeOnclick(){
	    	goAnalyze();
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
	                	startMonitoring();
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
		
		function startMonitoring(){
			var isCompleted = doMonitoring();
			if(isCompleted){
				stopMonitoring();
			}else{
				timeOutObj = setTimeout(startMonitoring, 1 * 1000);
			}
		}

		function stopMonitoring(){
			if(timeOutObj){
				clearTimeout(timeOutObj);
			}
		}

		function doMonitoring(){
			var isCompleted = false;
			var isStarted = false;
			var isAllDone = false;
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
	                		return;
	                	}
	                	//console.log('success ===>>> data:' + (JSON.stringify(data))); 
	                	var returnList = data.returnObj;
	                	
	                	if(returnList){
	                		isAllDone = true;
		                	for(var i = 0; i<returnList.length; i++){
		                		var returnRow = returnList[i];
		                		var taskId = returnRow["taskId"];
		                		var taskRate = returnRow["taskRate"];
		                		//console.log('taskId ===>>>' + taskId + ", taskRate:" + taskRate );
		                		
		                		if( taskId && taskRate ){
		                			isStarted = true;
		                			if( parseInt(taskRate) < 100 ){
		                				isAllDone = false;
		                			}
			                		console.log('line 171  ===>>>taskId:' + taskId + ", taskRate:" + taskRate ); 
			                		var imgObj = $("#ANALYZE_" + taskId + "_IMG");
			                		imgObj.css({'width' : taskRate + '%' });
		                		}else{
		                			isAllDone = false;
		                		}
		                	}
console.log("line 162 ===>>>" + " isStarted" + isStarted + ", isAllDone" + isAllDone + ", isCompleted:" + isCompleted ); 
	                	}
	                }else{ 
	                    console.log('failure ===>>> data:' + (JSON.stringify(data))); 
	                    alert("failure ERR_MSG:" + ERR_MSG); 
	                }
	            }, 
	            error : function(data, status, e) { 
	                console.log('system error ===>>> data:' + (JSON.stringify(data))); 
	                alert("system error"); 
	            } 
	        });	
	        if( isStarted && isAllDone ){
	        	isCompleted = true;
	        }
	        return isCompleted;
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
												<th width="15%">분류</th>
												<th width="3%"><input type="checkbox" id="checkAll" /></th>
												<th width="*">작업</th>
											</tr>
										</thead>
										<tbody>
											<!-- 클래스분석 -->
											<tr>
												<td rowspan="6" >1. 클래스분석</td>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_11_ANALYZE_CLASS%>"  />
												</td>
												<td>1-1. 클래스파일리스트 에서 패키지ID/클래스ID/클래스명/기능종류 등이 담긴 클래스분석파일리스트 추출</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_11_ANALYZE_ID_CLASS%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_12_ANALYZE_CLASS_IMPL%>"  />
												</td>
												<td>1-2. 클래스파일리스트 에서 인터페이스구현하위클래스ID목록을 추출하여 클래스분석파일리스트에 추가</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_12_ANALYZE_ID_CLASS_IMPL%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_13_ANALYZE_CLASS_ALIAS%>"  />
												</td>
												<td>1-3. 클클래스파일리스트 에서 호출알리아스 추출하여 클래스분석파일리스트에 추가</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_13_ANALYZE_ID_CLASS_ALIAS%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td colspan="3" style="background-color: #d1cfcd;" > </td>
											</tr>
											
											<!-- 쿼리분석 -->
											<tr>
												<td rowspan="4" >2. 쿼리분석</td>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_21_ANALYZE_QUERY%>"  />
												</td>
												<td>2-1. 쿼리파일리스트 에서 KEY/네임스페이스/쿼리ID/쿼리종류/쿼리내용 등이 담긴 쿼리분석파일리스트 추출</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_21_ANALYZE_ID_QUERY%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_22_ANALYZE_QUERY_CALLTBL%>"  />
												</td>
												<td>2-2. 쿼리분석파일리스트 에 호출테이블ID정보목록 추가</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_22_ANALYZE_ID_QUERY_CALLTBL%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td colspan="3" style="background-color: #d1cfcd;" > </td>
											</tr>
											
											<!-- 메소드분석 -->
											<tr>
												<td rowspan="6" >3. 메소드분석</td>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_31_ANALYZE_MTD%>"  />
												</td>
												<td>3-1. 클래스파일리스트 에서 기능ID/메소드ID/메소드명/메소드URL/메소드내용 등이 담긴 메소드분석파일리스트 추출</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_31_ANALYZE_ID_MTD%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_32_ANALYZE_MTD_CALLMTD%>"  />
												</td>
												<td>3-2. 메소드분석파일리스트 에 메소드내 타 호출메소드 목록 추가</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_32_ANALYZE_ID_MTD_CALLMTD%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_33_ANALYZE_MTD_CALLTBL%>"  />
												</td>
												<td>3-3. 메소드분석파일리스트 에 메소드내 호출테이블 목록 추가</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_33_ANALYZE_ID_MTD_CALLTBL%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td colspan="3" style="background-color: #d1cfcd;" > </td>
											</tr>
											
											<!-- UI분석 -->
											<tr>
												<td rowspan="4" >4. UI분석</td>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_41_ANALYZE_UI%>"  />
												</td>
												<td>4-1. UI파일로부터  UI아이디/UI명 등이 담긴 UI분석파일목록 추출</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_41_ANALYZE_ID_UI%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_42_ANALYZE_UI_LINK%>"  />
												</td>
												<td>4-2. UI파일로부터 링크정보 추출</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_42_ANALYZE_ID_UI_LINK%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td colspan="3" style="background-color: #d1cfcd;" > </td>
											</tr>
											
											<!-- METRIX 추출 -->
											<tr>
												<td rowspan="2" >5. METRIX 추출</td>
												<td align="center">
													<input type="checkbox" name="ANALYZE_JOB_KIND" value="<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_51_ANALYZE_SAVE_METRIX%>"  />
												</td>
												<td>5-1. 클래스, 쿼리, 메소드, UI분석결과를 통한 METRIX 추출</td>
											</tr>
											<tr>
												<td colspan="2" >
													<img src="<%=requestUtil.getStrContextPath()%>/analyzer/images/pic2.jpg" id="ANALYZE_<%=net.dstone.common.tools.analyzer.AppAnalyzer.JOB_KIND_51_ANALYZE_ID_SAVE_METRIX%>_IMG" alt="" style="vertical-align:middle; height: 15px; width: 0%;" />
												</td>
											</tr>
											<tr>
												<td colspan="3" style="background-color: #d1cfcd;" > </td>
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