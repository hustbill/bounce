package bounce;

import java.util.Iterator;

import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	int bounces;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		
		bg.ball.render(g);
		bg.paddle.render(g);
		
		for( Brick bk : bg.bricks)
			bk.render(g);
		
	
		g.drawString("Bounces: " + bounces, 10, 30);
		for (Bang b : bg.explosions)
			b.render(g);

	
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
		
//		if (input.isKeyDown(Input.KEY_W)) {
//			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, -.001f)));
//		}
//		if (input.isKeyDown(Input.KEY_S)) {
//			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(0f, +.001f)));
//		}
//		if (input.isKeyDown(Input.KEY_A)) {
//			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(-.001f, 0)));
//		}
//		if (input.isKeyDown(Input.KEY_D)) {
//			bg.ball.setVelocity(bg.ball.getVelocity().add(new Vector(+.001f, 0f)));
//		}
		
		if (input.isKeyDown(Input.KEY_A)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(-.002f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(+.002f, 0f)));
		}
		
		// bounce the ball...
		boolean bounced = false;
		if (bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.ball.getCoarseGrainedMinX() < 0) {
			bg.ball.bounce(90);
			bounced = true;
		} else if (bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight
				|| bg.ball.getCoarseGrainedMinY() < 0) {
			bg.ball.bounce(0);
			bounced = true;
		}
		if (bounced) {
			bg.explosions.add(new Bang(bg.ball.getX(), bg.ball.getY()));
			bounces++;
		}
		bg.ball.update(delta);
		
		// move the paddle ...
		if (bg.paddle.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.paddle.getCoarseGrainedMinX() < 0) {
			bg.paddle.bounce(90);
			bounced = true;
		}
		// detect the collision between ball and paddle
		if(detectCollision_paddle(bg.ball.getX() ,
				bg.ball.getY(), 
				bg.paddle.getX(), bg.paddle.getY(),
				bg.paddle.getCoarseGrainedHeight(),				 
				bg.paddle.getCoarseGrainedWidth(), 	16.0f)) {
			bg.ball.bounce(180);
			bounced = true;
		}
		bg.paddle.update(delta);
		
		// detect the collosion between ball and bricks
		for( int i=0; i< bg.bricks.size(); i++) {
			Brick bk = bg.bricks.get(i);
			if(detectCollision_brick(bg.ball.getX() ,
					bg.ball.getY(), 
					bk.getX(), bk.getY(),
					bk.getCoarseGrainedHeight(),				 
					bk.getCoarseGrainedWidth(), 16.0f)) {
				bg.ball.bounce(180);
				bounced = true;
				bk.update(delta); //destroy the brick				
				bg.bricks.remove(bk); //remove brick from ArrayList
			}	
		}
		if (bg.bricks.size() == 0) {
			//bg.enterState(BounceGame.PLAYINGSTATE);	//restart the game or go to next level
//			((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
//			game.enterState(BounceGame.GAMEOVERSTATE);
			System.out.println("Congratulations! You can go to the next level! ");
		}

		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}

		if (bounces >= 30) {
			((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
			game.enterState(BounceGame.GAMEOVERSTATE);
		}
	}
	
	// collision detection between paddle and ball
	public boolean detectCollision_paddle( float ball_x, float ball_y,  
							  	float paddle_x,	float paddle_y,
							float paddle_height, float paddle_length,
							float radius) {
	//public boolean detectCollision( float ball_x, float ball_y, float paddle_x, float paddle_y) {
		float delta_x = ball_x -paddle_x;
		float delta_y = ball_y -paddle_y;
		if(Math.abs(delta_x) < (paddle_length/2 + radius)
				&& Math.abs(delta_y) < (paddle_height/2 +radius ) ) {
			//System.out.println("radius = " + radius);
			System.out.println("collision with paddle!\n");
//			System.out.println("delta_x = " + Math.abs(delta_x));
//			System.out.println("delta_y = " + Math.abs(delta_y));
			return true ;
		}
				
		else 
			return false;
	}
	
	
	// collision detection between ball and bricks
	public boolean detectCollision_brick( float ball_x, float ball_y,  
							  	float brick_x,	float brick_y,
							float brick_height, float brick_length,
							float radius) {
	//public boolean detectCollision( float ball_x, float ball_y, float brick_x, float brick_y) {
		float delta_x = ball_x -brick_x;
		float delta_y = ball_y -brick_y;
		if(Math.abs(delta_x) < (brick_length/2 + radius)
				&& Math.abs(delta_y) < (brick_height/2 +radius ) ) {			
			System.out.println("collision with brick!\n");
//			System.out.println("delta_x = " + Math.abs(delta_x));
//			System.out.println("delta_y = " + Math.abs(delta_y));
			return true ;
		}
				
		else 
			return false;
	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}
	
}