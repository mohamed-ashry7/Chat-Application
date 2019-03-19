import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class Client {
	static Socket clientSocket;
	static BufferedReader inFromServer;
	static DataOutputStream outToServer;
	private String clientName;
	private static String Chatt = "";
	static String MemeberList = "";
	receiveMessage receive;

	private static class receiveMessage implements Runnable {

		public receiveMessage() {
		}

		public void run() {
			while (true) {
				try {
					String modifiedSentence;
					while ((modifiedSentence = inFromServer.readLine()) != null) {
						StringTokenizer ss = new StringTokenizer(modifiedSentence);
						String fir = ss.nextToken();
						if (!fir.equals("MEMBERLIST")) {
							Chatt = Chatt + "\n" + modifiedSentence;
							modifiedSentence = null;
						} else {
							MemeberList = modifiedSentence.substring("MEMBERLIST".length() + 1);
						}
					}
					System.out.println(modifiedSentence);

				} catch (IOException e) {
					break;
				}
			}
		}
	}

	//
	// private static class sendMessage implements Runnable {
	// String Destination="";
	//
	// public void run() {
	//
	// try {
	//
	// System.out.println("if you want to connect to somebody just
	// write\"CONNECT \" and then the name ");
	// while (true) {
	// if (Destination.length() != 0 ) {
	// outToServer.writeBytes("CONNECT " + Destination);
	//
	// }
	// String sentence;
	// while (inFromUser != null && (sentence = inFromUser.readLine()) != null)
	// {
	// System.out.println(3+sentence);
	// outToServer.writeBytes(sentence + '\n');
	// }
	//
	// }
	//
	// } catch (IOException e) {
	//
	// }
	//
	// }
	// }
	public void quit() {
		try {
			outToServer.writeBytes("quit" + '\n');
			System.out.println("YOU HAVE QUITTED , BYE FELLOW ");
			clientSocket.close();
			outToServer.writeBytes("" + '\n');
		} catch (IOException e) {

		}
	}

	public boolean join(String name, int port) {

		try {
			clientSocket = new Socket("localhost", port);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			outToServer.writeBytes(name + '\n');
			String response = inFromServer.readLine();
			System.out.println(response);
			if (response.equals("Sorry this name does already exist ")) {
				clientSocket.close();
				return false;
			}
			this.clientName = name.trim();
			receive = new receiveMessage();
			Thread r = new Thread(receive);
			r.start();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void Chat(String Source, String Destination, String message, int ttl) {

		if (ttl <= 0) {
			Chatt += Chatt + "\n" + "Sorry this message cannot be send because of ttl ";
		} else {
			try {
				outToServer.writeBytes("CONNECT " + Destination + " " + ttl + '\n');
				Chatt = Chatt + "\n" + "You :" + message;

				String sentence;
				BufferedReader messages = new BufferedReader(new StringReader(message));
				while ((sentence = messages.readLine()) != null) {
					outToServer.writeBytes(sentence + '\n');
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String getClientName() {
		return clientName;
	}

	public String getChatt() {

		return Chatt;
	}

	public String getMemberList() {

		try {
			outToServer.writeBytes("get members" + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (MemeberList.length() == 0) {
			System.out.println(" in the :oop ");
		}
		StringTokenizer mem = new StringTokenizer(MemeberList, ",");
		String values = "You\n";
		while (mem.hasMoreTokens()) {
			String instance = mem.nextToken().trim();
			if (!this.clientName.equals(instance)) {
				values += instance + "\n";
			}
		}
		MemeberList = "";
		return values;

	}

}