package net.dstone.common.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.dstone.common.core.BaseObject;
import net.dstone.common.utils.LogUtil;

public class Server extends BaseObject {
	
	LogUtil logger = new LogUtil(Server.class);

	public static void main(String[] args) {
		try {
			
			Server  server = new Server();
			
			/***************************** ServerTaskItem 등록 시작 *****************************/
			// 1. 에코
			ServerTaskItem echoTaskItem = new net.dstone.common.socket.ServerTaskItem(){
				protected Object doTask(Object param){
					System.out.println("param===>>" + param);
					return param;
				}
			};
			server.registerServerTask("echoTaskItem", 1000, echoTaskItem);

			// 2. 인사
			ServerTaskItem greetingTaskItem = new net.dstone.common.socket.ServerTaskItem(){
				protected Object doTask(Object param){
					Object output = "Hi [" + param + "] ~!";
					System.out.println(output);
					return output;
				}
			};
			server.registerServerTask("greetingTaskItem", 2000, greetingTaskItem);
			
			/***************************** ServerTaskItem 등록 종료 *****************************/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static Map<String, ServerTask> SERVER_SOCKET_MAP = new HashMap<String, ServerTask>();

	public ServerTask registerServerTask(String serverSocketId, int port, ServerTaskItem task){
		ServerTask serverTask = null;
		if( !SERVER_SOCKET_MAP.containsKey(serverSocketId) ) {
			serverTask = new ServerTask(port);
			SERVER_SOCKET_MAP.put(serverSocketId, serverTask);
		}else {
			serverTask = SERVER_SOCKET_MAP.get(serverSocketId);
		}
		return serverTask;
	}

	class ServerTask{
		private int port;
		
		ServerTask(int port){
			this.port = port;
		}
		
		public void start(ServerTaskItem taskItem) {
			logger.info("ServerSoket Starting ....");
			
			try(ServerSocket server = new ServerSocket(this.port)){
				
				while(true){
					Socket socket = server.accept();
					taskItem.setSocket(socket);
					taskItem.setDaemon(true);
					taskItem.start();
					logger.info("ServerSoket Started !!!");
				}
				
			} catch(IOException e){
				LogUtil.sysout(e);
			}
		}
		
	}
	
}
