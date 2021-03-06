import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.print.DocFlavor.BYTE_ARRAY;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JPanel implements ActionListener{
	
	JLabel userL = new JLabel("choose a Username : ");
	JTextField userTF = new JTextField();
	JLabel passL = new JLabel("Password: ");
	JPasswordField passTF = new JPasswordField();
	JLabel passLC = new JLabel("Confirm Password: ");
	JPasswordField passC = new JPasswordField();
	JButton register = new JButton("Register: ");
	JButton back = new  JButton("Go Back");
	JLabel nouser = new JLabel("");
	
	public Register(){
		
		JPanel loginP = new JPanel();
		loginP.setLayout(new GridLayout(5, 2));
		loginP.add(userL);
		loginP.add(userTF);
		loginP.add(passL);
		loginP.add(passTF);
		loginP.add(passLC);
		loginP.add(passC);
		loginP.add(register);
		loginP.add(back);
		loginP.add(nouser);
		register.addActionListener(this);
		back.addActionListener(this);
		add(loginP);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == register && passTF.getPassword().length > 0 && userTF.getText().length() > 0){
			String pass = new String(passTF.getPassword());
			String cpass = new String(passC.getPassword());
			
			if(pass.equals(cpass)){
				try {
					
					BufferedReader input = new BufferedReader(new FileReader("passwords.txt"));
					String line = input.readLine();
					while(line != null){
						StringTokenizer st = new StringTokenizer(line);
						
						if(userTF.getText().equals(st.nextToken())){
							nouser.setText("User Already Exists!!");
							return;
						}
						line = input.readLine();
					}
					input.close();
					
					try {
						
						MessageDigest md = MessageDigest.getInstance("SHA-256");
						md.update(pass.getBytes());
						byte byteData[] = md.digest();
						StringBuffer sb = new StringBuffer();
						
						for(int i=0; i<byteData.length; i++){
							
							sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));	//encryption
							BufferedWriter output = new BufferedWriter(new FileWriter("passwords.txt", true));	//true means we are appending to the file
							output.write(userTF.getText() + " " + sb.toString() + "\n");
							output.close();
							Login login = (Login) getParent();
							login.cl.show(login, "login");
							
						}
				
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				} catch (FileNotFoundException e1) {
					
					e1.printStackTrace();
				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				nouser.setText("Passwords Do not Match!!");
			}
		}
		
		if(e.getSource() == back){
			Login login = (Login) getParent();
			login.cl.show(login, "login");
		}
		
	}
	
	
}
