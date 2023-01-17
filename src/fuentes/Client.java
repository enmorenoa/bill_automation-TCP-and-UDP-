package fuentes;

import java.net.*;

import pcd.util.TextIO4GUI;
import java.io.*;

public class Client {
	static final int DEFAULT_PORT = 8080;

	public static void main(String[] args) throws IOException {
		String userInput;
		String host; // Host del servidor (IP o nombre de dominio)
		int port; // Puerto del servidor
		Socket connection; // Socket de comunicación con el servidor

		new TextIO4GUI("Echo Client");

		// / Comprobación argumentos host y puerto
		if (args.length == 0) {
			System.out.println("Usage:  java EchoClient <computer-name> [<port>]");
			return;
		}
		host = args[0];

		if (args.length == 1)
			port = DEFAULT_PORT;
		else {
			try {
				port = Integer.parseInt(args[1]);
				if (port <= 0 || port > 65535)
					throw new NumberFormatException();
			} catch (NumberFormatException e) {
				System.out.println("Illegal port number, " + args[1]);
				return;
			}
		}
		try {
			TextIO4GUI.putln("Connecting to " + host + " on port " + port);
			connection = new Socket(host, port);
			TextIO4GUI.putln("... connected to " + connection);

			// PrintWriter vacía la salida automáticamente
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())),
					true);
			
			String text = in.readLine();
			TextIO4GUI.putln("Server: "+text);
			TextIO4GUI.putln("Waiting for your message; <RequestLetter> Request the product menu; <RequestBill> ends the program");
			userInput = TextIO4GUI.getlnString();
			while (!userInput.equalsIgnoreCase("RequestBill")) {
				out.println(userInput);
				text = in.readLine();
				System.out.println(text);
				TextIO4GUI.putln("Server: "+text);
				if(userInput.equalsIgnoreCase("RequestLetter")) {
					TextIO4GUI.putln("Waiting for your message; <CocaCola> Request a CocaCola to the bar; "
							+ "<FantaL> Request a Lemon Fanta to the bar; "
							+ "<FantaO> Request a Orange Fanta to the bar;");
				}
				userInput = TextIO4GUI.getlnString();
			}
					
					
			out.println("end");
			text = in.readLine();
			System.out.println(text);
			while(text.length() < 1) {
				text = in.readLine();	
			}
			TextIO4GUI.putln("Server: "+text);
			TextIO4GUI.putln("END");
			connection.close();

		} catch (Exception e) {
			System.out.println("An error occurred while opening connection.");
			System.out.println(e.toString());
			return;
		}
	}
}
