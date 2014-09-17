package bounce;

import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;


/**
 * The Paddle class is an Entity that has a velocity (since it's moving).
 * User can control a paddle to influences the path of ball
 * 
 *  @author Hua Zhang
 */

public class Paddle extends Entity{
	
	private Vector velocity;
	private int countdown;
	public float angle;

	public Paddle(final float x, final float y, final float vx, float vy, float angle_degree) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.PADDLE_RSC));
		vy=0.0f;
		velocity = new Vector(vx, vy);
		countdown = 0;
		angle =angle_degree;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}
	
	public void setAngle(final float angle_num){
		angle = angle_num;
	}
	
	public float getAngle() {
		return angle;
	}
	
	/*
	 *  Configure the paddle: location, shape, image
	 */
	public void configPaddle(StateBasedGame game, int levels) {
		BounceGame bg = (BounceGame) game;
		System.out.println("levels= " + levels);
		switch (levels) {
		case 1:
			bg.paddle.scale(1.0f);
			break;
		case 2:
			bg.paddle.scale(.9f);
			break;			
		case 3:
			bg.paddle.scale(.8f);
			break;
		case 4:
			bg.paddle.scale(.7f);
			break;

		default:
			bg.paddle.scale(.8f);
			break;
		}
	}
	
	
	/**
	 * Bounce the ball off a surface. This simple implementation, combined
	 * with the test used when calling this method can cause "issues" in
	 * some situations. Can you see where/when? If so, it should be easy to
	 * fix!
	 * 
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
//		removeImage(ResourceManager.getImage(BounceGame.BALL_BALLIMG_RSC));
//		addImageWithBoundingBox(ResourceManager
//				.getImage(BounceGame.BALL_BROKENIMG_RSC));
		countdown = 500;
		velocity = velocity.bounce(surfaceTangent);
	}

	/**
	 * Update the Ball based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
//			if (countdown <= 0) {
//				addImageWithBoundingBox(ResourceManager
//						.getImage(BounceGame.BALL_BALLIMG_RSC));
//				removeImage(ResourceManager
//						.getImage(BounceGame.BALL_BROKENIMG_RSC));
//			}
		}
	}
	

}
