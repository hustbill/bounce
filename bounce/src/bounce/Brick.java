package bounce;


import jig.Entity;
import jig.ResourceManager;
import jig.Vector;


/**
 * The Bricks class is an Entity that cannot move.
 *  Bricks near the top of the screen, and are "destroyed" when hit by ball
 * 
 */
public class Brick extends Entity {
	public int hit_times=0;
	
	public Brick( float x,  float y) {
		super(x, y);
		
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BRICK_RSC));
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
		
		//velocity = velocity.bounce(surfaceTangent);
	}

	public void changePic(int levels) {
		switch(levels) {
		case 2:
			removeImage(ResourceManager
					.getImage(BounceGame.BRICK_RSC));
			//http://icons.iconarchive.com/icons/pino/looney/32/Porky-Pig-icon.png
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.PIG_RSC));
			break;
		
		case 3:
			removeImage(ResourceManager
					.getImage(BounceGame.BRICK_RSC));
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.FISH_RSC));
			break;
		case 4:
			removeImage(ResourceManager
					.getImage(BounceGame.BRICK_RSC));
			//http://img1.wikia.nocookie.net/__cb20110116163235/zombiefarm/images/a/a2/Zombie_Quest.png
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.ZOMBIE_RSC));		
			break;
			
		default:
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_RSC));
			break;
			
		}
	}
	
	/**
	 * "Destroy" the Brick when the ball hit...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	//public void update(final int delta, int levels) {
	public void update(int levels) {
		
		switch(levels) {
			case 2: //for first hit, just change the image to skull
				removeImage(ResourceManager
						.getImage(BounceGame.ZOMBIE_RSC));
				addImageWithBoundingBox(ResourceManager
						.getImage(BounceGame.SKULL_RSC)); //for second hit, the bricks were destroyed
				
				break;
			case 3: 
				removeImage(ResourceManager
						.getImage(BounceGame.COIN_RSC));
				addImageWithBoundingBox(ResourceManager
						.getImage(BounceGame.SKULL_RSC)); //for second hit, the bricks were destroyed
			default:
				addImageWithBoundingBox(ResourceManager
						.getImage(BounceGame.SKULL_RSC));	
			break;
		}
	}
	

}
