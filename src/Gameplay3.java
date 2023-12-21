import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.Timer;

public class Gameplay3 extends JPanel implements KeyListener, ActionListener, MouseMotionListener{
	
	private static final long serialVersionUID = 2L;
	public static final Color Light_Orange = new Color(255, 140, 0);
	private boolean play = false;
	protected int score = 0;
	
	private int totalBricks = 49;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 270;
	
	private int ballposX = 330;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	// Sound
	Sound sound = new Sound();
	
	// Plays music
	public void playMusic(int i)
	{
		sound.setFile(i);
		sound.play();
		sound.loop();
	}
	
	// Stops Music
	public void stopMusic() {
		sound.stop();
	}
	
	// Play Audio Clip
	public void playSFX(int i) {
		sound.setFile(i);
		sound.play();
	}
	
	// Constructor for the game
	public Gameplay3()
	{		
		map = new MapGenerator(7, 7);
		setFocusable(true);
		this.requestFocus();
		addKeyListener(this);
		setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
		timer.start();
		
		this.addMouseMotionListener(new MouseMotionListener() {
	        public void mouseMoved(MouseEvent e) {
	        	playerX = e.getX();
	        	if(playerX >= 600)
				{playerX = 590;}
	        	if(playerX < 10)
				{playerX = 10;}
	        }

			public void mouseDragged(MouseEvent e) {}
		});
	}
	
	public void paint(Graphics g)
	{    
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// drawing map
		map.draw((Graphics2D) g);
		
		// borders
		g.setColor(Light_Orange);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		// the scores 		
		g.setColor(Color.green);
		g.setFont(new Font("serif",Font.BOLD, 25));
		g.drawString(""+score, 590,30);
		
		// the paddle
		g.setColor(Light_Orange);
		g.fillRect(playerX, 550, 100, 8);
		
		// the ball
		g.setColor(Color.white);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		// when you start the level
		if(!play)
		{
             ballXdir = 0;
     		 ballYdir = 0;
             g.setColor(Color.LIGHT_GRAY);
             g.setFont(new Font("serif",Font.BOLD, 30));
             g.drawString("Welcome to Level 1", 230, 300);
             
             g.setColor(Color.LIGHT_GRAY);
             g.setFont(new Font("serif",Font.BOLD, 20));           
             g.drawString("Press (Enter) to Start the Game", 220, 350);
             playMusic(1); 
		}
	
		// when you won the game
		if(totalBricks <= 0)
		{
			 play = false;
             ballXdir = 0;
     		 ballYdir = 0;
             g.setColor(Color.LIGHT_GRAY);
             g.setFont(new Font("serif",Font.BOLD, 30));
             g.drawString("You Won", 260, 300);
             
             String url = "jdbc:sqlite:bin\\Users.db";
             
             Database current = new Database();
             String currentUser = current.get();
             
             try
             {
             	Connection con = DriverManager.getConnection(url);
             	Statement st = con.createStatement();
             	
             	String query = "UPDATE HighScores SET LastScore = '"+score+"' WHERE Username = '"+currentUser+"';";
             	st.executeUpdate(query);
 				
             	String query1 = "UPDATE HighScores SET Score = (CASE WHEN LastScore > Score THEN LastScore ELSE Score END) WHERE Username = '"+currentUser+"';";
             	st.executeUpdate(query1);
             	
             	con.close();
             }
             
             catch(SQLException e)
             {
             	e.printStackTrace();
             }
             
             g.setColor(Color.LIGHT_GRAY);
             g.setFont(new Font("serif",Font.BOLD, 20));           
             g.drawString("Press (Enter) to Restart", 230, 350); 
             stopMusic();
             
		}
		
		// when you lose the game
		if(ballposY > 570)
        {
			 play = false;
             ballXdir = 0;
     		 ballYdir = 0;
             g.setColor(Color.LIGHT_GRAY);
             g.setFont(new Font("serif",Font.BOLD, 30));
             g.drawString("Game Over, Scores: " + score, 190,300);
             
             String url = "jdbc:sqlite:bin\\Users.db";
             
             try
             {
            	Database current = new Database();
                String currentUser = current.get();
            	 
             	Connection con = DriverManager.getConnection(url);
             	Statement st = con.createStatement();
             	
             	System.out.print(currentUser);

             	String query = "UPDATE HighScores SET LastScore = '"+score+"' WHERE Username = '"+currentUser+"';";
             	st.executeUpdate(query);
 				
             	String query1 = "UPDATE HighScores SET Score = (CASE WHEN LastScore > Score THEN LastScore ELSE Score END) WHERE Username = '"+currentUser+"';";
             	st.executeUpdate(query1);
             	
             	con.close();
             }
             
             catch(SQLException e)
             {
             	e.printStackTrace();
             }
             
             g.setColor(Color.LIGHT_GRAY);
             g.setFont(new Font("serif",Font.BOLD, 20));           
             g.drawString("Press (Enter) to Restart", 230,350);  
             stopMusic();
        }
		
		g.dispose();
	}	
	
	// needed methods for MouseMotionListener
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	// if the user presses the ENTER key
	public void keyPressed(KeyEvent e) 
	{		
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{   
			if(!play)
			{
				play = true;
				ballposX = 325;
				ballposY = 325;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 270;
				score = 0;
				totalBricks = 49;
				map = new MapGenerator(7, 7);
				
				repaint();
			}
        }		
	}
	
	// needed methods for KeyListener
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	// ball collision with environment
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play) {	
			// check collision with the paddle
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 30, 8)))
			{
				ballYdir = -(ballYdir + 2);
				ballXdir = -4;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 550, 30, 8)))
			{
				ballYdir = -(ballYdir + 2);
				ballXdir = ballXdir + 3;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 550, 40, 8)))
			{
				ballYdir = -(ballYdir + 2);
			}
			// check map collision with the ball		
			A: for(int i = 0; i < map.map.length; i++) {
				  for(int j =0; j < map.map[0].length; j++) {				
					if(map.map[i][j] > 0) {
						// increment the score
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						if(ballRect.intersects(brickRect)) {					
							map.setBrickValue(0, i, j);
							score += 5;	
							totalBricks--;
							// when ball hit right or left of brick
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)
								{ballXdir = -ballXdir;
								playSFX(0);}
							// when ball hits top or bottom of brick
							else
								{ballYdir = -ballYdir;
								playSFX(0);}
							break A; } } } }
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0)
				{ballXdir = -ballXdir;}
			if(ballposY < 0)
				{ballYdir = -ballYdir;}
			if(ballposX > 670)
				{ballXdir = -ballXdir;}		
			repaint(); } }
	
	public int getScore() {
		return this.score;
	}
	
}

