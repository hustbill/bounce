package bounce;
// Sprite.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A Sprite has a position, velocity (in terms of steps),
   an image, and can be deactivated.

   The sprite's image is managed with an ImagesLoader object,
   and an ImagesPlayer object for looping.

   The images stored until the image 'name' can be looped
   through by calling loopImage(), which uses an
   ImagesPlayer object.

   ------------------
   ADDED: getImage()

*/

import java.awt.*;

import javax.swing.*;

import java.awt.image.*;

import jig.ResourceManager;

import org.newdawn.slick.Graphics;


public class Sprite 
{
  // default step sizes (how far to move in each update)
  private static final int XSTEP = 5; 
  private static final int YSTEP = 5;

  // default dimensions when there is no image
  private static final int SIZE = 12;   

  // image-related
  private ImagesLoader imsLoader;
  private String imageName;
  private BufferedImage image;
  private int width, height;     // image dimensions

  //private ImagesPlayer player;  // for playing a loop of images
  private boolean isLooping;

  private int pWidth, pHeight;   // panel dimensions

  private boolean isActive = true;      
  // a sprite is updated and drawn only when it is active

  // protected vars
  protected int locx, locy;        // location of sprite
  protected int dx, dy;            // amount to move for each update



  public Sprite(int x, int y, int w, int h, ImagesLoader imsLd, String name) 
  { 
    locx = x; locy = y;
    pWidth = w; pHeight = h;
    dx = XSTEP; dy = YSTEP;

    imsLoader = imsLd;
    setImage(name);    // the sprite's default image is 'name'
  } // end of Sprite()


  public void setImage(String name)
  // assign the name image to the sprite
  {
    imageName = name;
    image = imsLoader.getImage(imageName);
    if (image == null) {    // no image of that name was found
      System.out.println("No sprite image for " + imageName);
      width = SIZE;
      height = SIZE;
    }
    else {
      width = image.getWidth();
      height = image.getHeight();
    }
    // no image loop playing 
    isLooping = false;
  }  // end of setImage()



  public BufferedImage getImage()
  // NEW in AlienTiles
  { 
      return image;
  }  // getImage()




  public int getWidth()    // of the sprite's image
  {  return width;  }

  public int getHeight()   // of the sprite's image
  {  return height;  }

  public int getPWidth()   // of the enclosing panel
  {  return pWidth;  }

  public int getPHeight()  // of the enclosing panel
  {  return pHeight;  }


  public boolean isActive() 
  {  return isActive;  }

  public void setActive(boolean a) 
  {  isActive = a;  }

  public void setPosition(int x, int y)
  {  locx = x; locy = y;  }

  public void translate(int xDist, int yDist)
  {  locx += xDist;  locy += yDist;  }

  public int getXPosn()
  {  return locx;  }

  public int getYPosn()
  {  return locy;  }


  public void setStep(int dx, int dy)
  {  this.dx = dx; this.dy = dy; }

  public int getXStep()
  {  return dx;  }

  public int getYStep()
  {  return dy;  }


  public Rectangle getMyRectangle()
  {  return  new Rectangle(locx, locy, width, height);  }


  public void updateSprite()
  // move the sprite
  {
    if (isActive()) {
      locx += dx;
      locy += dy;
   
    }
  } // end of updateSprite()



  public void drawSprite(Graphics g) 
  {
	  
	  g.drawImage(ResourceManager.getImage(BounceGame.BALL_BALLIMG_RSC), locx, locy);
    if (isActive()) {
      if (image == null) {   // the sprite has no image
        //g.setColor(Color.yellow);   // draw a yellow circle instead
        g.fillOval(locx, locy, SIZE, SIZE);
        //g.setColor(Color.black);
      }
      else {
        if (isLooping)
          //image = player.getCurrentImage();
        	g.drawImage(ResourceManager.getImage(BounceGame.BALL_BALLIMG_RSC), locx, locy);
         //g.drawImage(image, locx, locy);
      }
    }
  } // end of drawSprite()

}  // end of Sprite class
