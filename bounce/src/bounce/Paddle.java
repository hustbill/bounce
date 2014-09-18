package bounce;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The Paddle class is an Entity that has a velocity (since it's moving). User
 * can control a paddle to influences the path of ball
 * 
 * @author Hua Zhang
 */

public class Paddle extends Entity {

	private Vector velocity;
	private int countdown;
	public float angle;

	public Paddle(final float x, final float y, final float vx, float vy,
			float angle_degree) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(BounceGame.PADDLE_RSC));
		vy = 0.0f;
		velocity = new Vector(vx, vy);
		countdown = 0;
		angle = angle_degree;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setAngle(final float angle_num) {
		angle = angle_num;
	}

	public float getAngle() {
		return angle;
	}

	/*
	 * Control the velocity and location of paddle in playingState
	 */

	public void controlPaddle(Input input, BounceGame bg) throws SlickException {

		// Press C to save your ball
		if (input.isKeyDown(Input.KEY_C)) {

			bg.paddle.setX(bg.ball.getX());
			if (bg.ball.getCoarseGrainedMinY() > bg.ScreenHeight / 2)
				bg.paddle.setY(bg.ball.getY() + 32.0f);
			if (bg.ball.getCoarseGrainedMinY() < bg.ScreenHeight / 2)
				bg.paddle.setY(bg.ball.getY() - 32.0f);
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			if(bg.paddle.getVelocity().getX() >0 ) {
				bg.paddle.setVelocity(new Vector(0.0f, 0.0f));
//				bg.paddle.setVelocity(new Vector(-bg.paddle.getVelocity().getX(), 0.0f));
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(
					new Vector(-.02f, 0)));
			}
			else
				bg.paddle.setVelocity(bg.paddle.getVelocity().add(
						new Vector(-.005f, 0)));
			
			System.out.println("Left key: bg.paddle.getVelocity().getX() = " + bg.paddle.getVelocity().getX());
			
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			if(bg.paddle.getVelocity().getX() <0 ) {
				bg.paddle.setVelocity(new Vector(0.0f, 0.0f));
				bg.paddle.setVelocity(bg.paddle.getVelocity().add(
						new Vector(.02f, 0f)));
			}
//				bg.paddle.setVelocity(new Vector(-bg.paddle.getVelocity().getX(), 0.0f));
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(
					new Vector(.005f, 0f)));
			System.out.println("Right Key: bg.paddle.getVelocity().getX() = " + bg.paddle.getVelocity().getX());
		}		
		if (input.isKeyDown(Input.KEY_DOWN)) {
			bg.paddle.setVelocity(new Vector(0.0f, 0.0f));
		}
	}

	/*
	 * Configure the paddle: location, shape, image
	 */
	public void configPaddle(StateBasedGame game, int levels) {
		BounceGame bg = (BounceGame) game;
		System.out.println("levels= " + levels);
		bg.paddle.setX(400.0f); // set the position and velocity of paddle
		bg.paddle.setY(560.0f);
		bg.paddle.setVelocity(new Vector(0.0f, 0.0f));
		switch (levels) {
		case 1:
			bg.paddle.scale(1.0f);
			break;
		case 2:
			bg.paddle.scale(.95f);
			break;
		case 3:
			bg.paddle.scale(.90f);
			break;
		case 4:
			bg.paddle.scale(.85f);
			break;

		default:
			bg.paddle.scale(.90f);
			break;
		}
	}

	/**
	 * Bounce the paddle off a surface. 
	 * 
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
		// removeImage(ResourceManager.getImage(BounceGame.BALL_BALLIMG_RSC));
		// addImageWithBoundingBox(ResourceManager
		// .getImage(BounceGame.BALL_BROKENIMG_RSC));
		countdown = 500;
		velocity = velocity.bounce(surfaceTangent);
	}

	/**
	 * Update the Paddle based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
//		if (countdown > 0) {
//			countdown -= delta;
//			 if (countdown <= 0) {
//			 addImageWithBoundingBox(ResourceManager
//			 .getImage(BounceGame.BALL_BALLIMG_RSC));
//			 removeImage(ResourceManager
//			 .getImage(BounceGame.BALL_BROKENIMG_RSC));
//			 }
//		}
	}

}
