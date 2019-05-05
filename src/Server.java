import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	static ArrayList<Handler> clients = new ArrayList<Handler>();
	static String currentName;
	private static Socket theOtherServer;
	private static PrintWriter outServer;
	private static BufferedReader inFromOtherServer;
	static Handler ser;
	private static String check = "";



	public static boolean exists(String name) throws IOException {
		for (int i = 0; i < clients.size(); i++) {
			if (name.equals(clients.get(i).clientName)) {
				return true;
			}
		}
		outServer.println("EXIST " + name);

		while (check.equals("")) {
		}
		if (check.equals("ALLOW")) {
			check = "";
			return false;
		}

		else
			check = "";

		return true;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("The Server is running.");
		ExecutorService pool = Executors.newCachedThreadPool();
		int clientNumber = 0;
		theOtherServer = new Socket("DESKTOP-NF39RN5", 6000);
		outServer = new PrintWriter(theOtherServer.getOutputStream(), true);
		inFromOtherServer = new BufferedReader(new InputStreamReader(theOtherServer.getInputStream()));
		outServer.println("SERVER");
		Handler h = new Handler(theOtherServer, clientNumber++, "SERVER1");
		ser = h;
		pool.execute(h);
		try (ServerSocket listener = new ServerSocket(5999)) {

			while (true) {
				Socket connectionSocket = listener.accept();
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				String client = inFromClient.readLine();
				if (!exists(client)) {
					outToClient.writeBytes("Hallo  , " + client + '\n');
					h = new Handler(connectionSocket, clientNumber++, client);
					clients.add(h);
					pool.execute(h);
				} else {
					outToClient.writeBytes("Sorry this name does already exist " + '\n');
				}
			}
		}
	}

	public static class Handler implements Runnable {
		private Socket socket;
		private int clientNumber;
		String clientName;
		private Socket targetSocket = null;
		private PrintWriter out1;
		private String tarName;
		private static final String FROM_SERVER = "FROM SERVER :";

		public Handler(Socket socket, int clientNumber, String name) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			this.clientName = name;
			if (!this.socket.equals(theOtherServer))
				System.out
						.println("New client #" + clientNumber + " whose name is " + name + " connected at " + socket);
			else
				System.out.println("SERVER IS CONNECTED TO THE SERVER1");
		}

	

		public String getMembers() {
			String x = "" ; 
			for (int i = 0; i < clients.size(); i++) {
				x += ", " +clients.get(i).clientName ;
			}

			return x;

		}

		public static Socket findSocket(String x) {
			for (int i = 0; i < clients.size(); i++) {
				if (x.equals(clients.get(i).clientName)) {
					return clients.get(i).socket;
				}
			}
			return null;
		}

		public void run() {

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				while (true) {

					String x = in.readLine();
					StringTokenizer s = new StringTokenizer(x);
					String first = "";
					if (x.length() != 0)
						first = s.nextToken();
					Socket target;
					String name;
					
					
					if (first.equals("MEMBERS?")) { 
						String members = getMembers() ;
						outServer.println("MEM " + members);
					}
					else if (first.equals("MEM")){
						if (x.length() > 4)
						x = x.substring(6) + getMembers() ;
						else {
							x = getMembers() ; 
							x=x.substring(2) ; 
						}
						out1 = new PrintWriter(this.targetSocket.getOutputStream(), true);
						out1.println("MEMBERLIST "+ x); 
						
							
						
						
					}
					else if (first.equals("EXIST")) {
						name = s.nextToken();
						target = findSocket(name);

						if (name.equals("ALLOW")) {
							check = "ALLOW";
						} else if (name.equals("Nope")) {
							check = "nope";
						} else if (target == null) {
							outServer.println("EXIST ALLOW");
						} else
							outServer.println("EXIST Nope");
					} else if (first.equals("CONNECT")) {
						name = s.nextToken();
						target = findSocket(name);
						if (this.socket.equals(theOtherServer)) {
							if (name.equals("YESS")) {
//								out1.println("YOU CAN NOW CHAT WITH " + this.tarName);
							} else if (name.equals("NOO")) {
								out1.println("there is no one with that name ");
							} else {
								if (target == null) {
									outServer.println("CONNECT NOO");
								} else {
									outServer.println("CONNECT YESS");
									this.targetSocket = target;
								}

							}
						} else {
							tarName = name;

							if (target == null) {
								String sec = s.nextToken() ;  
								int ttl = Integer.parseInt(sec) ; 
								if (ttl == 1) { 
									out.println("Sorry this message was not sent because of ttl ");
								}
								else {
								outServer.println("CONNECT " + name +" 2");
								ser.targetSocket = this.socket;
								ser.out1 = new PrintWriter(this.socket.getOutputStream(), true);
								ser.tarName = name;
								this.targetSocket = theOtherServer;
								}
							} else {
//								out.println("YOU CAN NOW CHAT WITH " + name);
								targetSocket = target;
								out1 = new PrintWriter(this.targetSocket.getOutputStream(), true);

							}
						}

					}

					else {
						if (x.equals("quit")) {
							for (int i = 0; i < clients.size(); i++) {
								if (clients.get(i).socket.equals(this.socket)) {
									clients.remove(i); // check if you deleted
														// someone
								}

							}
							for (int i = 0; i < clients.size(); i++) {

								System.out.println(clients.get(i).clientName); // check

							}
							break;
						} else if (x.equals("get members")) {
							
							ser.targetSocket = this.socket ; 
							outServer.println("MEMBERS?");

						}

						else if (targetSocket != null) {
							if (this.socket.equals(theOtherServer)) {
								Socket temp = findSocket(first);

								while (true) {
									if (x.charAt(0) == ' ') {
										x = x.substring(1);
										break;
									}
									x = x.substring(1);
								}
								out1 = new PrintWriter(temp.getOutputStream(), true);
								out1.println("FROM " + x + "  ");
							} else if (targetSocket.equals(theOtherServer)) {
								String wan = tarName + " " + this.clientName + " : " + x;
								out1 = new PrintWriter(theOtherServer.getOutputStream(), true);
								out1.println(wan);

							} else {
								out1.println("FROM " + this.clientName + " : " + x);
							}
							targetSocket = null ; 

						} else {
//							out.println(FROM_SERVER + x);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

				System.out.println("Error handling client #" + clientNumber);
			} finally {

				System.out.println("Connection with client # " + clientNumber + " closed");
			}
		}
	}

}
