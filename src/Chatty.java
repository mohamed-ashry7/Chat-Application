import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chatty extends JPanel {
	private JTextField message;
	
	/**
	 * Create the panel.
	 */
	public Chatty(Client client ) {
		setLayout(null);
		JTextArea theChat = new JTextArea();
		Thread cc = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized(client) {
					while (true ) { 
						theChat.setText(client.getChatt());
					}
			      }
				
			}
		}) ; 
		cc.start();
		theChat.setBounds(25, 293, 262, 275);
		add(theChat);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(156, 209, 169, 22);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					client.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader br = new BufferedReader(new StringReader(client.getMemberList())) ; 
				client.notify();
				String member ="" ; 
				try { 
				while (( member = br.readLine())!= null) { 
					if (!member.equals("You")){
						comboBox.addItem(member);
					}
				}
				}
				catch (IOException ee ) { 
					ee.printStackTrace();
				}				
			}
		});
		
		add(comboBox);
		
		
		
		
		JTextArea textArea = new JTextArea("Chat with : ");
		textArea.setBounds(25, 209, 118, 22);
		add(textArea);
		
		JTextArea memberList = new JTextArea();
		memberList.setBounds(671, 244, 190, 241);
		add(memberList);
		
		
		
		message = new JTextField();
		message.setBounds(25, 244, 262, 36);
		add(message);
		message.setColumns(10);
		
		JButton btnSendMessage = new JButton("Send Message");
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(message.getText().length()!=0) { 
					String destination = (String )comboBox.getSelectedItem() ;
					client.Chat(client.getClientName(), destination, message.getText(), 2); ; 
				}
			}
		});
		btnSendMessage.setBounds(297, 247, 118, 31);
		add(btnSendMessage);
		System.out.println("aaaaaaaaaaasda" + client.getMemberList());

		JButton btnOnlinePeople = new JButton("Online People");
		btnOnlinePeople.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					client.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				memberList.setText(client.getMemberList());
				client.notify();
			}
		});
		btnOnlinePeople.setBounds(671, 208, 190, 25);
		add(btnOnlinePeople);
		
		
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.quit();
				
				
			}
		});
		btnQuit.setBounds(369, 513, 180, 55);
		add(btnQuit);
		
	

	}
}
