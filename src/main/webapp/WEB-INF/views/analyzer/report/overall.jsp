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
	                        var totalCnt = data.returnObj.totalCnt; 
	                        var maxLevel = data.returnObj.maxLevel; 
	                        overAllData = data.returnObj.returnObj; 
	                        $("#totalCnt").text(totalCnt + "건");
	                        $("#maxLevel").text(maxLevel);
	                        
	                        refreshSlickGrid(overAllData, maxLevel);
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
												<th >호출URL</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="BASIC_URL" value="" style="width: 100%" />
												</td>
												<th >API ID</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="FUNCTION_ID" value="" style="width: 100%" />
												</td>
												<th >API 명</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="FUNCTION_NM" value="" style="width: 100%" />
												</td>
											</tr>		
											<tr>
												<th >테이블</th>
												<td align="left" style="padding: 2px;">
													<input type="text" name="CALL_TBL" value="" size="30%" />
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

									<table width="100%" style="font-size:14px; border-color:gray; border-width:thin; border-style: solid; " >
										<thead>
											<tr>
												<th width="10%">총건수</th>
												<td width="15%" align="right" style="padding: 5px;" id="totalCnt" >0건</td>
												<th width="20%">Max API-레벨(호출레벨)</th>
												<td width="15%" align="right" style="padding: 5px;" id="maxLevel" ></td>
												<th width="10%">API종류</th>
												<td width="30%" align="left" style="padding: 5px;">
													CT:컨트롤러, SV:서비스, DA:DAO, OT:기타
												</td>
											</tr>												
										</thead>
									</table>
									
									<h5>&nbsp;</h5>
									<div id="myGrid" class="slick-container" style="width:100%;height:500px;"></div>
								
									<h2 align="right">
										<a id="downloadLink"  class="mini_button" >Excel Down</a>
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

	<script>
	
	  	var grid;		// 그리드
	  	var dataView; 	// 데이터뷰
	    var detailView; // 상세데이터뷰 플러그인
	    var columns = new Array();
	    
	    function setColums( callLevel ) {	
	    	columns = new Array();
			columns[columns.length] 	= { id: "DETAIL"				, name: ""				, field: "DETAIL"				, width: 30	, sortable: true	, columnGroup:""};
			columns[columns.length] 	= { id: "RNUM"					, name: "No"			, field: "RNUM"					, width: 40	, sortable: true	, columnGroup:""};
			columns[columns.length] 	= { id: "UI_ID"					, name: "ID"			, field: "UI_ID"				, width: 120, sortable: true	, columnGroup:"화면"};
			columns[columns.length] 	= { id: "UI_NM"					, name: "명"				, field: "UI_NM"				, width: 100, sortable: true	, columnGroup:"화면"};
			columns[columns.length] 	= { id: "BASIC_URL"				, name: "호출URL"			, field: "BASIC_URL"			, width: 150, sortable: true	, columnGroup:"화면"};
			for(var i=0; i<callLevel; i++){
				//columns[columns.length] = { id: "FUNCTION_ID_"+(i+1)	, name: "ID"			, field: "FUNCTION_ID_"+(i+1)	, width: 150, sortable: true	, columnGroup:"API-레벨-"+(i+1)+""  };
				columns[columns.length] = { id: "DISPLAY_ID_"+(i+1)		, name: "ID"			, field: "DISPLAY_ID_"+(i+1)	, width: 200, sortable: true	, columnGroup:"API-레벨-"+(i+1)+""  };
				columns[columns.length] = { id: "FUNCTION_NAME_"+(i+1)	, name: "명"				, field: "FUNCTION_NAME_"+(i+1)	, width: 100, sortable: true	, columnGroup:"API-레벨-"+(i+1)+""  };
				columns[columns.length] = { id: "CLASS_KIND_"+(i+1)		, name: "종류"			, field: "CLASS_KIND_"+(i+1)	, width: 50	, sortable: true	, columnGroup:"API-레벨-"+(i+1)+""  };
			}
			columns[columns.length] 	= { id: "CALL_TBL"				, name: "참조테이블"		, field: "CALL_TBL"				, width: 2000, sortable: true	, columnGroup:"테이블"   };
	    }

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
			refreshSlickGrid(data, 5);
		}
		
		function refreshSlickGrid(data, callLevel) {
			// 컬럼세팅
			setColums( callLevel );
			// DataView 선언
			dataView = new Slick.Data.DataView();
			
			// 상세화면 플러그인 선언
			detailView = new Slick.Plugins.RowDetailView({
		        cssClass: "detailView-toggle",
		        preTemplate: loadingTemplate,
		        process: simulateServerCall,
		        postTemplate: loadDetailView,
		        useRowClick: true,
		        // how many grid rows do we want to use for the detail panel
		        // also note that the detail view adds an extra 1 row for padding purposes
		        // example, if you choosed 6 panelRows, the display will in fact use 5 rows
		        panelRows: 6,
		        // make only every 2nd row an expandable row,
		        // by using the override function to provide custom logic of which row is expandable
		        // you can override it here in the options or externally by calling the method on the plugin instance
		        //expandableOverride: function(row, dataContext, grid) {
		        //  return (dataContext.id % 2 === 1);
		        //}
			});
			var dOptions = detailView.getOptions();
			dOptions.width = 30;
			detailView.setOptions(dOptions);
			/************************ 그리드 생성 시작 ************************/
			// 상세화면 플러그인 적용-1
			//columns[0] = detailView.getColumnDefinition();
			grid = new Slick.Grid("#myGrid", dataView, columns, options);

			// 상세화면 플러그인 적용-2
			//grid.registerPlugin(detailView);

			/*** 이벤트 선언 시작 ***/
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
 			// 상세화면 토글되기 전
 	      	detailView.onBeforeRowDetailToggle.subscribe(function(e, args) {
 		    	console.log('before toggling row detail', args.item);
 		    });
 			// 상세화면 토글된 후
 			detailView.onAfterRowDetailToggle.subscribe(function(e, args) {
 				console.log('after toggling row detail', args.item);
 				if (args.item._collapsed) {
 				}
 			});
			/*** 이벤트 선언 끝 ***/
			
			// 그리드 시작
			grid.init();
			// 헤더 만들기
			createAddlHeaderRow();
			// 데이터 입력
			dataView.beginUpdate();
			dataView.setItems(data);
			dataView.endUpdate();
			// 엑셀데이터 생성
			setExcelData(data, callLevel);
			/************************ 그리드 생성 끝 ************************/
		}
		

		/************************ 상세화면 선언 시작 ************************/
 	    function loadingTemplate() {
 	      return '<div class="preload" style="text-align:left;">Loading...</div>';
 	    }
		function simulateServerCall(item) {
			console.log('simulateServerCall has been called !!!');
			setTimeout(function() {
				notifyTemplate(item);
			}, 0.5* 1000);
		}
	    // notify the onAsyncResponse with the "args.item" (required property)
	    // the plugin will then use itemDetail to populate the detail panel with "postTemplate"
		function notifyTemplate(itemDetail) {
			console.log('notifyTemplate has been called !!!');
			detailView.onAsyncResponse.notify({
				"item": itemDetail
			}, undefined, this);
		}		
 	    function loadDetailView(itemDetail) {
 	    	console.log('loadDetailView has been called !!!');	
 	    	return [
 	    		  '<div class="container">',
 	    		  // |UI_ID|UI_NM|BASIC_URL|FUNCTION_ID_1|FUNCTION_NAME_1|CLASS_KIND_1|FUNCTION_ID_2|FUNCTION_NAME_2|CLASS_KIND_2|FUNCTION_ID_3|FUNCTION_NAME_3|CLASS_KIND_3|FUNCTION_ID_4|FUNCTION_NAME_4|CLASS_KIND_4|FUNCTION_ID_5|FUNCTION_NAME_5|CLASS_KIND_5|FUNCTION_ID_6|FUNCTION_NAME_6|CLASS_KIND_6|FUNCTION_ID_7|FUNCTION_NAME_7|CLASS_KIND_7|FUNCTION_ID_8|FUNCTION_NAME_8|CLASS_KIND_8|FUNCTION_ID_9|FUNCTION_NAME_9|CLASS_KIND_9|FUNCTION_ID_10|FUNCTION_NAME_10|CLASS_KIND_10|CALL_TBL|
 	    		  //'	<label>Assignee:</label> <input id="assignee_' + itemDetail.id + '" type="text" value="' + itemDetail.assignee + '"/>',
 	    		 // '	<span style="float: right">Find out who is the Assignee <button id="who-is-assignee_' + itemDetail.id + '">Click Me</button></span></div>',
 	    		  
 	    		  '		<div class="col-12 col-6-medium" >',
 	    		  '			<section >',
 	    		  '				<ul>',
 	    		  '					<li><h4>호출상세</h4></li>',
 	    		  '				</ul>',
 	    		  '			</section>',
 	    		  '			<section >',
 	    		  '				<label>아이디</label> <span>' + itemDetail.ID + '</span></div>',
 	    		  '				<label>UI</label> <span>' + itemDetail.ID + '</span></div>',
 	    		  '			</section>',
 	    		  '		</div>',
 	    		  
 	    		  '	</div>',
 	    		  '</div>'
 	    		].join('');
 	    }
		/************************ 상세화면 선언 끝 ************************/
		
		/************************ Excel 관련 시작 ************************/
		//default excel options
		var excelOptions = {
			headerStyle: {
				font: {
					bold: true,  //enable bold
					font: 12, // font size
					color: '00ffffff' //font color --Note: Add 00 before the color code
	  			}
				,fill: {   //fill background
					type: 'pattern', 
					patternType: 'solid',
					fgColor: '00428BCA' //background color --Note: Add 00 before the color code
				}
			},
			cellStyle: {
				font: {
					bold: false,  //enable bold
					font: 12, // font size
					color: '00000000' //font color --Note: Add 00 before the color code
				}
				,fill: {   //fill background
					type: 'pattern',
					patternType: 'solid',	
					fgColor: '00ffffff' //background color --Note: Add 00 before the color code
				}
      		},
  		};
		
		function setExcelData(data, callLevel){
			var xlsData = new Array();
			for(var i=0; i<data.length; i++){
				var row = new Object();
				row["ID"] 			= data[i]["ID"];
				row["UI_ID"] 		= data[i]["UI_ID"];
				row["UI_NM"] 		= data[i]["UI_NM"];
				row["BASIC_URL"] 	= data[i]["BASIC_URL"];
				for(var k=0; k<callLevel; k++){
					row["FUNCTION_ID_"+(k+1)+""] 	= data[i]["FUNCTION_ID_"+(k+1)+""];
					row["DISPLAY_ID_"+(k+1)+""] 	= data[i]["DISPLAY_ID_"+(k+1)+""];
					row["FUNCTION_NAME_"+(k+1)+""]	= data[i]["FUNCTION_NAME_"+(k+1)+""];
					row["CLASS_KIND_"+(k+1)+""] 	= data[i]["CLASS_KIND_"+(k+1)+""];
				};
				row["CALL_TBL"] 	= data[i]["CALL_TBL"];
				
				xlsData.push(row);
			}
			
		    $('#myGrid').exportToExcel("OverAll.xlsx", "OverAll", xlsData, excelOptions, function (response) {
		        //console.log(response);
		    });
		}
		
		/************************ Excel 관련  끝 ************************/
		
	</script>

</html>