package fuentes;

import java.io.*;
import java.net.*;

class MessageSender implements Runnable {
	public final static int PORT = 7331;
	private DatagramSocket sock;
	private String hostname;
	String ss;

	MessageSender(DatagramSocket s, String h) {
		sock = s;
		hostname = h;
	}
	
	public void setString(String s) {
		this.ss = s;
	}


	private void sendMessage(String s) throws Exception {
		byte buf[] = s.getBytes();
		InetAddress address = InetAddress.getByName(hostname);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
		sock.send(packet);
	}

	public void run() {
		boolean connected = false;
		do {
			try {
				sendMessage(this.ss);
				System.out.println("MENSAJE ENVIADO");
				connected = true;
				} catch (Exception e) {

			}
		} while (!connected);	
	}
}

class MessageReceiver implements Runnable {
	DatagramSocket sock;
	byte buf[];
	String result="";
	
	MessageReceiver(DatagramSocket s) {
		sock = s;
		buf = new byte[1024];
	}

	public void getString(String s) {
		this.result = s;
	}
	
	public void run() {
		int lon = 0; 
		while (lon < 1) {
			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				sock.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				lon = packet.getLength();
				if(lon>1) {
					this.getString(received);
				}
				System.out.println(received);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}

public class ClientUDP {
	
	public String calculateData(String data, String host) throws Exception {
		String result="";
		String hostl = null;
		if (host.length() < 1) {
			System.out.println("Usage: java ChatClient <server_hostname>");
			System.exit(0);
		} else {
			hostl = host;
		}
		DatagramSocket socket = new DatagramSocket();
		
		MessageReceiver r = new MessageReceiver(socket);
		MessageSender s = new MessageSender(socket, host);
		s.setString(data);
		Thread rt = new Thread(r);
		Thread st = new Thread(s);
		rt.start();
		st.start();
		System.out.println("INICIANDO ECHA CUENTAS");
		
		while(rt.isAlive()) {
			
		}
		
		result = r.result;
		return result;
	}
}
