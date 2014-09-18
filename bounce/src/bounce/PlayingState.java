package bounce;

import java.util.Iterator;

import jig.Entity;
import jig.ResourceManager;
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
	int lives = 3;
	int bounces;
	int levels = 1;
	int scores = 0;
	float radius = 16.0f; // ball radius

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		BounceGame bg = (BounceGame) game;

		if (levels <= 4) {
			lives = 3; // reset the lives to 3 for each level
			ResourceManager.getSound(BounceGame.START_GAME_RSC).play();
			bg.brick.configBricks(game, levels);
			bg.paddle.configPaddle(game, levels);
			bg.ball.configBall(game, levels);
			bg.coin.configBonus(game, levels);
		}
		// System.out.println("init playingState, initial lives=" + lives);
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
		// bg.coin.render(g);
		for (Brick bk : bg.bricks)
			bk.render(g);
		for (Bonus cn : bg.coins)
			cn.render(g);

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
		// If all bricks are destroyed, go to next level or enter ConfigState to
		// check score
		if (bg.bricks.size() == 0) {
			levels++;
			if (levels <= 4) {
				// System.out.println("Congratulations! Go to next level!");
				init(container, game);

			} else {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bg.enterState(BounceGame.CONFIGSTATE);// enter ConfigState to
														// check score
			}
		}
		bg.ball.controlBall(input, bg); // control velocity of ball by press Keys
		bg.paddle.controlPaddle(input,bg);
		if (input.isKeyDown(Input.KEY_HOME))
			bg.enterState(BounceGame.CONFIGSTATE);

		// bounce the ball...
		boolean bounced = false;
		// System.out.println("***** check point 1 begin *********");
		// System.out.println("ball.x = "+ bg.ball.getX() + "ball.y =" +
		// bg.ball.getY());
		// System.out.println("bg.ball.getCoarseGrainedMinY()="
		// +bg.ball.getCoarseGrainedMinY());
		// System.out.println("bg.ball.getCoarseGrainedMaxY()="
		// +bg.ball.getCoarseGrainedMaxY());
		// System.out.println("ball.vx= " + bg.ball.getVelocity().getX());
		// System.out.println("ball.vY= " + bg.ball.getVelocity().getY());
		// System.out.println("***** check point 1 end *********");
		if ((bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth && bg.ball
				.getVelocity().getX() > 0)
				|| (bg.ball.getCoarseGrainedMinX() < 0 && bg.ball.getVelocity()
						.getX() < 0)) {
			bg.ball.bounce(90);
			bounced = true;
		} else if (bg.ball.getCoarseGrainedMinY() < 0
				&& bg.ball.getVelocity().getY() < 0) {
			bg.ball.bounce(0);
			bounced = true;
		}

		if (bg.ball.getY() > bg.ScreenHeight + radius
				&& bg.ball.getVelocity().getY() > 0) {
			lives--;
			// reset position & velocity of paddle and ball
			bg.paddle.configPaddle(game, levels); 
			bg.ball.configBall(game, levels);
			//bg.coin.configBonus(game, levels);
			
			// add music or audio here to notify player who lost one life
		}
		if (bounced) {
			bg.explosions.add(new Bang(bg.ball.getX(), bg.ball.getY()));
			bounces++;
			System.out.println("bounces= " + bounces);
		}
		bg.ball.update(delta);

		for (int i = 0; i < bg.coins.size(); i++) {
			Bonus cn = bg.coins.get(i);
			cn.setVelocity(new Vector(-0.015f * i, 0.01f * i));
			cn.update(delta); // make coin fly
			
		}
		// detect the collision between coin and paddle
		for (int i = 0; i < bg.coins.size(); i++) {
			Bonus cn = bg.coins.get(i);		
			if (detectCollision(cn, bg.paddle)) {
				bg.coins.remove(cn); // remove coin from ArrayList
				ResourceManager.getSound(BounceGame.PICKED_COIN_RSC).play();
				//coin sound http://www.freesound.org/people/NenadSimic/sounds/171696/
				scores += 10; // one coin equals 10 points
			}else{
				if(cn.getCoarseGrainedMaxX() > bg.ScreenWidth ||
						cn.getCoarseGrainedMinX() <0 ||
						cn.getCoarseGrainedMaxY() > bg.ScreenHeight) {
					bg.coins.remove(cn);					
				}
			}
		}

		// move the paddle ...
		if (bg.paddle.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.paddle.getCoarseGrainedMinX() < 0) {
			bg.paddle.bounce(90);

		}
		// detect the collision between ball and paddle
		if (detectCollision(bg.ball, bg.paddle)) {
			bg.ball.bounce(180);
			bg.ball.setY(bg.paddle.getY() - radius * 4 / 3);	
			bg.ball.powerUp(bg);
			//add powerUp sound 
			ResourceManager.getSound(BounceGame.GET_POWERUP_RSC).play();
			bounced = true;
		}
		bg.paddle.update(delta);

	

		// detect the collision between ball and bricks
		for (int i = 0; i < bg.bricks.size(); i++) {
			Brick bk = bg.bricks.get(i);
			if (detectCollision(bg.ball, bk)) {  //treat brick as a paddle to detect collision
				bg.ball.bounce(180);			
				bounced = true;
				if (levels == 1) {
					bg.bricks.remove(bk); // remove brick from ArrayList
					ResourceManager.getSound(BounceGame.DROP_BRICK_RSC).play();
					scores++;
				} else {
					bk.update(levels); // destroy the brick
					bk.hit_times++;
					System.out.println("levels= " + levels);
				}
				if (bk.hit_times == 2) { // to destroy pig, fish, zombie, need
											// hit them twice
					bg.bricks.remove(bk);
					ResourceManager.getSound(BounceGame.DROP_BRICK_RSC).play();
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
			((GameOverState) game.getState(BounceGame.GAMEOVERSTATE))
					.setUserScore(lives);
			game.enterState(BounceGame.GAMEOVERSTATE);
		}
	}

	/*
	 * collision detection between ball, coin and paddle
	 */
	public boolean detectCollision(Entity coin, Entity paddle) {
		float delta_x = coin.getX() - paddle.getX();
		float delta_y = coin.getY() - paddle.getY();

		if (Math.sqrt(delta_y * delta_y + delta_x * delta_x) < radius
				+ paddle.getCoarseGrainedWidth()) {
			if (Math.abs(delta_x) < (radius + paddle.getCoarseGrainedWidth() / 2 )
					&& Math.abs(delta_y) < ( radius + paddle.getCoarseGrainedHeight() / 2)) {
				System.out.println("collision detected! ");
				return true;
			} else
				return false;
		} else
			return false;

	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}

}