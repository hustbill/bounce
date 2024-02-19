package bounce;
import java.awt.BorderLayout;  
import java.awt.Canvas;  
import java.awt.Dimension;  
import java.awt.Graphics;  
import java.awt.image.BufferStrategy;  
import java.awt.image.BufferedImage;  
import java.awt.image.DataBufferInt;  
  

import javax.swing.JFrame;  

import jig.ResourceManager;
  
public class Game extends Canvas implements Runnable {  
    private static final long serialVersionUID = 1L;  
  
    public static final String NAME = "Pixel Example";  
    public static final int HEIGHT = 240;  
    public static final int WIDTH = HEIGHT * 16 / 9;  
    public static final int SCALE = 2;  
  
  //  private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);  
  //  private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();  
    private boolean running = false;  
  
    public void start() {  
        running = true;  
        new Thread(this).start();  
    }  
  
    public void stop() {  
        running = false;  
    }  
  
    public void run() {  
        while (running) {  
            tick();  
            render();  
        }  
    }  
  
    int tickCount;  
      
    public void tick() {  
        tickCount++;  
    }  
    
    public void createSpriteSheet() {
//    	BufferedImage bigImg = ResourceManager.getImage(BounceGame.SPRITE_SHEET_RSC);
//    //	BufferedImage bigImg = ImageIO.read(new File("sheet.png"));
//    	// The above line throws an checked IOException which must be caught.
//
//    	final int width = 10;
//    	final int height = 10;
//    	final int rows = 5;
//    	final int cols = 5;
//    	BufferedImage[] sprites = new BufferedImage[rows * cols];
//
//    	for (int i = 0; i < rows; i++)
//    	{
//    	    for (int j = 0; j < cols; j++)
//    	    {
//    	        sprites[(i * cols) + j] = bigImg.getSubimage(
//    	            j * width,
//    	            i * height,
//    	            width,
//    	            height
//    	        );
//    	    }
//    	}
    }
    
    
  
    public void render() {  
        BufferStrategy bs = getBufferStrategy();  
        if (bs == null) {  
            createBufferStrategy(3);  
            return;  
        }  
          
//        for (int i = 0; i < pixels.length; i++) {  
//            pixels[i] = i + tickCount;  
//        }  
          
        Graphics g = bs.getDrawGraphics();  
        //g.drawImage(image, 0, 0, getWidth(), getHeight(), null);  
       // g.draw3DRect(30, 20, 200, 320, true);
        g.dispose();  
        bs.show();  
    }  
      
    public static void main(String[] args) {  
        Game game = new Game();  
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));  
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));  
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));  
          
        JFrame frame = new JFrame(NAME);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setLayout(new BorderLayout());  
        frame.add(game);  
        frame.pack();  
        frame.setResizable(false);  
        frame.setLocationRelativeTo(null);  
        frame.setVisible(true);  
          
        game.start();  
    }  
}  