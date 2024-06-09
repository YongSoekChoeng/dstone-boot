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
			readySlickGrid();
			$("#btnSearch").on("click", listOverAll);
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
	                        //console.log('success ===>>> slickGridData:' + (JSON.stringify(slickGridData))); 
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


	    var loadingIndicator = null;

		var overAllData = [];
	    function listOverAll(){ 
	    	
	    	overAllData = [];
	    	refreshSlickGrid(overAllData);
	        if (!loadingIndicator) {
	            loadingIndicator = $("<span class='loading-indicator'><label>Buffering...</label></span>").appendTo(document.body);
	            var $g = $("#myGrid");
	            var loadingIndicatorTop  = $g.position().top  + ($g.height()/2) - (loadingIndicator.height()/2);
	            var loadingIndicatorLeft = $g.position().left + ($g.width()/2)  - (loadingIndicator.width()/2);
	            loadingIndicator.css("position", "absolute");
	            loadingIndicator.css("top", loadingIndicatorTop);
	            loadingIndicator.css("left", loadingIndicatorLeft);
			}
			loadingIndicator.show();
			
	        $.ajax({  
	            type:"POST",  
	            url:"/analyzer/report/listOverAll.do", 
	            data:encodeURIComponent(JSON.stringify($(document.FORM_REPORT).serializeObject())),  
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
	                        overAllData = data.returnObj.returnObj; 
	                        refreshSlickGrid(overAllData);
	                    } 
	                }else{ 
	                    console.log('failure ===>>> data:' + (JSON.stringify(data))); 
	                    alert("failure ERR_MSG:" + ERR_MSG); 
	                }
	                loadingIndicator.fadeOut();
	            }, 
				error : function(data, status, e) { 
					loadingIndicator.fadeOut();
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
								<form name="FORM_REPORT" id="FORM_REPORT" method="post" >
									
								<section>
								
									<h2>● Over all</h2>

									<table width="100%" border="1" style="border-width:thin; border-color:gray; border-bottom-style: solid; border-top-style: solid; border-left-style: solid; border-right-style: solid;" >
										<thead>
											<tr>
												<th width="10%">Application</th>
												<td width="23%" align="left" style="padding: 2px;">
													<select name="SYS_ID" id="SYS_ID" ></select>
												</td>
												<th width="10%">화면ID</th>
												<td width="23%" align="left" style="padding: 2px;">
													<input type="text" name="UI_ID" value="" style="width: 100%" />
												</td>
												<th width="10%">화면명</th>
												<td width="23%" align="left" style="padding: 2px;">
													<input type="text" name="UI_NM" value="" style="width: 100%" />
												</td>
											</tr>		
											<tr>
												<th >기준URL</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="URL" value="" style="width: 100%" />
												</td>
												<th >기능ID</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="FUNCTION_ID" value="" style="width: 100%" />
												</td>
												<th >기능명</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="FUNCTION_NM" value="" style="width: 100%" />
												</td>
											</tr>		
											<tr>
												<th >테이블</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="TBL" value="" size="30%" />
												</td>
												<th >테이블CRUD</th>
												<td align="left" style="padding: 2px;">
													<select name="CRUD" id="CRUD" >
														<option value="" selected >전체</option>
														<option value="R" >조회</option>
														<option value="C" >입력</option>
														<option value="U" >수정</option>
														<option value="D" >삭제</option>
													</select>
												</td>
												<th >조회갯수</th>
												<td align="left" style="padding: 2px;">
													<select name="LIMIT" id="LIMIT" >
														<option value="<%=Integer.MAX_VALUE %>">전체</option>
														<option value="100" selected >100</option>
														<option value="300">300</option>
														<option value="500">500</option>
														<option value="1000">1,000</option>
														<option value="3000">3,000</option>
														<option value="5000">5,000</option>
														<option value="10000">10,000</option>
													</select>
												</td>
											</tr>														
										</thead>
									</table>

									<h2 align="right">
										<button type="button" id="btnSearch"  class="mini_button" >조회</button>
									</h2>

									<div id="myGrid" class="slick-container" style="width:100%;height:500px;"></div>
								
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
		
	<script>
	  	var grid;
	  	var dataView;

		var callLevel = 10;
		var columns = new Array();
		columns[columns.length] 	= { id: "SEQ"					, name: "SEQ"			, field: "SEQ"					, width: 40, sortable: true		, columnGroup:"기본"};
		columns[columns.length] 	= { id: "UI_ID"					, name: "ID"			, field: "UI_ID"				, width: 120, sortable: true	, columnGroup:"기본"};
		columns[columns.length] 	= { id: "UI_NM"					, name: "명"				, field: "UI_NM"				, width: 100, sortable: true	, columnGroup:"기본"};
		columns[columns.length] 	= { id: "BASIC_URL"				, name: "URL"			, field: "BASIC_URL"			, width: 150, sortable: true	, columnGroup:"기본"};
		for(var i=0; i<callLevel; i++){
			columns[columns.length] = { id: "FUNCTION_ID_"+(i+1)	, name: "ID"			, field: "FUNCTION_ID_"+(i+1)	, width: 100, sortable: true	, columnGroup:"호출LEVEL-"+(i+1)+""  };
			columns[columns.length] = { id: "FUNCTION_NAME_"+(i+1)	, name: "명"				, field: "FUNCTION_NAME_"+(i+1)	, width: 100, sortable: true	, columnGroup:"호출LEVEL-"+(i+1)+""  };
			columns[columns.length] = { id: "CLASS_KIND_"+(i+1)		, name: "종류"			, field: "CLASS_KIND_"+(i+1)	, width: 100, sortable: true	, columnGroup:"호출LEVEL-"+(i+1)+""  };
		}
		columns[columns.length] 	= { id: "CALL_TBL"				, name: "호출테이블"		, field: "CALL_TBL"				, width: 2000, sortable: true	, columnGroup:"테이블"   };

		var options = {
		    enableCellNavigation: true,
		    enableColumnReorder: false,
		    createPreHeaderPanel: true,
		    showPreHeaderPanel: true,
		    preHeaderPanelHeight: 23,
		    explicitInitialization: true
		};
		

		function readySlickGrid() {	
			var data = [];
			refreshSlickGrid(data);
		}
		
		function refreshSlickGrid(data) {
			dataView = new Slick.Data.DataView();
			grid = new Slick.Grid("#myGrid", dataView, columns, options);

			dataView.onRowCountChanged.subscribe(function (e, args) {
				grid.updateRowCount();
			    grid.render();
			});

			dataView.onRowsChanged.subscribe(function (e, args) {
			    grid.invalidateRows(args.rows);
			    grid.render();
			});
			
			grid.onColumnsResized.subscribe(function (e, args) {
				createAddlHeaderRow();
			});
			
			grid.init();
			createAddlHeaderRow();
			
			// Initialise data
			dataView.beginUpdate();
			dataView.setItems(data);
			dataView.endUpdate();
		}
		
	</script>

</html>