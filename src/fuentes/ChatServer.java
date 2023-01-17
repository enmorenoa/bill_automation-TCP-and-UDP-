package fuentes;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import pcd.util.TextIO4GUI;

public class ChatServer implements Runnable {
	static final int DEFAULT_PORT = 8080;
	public Socket ClientSocket;
	public Map<String, Integer> bill = new HashMap<String, Integer>();
	
	static TextIO4GUI TextIO = new TextIO4GUI("Bar Server");

	public ChatServer(Socket _ClientSocket) {
		this.ClientSocket = _ClientSocket;
		this.bill.put("CocaCola",0);
		this.bill.put("FantaL",0);
		this.bill.put("FantaO",0);
	}
	
	public static void main(String[] args) throws IOException {
		int port; // Puerto de escucha del servidor
		ServerSocket listener;
		Socket connection;
		if (args.length == 0)
			port = DEFAULT_PORT;
		else {
			try {
				port = Integer.parseInt(args[0]);
				if (port < 0 || port > 65535)
					throw new NumberFormatException();
			} catch (NumberFormatException e) {
				System.out.println("Illegal port number, " + args[0]);
				return;
			}
		}
		try {
			listener = new ServerSocket(port);
			TextIO4GUI.putln("Listening on port " + listener.getLocalPort());
			// Se bloquea hasta recibir una conexión
			int i = 1;
			while (i < 5) {
				connection = listener.accept();
				Thread cliente = new Thread(new ChatServer(connection), "Cliente " + i);
				cliente.start();
				i++;
			}
			listener.close();
		} catch (Exception e) {
			System.out.println("An error occurred while opening connection.");
			System.out.println(e.toString());
			return;
		}
	}

	public void run() {
		String threadName = Thread.currentThread().getName();
		try {
			TextIO4GUI.putln("\nNueva conexión " + ClientSocket + " desde la dirección: "
					+ ClientSocket.getInetAddress().getHostName() + "   ..... IDENTIFICADO COMO: " + threadName);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream())),
					true);
			
			String str = "Welcome to our bar, do you wish something?";
			out.println(str);
			
			str = in.readLine();
			while (!str.equalsIgnoreCase("end")) {
				switch(str) {
				case "RequestLetter":
					TextIO4GUI.putln(threadName + ": \n"
							+ "has asked for the product menu of the bar.");
					str = "MENU BAR; 1.CocaCola 1.50€; 2.FantaL 1.50€; 3.FantaO 1.50€";
					out.println(str);
					break;
				case "CocaCola":
					TextIO4GUI.putln(threadName + ": \n"
							+ "has asked a CocaCola.");
					str = "Here is your CocaCola, do you wish something more?";
					this.bill.put("CocaCola", this.bill.get("CocaCola")+1);
					out.println(str);
					break;
				case "FantaL": 
					TextIO4GUI.putln(threadName + ": \n"
							+ "has asked a Lemon Fanta.");
					str = "Here is your Lemon Fanta, do you wish something more?";
					this.bill.put("FantaL", this.bill.get("FantaL")+1);
					out.println(str);
					break;
				case "FantaO":
					TextIO4GUI.putln(threadName + ": \n"
							+ "has asked a Lemon Fanta.");
					str = "Here is your Lemon Fanta, do you wish something more?";
					this.bill.put("FantaL", this.bill.get("FantaO")+1);
					out.println(str);
					break;
				default:
					TextIO4GUI.putln(threadName + ": \n"
							+ "has asked an unknown thing.");
					str = "We don't understand what you have asked for, can you repeat please?";
					out.println(str);
					break;
				}
				
				str = in.readLine();
					
			}
			String data = "";

			for (String key: bill.keySet()) {
				data += key+","+bill.get(key)+";";
			}
			data = data.substring(0, data.length() - 1);
			ClientUDP cl = new ClientUDP();
			String b="";
			try {
				b = cl.calculateData(data, "localhost");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String[] parts = b.split(";");
			String result="";
			Double total = 0.0;
			for(String part : parts) {
				String[] par = part.split(",");
				if(par[0].equalsIgnoreCase("CocaCola")) {
					if(Double.parseDouble(par[1])>0.50) {
						result += "  --"+par[0]+": "+String.valueOf(Double.parseDouble(par[1]))+"€;";
						total+=Double.parseDouble(par[1]);
					}

				}
				if(par[0].equalsIgnoreCase("FantaL")) {
					if(Double.parseDouble(par[1])>0.50) {
						result += "  --"+par[0]+": "+String.valueOf(Double.parseDouble(par[1]))+"€;";
						total+=Double.parseDouble(par[1]);
					}

				}
				if(par[0].equalsIgnoreCase("FantaO")) {
					if(Double.parseDouble(par[1])>0.50) {
						result += "  --"+par[0]+": "+String.valueOf(Double.parseDouble(par[1]))+"€;";
						total+=Double.parseDouble(par[1]);
					}
			}
			}
			result += "  In total, the bill prize is "+total.toString()+"€";
			out.println(result);
			TextIO4GUI.putln("The bill has sent to the client");
			TextIO4GUI.putln("Desconexion del " + threadName);
			ClientSocket.close();
		} catch (IOException e) {
			System.err.println("Error al enviar datos por el socket : " + e);
			System.exit(-1);
		}
	}
}