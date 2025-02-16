package net.dstone.common.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.dstone.common.core.BaseObject;

public class Client extends BaseObject{

	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	String ip;
	int port;
	
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public void connect() {
		try {
			// 서버에 요청 보내기
			socket = new Socket(ip, port);
			info(socket.getInetAddress().getHostAddress() + " 연결됨");
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream (socket.getInputStream());
		} catch (Exception e) {
			error(e);
		}
	}

	public void disConnect() {
		if( this.socket != null ) {
			try {
				this.socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if( this.oos != null ) {
			try {
				this.oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if( this.ois != null ) {
			try {
				this.ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Object sendMsg(Object sendMsg) {
		Object receiveMsg = null;
		try {
			// 서버에 요청 보내기
			if(this.socket == null) {
				throw new Exception("소켓연결이 되어있지 않습니다.");
			}
			
			//VO 메시지 발송
			oos.writeObject(sendMsg);
			oos.flush();

			//발송 후 메시지 받기
			receiveMsg = ois.readObject();
			
		} catch (Exception e) {
			error(e);
		}
		return receiveMsg;
	}
	
	
}
