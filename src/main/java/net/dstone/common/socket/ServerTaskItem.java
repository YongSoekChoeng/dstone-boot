package net.dstone.common.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.dstone.common.utils.LogUtil;

public abstract class ServerTaskItem extends Thread {

    LogUtil logger = new LogUtil(ServerTaskItem.class);

	private Socket socket;

	public ServerTaskItem(){
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void run(){
		
		ObjectOutputStream oos = null;
		ObjectInputStream  ois = null;
		try{
			if(this.socket == null ) {
				throw new Exception( "Socket should be set first !!!" );
			}
			
			ois = new ObjectInputStream (socket.getInputStream());  
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			Object response = this.doTask(ois.readObject());
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
				this.closeSocket();
			}catch(IOException e){
				logger.error(e);
			}
		}
	}

	public void closeSocket() {
		try{
			if(socket != null){socket.close();}
		}catch(IOException e){
			logger.error(e);
		}
	}
	
	protected abstract Object doTask(Object param);

}
