package bounce;

import java.awt.Font;
import java.io.File;
import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ConfigState extends BasicGameState{

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub	
	}
	
	public void enter(GameContainer container, StateBasedGame game) {
//		lives = 3;
//		bounces = 0;
//		levels = 1;
		container.setSoundOn(true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		
	
		//g.drawString("Bounces: ?", 10, 30);
	 	//g.drawString("Levels: ?", 10, 30);	
	
	   String[][] data = new String[10][5]; // only display top ten players
	   data = readXml("games.xml");
	   
	   g.drawString( "Player \tScore \tDate \t " , 260, 60 );
	   String player, date, score, resources;
	   for(int k=0; k< 5 ; k++) {			
		    player =data[k][0];
		    score = data[k][1];
			date = data[k][2];
			resources = data[k][3];
			
		g.drawString( player +"\t " + score + "\t " + date +"\t " , 260, 100+30*k);
	   }
	   
	   
		g.drawImage(ResourceManager.getImage(BounceGame.YOUWIN_BANNER_RSC),
				225, 270);	

		g.drawString("Press Space to Start...", 280, 320);	
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
		
		

		if (input.isKeyDown(Input.KEY_SPACE))
			bg.enterState(BounceGame.PLAYINGSTATE);	
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return BounceGame.CONFIGSTATE;
	}
	
	

	public static String[][] readXml(String filename) {
		String[][] data = new String[10][5];
		final String dir = System.getProperty("user.dir");
		//System.out.println("current dir = " + dir);

		try {
			File file = new File(dir + "//src//bounce//resource//" + filename); //testCases.xml
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("game");
			//System.out.println("Information of all games");

			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) fstNode;
					data[s][0] = (getValue("player", element));
					data[s][1] = (getValue("score", element));
					data[s][2] = (getValue("date", element));
					data[s][3] = (getValue("resource", element));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0)
				.getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

}
