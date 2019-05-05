import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chatty extends JPanel {
	private JTextField message;
	private JTextField TTL;
	private JTextArea theChat ; 
	
	public void setTheChat (String Chat ) { 
		theChat.setText(Chat);

	}
	/**
	 * Create the panel.
	 */
	public Chatty(Client client , ClientGui gooei) {
		setLayout(null);
		 
		 TTL = new JTextField();
		 TTL.setBounds(334, 209, 62, 22);
		 add(TTL);
		 TTL.setColumns(10);
		 
		theChat = new JTextArea();
//		Thread cc = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//					while (true ) { 
//				
//						theChat.setText(client.getChatt());
//					}
//			      
//				
//			}
//		}) ; 
//		cc.start();
		theChat.setBounds(25, 293, 300, 275);
		add(theChat);
		
		JComboBox<String > comboBoxx = new JComboBox<String>();
		comboBoxx.setBounds(156, 209, 169, 22);
		comboBoxx.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {

				comboBoxx.removeAllItems();

				BufferedReader br = new BufferedReader(new StringReader(client.getMemberList())) ; 
				String member ="" ; 
				try { 
				while (( member = br.readLine())!= null) { 
					System.out.println(member);
					if (!member.equals("You")){
						comboBoxx.addItem(new String (member));
					}
				}
				}
				catch (IOException ee ) { 
					ee.printStackTrace();
				}				
							
							
							
			}
		});

		
		add(comboBoxx);
		
		
		
		
		JTextArea textArea = new JTextArea("Chat with : ");
		textArea.setBounds(25, 209, 118, 22);
		textArea.setEditable(false);
		add(textArea);
		
		JTextArea memberList = new JTextArea();
		memberList.setBounds(671, 244, 190, 241);
		memberList.setEditable(false);
		add(memberList);
		
		
		
		message = new JTextField();
		message.setBounds(25, 244, 262, 36);
		add(message);
		message.setColumns(10);
		
		JButton btnSendMessage = new JButton("Send Message");
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(message.getText().length()!=0) { 
					String destination = (String )comboBoxx.getSelectedItem() ;
					int def = 2 ; 
					String xx = TTL.getText().trim() ; 
					if (xx.length()!=0) { 
						def = Integer.parseInt(xx) ; 
					}
					client.Chat(client.getClientName(), destination, message.getText(), def); ; 
				}
			}
		});
		btnSendMessage.setBounds(297, 247, 118, 31);
		add(btnSendMessage);

		JButton btnOnlinePeople = new JButton("Online People");
		btnOnlinePeople.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				memberList.setText(client.getMemberList());
			}
		});
		btnOnlinePeople.setBounds(671, 208, 190, 25);
		add(btnOnlinePeople);
		
		
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.quit();
//                JOptionPane.showInternalMessageDialog(null , "YOU HAVE QUITTED , BYE YOUNG FELLOW COME SOON ");
				gooei.frame.setVisible(false);;
				
			}
		});
		btnQuit.setBounds(369, 513, 180, 55);
		add(btnQuit);
		
		JTextArea HALLO = new JTextArea();
		HALLO.setBounds(36, 34, 379, 55);
		add(HALLO);
		 HALLO.setText("Hello " + client.getClientName());
		 HALLO.setEditable(false );
		 
		 JButton clearChat = new JButton("Clear Chat");
		 clearChat.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 		client.Chatt = "" ; 
		 		theChat.setText("");
		 	}
		 });
		 clearChat.setBounds(441, 250, 97, 25);
		 add(clearChat);

	

	}
}
