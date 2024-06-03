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
			readySlickGrid();
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

									<div class="scrollable" >
										
										<table>
											<thead >
												<tr >
													<th  >메뉴</th>
													<th  >UI</th>
													<th  >UI명</th>
													<th style="width:250px;"  >URL</th>
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
								
								<table id="grid"></table>
								
								<div id="myGrid" class="slick-container" style="width:600px;height:500px;"></div>
								
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
		
	<script>
	  var grid;
	  var data = [];
	  
	  var columns = [
	    { id: "title", name: "Title", field: "title", sortable: true },
	    { id: "duration", name: "Duration", field: "duration", sortable: true, formatter: dayFormatter },
	    { id: "%", name: "% Complete", field: "percentComplete", width: 95, sortable: true },
	    { id: "start", name: "Start", field: "start", formatter: dateFormatter, sortable: true },
	    { id: "finish", name: "Finish", field: "finish", formatter: dateFormatter, sortable: true },
	    { id: "effort-driven", name: "Effort Driven", field: "effortDriven", width: 95, sortable: true }
	  ];
	
	  function dayFormatter(row, cell, value, columnDef, dataContext) {
	      return value + ' days';
	  }
	
	  function dateFormatter(row, cell, value, columnDef, dataContext) {
	      return value.getMonth() + '/' + value.getDate() + '/' + value.getFullYear();
	  }
	
	  var options = {
	    enableCellNavigation: true,
	    enableColumnReorder: false,
	    multiColumnSort: true
	  };
	
	  //document.addEventListener("DOMContentLoaded", function() {
	  function readySlickGrid() {

	    var MS_PER_DAY = 24 * 60 * 60 * 1000;
	    
	    for (var i = 0; i < 500; i++) {
	      var startDate = new Date(new Date("1/1/1980").getTime() + Math.round(Math.random() * 365 * 25) * MS_PER_DAY);
	      var endDate = new Date(startDate.getTime() + Math.round(Math.random() * 365) * MS_PER_DAY);
	      data[i] = {
	        title: "Task " + i,
	        duration: Math.round(Math.random() * 30) + 2,
	        percentComplete: Math.round(Math.random() * 100),
	        start: startDate,
	        finish: endDate,
	        effortDriven: (i % 5 == 0)
	      };

	    }
	
	    grid = new Slick.Grid("#myGrid", data, columns, options);
	
	    // when "onBeforeSort" returns false, the "onSort" won't execute (for example a backend server error while calling backend query to sort)
	    grid.onBeforeSort.subscribe(function (e, args) {
	      return true;
	    });
	
	    grid.onSort.subscribe(function (e, args) {
	      var cols = args.sortCols;
	
	      data.sort(function (dataRow1, dataRow2) {
	        for (var i = 0, l = cols.length; i < l; i++) {
	          var field = cols[i].sortCol.field;
	          var sign = cols[i].sortAsc ? 1 : -1;
	          var value1 = dataRow1[field], value2 = dataRow2[field];
	          var result = (value1 == value2 ? 0 : (value1 > value2 ? 1 : -1)) * sign;
	          if (result != 0) {
	            return result;
	          }
	        }
	        return 0;
	      });
	      grid.invalidate();
	      grid.render();
	    });
	  }
	</script>

	<script>
		var jqGridData = [
            { id: "10",  invdate: "2015-10-01", name: "test",   amount: "" },
            { id: "20",  invdate: "2015-09-01", name: "test2",  amount: "300.00", tax:"20.00", closed:false, ship_via:"FE", total:"320.00"},
            { id: "30",  invdate: "2015-09-01", name: "test3",  amount: "400.00", tax:"30.00", closed:false, ship_via:"FE", total:"430.00"},
            { id: "40",  invdate: "2015-10-04", name: "test4",  amount: "200.00", tax:"10.00", closed:true,  ship_via:"TN", total:"210.00"},
            { id: "50",  invdate: "2015-10-31", name: "test5",  amount: "300.00", tax:"20.00", closed:false, ship_via:"FE", total:"320.00"},
            { id: "60",  invdate: "2015-09-06", name: "test6",  amount: "400.00", tax:"30.00", closed:false, ship_via:"FE", total:"430.00"},
            { id: "70",  invdate: "2015-10-04", name: "test7",  amount: "200.00", tax:"10.00", closed:true,  ship_via:"TN", total:"210.00"},
            { id: "80",  invdate: "2015-10-03", name: "test8",  amount: "300.00", tax:"20.00", closed:false, ship_via:"FE", total:"320.00"},
            { id: "90",  invdate: "2015-09-01", name: "test9",  amount: "400.00", tax:"30.00", closed:false, ship_via:"TN", total:"430.00"},
            { id: "100", invdate: "2015-09-08", name: "test10", amount: "500.00", tax:"30.00", closed:true,  ship_via:"TN", total:"530.00"},
            { id: "110", invdate: "2015-09-08", name: "test11", amount: "500.00", tax:"30.00", closed:false, ship_via:"FE", total:"530.00"},
            { id: "120", invdate: "2015-09-10", name: "test12", amount: "500.00", tax:"30.00", closed:false, ship_via:"FE", total:"530.00"}
        ];
			
	    $(function () {
	        "use strict";
	        $("#grid").jqGrid({
	            colModel: [
	                { name: "name", label: "Client", width: 53 },
	                { name: "invdate", label: "Date", width: 75, align: "center", sorttype: "date", formatter: "date", formatoptions: { newformat: "d-M-Y" } },
	                { name: "amount", label: "Amount", width: 65, template: "number" },
	                { name: "tax", label: "Tax", width: 41, template: "number" },
	                { name: "total", label: "Total", width: 51, template: "number" },
	                { name: "closed", label: "Closed", width: 59, template: "booleanCheckbox", firstsortorder: "desc" },
	                { name: "ship_via", label: "Shipped via", width: 87, align: "center", formatter: "select", formatoptions: { value: "FE:FedEx;TN:TNT;DH:DHL", defaultValue: "DH" } }
	            ],
	            data: jqGridData,
	            iconSet: "fontAwesome",
	            idPrefix: "g1_",
	            rownumbers: true,
	            sortname: "invdate",
	            sortorder: "desc",
	            autowidth: true, 
	            caption: "The grid, which uses predefined formatters and templates"

	        });
	        
	        jQuery("#grid").setGridWidth(1200,false);
	        jQuery("#grid").setGridHeight(200,false);
	    });
	    
	</script> 

</html>