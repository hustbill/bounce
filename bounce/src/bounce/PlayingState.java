package bounce;

import java.io.File;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	int lives =3;
	int bounces;
	int levels = 1;
	int scores = 0;
	float radius = 16.0f; // ball radius

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		BounceGame bg = (BounceGame) game;
		if (levels <= 4) {
			bg.brick.configBricks(game, levels);
			bg.paddle.configPaddle(game, levels);
			bg.ball.configBall(game, levels);
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		lives = 3;
		bounces = 0;
		levels = 1;
		container.setSoundOn(true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		BounceGame bg = (BounceGame) game;

		bg.ball.render(g);
		bg.paddle.render(g);
		for (Brick bk : bg.bricks)
			bk.render(g);

		g.drawString("Lives Remaining: " + lives, 10, 30);
		g.drawString("Scores: " + scores, 20, bg.ScreenHeight - 25);
		g.drawString("Levels: " + levels, bg.ScreenWidth - 90,
				bg.ScreenHeight - 25);
		for (Bang b : bg.explosions)
			b.render(g);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame) game;

		//If all bricks a
		if (bg.bricks.size() == 0) {
			levels++;
			if (levels <= 4) {
				// System.out.println("Congratulations! Go to next level!");
				init(container, game);

			} else {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bg.enterState(BounceGame.CONFIGSTATE);
				levels = 1; // reset levels to initial level
			}
		}

		if (input.isKeyDown(Input.KEY_W)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(0f, -.001f)));
		}
		if (input.isKeyDown(Input.KEY_S)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(0f, +.001f)));
		}
		if (input.isKeyDown(Input.KEY_A)) {
			bg.ball.setVelocity(bg.ball.getVelocity()
					.add(new Vector(-.001f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(+.001f, 0f)));
		}

		if (input.isKeyDown(Input.KEY_N)) {
//			bg.ball.setX( 545.4f);
//			bg.ball.setY(-8.2f);
			bg.ball.setX(400.0f);
			bg.ball.setY(580.0f);
		}
		
		// Press C to save your ball
		if (input.isKeyDown(Input.KEY_C)) {
			//check point 1
//			bg.ball.setX( 545.4f);
//			bg.ball.setY(-8.2f);
			
			// check point 2
//			bg.ball.setX(-10.0f);
//			bg.ball.setY(288.2f);
			
			// check point 3
//			bg.ball.setX(378.0f);
//			bg.ball.setY(11.0f);
			
			// check point 4
//			bg.ball.setX(423.0f);
//			bg.ball.setY(613.0f);			
	
			bg.paddle.setX(bg.ball.getX());	
			bg.paddle.setY(bg.ball.getY());
//			 if( bg.ball.getCoarseGrainedMinY() > bg.ScreenHeight /2)
//			 bg.paddle.setY(bg.ball.getY()+ radius*2 );
//			 if( bg.ball.getCoarseGrainedMinY() < bg.ScreenHeight /2)
//			 bg.paddle.setY(bg.ball.getY()- radius*2);

		}
		// Cheat codes to allow user to access all of levels by press "P"
		if (input.isKeyDown(Input.KEY_P)) {
			for (int i = 0; i < bg.bricks.size(); i++) {
				Brick bk = bg.bricks.get(i);
				bg.ball.setY(bk.getY());
				bg.ball.setX(bk.getX());
			}
		}
		if (input.isKeyDown(Input.KEY_H)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(
					new Vector(-.005f, 0)));
		}
		if (input.isKeyDown(Input.KEY_L)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(
					new Vector(+.005f, 0f)));
		}

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			bg.ball.bounce(90);
		}

		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			bg.ball.bounce(180);

		}
		if (input.isKeyDown(Input.KEY_ADD)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(+.001f, 0f)));

		}
		if (input.isKeyDown(Input.KEY_0)) {
			bg.ball.scale(0.5f);
		}

		if (input.isKeyDown(Input.KEY_1)) {
			bg.ball.scale(1.5f);

		}

		if (input.isKeyDown(Input.KEY_HOME))
			bg.enterState(BounceGame.CONFIGSTATE);

		// bounce the ball...
		boolean bounced = false;
//		if (bg.ball.getCoarseGrainedMaxX() > (bg.ScreenWidth -radius)
//				|| bg.ball.getCoarseGrainedMinX() < radius) {
//			bg.ball.bounce(90);		
//			System.out.println("Object enter into target area -0");
//			bounced = true;
//		} else if (bg.ball.getCoarseGrainedMaxY() > (bg.ScreenHeight - radius)
//				|| bg.ball.getCoarseGrainedMinY() < radius) {
//			bg.ball.setVelocity(new Vector( bg.ball.getVelocity().getX(),
//					-bg.ball.getVelocity().getY())); // );
//			System.out.println("Object enter into target area -1");
//			bg.ball.bounce(0);
//			bounced = true;
//		}
		int bounces_x_count =0;
		int bounces_y_count =0;
		System.out.println("***** check point 1 begin *********");
		System.out.println("ball.x = "+  bg.ball.getX() + "ball.y =" + bg.ball.getY());
		System.out.println("bg.ball.getCoarseGrainedMinY()=" +bg.ball.getCoarseGrainedMinY());
		System.out.println("bg.ball.getCoarseGrainedMaxY()=" +bg.ball.getCoarseGrainedMaxY());
		System.out.println("ball.vx= " + bg.ball.getVelocity().getX());
		System.out.println("ball.vY= " + bg.ball.getVelocity().getY());
		System.out.println("***** check point 1 end *********");
		if ((bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth && bg.ball.getVelocity().getX()>0)		
				|| ( bg.ball.getCoarseGrainedMinX() < 0 && bg.ball.getVelocity().getX()<0)) {
			bg.ball.bounce(90);
			bounces_x_count++;		
			bounced = true;
		} else if ((bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight && bg.ball.getVelocity().getY()>0)				
				|| (bg.ball.getCoarseGrainedMinY() < 0 && bg.ball.getVelocity().getY()<0)) {
			System.out.println("Before ball.vx= " + bg.ball.getVelocity().getX());
			System.out.println("Before ball.vY= " + bg.ball.getVelocity().getY());
			bg.ball.bounce(0);
			System.out.println("After ball.vx= " + bg.ball.getVelocity().getX());
			System.out.println("After ball.vY= " + bg.ball.getVelocity().getY());		
			bounced = true;
		}
		if (bounced) {
			bg.explosions.add(new Bang(bg.ball.getX(), bg.ball.getY()));
			bounces++;
			System.out.println("bounces= " + bounces);
			if (bounces == 10 || bounces == 20 || bounces == 30) {
				lives--;
				System.out.println("lives= " + lives);
			}
		}
		bg.ball.update(delta);

		// move the paddle ...
		if (bg.paddle.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.paddle.getCoarseGrainedMinX() < 0) {
			bg.paddle.bounce(90);
			bounced = true;
		}
		// detect the collision between ball and paddle
		if (detectCollision_paddle(bg.ball.getX(), bg.ball.getY(),
				bg.paddle.getX(), bg.paddle.getY(),
				bg.paddle.getCoarseGrainedHeight(),
				bg.paddle.getCoarseGrainedWidth(), radius)) {
			bg.ball.bounce(180);
			// bg.ball.setX(bg.paddle.getX() +
			// bg.paddle.getCoarseGrainedHeight()/2);
			bg.ball.setY(bg.paddle.getY() - radius * 4 / 3);
			bounced = true;
		}
		bg.paddle.update(delta);

		// detect the collision between ball and bricks
		for (int i = 0; i < bg.bricks.size(); i++) {
			Brick bk = bg.bricks.get(i);
			if (detectCollision_brick(bg.ball.getX(), bg.ball.getY(),
					bk.getX(), bk.getY(), bk.getCoarseGrainedHeight(),
					bk.getCoarseGrainedWidth(), radius)) {
				bg.ball.bounce(180);
				bounced = true;
				if (levels == 1) {
					bg.bricks.remove(bk); // remove brick from ArrayList
					scores++;
				} else {
					bk.update(levels); // destroy the brick
					bk.hit_times++;
					System.out.println("levels= " + levels);
				}
				if (bk.hit_times == 2) {
					bg.bricks.remove(bk);
					scores += 2;
				}
			}
		}

		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}

		if (lives == 0) {
			System.out.println("ball.x = "+  bg.ball.getX() + "ball.y =" + bg.ball.getY());
			System.out.println("ball.vx= " + bg.ball.getVelocity().getX());
			System.out.println("ball.vY= " + bg.ball.getVelocity().getY());
			System.out.println("bg.ball.getCoarseGrainedMinY()=" +bg.ball.getCoarseGrainedMinY());
			System.out.println("bg.ball.getCoarseGrainedMaxY()=" +bg.ball.getCoarseGrainedMaxY());
			
			((GameOverState) game.getState(BounceGame.GAMEOVERSTATE))
					.setUserScore(lives);
			game.enterState(BounceGame.GAMEOVERSTATE);
		}
	}

	// collision detection between paddle and ball
	public boolean detectCollision_paddle(float ball_x, float ball_y,
			float paddle_x, float paddle_y, float paddle_height,
			float paddle_length, float radius) {
	
		// System.out.println("ball_x=" + ball_x);
		// System.out.println("ball_y=" + ball_y);
		float delta_x = ball_x - paddle_x;
		float delta_y = ball_y - paddle_y;
		// System.out.println("paddle_x=" + paddle_x);
		// System.out.println("paddle_y=" + paddle_y);
		if (Math.abs(delta_x) < (paddle_length / 2 + radius)
				&& Math.abs(delta_y) < (paddle_height / 2 + radius)) {
			System.out.println("collision with paddle!\n");
			System.out.println("delta_x = " + Math.abs(delta_x));
			System.out.println("delta_y = " + Math.abs(delta_y));
			return true;
		}
		else
			return false;
	}

	// collision detection between ball and bricks
	public boolean detectCollision_brick(float ball_x, float ball_y,
			float brick_x, float brick_y, float brick_height,
			float brick_length, float radius) {
		// public boolean detectCollision( float ball_x, float ball_y, float
		// brick_x, float brick_y) {
		float delta_x = ball_x - brick_x;
		float delta_y = ball_y - brick_y;

		if (Math.abs(delta_x) < (brick_length / 2 + radius)
				&& Math.abs(delta_y) < (brick_height / 2 + radius)) {
			// System.out.println("collision with brick!\n");
			// System.out.println("delta_x = " + Math.abs(delta_x));
			// System.out.println("delta_y = " + Math.abs(delta_y));
			return true;
		}

		else
			return false;
	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}

}