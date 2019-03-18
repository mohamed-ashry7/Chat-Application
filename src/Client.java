import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {
	static Socket clientSocket;
	static BufferedReader inFromUser;
	static BufferedReader inFromServer;
	static DataOutputStream outToServer;
	private String clientName;
	private static  String Chatt =""; 
//	private static class receiveMessage implements Runnable {
//
//		public receiveMessage() {
//		}
//
//		public void run() {
//			while (true) {
//				try {
//					
//					inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//					String modifiedSentence = inFromServer.readLine();
//					Chatt = Chatt +"\n" + modifiedSentence ; 
//					System.out.println(modifiedSentence);
//				} catch (IOException e) {
//					break;
//				}
//			}
//		}
//	}

	private static class sendMessage implements Runnable {
		public sendMessage() {

		}

		public void run() {

			try {

				System.out.println("if you want to connect to somebody just write \"CONNECT \" and then the name ");
				while (true) {
					String sentence;
					sentence = inFromUser.readLine();

					outToServer.writeBytes(sentence + '\n');

				}

			} catch (IOException e) {

			}

		}
	}

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

		// inFromUser = new BufferedReader(new InputStreamReader(System.in));
		// System.out.println("Choose your server for Server1 enter 6000 Server
		// enter 5999");
		// String portt = inFromUser.readLine();

		// int port = Integer.parseInt(portt);
		// System.out.println("Enter" + " your name ");
		try {
			clientSocket = new Socket("localhost", port);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());

			// String name = inFromUser.readLine();
			outToServer.writeBytes(name + '\n');
			String response = inFromServer.readLine();
			System.out.println(response);
			if (response.equals("Sorry this name does already exist ")) {
				clientSocket.close();
				return false;
			}
			this.clientName = name;
//			receiveMessage receive = new receiveMessage();
//			sendMessage send = new sendMessage();
//			Thread r = new Thread(receive);
//			Thread s = new Thread(send);
//			r.start();
//			s.start();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void Chat(String Source, String Destination, String message, int ttl) {
		try {
			outToServer.writeBytes("CONNECT " + Destination);
			inFromUser = new BufferedReader(new StringReader(message));
			Chatt = Chatt +"\n" +"You :" +  message ; 
			String sentence;
			while ((sentence = inFromUser.readLine())!= null){
				

				outToServer.writeBytes(sentence + '\n');

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getClientName() {
		return clientName;
	}

	public String getChatt () { 
		try {

			String modifiedSentence = inFromServer.readLine();
			Chatt = Chatt +"\n" + modifiedSentence ; 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Chatt; 
	}
	public String getMemberList() {
//		try {
//			outToServer.writeBytes("get members" + '\n');
//			String members = inFromServer.readLine();
//			StringTokenizer mem = new StringTokenizer(members, ",");
//			String values = "You\n";
//			while (mem.hasMoreTokens()) {
//				String instance = mem.nextToken();
//				if (!this.clientName.equals(instance))
//					values += instance + "\n";
//			}
			return "hhahahahah \n lknaslknflkansf \n akjsnfkasdsad";
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//
//		}
	}
	// public static void main(String[] args) throws IOException,
	// InterruptedException {
	// }

}