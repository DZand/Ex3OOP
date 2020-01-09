package gameClient;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import Server.Fruit;
import Server.Game_Server;
import Server.RobotG;
import Server.game_service;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_node_data;
import oop_utils.OOP_Point3D;
import oop_dataStructure.oop_graph;
import oop_dataStructure.OOP_DGraph;
import utils.Point3D;
import utils.StdDraw;

public class MyGameGUI extends JFrame implements ActionListener , MouseListener, Runnable 
{
	public Map m;
	public BufferedImage background; //game background image.
	public BufferedImage robotImage; //robot icon.
	public BufferedImage fruitImage; //fruit icon.
	public ArrayList<oop_edge_data> eList; //list of lines.
	ArrayList<Fruit> fruitsArray;//list of fruits.
	ArrayList<RobotG> robotsArray;//list of robots.
	public ArrayList<Point3D> linePoints; //lines pixels list for point 1.
	public ArrayList<Point3D> linePoints2; //lines pixels list for point 2.
	public ArrayList<Point3D> robotPoint; //robots pixels list.
	public ArrayList<Point3D> fruitPoint; //fruits pixel list.
	public int countRobots; //robot id.
	public int countFruit; //fruit id.
	private boolean WhoAreYOU; //if true - draws robot. else - draws fruit.
	OOP_DGraph currGraph;
	Game_Server numGame;
	
	public MyGameGUI() throws IOException 
	{

		//m = new Map();
		eList = new ArrayList<oop_edge_data>();
		this.robotsArray = new ArrayList<RobotG>();
		this.fruitsArray = new ArrayList<Fruit>();
		robotPoint = new ArrayList<Point3D>();
		fruitPoint = new ArrayList<Point3D>();
		countRobots = 0;
		countFruit = 0;
		WhoAreYOU = true;
		this.currGraph=null;
		init(this.currGraph);	
		Game_Server numGame = null;
		this.addMouseListener(this);
	}

	
	public MyGameGUI(OOP_DGraph g) 
	{
		//m = new Map();
		this.currGraph=g;
		this.fruitsArray = new ArrayList<Fruit>();
		this.robotsArray = new ArrayList<RobotG>();
		WhoAreYOU = true; //draws robots.
		repaint();
		init(this.currGraph);	
		this.addMouseListener(this);
	}
	
	public MyGameGUI(ArrayList<Fruit> fruits, ArrayList<RobotG> robots) 
	{
		Iterator<Fruit> iter1 = fruits.iterator();
		Iterator<RobotG> iter2 = robots.iterator();
		while (iter1.hasNext()) 
		{
			Fruit runner1 = iter1.next();
			this.fruitsArray.add(runner1);
		}
		while (iter2.hasNext()) 
		{
			RobotG runner2 = iter2.next();
			this.robotsArray.add(runner2);
		}
	}
	
	public void init(oop_graph g) 
	{
		this.setSize(1000, 10000);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		
		MenuBar menuBar = new MenuBar();
		Menu icons = new Menu("Type"); //Icons - Robot, Fruit.
		MenuItem robot = new MenuItem("Robot");
		MenuItem fruit = new MenuItem("Fruit");

		Menu data = new Menu("Data"); //Data - Speed, Radius, Weight.
		MenuItem result = new MenuItem("Result (robot)");
		MenuItem time = new MenuItem("Time (robot)");


		Menu options = new Menu("Options"); //Options - Run, Create kml file, Read game, Save game, Clear.
		MenuItem run = new MenuItem("Run");
		MenuItem createKML = new MenuItem("Create kml file");
		MenuItem readJSON = new MenuItem("Read game");
		MenuItem clear = new MenuItem("Clear");
		
		robot.addActionListener(this);
		fruit.addActionListener(this);
		result.addActionListener(this);
		time.addActionListener(this);
		run.addActionListener(this);
		createKML.addActionListener(this);
		readJSON.addActionListener(this);
		clear.addActionListener(this);

		menuBar.add(icons);
		icons.add(robot);
		icons.add(fruit);

		menuBar.add(data);
		data.add(result);
		data.add(time);


		menuBar.add(options);
		options.add(run);
		options.add(createKML);
		options.add(readJSON);
		options.add(clear);

		this.setMenuBar(menuBar);
		
		repaint();
		
		
	}
	
	@Override
	/** public void actionPerformed (ActionEvent event) 
	    {
	    	String action = event.getActionCommand();
	    	switch(action) 
	    	{
	    	case "robot" : WhoAreYOU = true;
	    	break;
	    	case "fruit" : WhoAreYOU = false;
	    	break;
	    	case "result" : shortestPathDist();
	    	break;
	    	case "time" : tsp();
	    	break;
	    	case "run" : 
	    		ShortestPathAlgo shortPath = new ShortestPathAlgo();
				Game_Server g = new Game_Server(pList, fList , lList);
				try 
				{
					shortPath.closestFruit(g);
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				ThreadPacks P = new ThreadPacks(); //NEW
				P.start(); //NEW
				repaint();
				break;
	    	case "createKML" : 
	    		Game_Server g = new Game_Server(pList, fList ,lList);
				try 
				{
					g.createKML(g, "C:\\Users\\danielle\\eclipse-workspace\\oop3\\data\\myGame.kml");
				}
				catch (ParseException e1) 
				{
					e1.printStackTrace();
				}
				break;
	    	case "readJSON" :
	    	break;
	    	default: //Default set to be clear
	    		pList.clear();
				fList.clear();
				lList.clear();
				pacmanPixel.clear();
				fruitPixel.clear();
				linePixel.clear();
				countPacman = 0;
				countFruit = 0;
				repaint();
	    		break;
	    	}
	    }*/
	
	public void paint(Graphics graph) 
	{
		super.paint(graph);
		graph.setFont(new Font ("Courier", Font.PLAIN,20));
		if (currGraph == null) 
		{
			JFrame mesg = new JFrame(); 
			JOptionPane.showMessageDialog(mesg, "There Isn't Graph To Show");
		}
		else 
		{
			for (oop_node_data currNode : currGraph.getV()) 
			{
				graph.setColor(Color.blue);
				OOP_Point3D srcNode = currNode.getLocation();
				graph.fillOval(srcNode.ix(), srcNode.iy(), 12, 12);
				graph.setColor(Color.RED);
				graph.drawString(""+currNode.getKey(), srcNode.ix()-4, srcNode.iy()-4);
				if ((currGraph.getE(currNode.getKey())!=null)) 
				{
					for (oop_edge_data edge : currGraph.getE(currNode.getKey())) 
					{
						graph.setColor(Color.RED);
						((Graphics2D) graph).setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
						OOP_Point3D destPoint = currGraph.getNode(edge.getDest()).getLocation();
						graph.drawLine(srcNode.ix()+5, srcNode.iy()+5, destPoint.ix()+5, destPoint.iy()+5);
						graph.setColor(Color.black);
						graph.fillOval((int)((srcNode.ix()*0.7)+(0.3*destPoint.ix()))+2, (int)((srcNode.iy()*0.7)+(0.3*destPoint.iy())), 9, 9);
						graph.drawString(String.valueOf(edge.getWeight()), 1+(int)((srcNode.ix()*0.7)+(0.3*destPoint.ix())), (int)((srcNode.iy()*0.7)+(0.3*destPoint.iy()))-2);
					}
				}	
			}
		}
		
		if(!fruitsArray.isEmpty())
		{
			for (int i = 0; i < fruitsArray.size(); i++) 
			{
				Fruit currFruit = fruitsArray.get(i);
				OOP_Point3D fruitLocation = currFruit.getLocation();
				if(currFruit.getType()<0)
				{
					//apple
					graph.setColor(Color.red);
				}
				else
				{
					//banana
					graph.setColor(Color.yellow);
				}
				
				graph.fillOval(fruitLocation.ix(), fruitLocation.iy(), 12, 12);
				
			}
		}
		if (!robotsArray.isEmpty()) 
		{
			for (int i =0; i<robotsArray.size();i++) 
			{
				RobotG currRobot = robotsArray.get(i);
				OOP_Point3D robotLocation = currRobot.getLocation();
				graph.setColor(randomColor());
				graph.fillOval(robotLocation.ix(), robotLocation.iy(), 25, 25);
			}
		}
	}

	
	public void chooseScenario() 
	{
		JFrame window = new JFrame();
		String inputUser = JOptionPane.showInputDialog(window, "Please enter scenario number between 0-23");
		int scenarioNum;
		try
		{
			scenarioNum=Integer.parseInt(inputUser);
		}
		catch(Exception e)
		{
			throw new RuntimeException("The scenario must be a number, please try again");
		}
		if(scenarioNum<0 || scenarioNum>23) 
		{
			JOptionPane.showMessageDialog(window, "The number need to be between 0-23, please try again " );
		}
		else 
		{
			game_service game = Game_Server.getServer(scenarioNum); 
			String graphJson = game.getGraph();
			OOP_DGraph graphDGraph = new OOP_DGraph();
			graphDGraph.init(graphJson);
			MyGameGUI gameGraphGui = new MyGameGUI(graphDGraph);
		}
		
	}
	
	public void draw() 
	{
		
	}
	
	public void readRobotJson (Game_Server game) 
	{
		List<String> listOfRobot = game.getRobots();
		if (listOfRobot != null) 
		{
			for (String rob : listOfRobot) 
			{
				
				try 
				{
					JSONObject objRob = new JSONObject(rob);
					JSONObject currntRobot = objRob.getJSONObject("Robot");
					String[] positionRob = currntRobot.getString("pos").split(",");
					Point3D curPoint = new Point3D(Double.parseDouble(positionRob[0]), Double.parseDouble(positionRob[1]));
					int key = currntRobot.getInt("id");
					RobotG addRobot = new RobotG(this.currGraph, key);
					robotsArray.add(addRobot);
					
				} catch (Exception e)
				{
					System.out.println("The parse from json file field.");
				}
			}
		}
	}
	
	public void readFruitJson (Game_Server game) 
	{
		List<String>listOfFruit = game.getFruits();
		if (listOfFruit != null) 
		{
			for (String fruit : listOfFruit) 
			{
				StdDraw.setPenColor(randomColor());
				try 
				{
					
					JSONObject objFru = new JSONObject(fruit);
					JSONObject currntFruit = objFru.getJSONObject("Fruit");
					String[] positionFruit = currntFruit.getString("pos").split(",");
					OOP_Point3D curPoint = new OOP_Point3D(Double.parseDouble(positionFruit[0]), Double.parseDouble(positionFruit[1]));
					int fruitType = currntFruit.getInt("type");
					Double fruitWeight = currntFruit.getDouble("value");
					oop_edge_data edgeFruit = findFruitEdge(curPoint);
					Fruit addFruit = new Fruit(fruitWeight,curPoint,edgeFruit);
					fruitsArray.add(addFruit);
					
				} catch (Exception e)
				{
					System.out.println("The parse from json file field.");
				}
			}
		}
	}
		
	private static int nextNode(oop_graph g, int src) {
		int ans = -1;
		Collection<oop_edge_data> ee = g.getE(src);
		Iterator<oop_edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	private oop_edge_data findFruitEdge (OOP_Point3D fruitPoint) 
	{
		if(this.currGraph != null) 
		{
			if (this.currGraph.getV()!=null) 
			{
				for (oop_node_data node: this.currGraph.getV()) 
				{
					if (this.currGraph.getE(node.getKey())!=null) 
					{
						for (oop_edge_data edge : this.currGraph.getE(node.getKey())) 
						{
							OOP_Point3D src = this.currGraph.getNode(edge.getSrc()).getLocation();
							OOP_Point3D dest = this.currGraph.getNode(edge.getDest()).getLocation();
							Double m = (src.y()-dest.y())/(src.x() - dest.x());
							if (src.y()-fruitPoint.y() == (m * (src.x()-fruitPoint.x())))
							{
								return edge;
							}
						}
					}

				}
			}
		}
		return null;
		
		
	}
	
	public ArrayList<RobotG> nearestFruits(Game_Server g)
	{
		Double time;
		Fruit fruitEat = null;
		ArrayList<Fruit> leftFruitArray = new ArrayList<Fruit>();
		ArrayList<RobotG> robotArray = new ArrayList<RobotG>();
		readFruitJson(g);
		for (Fruit f: fruitsArray) 
		{
			leftFruitArray.add(f);
		}
		while (!leftFruitArray.isEmpty()) 
		{
			int indexFruit = 0;
			int indexRobot = 0;
			
		}
		return null;
	}

	//create random color
	private Color randomColor()
	{
	  Random random=new Random(); 
	  int red=random.nextInt(256);
	  int green=random.nextInt(256);
	  int blue=random.nextInt(256);
	  return new Color(red, green, blue);
	}
	
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent arg0) 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) 
		{
			// TODO Auto-generated method stub
			
		}


		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// TODO Auto-generated method stub
			
		}
}
