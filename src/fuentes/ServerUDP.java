package fuentes;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerUDP extends Thread {
	public final static int PORT = 7331;
	private final static int BUFFER = 1024;

	private DatagramSocket socket;
	private ArrayList<InetAddress> clientAddresses;
	private ArrayList<Integer> clientPorts;
	private HashSet<String> existingClients;

	public ServerUDP() throws IOException {
		socket = new DatagramSocket(PORT);
		clientAddresses = new ArrayList(); // Lista de direcciones de clientes
		clientPorts = new ArrayList(); // Lista de puertos de clientes
		existingClients = new HashSet(); // Tabla hash con direcciones y puertos de los clientes
	}

	public void run() {
		byte[] buf = new byte[BUFFER];
		while (true) {
			try {
				Arrays.fill(buf, (byte) 0);
				// Preparamos un DatagramPacket para recibir los datos
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				// Esperamos el paquete a través del socket
				socket.receive(packet);

				String content = new String(buf);

				// // Extraemos dirección (clientAddress) y puerto (clientPort)
				InetAddress clientAddress = packet.getAddress();
				int clientPort = packet.getPort();
				System.out.println("AQUI LLEGO");
				// Añadimos los datos a la tabla hash
				String id = clientAddress.toString() + "," + clientPort;
				if (!existingClients.contains(id)) {
					existingClients.add(id);
					clientPorts.add(clientPort);
					clientAddresses.add(clientAddress);
				}

				// Mostramos en consola el nombre del emisor y el mensaje recibido
				System.out.println(id + " : " + content);
				if(content.length()>0) {
				String[] parts = content.split(";");
				String result = "";

				for(String part : parts) {
					
					String[] par = part.split(",");
					if(par[0].equalsIgnoreCase("CocaCola")) {
						result += par[0]+","+String.valueOf(Double.parseDouble(par[1])*1.50)+";";

					}
					if(par[0].equalsIgnoreCase("FantaL")) {
						result += par[0]+","+String.valueOf(Double.parseDouble(par[1])*1.50)+";";	

					}
					if(par[0].equalsIgnoreCase("FantaO")) {
						result += par[0]+","+String.valueOf(Double.parseDouble(par[1])*1.50)+";";							
					}
					
				}
				result = result.substring(0, result.length() - 1);
				byte[] data = result.getBytes();
				packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
				socket.send(packet);
				}
				/*
				// Enviamos esa línea del chat a cada uno de los clientes
				byte[] data = (id + " : " + content).getBytes();
				for (int i = 0; i < clientAddresses.size(); i++) {
					InetAddress address = clientAddresses.get(i);
					int port = clientPorts.get(i);
					packet = new DatagramPacket(data, data.length, address, port);
					socket.send(packet);
				}/*/

			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	public static void main(String args[]) throws Exception {
		ServerUDP s = new ServerUDP();
		System.out.println("EMPEZANDO");
		s.start();
	}
}