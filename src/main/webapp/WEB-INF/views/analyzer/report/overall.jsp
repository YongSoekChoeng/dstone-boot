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

								<section class="middle-content">
									<h2 align="center" style="font-weight: bold;">종합결과</h2>
								</section>

								<!--폼 시작--> 
								<form name="FORM_ANALYSIS" id="FORM_ANALYSIS" method="post" >
									
								<section>
								
									<h2>● Over all</h2>

									<table width="100%" border="1" style="border-width:thin; border-color:gray; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid;" >
										<thead>
											<tr>
												<th width="20%">Application</th>
												<td width="30%" align="left" style="padding: 2px;">
													<select name="SYS_ID" id="SYS_ID" ></select>
												</td>
												<th width="20%">UI</th>
												<td width="30%" align="left" style="padding: 2px;">
													<input type="text" name="UI" value="" size="30%" />
												</td>
											</tr>		
											<tr>
												<th >URL</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="URL" value="" size="30%" />
												</td>
												<th >API</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="FUNCTION" value="" size="30%" />
												</td>
											</tr>		
											<tr>
												<th >테이블</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="TBL" value="" size="30%" />
												</td>
												<th >CRUD</th>
												<td align="left" style="padding: 2px;">
													<select name="CRUD" id="CRUD" >
														<option value="" selected >전체</option>
														<option value="R">조회</option>
														<option value="C">입력</option>
														<option value="U">수정</option>
														<option value="D">삭제</option>
													</select>
												</td>
											</tr>														
										</thead>
									</table>

									<h2 align="right">
										<button type="button" id="btnSearch"  class="mini_button" >조회</button>
									</h2>

									<div class="fixed_headers" >
									<table>
										<thead >
											<tr >
												<th  >메뉴</th>
												<th  >UI</th>
												<th  >UI명</th>
												<th  >URL</th>
												<th  >API-LVL-1</th>
												<th  >API-LVL-1명</th>
												<th  >API-LVL-1종류</th>
												<th  >API-LVL-2</th>
												<th  >API-LVL-2명</th>
												<th  >API-LVL-2종류</th>
												<th  >API-LVL-3</th>
												<th  >API-LVL-3명</th>
												<th  >API-LVL-3종류</th>
												<th  >테이블</th>
											</tr>														
										</thead>													
										<tbody >	
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>	
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>	
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>		
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>			
											<tr >
												<td  >Application</td>
												<td  >UI</td>
												<td  >UI명</td>
												<td  >URL</td>
												<td  >API-LVL-1</td>
												<td  >API-LVL-1명</td>
												<td  >API-LVL-1종류</td>
												<td  >API-LVL-2</td>
												<td  >API-LVL-2명</td>
												<td  >API-LVL-2종류</td>
												<td  >API-LVL-3</td>
												<td  >API-LVL-3명</td>
												<td  >API-LVL-3종류</td>
												<td  >테이블</td>
											</tr>												
										</tbody>
									</table>
									</div>

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