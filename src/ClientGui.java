import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ClientGui {

	public JFrame frame;
	private JTextField textField;
	private Client client ; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGui window = new ClientGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGui() {
		initialize();
		client = new Client () ; 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 919, 643);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e)
	            {
	                client.quit() ; 
	                e.getWindow().dispose();
	            }
		});
		frame.addMouseListener(new MouseListener() {
		
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.requestFocus();
			}
		});
		JComboBox comboBox = new JComboBox();
		comboBox.addItem(new String("Server"));
		comboBox.addItem(new String("Server1"));
		comboBox.setBounds(327, 289, 198, 22);
		frame.getContentPane().add(comboBox);

		JButton btnJoin = new JButton(" Join ");
		btnJoin.setEnabled(false);
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = textField.getText();
				String str_server = (String) comboBox.getSelectedItem();
				int port = 5999;
				if (str_server.equals("Server")) {
					port = 5999;
				} else {
					port = 6000;
				}
				boolean flag = client.join(name, port) ;
				if (flag) { 
//					frame.getContentPane().removeAll();
					frame.remove(textField);
					frame.remove(comboBox);
					frame.remove(btnJoin);
					frame.setLayout(new BorderLayout());
					frame.add(new Chatty(client) ,BorderLayout.CENTER) ; 
					frame.repaint();
					frame.revalidate();
				}
				else {
					JOptionPane.showMessageDialog(null, "Sorry This Name Already Taken ");
				}
			}
		});

		btnJoin.setBounds(369, 355, 97, 25);
		frame.getContentPane().add(btnJoin);

		textField = new JTextField("Enter you name ");
		textField.setBounds(327, 213, 198, 46);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setForeground(Color.GRAY);
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				if (textField.getText().length() == 0) {
					textField.setText("Enter you name ");
					textField.setForeground(Color.GRAY);
					btnJoin.setEnabled(false);

				} else {
					btnJoin.setEnabled(true);
				}

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				if (textField.getText().equals("Enter you name ")) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}

			}
		});

	}
	
}
