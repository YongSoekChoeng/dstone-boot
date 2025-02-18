package net.dstone.common.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.dstone.common.task.TaskItem;
import net.dstone.common.utils.LogUtil;

public class ServerTaskItem extends TaskItem {

    LogUtil logger = new LogUtil(ServerTaskItem.class);

	private Socket socket;
	private Object response;

	public ServerTaskItem(){
		
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Object getResponse() {
		return response;
	}

	
	public TaskItem doTheTask() {
		
		ObjectOutputStream oos = null;
		ObjectInputStream  ois = null;
		try{
			if(this.socket == null ) {
				throw new Exception( "Socket should be set first !!!" );
			}
			
			ois = new ObjectInputStream (socket.getInputStream());  
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			Object response = this.doTheServerJob(ois.readObject());
			if( response != null ) {
				oos.writeObject(response);
			}
			oos.flush();
		}catch(Exception e){
			logger.error(e);
		}finally{
			try{
				if(oos != null) { oos.close();}
				if(ois != null) { ois.close();}
				if(socket != null){socket.close();}
			}catch(IOException e){
				logger.error(e);
			}
		}
		
		return this;
	}
	
	protected Object doTheServerJob( Object param) {
		Object output = "Hi [" + param + "] ~!";
		System.out.println(output);
		return output;
	}

}
