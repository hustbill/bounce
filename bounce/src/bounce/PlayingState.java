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
	int lives;
	int bounces;
	int levels =1;
	float radius = 16.0f;  // ball radius 
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		// configuration the Bricks
		configBricks(game, levels);
	}
	
	// configuration the Bricks- 17 columns , 4 rows
	public void configBricks(StateBasedGame game, int levels) {
		BounceGame bg = (BounceGame)game;
		System.out.println("levels= " + levels);
		switch(levels) {
			case 1:
			  	for( int i =0; i<17; i++) {
					for(int j=0; j< 3; j++) {
						bg.brick = new Brick(bg.ScreenWidth / 7 + 36*i, bg.ScreenHeight * 1/5 + 32*j);
						bg.brick.changePic(levels);
						bg.bricks.add(bg.brick);
					}
				}				
			break;
			
			case 2 :
				for( int i =0; i<10; i++) {
					for(int j=10-i; j< 10; j++) {
						bg.brick = new Brick(bg.ScreenWidth / 7 + 36*i, bg.ScreenHeight * 1/5 + 32*j);
						bg.brick.changePic(levels);
						bg.bricks.add(bg.brick);
						
					}
				
				}
				break;
				
		case 3:
				
				for( int i =0; i<8; i++) {
					for(int j=7-i; j< 8+i; j++) {
						bg.brick = new Brick(bg.ScreenWidth / 7 + 36*i, bg.ScreenHeight * 1/10+ 32*j);
						bg.brick.changePic(levels);
						bg.bricks.add(bg.brick);
					}
				}
				break;
		    default :
		    	for( int i =0; i<8; i++) {
					i=i+1;
					for( int j =0; j<8; j++) {
						j=j+1;
						for(int k=1;k<=2*i-1;k++){
						if(k==1 || k==2*i-1){
							bg.brick = new Brick(bg.ScreenWidth / 7 + 32*(k),  32*(k-i+j));
							bg.brick.changePic(levels);
							bg.bricks.add(bg.brick);
							}
						}
					 }
					}
		    	break;
		}
		
		
				
	}
	

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		lives = 3;
		bounces= 0;
		levels =1;
		

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
		
		g.drawString("Lives Remaining: " + lives, 10, 30);
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
		
		
		if (input.isKeyDown(Input.KEY_C)) {			
			bg.paddle.setX(bg.ball.getX());
			bg.paddle.setY(bg.ball.getY());
//			if( bg.ball.getCoarseGrainedMinY() > bg.ScreenHeight /2) 
//				bg.paddle.setY(bg.ball.getY()+30);
//			if( bg.ball.getCoarseGrainedMinY() < bg.ScreenHeight /2) 
//				bg.paddle.setY(bg.ball.getY()-30);

		}
		//Cheat codes to allow user to access all of levels by press "P"
		if (input.isKeyDown(Input.KEY_P)) {			
			for( int i=0; i< bg.bricks.size(); i++) {
				Brick bk = bg.bricks.get(i);
				bg.ball.setY(bk.getY());
				bg.ball.setX(bk.getX());				
			}
		}
		if (input.isKeyDown(Input.KEY_A)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(-.002f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(+.002f, 0f)));
		}
		
		if (input.isKeyDown(Input.KEY_0)) {
			//bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(+.002f, 0f)));
		    bg.ball.scale(0.5f);
		}
		
		if (input.isKeyDown(Input.KEY_1)) {
			//bg.paddle.setVelocity(bg.paddle.getVelocity().add(new Vector(+.002f, 0f)));
		    bg.ball.scale(1.5f);
		    
		}
		
				
		// bounce the ball...
		boolean bounced = false;
		if (bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.ball.getCoarseGrainedMinX() < 0) {
			bg.ball.bounce(90);
			if (bg.ball.getX() > 0 )
					bg.ball.setX(bg.ball.getX() - radius);
			if(bg.ball.getX() < 0)
					bg.ball.setX(bg.ball.getX() + radius);
			
			bounced = true;
		} else if (bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight
				|| bg.ball.getCoarseGrainedMinY() < 0) {
			
			bg.ball.bounce(0);
			if (bg.ball.getY() > 0 )
				bg.ball.setY(bg.ball.getY() - 2* radius);
			if(bg.ball.getY() < 0)
				bg.ball.setY(bg.ball.getY() + 2* radius);
			bounced = true;
		}
		
		if (bounced) {
			
			bg.explosions.add(new Bang(bg.ball.getX(), bg.ball.getY()));
			bounces++;
			System.out.println("bounces= " + bounces);
			if (bounces == 10 || bounces==20 || bounces==30){
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
		if(detectCollision_paddle(bg.ball.getX() ,
				bg.ball.getY(), 
				bg.paddle.getX(), bg.paddle.getY(),
				bg.paddle.getCoarseGrainedHeight(),				 
				bg.paddle.getCoarseGrainedWidth(), radius)) {
			bg.ball.bounce(180);
			//bg.ball.setX(bg.paddle.getX() + bg.paddle.getCoarseGrainedHeight()/2);
			bg.ball.setY(bg.paddle.getY()- radius *4/3);
			bounced = true;
		}
		bg.paddle.update(delta);
		
		// detect the collision between ball and bricks
		for( int i=0; i< bg.bricks.size(); i++) {
			Brick bk = bg.bricks.get(i);
			if(detectCollision_brick(bg.ball.getX() ,
					bg.ball.getY(), 
					bk.getX(), bk.getY(),
					bk.getCoarseGrainedHeight(),				 
					bk.getCoarseGrainedWidth(), radius)) {
				bg.ball.bounce(180);
				bounced = true;		
				if(levels==1)
				  bg.bricks.remove(bk); //remove brick from ArrayList
				else{
					bk.update(levels); //destroy the brick
					bk.hit_times++;
					System.out.println("levels= " + levels);
				}
				if(bk.hit_times ==2)
					 bg.bricks.remove(bk);
				
			}	
		}
		if (bg.bricks.size() == 0) {
			//System.out.println("Congratulations! You can go to the next level! ");		
			levels++;
			if (levels <=4) {
				this.init(container, game);
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(lives);
				game.enterState(BounceGame.GAMEOVERSTATE);
			}			   
		}

		// check if there are any finished explosions, if so remove them
//		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
//			if (!i.next().isActive()) {
//				i.remove();
//			}
//		}

		if (lives == 0) {
			((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(lives);
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
//		System.out.println("ball_x=" + ball_x);
//		System.out.println("ball_y=" + ball_y);
//		System.out.println("paddle_x=" + paddle_x);
//		System.out.println("paddle_y=" + paddle_y);
		
		if(Math.abs(delta_x) < (paddle_length/2 + radius)
				&& Math.abs(delta_y) < (paddle_height/2 +radius ) ) {
			//System.out.println("radius = " + radius);
			System.out.println("collision with paddle!\n");
			System.out.println("delta_x = " + Math.abs(delta_x));
			System.out.println("delta_y = " + Math.abs(delta_y));
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
			//System.out.println("collision with brick!\n");
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