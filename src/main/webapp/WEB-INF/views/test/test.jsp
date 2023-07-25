<%@page import="net.dstone.common.utils.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    
	String SUCCESS_YN = StringUtil.nullCheck(response.getHeader("SUCCESS_YN"), "");
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript" src="/js/jquery-1.7.2.js"></script> 
<script type="text/javascript" src="/js/jquery.json-2.4.js" ></script> 
<script type="text/javascript">
	
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

	function doTestAjax(){ 
		$.ajax({ 
			type:"POST", 
			url:"/test/doTestAjax.do", 
			data:encodeURIComponent(JSON.stringify($(document.AJAX_FORM).serializeObject())), 
			dataType:"json", 
			success:function(data, status, request){
				var SUCCESS_YN = request.getResponseHeader('SUCCESS_YN');
				var ERR_CD = data['ERR_CD'];
				var ERR_MSG = data['ERR_MSG'];
				if( 'Y' == SUCCESS_YN ){
					console.log('success ===>>> data:' + (JSON.stringify(data)));
					alert("success");
					$("#SUCCESS_YN").text("성공");
				}else{
					console.log('failure ===>>> data:' + (JSON.stringify(data)));
					alert("failure");
					$("#SUCCESS_YN").text("실패");
				}
				
			}, 
			error : function(data, status, e) { 
				console.log('system error ===>>> data:' + (JSON.stringify(data))); 
				alert("system error");
				$("#SUCCESS_YN").text("에러]");
			} 
		}); 
	} 

	function doTestSubmit(){ 
		document.SUBMIT_FORM.action="/test/doTestSubmit.do";
		document.SUBMIT_FORM.submit();
	} 
 
</script>

<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Test</title>
</head>
<body>
		
	<a href="/index.html" >index</a>
	<br><br>
	
	폼서밋 테스트<%=("Y".equals(SUCCESS_YN)?"성공":("N".equals(SUCCESS_YN)?"실패":"")) %><br>
	<form name="SUBMIT_FORM" method="post" action="">
	TEST_PARAM1:<input type="text" name="MY_NAME" value="TEST..." >
	<br>
	FORCE_EXCEPTION_YN:<select name="FORCE_EXCEPTION_YN" ><option value="N">성공</option><option value="Y">실패</option></select>
	<br>
	<table border=1>
		<tr>
			<td colspan="2"> <input type="button" value="GO" onclick="javascript:doTestSubmit();" > </td>
		</tr>
	</table>
	</form>
	
	<br>
	<br>
	
	AJAX 테스트<span id="SUCCESS_YN"></span><br>
	<form name="AJAX_FORM" method="post" action="">
	TEST_PARAM1:<input type="text" name="MY_NAME" value="TEST..." >
	<br>
	FORCE_EXCEPTION_YN:<select name="FORCE_EXCEPTION_YN" ><option value="N">성공</option><option value="Y">실패</option></select>
	<br>
	<table border=1>
		<tr>
			<td colspan="2"> <input type="button" value="GO" onclick="javascript:doTestAjax();" > </td>
		</tr>
	</table>
	</form>
	
</body>
</html>