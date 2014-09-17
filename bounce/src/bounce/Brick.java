package bounce;


import org.newdawn.slick.state.StateBasedGame;

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
	
	/*
	 *  Configure the Bricks: location, shape, image
	 */
	public void configBricks(StateBasedGame game, int levels) {
		BounceGame bg = (BounceGame) game;
		System.out.println("levels= " + levels);
		switch (levels) {
		case 1:
			for (int i = 0; i < 17; i++) {
				for (int j = 0; j < 4; j++) {
					bg.brick = new Brick(bg.ScreenWidth / 7 + 36 * i,
							bg.ScreenHeight * 1 / 7 + 32 * j);				
					bg.bricks.add(bg.brick);

				}

			}
			break;

		case 2:
			for (int i = 0; i < 8; i++) {
				for (int j = 8 - i; j < 8; j++) {
					bg.brick = new Brick(bg.ScreenWidth / 7 + 36 * i,
							bg.ScreenHeight * 1 / 10 + 32 * j);
					bg.brick.changePic(levels);
					bg.bricks.add(bg.brick);

				}

			}
			for (int i = 8; i > 0; i--) {
				for (int j = i; j < 8; j++) {
					bg.brick = new Brick(bg.ScreenWidth / 2 + 36 * i,
							bg.ScreenHeight * 1 / 10 + 32 * j);
					bg.brick.changePic(levels);
					bg.bricks.add(bg.brick);
				}
			}
			break;

		case 3:
			for (int i = 0; i < 7; i += 2) {
				for (int j = 6 - i; j < 7 + i; j += 2) {
					bg.brick = new Brick(bg.ScreenWidth / 7 + 36 * i,
							bg.ScreenHeight * 1 / 10 + 32 * j);
					bg.brick.changePic(levels);
					bg.bricks.add(bg.brick);
				}
			}

			for (int i = 7; i > 0; i -= 2) {
				for (int j = 6 - i; j < 6 + i; j += 2) {
					bg.brick = new Brick(bg.ScreenWidth / 2 + 36 * i,
							bg.ScreenHeight * 1 / 10 + 32 * j);
					bg.brick.changePic(levels);
					bg.bricks.add(bg.brick);
				}
			}
			break;
		case 4:
			for (int i = 0; i < 8; i++) {
				i = i + 1;
				for (int j = 0; j < 8; j++) {
					j = j + 1;
					for (int k = 1; k <= 2 * i - 1; k++) {
						if (k == 1 || k == 2 * i - 1) {
							bg.brick = new Brick(bg.ScreenWidth / 4 + 32 * (k),
									32 * (k - i + j));
							bg.brick.changePic(levels);
							bg.bricks.add(bg.brick);
						}
					}
				}
			}
			break;
		default:
			// for (int i = 0; i < 8; i++) {
			// for (int j = 7 - i; j < 8 + i; j++) {
			// bg.brick = new Brick(bg.ScreenWidth / 7 + 36 * i,
			// bg.ScreenHeight * 1 / 10 + 32 * j);
			// bg.brick.changePic(levels);
			// bg.bricks.add(bg.brick);
			// }
			// }
			for (int i = 0; i < 17; i++) {
				for (int j = 0; j < 4; j++) {
					bg.brick = new Brick(bg.ScreenWidth / 7 + 36 * i,
							bg.ScreenHeight * 1 / 7 + 32 * j);
					bg.brick.changePic(levels);
					bg.bricks.add(bg.brick);

				}

			}

			break;
		}

	}

	public void changePic(int levels) {
		switch(levels) {
		case 1:
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_RSC));
			break;
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
			case 1:
				removeImage(ResourceManager
						.getImage(BounceGame.BRICK_RSC));
			break;
			
			case 2: //for first hit, just change the image to skull
				System.out.println("Enter into Brick update method");
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
