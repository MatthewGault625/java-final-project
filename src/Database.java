import java.awt.*;
import javax.swing.*;
import java.awt.Font;
import java.sql.*;
import java.util.logging.*;
import java.awt.event.*;

public class Database 
{
	String url = "jdbc:sqlite:bin\\Users.db";
	
	private JFrame frame;
	// New Jframe for the game menu
	private JFrame frame2;
	private JTextField usernameTxtFld;
	private JTextField PasswordTxtFld;
	public static void main(String[] args)				//Launch the application
	{
				
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Database window = new Database();
					window.frame.setVisible(true);
				} 
				
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public Database()				// Create the application
	{
		initialize();
	}

	private void initialize()				//Initialize the contents of the frame
	{
		frame = new JFrame();
		frame.setBounds(10, 10, 700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		usernameTxtFld = new JTextField();
		usernameTxtFld.setColumns(10);
		usernameTxtFld.setBounds(209, 150, 217, 39);
		frame.getContentPane().add(usernameTxtFld);
		
		JLabel logInLabel = new JLabel("Please Log In/Sign Up");
		logInLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		logInLabel.setBounds(209, 81, 207, 59);
		frame.getContentPane().add(logInLabel);
		
		PasswordTxtFld = new JTextField();
		PasswordTxtFld.setColumns(10);
		PasswordTxtFld.setBounds(209, 199, 217, 39);
		frame.getContentPane().add(PasswordTxtFld);
		
		JLabel userLabel = new JLabel("Username:");
		userLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		userLabel.setBounds(102, 154, 98, 26);
		frame.getContentPane().add(userLabel);
		
		JLabel passLabel = new JLabel("Password:");
		passLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passLabel.setBounds(102, 203, 98, 26);
		frame.getContentPane().add(passLabel);
		
		JButton SignUp = new JButton("Sign Up");
		SignUp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		SignUp.addActionListener(new SignUpClicked());
		SignUp.setBounds(328, 248, 98, 29);
		frame.getContentPane().add(SignUp);
		
		JButton LogIn = new JButton("Log In");
		LogIn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		LogIn.addActionListener(new LogInClicked());
		LogIn.setBounds(209, 248, 98, 29);
		frame.getContentPane().add(LogIn);
		
		// Code below is from old main
		frame2 = new JFrame();
		frame2.getContentPane().setBackground(Color.BLACK);
		frame2.setResizable(false);
		frame2.setBackground(Color.BLACK);
		frame2.setBounds(100, 100, 450, 300);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Start Game");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			new Game();
			
			}
		});
		btnNewButton.setBounds(87, 178, 117, 29);
		frame2.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_2 = new JButton("Quit");
		btnNewButton_2.setBackground(Color.WHITE);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton_2.setBounds(244, 178, 117, 29);
		frame2.getContentPane().add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("Brick Breaker ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 20));
		lblNewLabel.setBounds(112, 46, 212, 16);
		frame2.getContentPane().add(lblNewLabel);
		
		// Code above is from old main
		
	}
	
	
	
	private class SignUpClicked implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			try
			{	
				SwingUtilities.updateComponentTreeUI(frame);
				
				Connection con = DriverManager.getConnection(url);
				Statement st = con.createStatement();
				
				String query = "INSERT INTO HighScores VALUES ('"+usernameTxtFld.getText()+"','"+PasswordTxtFld.getText()+"', 0, 0);";
				
				st.executeUpdate(query);
				
				JLabel welcome = new JLabel("Welcome New User, " + usernameTxtFld.getText() + "!");
				welcome.setFont(new Font("Tahoma", Font.PLAIN, 18));
				welcome.setBounds(209, 315, 467, 71);
				frame.getContentPane().add(welcome);
				
				SwingUtilities.updateComponentTreeUI(frame);
				
				String query1 = "UPDATE HighScores SET Score = 0 WHERE Username = '"+usernameTxtFld.getText()+"';";
				st.executeUpdate(query1);
				
				// This to to open the game menu
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Database window = new Database();
							window.frame2.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				con.close();
			}
			
			catch (SQLException e2)
			{
				SwingUtilities.updateComponentTreeUI(frame);
				
				JTextPane SignUpError = new JTextPane();
				SignUpError.setBackground(SystemColor.control);
				SignUpError.setText("Please enter username AND password.\nIf you already have an account please click \"Log In\" instead.");
				SignUpError.setFont(new Font("Tahoma", Font.PLAIN, 18));
				SignUpError.setBounds(102, 300, 468, 80);
				frame.getContentPane().add(SignUpError);
				
				SwingUtilities.updateComponentTreeUI(frame);
				
				e2.printStackTrace();
			}
		}
	}
	
	private class LogInClicked implements ActionListener
	{	
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			try
			{	
				SwingUtilities.updateComponentTreeUI(frame);
				
				Connection con = DriverManager.getConnection(url);
				PreparedStatement ps = con.prepareStatement("SELECT Username, Password FROM HighScores WHERE Username = ? AND Password = ?;");
				ps.setString(1, usernameTxtFld.getText());
				ps.setString(2, PasswordTxtFld.getText());
				ResultSet result = ps.executeQuery();
				
				if(result.next())
				{
					SwingUtilities.updateComponentTreeUI(frame);
					
					JLabel welcome = new JLabel("Welcome Back, " + usernameTxtFld.getText() + "!");
					welcome.setFont(new Font("Tahoma", Font.PLAIN, 18));
					welcome.setBounds(209, 315, 467, 71);
					frame.getContentPane().add(welcome);
					
					SwingUtilities.updateComponentTreeUI(frame);
					
					// This to to open the game menu
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Database window = new Database();
								window.frame2.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
					con.close();
				}
				
				else
				{
					SwingUtilities.updateComponentTreeUI(frame);
					
					JTextPane SignUpError = new JTextPane();
					SignUpError.setBackground(SystemColor.control);
					SignUpError.setText("Please enter username AND password.\nIf you don't have an account please click \"Sign Up\" instead.");
					SignUpError.setFont(new Font("Tahoma", Font.PLAIN, 18));
					SignUpError.setBounds(102, 300, 468, 80);
					frame.getContentPane().add(SignUpError);
							
					SwingUtilities.updateComponentTreeUI(frame);
				}
				
				con.close();
			}
			
			catch(Exception e1)
			{	
				Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e1);
			}
		}
	}

	public String get()
	{		
		return usernameTxtFld.getText();
	}
}