<%@page import="net.dstone.common.utils.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%    
	String SUCCESS_YN = StringUtil.nullCheck(response.getHeader("SUCCESS_YN"), "");
%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
		<!-- Header 영역 -->
		<jsp:include page="../common/header.jsp" flush="true"/>
		
	    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
	    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
	    
	</head>
	<body class="is-preload">

		<!-- Wrapper -->
		<div id="wrapper">

			<!-- Main -->
			<div id="main">
			
				<div class="inner">

					<!-- Top 영역 -->
					<jsp:include page="../common/top.jsp" flush="true"/>
					
					<section>
						<!-- =============================================== Content 영역 Start =============================================== -->
						   
					    <h2>실시간 채팅</h2>
					    <input type="text" id="sender" placeholder="이름 입력" />
					    <input type="text" id="message" placeholder="메시지 입력" />
					    <button onclick="sendMessage()">보내기</button>
					    <hr>
					    <div id="chat-box"></div>
					
					    <script>
					        let stompClient = null;
					
					        function connect() {
					            const socket = new SockJS("/ws-stomp");
					            stompClient = Stomp.over(socket);
					            stompClient.connect({}, function () {
					                stompClient.subscribe("/sub/chat/messages", function (msg) {
					                    const msgObj = JSON.parse(msg.body);
					                    const text = msgObj.sender + ": " + msgObj.content;
					                    document.getElementById("chat-box").innerHTML += text + "<br/>";
					                });
					            });
					        }
					
					        function sendMessage() {
					            const sender = document.getElementById("sender").value;
					            const content = document.getElementById("message").value;
					            stompClient.send("/pub/chat/message", {}, JSON.stringify({
					                sender: sender,
					                content: content
					            }));
					        }
					
					        connect();
					    </script>
						<!-- =============================================== Content 영역 End =============================================== -->
					</section>

				</div>
			
			</div>

			<!-- Menu 영역 -->
			<jsp:include page="../common/left.jsp" flush="true"/>

		</div>

	</body>
</html>
