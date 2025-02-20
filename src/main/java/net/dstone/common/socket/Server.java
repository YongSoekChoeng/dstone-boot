package net.dstone.common.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import net.dstone.common.core.BaseObject;
import net.dstone.common.utils.LogUtil;

public class Server extends BaseObject {
	
	public static class SOCKET_SERVER_LIST{
		public static String ECHO = "ECHO";
		public static String GREETING = "GREETING";
	}
	
	LogUtil logger = new LogUtil(Server.class);
	
	private net.dstone.common.task.TaskHandler taskHandler;

	String executorServiceId;
	private int port;
	
	private Server(){
	}

	public Server(String executorServiceId, int port) throws Exception{
		this.executorServiceId = executorServiceId;
		this.port = port;
		taskHandler = net.dstone.common.task.TaskHandler.getInstance().addFixedExecutorService(executorServiceId+"_FIXED", 10);
	}
	
	public void start() throws Exception{

		try(ServerSocket server = new ServerSocket(this.port)){

			LogUtil.sysout("executorServiceId["+executorServiceId+"] START");
			
			while(true){
				Socket socket = server.accept();
				
				ServerTaskItem taskItem = new ServerTaskItem();
				taskItem.setSocket(socket);
				taskItem.setExecutorServiceId(executorServiceId);
				taskHandler.doTheAsyncTask(executorServiceId+"_FIXED", taskItem);
			}
			
		} catch(IOException e){
			LogUtil.sysout(e);
		} finally {
			LogUtil.sysout("executorServiceId["+executorServiceId+"] END");
		}
	}
	

	public static void main(String[] args) {
		
		try {
			
			Server greetingServer = new Server(SOCKET_SERVER_LIST.GREETING, 1000);
			greetingServer.start();

			Server echoServer = new Server(SOCKET_SERVER_LIST.ECHO, 2000);
			echoServer.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
