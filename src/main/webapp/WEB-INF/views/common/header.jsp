<%@page import="net.dstone.common.utils.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    
%>  
	<title>Dstone Framework</title>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/main.css" />
	<!-- Scripts -->
	<script src="<%=request.getContextPath()%>/assets/js/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/assets/js/browser.min.js"></script>
	<script src="<%=request.getContextPath()%>/assets/js/breakpoints.min.js"></script>
	<script src="<%=request.getContextPath()%>/assets/js/util.js"></script>
	<script src="<%=request.getContextPath()%>/assets/js/main.js"></script>
	<script src="<%=request.getContextPath()%>/js/jquery-1.7.2.js"></script> 
	<script src="<%=request.getContextPath()%>/js/jquery.json-2.4.js" ></script> 
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
	</script>
