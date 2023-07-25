<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%  
/******************************************* 변수 선언 시작 *******************************************/        	  
net.dstone.common.utils.RequestUtil requestUtil = new net.dstone.common.utils.RequestUtil(request, response);
String                                                         SUCCESS_YN;                                       
java.util.HashMap                                              returnObj;                                       
java.util.List<net.dstone.sample.vo.UserVo>                  returnVoList;                       
net.dstone.sample.vo.UserVo                                  userVo;                
net.dstone.common.utils.PageUtil                                      pageUtil;                                        
/******************************************* 변수 선언 끝 *********************************************/           
                                                                                                                
/******************************************* 변수 정의 시작 *******************************************/           
SUCCESS_YN           = net.dstone.common.utils.StringUtil.nullCheck(response.getHeader("SUCCESS_YN"), "");			
returnObj           = (java.util.HashMap)requestUtil.getAttribute("returnObj");                                   
userVo            = null;                                                                          
returnVoList        = null;                                                                          
pageUtil            = null;                                                                          
if(returnObj != null){                                                                                          
    returnVoList    = (java.util.List<net.dstone.sample.vo.UserVo>)returnObj.get("returnObj"); 
    pageUtil        = (net.dstone.common.utils.PageUtil)returnObj.get("pageUtil");                                   
}                                                                                                             
/******************************************* 변수 정의 끝 *********************************************/     
%>                                                                                                              
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">      
<html>                                                                                                          
<head>                                                                                                          
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">                                         
<title>Insert title here</title>                                       
<script type="text/javascript" src="/js/jquery-1.7.2.js"></script> 
<script type="text/javascript" src="/js/jquery.json-2.4.js" ></script>                                                                                 
<script type="text/javascript">                                                                               
	
	function init(){                                                                                             
		                                                                                                
	} 
	
	function goForSubmitSelect(){                                                                                          
		document.SUBMIT_SELECT_FORM.submit();                                                                             
	}   
	  
	$.fn.serializeObject = function() { 
	    var o = {}; 
	    $(this).find('input[type="hidden"], input[type="text"], input[type="password"], input[type="checkbox"]:checked, input[type="radio"]:checked, select').each(function() { 
	        if ($(this).attr('type') == 'hidden') { //if checkbox is checked do not take the hidden field 
	            var $parent = $(this).parent(); 
	            var $chb = $parent.find('input[type="checkbox"][name="' + this.name.replace(/\[/g, '\[').replace(/\]/g, '\]') + '\"]'); 
	            if ($chb != null) { 
	                if ($chb.prop('checked')) return; 
	            } 
	        } 
	        if (this.name === null || this.name === undefined || this.name === '') return; 
	        var elemValue = null; 
	        if ($(this).is('select')) elemValue = $(this).find('option:selected').val(); 
	        else elemValue = this.value; 
	        if (o[this.name] !== undefined) { 
	            if (!o[this.name].push) { 
	                o[this.name] = [o[this.name]]; 
	            } 
	            o[this.name].push(elemValue || ''); 
	        } else { 
	            o[this.name] = elemValue || ''; 
	        } 
	    }); 
	    return o; 
	} 
	   
	function goForAjaxSelect(){ 
		$.ajax({ 
			type:"POST", 
			url:"/sample/admin/listUser.do", 
			data:encodeURIComponent(JSON.stringify($(document.AJAX_SELECT_FORM).serializeObject())), 
			dataType:"json", 
			success:function(data, status, request){
				var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN');
				var ERR_CD = data['ERR_CD'];
				var ERR_MSG = data['ERR_MSG'];
				if( 'Y' == SUCCESS_YN ){
					console.log('success ===>>> data:' + (JSON.stringify(data)));
					var tbody = $("#AJAX_TBL"); 
					tbody.empty(); 
					var returnList = data.returnObj.returnObj; 
					var lineStr = ""; 
					for(var i=0; i<returnList.length; i++){ 
						lineStr = ""; 
						lineStr = lineStr + "<tr>"; 
						lineStr = lineStr + "<td>"+returnList[i].GROUP_ID+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].USER_ID+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].USER_PW+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].MEMBER_NAME+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].AGE+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].DUTY+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].REGION+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].ADDRESS+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].ADDRESS_DTL+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].JUMINNO+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].GENDER+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].TEL+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].HP+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].EMAIL+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].INPUT_DT+"</td>"; 
						lineStr = lineStr + "<td>"+returnList[i].UPDATE_DT+"</td>"; 
						lineStr = lineStr + "</tr>"; 
						tbody.append(lineStr); 
					} 
					document.getElementById("paging").innerHTML = data.pageHTML; 
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
	} 
    
	function goForInsert(){                                                                                          
		document.INSERT_FORM.submit();                                                                             
	}                                                                                                            
</script>                                                                                                       
</head>                                                                                                         
<body onload='javascript:init();' >                                                                             
                	
	<a href="/index.html" >index</a>
	<br><br>
		          
	폼서밋 방식 조회<br>                                                                                          
	<!--폼 시작-->
	<form name="SUBMIT_SELECT_FORM" method="post" action="/sample/admin/listUser.do" >                                                                                                                               
		<input type=hidden name="PAGE_NUM" value="<%= (pageUtil != null ? pageUtil.intPageNum : 1) %>">           
		<input type='button' name='' value='LIST' onclick='javascript:goForSubmitSelect();' >                                 
		<table border=1>                                                                                              
			<thead>                                                                                             
				<tr>                                                                                                      
					<th>GROUP_ID&nbsp;</th><th>USER_ID&nbsp;</th><th>USER_PW&nbsp;</th><th>MEMBER_NAME&nbsp;</th><th>AGE&nbsp;</th><th>DUTY&nbsp;</th><th>REGION&nbsp;</th><th>ADDRESS&nbsp;</th><th>ADDRESS_DTL&nbsp;</th><th>JUMINNO&nbsp;</th><th>GENDER&nbsp;</th><th>TEL&nbsp;</th><th>HP&nbsp;</th><th>EMAIL&nbsp;</th><th>INPUT_DT&nbsp;</th><th>UPDATE_DT&nbsp;</th>
				</tr>   
			</thead>  
			<tbody>                                                                                                 
			<%                                                                                                        
			if(returnVoList!=null){                                                                                   
				for(int i=0; i<returnVoList.size(); i++){                                                             
					userVo = returnVoList.get(i);                                                         
			%>                                                                                                        
			<tr>                                                                                                      
				<td><%=userVo.getGROUP_ID() %>&nbsp;</td><td><%=userVo.getUSER_ID() %>&nbsp;</td><td><%=userVo.getUSER_PW() %>&nbsp;</td><td><%=userVo.getMEMBER_NAME() %>&nbsp;</td><td><%=userVo.getAGE() %>&nbsp;</td><td><%=userVo.getDUTY() %>&nbsp;</td><td><%=userVo.getREGION() %>&nbsp;</td><td><%=userVo.getADDRESS() %>&nbsp;</td><td><%=userVo.getADDRESS_DTL() %>&nbsp;</td><td><%=userVo.getJUMINNO() %>&nbsp;</td><td><%=userVo.getGENDER() %>&nbsp;</td><td><%=userVo.getTEL() %>&nbsp;</td><td><%=userVo.getHP() %>&nbsp;</td><td><%=userVo.getEMAIL() %>&nbsp;</td><td><%=userVo.getINPUT_DT() %>&nbsp;</td><td><%=userVo.getUPDATE_DT() %>&nbsp;</td>
			</tr>	                                                                                                  
			<%                                                                                                        
				}                                                                                                     
			}                                                                                                         
			%>                                                                                                        
			<tr>                                                                                                      
				<td colspan=16 &nbsp; ><%= (pageUtil != null ? pageUtil.htmlPostPage(request, "SUBMIT_SELECT_FORM", "PAGE_NUM" ) : "" ) %></td> 
			</tr> 
			</tbody>                                                                                                  
		</table>                                                                                                      
	                                                                                                                 
	</form>                                                                                                          
	<!--폼 끝-->   
	<br><br>                                                                                                   
	                
	AJAX 방식 조회<br>                                                                                          
	<!--폼 시작-->                                                                                                   
	<form name="AJAX_SELECT_FORM" method="post" action="">                            
		<input type=hidden name="PAGE_NUM" value="<%= (pageUtil != null ? pageUtil.intPageNum : 1) %>">           
		<input type='button' name='' value='LIST' onclick='javascript:goForAjaxSelect();' >                                 
		<table border=1>      
			<thead>                                                                                             
				<tr>                                                                                                      
					<th>GROUP_ID&nbsp;</th><th>USER_ID&nbsp;</th><th>USER_PW&nbsp;</th><th>MEMBER_NAME&nbsp;</th><th>AGE&nbsp;</th><th>DUTY&nbsp;</th><th>REGION&nbsp;</th><th>ADDRESS&nbsp;</th><th>ADDRESS_DTL&nbsp;</th><th>JUMINNO&nbsp;</th><th>GENDER&nbsp;</th><th>TEL&nbsp;</th><th>HP&nbsp;</th><th>EMAIL&nbsp;</th><th>INPUT_DT&nbsp;</th><th>UPDATE_DT&nbsp;</th>
				</tr>   
			</thead>  
			<tbody id="AJAX_TBL">
			</tbody>                                                                                                                    
			<tr>                                                                                                      
				<td colspan=16 &nbsp; ><div id="paging" ></div> </td> 
			</tr>	                                                                                                  
		</table>                                                                                                      
	</form>                                                                                                          
	<!--폼 끝-->  
	<br><br>                                                                                                   
	                 
	폼서밋 방식 입력<br>                                                                                               
	<!--폼 시작-->                                                                                                    
	<form name="INSERT_FORM" method="post" action="/sample/admin/insertUser.do">                      		 
		<input type='button' name='' value='INSERT' onclick='javascript:goForInsert();' >                                 
		<table border=1>                                                                                              
			<tr>                                                                                                  
				<td>GROUP_ID&nbsp;</td><td><input type='text' name='GROUP_ID' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>USER_ID&nbsp;</td><td><input type='text' name='USER_ID' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>USER_PW&nbsp;</td><td><input type='text' name='USER_PW' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>MEMBER_NAME&nbsp;</td><td><input type='text' name='MEMBER_NAME' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>AGE&nbsp;</td><td><input type='text' name='AGE' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>DUTY&nbsp;</td><td><input type='text' name='DUTY' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>REGION&nbsp;</td><td><input type='text' name='REGION' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>ADDRESS&nbsp;</td><td><input type='text' name='ADDRESS' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>ADDRESS_DTL&nbsp;</td><td><input type='text' name='ADDRESS_DTL' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>JUMINNO&nbsp;</td><td><input type='text' name='JUMINNO' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>GENDER&nbsp;</td><td><input type='text' name='GENDER' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>TEL&nbsp;</td><td><input type='text' name='TEL' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>HP&nbsp;</td><td><input type='text' name='HP' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>EMAIL&nbsp;</td><td><input type='text' name='EMAIL' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>INPUT_DT&nbsp;</td><td><input type='text' name='INPUT_DT' value=''></td>                
			</tr>                                                                                                 
			<tr>                                                                                                  
				<td>UPDATE_DT&nbsp;</td><td><input type='text' name='UPDATE_DT' value=''></td>                
			</tr>                                                                                                 
		</table>                                                                                                      
	                                                                                                                
	</form>                                                                                                         
	<!--폼 끝-->                                                                                                    
	                                                                                                                
</body>                                                                                                         
</html>  