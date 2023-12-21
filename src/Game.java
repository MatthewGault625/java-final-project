import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Game extends JFrame{

	private static final long serialVersionUID = 1L;
			Game()
			{
				//Gameplay gamePlay = new Gameplay();
				Gameplay3 funPlay = new Gameplay3();
			
			
			this.setBounds(10, 10, 700, 600);
			this.setTitle("Brick Breaker Game");		
			this.setResizable(false);
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//this.add(gamePlay); 
			this.add(funPlay);
	        this.setVisible(true);
	        
	        // Transparent 16 x 16 pixel cursor image.
	        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	        // Create a new blank cursor.
	        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
	        // Set the blank cursor to the JFrame.
	        this.getContentPane().setCursor(blankCursor);
			}
		}

