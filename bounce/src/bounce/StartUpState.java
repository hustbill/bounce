package bounce;


import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the bounce counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {
	
	  private static final int PWIDTH = 800;   // size of panel
	  private static final int PHEIGHT = 400; 

	  private static final int NO_DELAYS_PER_YIELD = 16;
	  /* Number of frames with a delay of 0 ms before the animation thread yields
	     to other running threads. */
	  private static final int MAX_FRAME_SKIPS = 5;
	    // no. of frames that can be skipped in any one animation loop
	    // i.e the games state is updated but not rendered

	  // images, clips loader information files
	  private static final String IMS_INFO = "imsInfo.txt";
	  private static final String SNDS_FILE = "clipsInfo.txt";

	  // light blue for the background
	  private static final Color lightBlue = new Color(0.17f, 0.87f, 1.0f);

	  private Thread animator;           // the thread that performs the animation
	  private volatile boolean running = false;   // used to stop the animation thread
	  private volatile boolean isPaused = false;

	  private long period;                // period between drawing in _nanosecs_
	  private int count =0 ; // the steps count of player move.

	  private ClipsLoader clipsLoader;

	  // game entities
	  private WorldDisplay world;
	  private PlayerSprite player;
	  private AlienSprite aliens[]; 
	  

	  private long gameStartTime;   // when the game started
	  private int timeSpentInGame;

	  // used at game termination
	  private volatile boolean gameOver = false;
	  private int score = 0;

	  // for displaying messages
	  private Font msgsFont;
	  private FontMetrics metrics;

	  // off-screen rendering
	  private Graphics dbg; 
	  private org.newdawn.slick.Image dbImage = null;

	  // to display the title/help screen
	  private boolean showHelp;
	  private BufferedImage helpIm;

	  private GhostTiles alienTop;
	  private Random random ;
	  private SpriteSheet ss=  ResourceManager.getSpriteSheet(BounceGame.SPRITE_SHEET_RSC, 47, 60);
	  
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		  // initialise the loaders
	    ImagesLoader imsLoader = new ImagesLoader(IMS_INFO);
	    clipsLoader = new ClipsLoader(SNDS_FILE); 

	    // create the world, the player, and aliens
	    createWorld(imsLoader); 
	    
	 // prepare title/help screen
	    helpIm = imsLoader.getImage("title");
	    showHelp = true;    // show at start-up
	    isPaused = true;

	    // set up message showing stuff
	    msgsFont = new Font("SansSerif", Font.BOLD, 24);
	    //metrics = this.getFontMetrics(msgsFont);
	     random = new Random(); // for random change the sprite image.
	   
	}
	
	  private void createWorld(ImagesLoader imsLoader)
	  // create the game world, the player, and aliens
	  {
	    //world = new WorldDisplay(imsLoader, this);  // game world, a WorldDisplay object
		  world = new WorldDisplay(imsLoader);  // game world, a WorldDisplay object
	    player = new PlayerSprite(7,12, PWIDTH, PHEIGHT, clipsLoader, imsLoader, world, null);  // start on tile (7,12)
	    aliens = new AlienSprite[1];
	    aliens[0] = new AlienQuadSprite(10, 11, PWIDTH, PHEIGHT,  imsLoader, world);

	    //aliens[1] = new AlienAStarSprite(10, 11, PWIDTH, PHEIGHT, imsLoader, world);
	  //  aliens[0] = new AlienQuadSprite(6, 21, PWIDTH, PHEIGHT, imsLoader, world);
//	    aliens[2] = new AlienQuadSprite(14, 20, PWIDTH, PHEIGHT, imsLoader, world);
//	    aliens[3] = new AlienAStarSprite(34, 34, PWIDTH, PHEIGHT, imsLoader, world);
	       // use 2 AStar and 2 quad alien sprites
	       // the 4th alien is positioned at an illegal tile location (34,34)

	    world.addSprites(player, aliens);  // tell the world about the sprites
	  }  // end of createWor
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(false);
//		 long period = (long) 1000.0/60;
//		    // System.out.println("fps: " + DEFAULT_FPS + "; period: " + period + " ms");
//		   //new AlienTiles(period*1000000L);    // ms --> nanosecs 
//		    //new AlienTilesPanel( period);
//	
//		    // System.out.println("fps: " + DEFAULT_FPS + "; period: " + period + " ms");
//		    new GhostTiles(period*1000000L);    // ms --> nanosecs 
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
//		g.drawImage(ResourceManager.getImage(BounceGame.SURFACE_RSC),
//				10, 10);	
		

		
	
		//g.drawString("Bounces: ?", 10, 30);
		g.drawString("Lives Remaining: ?", 10, 30);
	
//		g.drawImage(ResourceManager.getImage(BounceGame.STARTUP_BANNER_RSC),
//				225, 270);	
		
		
//	    if (dbImage == null){
//	       // dbImage =  createImage(PWIDTH, PHEIGHT);
////			g.drawImage(ResourceManager.getImage(BounceGame.SURFACE_RSC),
////			10, 10);	
//	    	dbImage = ResourceManager.getImage(BounceGame.SURFACE_RSC);
//	    	
//	        if (dbImage == null) {
//	          System.out.println("dbImage is null");
//	          return;
//	        }
//	        else
//	          dbg = dbImage.getGraphics();
//	      }
//
//	      // a light blue background
//	   //  dbg.setColor(lightBlue);
//	    //dbg.setColor(Color.lightGray);
//	      dbg.fillRect(0, 0, PWIDTH/2, PHEIGHT/2);

	      // draw the game elements: order is important
	      world.draw(g);  
	     /* WorldDisplay draws the game world: the tile floor, blocks, 
	         pickups, and the sprites. */

	     // reportStats(dbg);

//	      if (gameOver)
//	        gameOverMessage(dbg);

//	      if (showHelp)    // draw the help at the very front (if switched on)
//	    	  g.drawImage(image, x, y);
//	        g.drawImage(helpIm, (PWIDTH-helpIm.getWidth())/2, 
//	                            (PHEIGHT-helpIm.getHeight())/2);
	      
	      player.drawSprite(g);
			//bg.ball.render(g);
			
//	    	Image bigImg = ResourceManager.getImage(BounceGame.SPRITE_SHEET_RSC);
//	        //	BufferedImage bigImg = ImageIO.read(new File("sheet.png"));
//	        	// The above line throws an checked IOException which must be caught.
//
//	        	final int width = 110;
//	        	final int height = 120;
//	        	final int rows = 5;
//	        	final int cols = 5;
//	        	Image[] sprites = new Image[rows * cols];
//
//	        	for (int i = 0; i < rows; i++)
//	        	{
//	        	    for (int j = 0; j < cols; j++)
//	        	    {
//	        	        sprites[(i * cols) + j] = bigImg.getSubImage(
//	        	            j * width,
//	        	            i * height,
//	        	            width,
//	        	            height
//	        	        );
//	        	       g.dr sprites[(i * cols) + j]
//	        	    }
//	        	}
	  	for (Bang b : bg.explosions)
			b.render(g);
	  	
	  	bg.ball.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
//		System.out.println("move north west");
//		  bg.ball.setX(bg.ball.getX() + 0.32f);   // move north west
//	         bg.ball.setY(bg.ball.getY() - 0.32f/2);
		
		
	  	
			
	  	//g.drawImage(ss.getSubImage(i, 0), bg.ball.getX(), bg.ball.getY());
		
		bg.ball.removeImage(ss.getSubImage(count%14, 0));
		
		 float x = (60*delta) /1000.0f;
//		 // move the player based on the numpad key pressed
		if (input.isKeyDown(Input.KEY_7)) {
			count++; 
			
			System.out.println("move north west");
	         bg.ball.setX(bg.ball.getX()- x);   // move north west
	         bg.ball.setY(bg.ball.getY() - x/2);
		}
	      else if (input.isKeyDown(Input.KEY_9)){
	       // player.move(TiledSprite.NE);   // north east
	    	  bg.ball.setX(bg.ball.getX()+ x);   // move north west
	         bg.ball.setY(bg.ball.getY() - x/2);
	         count++; 
	      }
	      else if  (input.isKeyDown(Input.KEY_3)){
//	        player.move(TiledSprite.SE);   // south east
	    	  bg.ball.setX(bg.ball.getX()+ x);   // move north west
	         bg.ball.setY(bg.ball.getY() + x/2);
	         count++; 
	      }
	      else if  (input.isKeyDown(Input.KEY_1)){
//	        player.move(TiledSprite.SW);   // south west
	    	  bg.ball.setX(bg.ball.getX()- x);   // move north west
		         bg.ball.setY(bg.ball.getY() + x/2);
		         count++; 
	      }
	      else if  (input.isKeyDown(Input.KEY_5)) {
//	        player.standStill();           // stand still
	    	  bg.ball.setX(bg.ball.getX());   // move north west
		         bg.ball.setY(bg.ball.getY() );
		         count++; 
	      }
		
		bg.ball.addImage(ss.getSubImage(count%14, 0));
		
//	      else if  (input.isKeyDown(Input.KEY_2))
//	        player.tryPickup();      // try to pick up from this tile
//
//		 player.updateSprite();
		if (input.isKeyDown(Input.KEY_SPACE))
			bg.enterState(BounceGame.PLAYINGSTATE);	
		
		// bounce the ball...
		boolean bounced = false;
		if((int)(bg.ball.getX() +x)%2 ==0 ) {
			bounced = true;
		}
		if((int)(bg.ball.getY() +x/2)%2 ==0 ) {
			bounced = true;
		}
		
		
//		if (bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth
//				|| bg.ball.getCoarseGrainedMinX() < 0) {
//			bg.ball.bounce(90);
//			bounced = true;
//		} else if (bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight
//				|| bg.ball.getCoarseGrainedMinY() < 0) {
//			bg.ball.bounce(0);
//			bounced = true;
//		}
		if (bounced) {
			
		
		
			//bg.explosions.add(new Bang(bg.ball.getX(),  bg.ball.getY()));
		}
		bg.ball.update(delta);

		// check if there are any finished explosions, if so remove them
//		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
//			if (!i.next().isActive()) {
//				i.remove();
//			}
//		}

	}

	@Override
	public int getID() {
		return BounceGame.STARTUPSTATE;
	}


	
}