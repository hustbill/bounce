package bounce;


import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;


/**
 * The Bonus class is an Entity that has a velocity (since it's moving).
 * User can let the ball hit the coin to increase their scores
 * 
 *  @author Hua Zhang
 */


public class Bonus extends Entity{
	
	private Vector velocity;
	private int countdown;

	public Bonus(final float x, final float y, float vx, float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.PADDLE_RSC));
		vx=0.0f;
		velocity = new Vector(vx, vy);
		countdown = 0;
		
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}
	

}
