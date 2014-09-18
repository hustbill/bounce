package bounce;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A Simple Game of Bounce.
 * 
 * The game has three states: StartUp, Playing, and GameOver, the game
 * progresses through these states based on the user's input and the events that
 * occur. Each state is modestly different in terms of what is displayed and
 * what input is accepted.
 * 
 * In the playing state, our game displays a moving rectangular "ball" that
 * bounces off the sides of the game container. The ball can be controlled by
 * input from the user.
 * 
 * When the ball bounces, it appears broken for a short time afterwards and an
 * explosion animation is played at the impact site to add a bit of eye-candy
 * additionally, we play a short explosion sound effect when the game is
 * actively being played.
 * 
 * Our game also tracks the number of bounces and syncs the game update loop
 * with the monitor's refresh rate.
 * 
 * Graphics resources courtesy of qubodup:
 * http://opengameart.org/content/bomb-explosion-animation
 * 
 * Sound resources courtesy of DJ Chronos:
 * http://www.freesound.org/people/DJ%20Chronos/sounds/123236/
 * 
 * 
 * @author wallaces
 * 
 */
public class BounceGame extends StateBasedGame {
	
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int CONFIGSTATE =3;
	
	public static final String BRICK_RSC = "bounce/resource/brick.png";
	public static final String PIG_RSC = "bounce/resource/pig.png";
	public static final String FISH_RSC = "bounce/resource/fish.png";
	public static final String ZOMBIE_RSC = "bounce/resource/zombie.png";
	public static final String SKULL_RSC = "bounce/resource/skull.png";	
	public static final String COIN_RSC = "bounce/resource/coin.png";
	
	public static final String PADDLE_RSC = "bounce/resource/paddle.png";
	public static final String BALL_BALLIMG_RSC = "bounce/resource/ball.png";	
	public static final String BALL_BROKENIMG_RSC = "bounce/resource/brokenball.png";
	public static final String YOUWIN_BANNER_RSC = "bounce/resource/youwin.png";
	public static final String GAMEOVER_BANNER_RSC = "bounce/resource/gameover.png";
	public static final String STARTUP_BANNER_RSC = "bounce/resource/PressSpace.png";
	public static final String BANG_EXPLOSIONIMG_RSC = "bounce/resource/explosion.png";
	public static final String BANG_EXPLOSIONSND_RSC = "bounce/resource/explosion.wav";
	public static final String START_GAME_RSC = "bounce/resource/gameStart.ogg";
	public static final String GET_POWERUP_RSC=  "bounce/resource/getPowerup.wav";
	public static final String PICKED_COIN_RSC =  "bounce/resource/pickedCoin.wav";
	public static final String DROP_BRICK_RSC =  "bounce/resource/dropBrick.wav";
	// Sound resources courtesy of plasterbrain__game-start
	// http://www.freesound.org/people/plasterbrain/sounds/243020/

	public final int ScreenWidth;
	public final int ScreenHeight;
	public int levels =1;

	Ball ball;
	Paddle paddle;
	Brick brick;
	Bonus coin;
	
	ArrayList<Bonus> coins;
	ArrayList<Brick> bricks;
	ArrayList<Bang> explosions;

	/**
	 * Create the BounceGame frame, saving the width and height for later use.
	 * 
	 * @param title
	 *            the window's title
	 * @param width
	 *            the window's width
	 * @param height
	 *            the window's height
	 */
	public BounceGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		explosions = new ArrayList<Bang>(10);
		bricks = new ArrayList<Brick>(10);
		coins = new ArrayList<Bonus>(10);
	
	
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());
		addState(new ConfigState());
		// the sound resource takes a particularly long time to load,
		// we preload it here to (1) reduce latency when we first play it
		// and (2) because loading it will load the audio libraries and
		// unless that is done now, we can't *disable* sound as we
		// attempt to do in the startUp() method.
		ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);	
		ResourceManager.loadSound(START_GAME_RSC);
		ResourceManager.loadSound(GET_POWERUP_RSC);
		ResourceManager.loadSound(PICKED_COIN_RSC);
		ResourceManager.loadSound(DROP_BRICK_RSC);

		// preload all the resources to avoid warnings & minimize latency...
		ResourceManager.loadImage(PADDLE_RSC);
		ResourceManager.loadImage(BRICK_RSC);	
		ResourceManager.loadImage(COIN_RSC);
		ResourceManager.loadImage(ZOMBIE_RSC);
		ResourceManager.loadImage(PIG_RSC);
		ResourceManager.loadImage(FISH_RSC);
		ResourceManager.loadImage(SKULL_RSC);
		ResourceManager.loadImage(BALL_BALLIMG_RSC);
		ResourceManager.loadImage(BALL_BROKENIMG_RSC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(STARTUP_BANNER_RSC);
		ResourceManager.loadImage(YOUWIN_BANNER_RSC);
		ResourceManager.loadImage(BANG_EXPLOSIONIMG_RSC);
		
		
		ball = new Ball(ScreenWidth / 2, ScreenHeight -60, .1f, -.12f);
		paddle = new Paddle(ScreenWidth / 2, ScreenHeight -40  , .0f, .0f, 90.0f);
		brick = new Brick(ScreenWidth / 7 , ScreenHeight * 1/5 );
		coin = new Bonus(300.0f, 32.0f, -.01f, 0.01f);

	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new BounceGame("Bounce!", 800, 600));
			app.setDisplayMode(800, 600, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	
}
