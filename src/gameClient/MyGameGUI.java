package gameClient;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.json.JSONException;
import org.json.JSONObject;

import gameClient.Fruit;
import Server.Game_Server;
import gameClient.Robot;
import Server.game_service;
import utils.Point3D;
import dataStructure.*;
import gameClient.KML_Logger;


public class MyGameGUI extends JFrame implements ActionListener , MouseListener, Runnable 
{
	public Map m;
	public BufferedImage background; //game background image.
	public BufferedImage robotImage; //robot icon.
	public BufferedImage appleImage; 
	public BufferedImage bannaImage; 
	public ArrayList<Fruit> fruitArrayList;//list of fruits.
	public ArrayList<Robot> robotsArrayList;//list of robots.
	private DGraph currGraph;
	private game_service game;
	private String inputTypeGame;
	private static double maxX = Double.NEGATIVE_INFINITY;
	private static double maxY = Double.NEGATIVE_INFINITY;
	private static double minX = Double.POSITIVE_INFINITY;
	private static double minY = Double.POSITIVE_INFINITY;
	private final int Offset = 50;
	private final int xRange = 600;
	private final int yRange = 600;
	private int robotsCount;
	private double epsilon = 0.001;
	private JLabel timeLable= new JLabel("Timer:");
	private Boolean PaintRobots;
	boolean ManuelMode;
	Thread clientThread;
	 JButton manualButton, autoButton;
	 private boolean firstpress=false;
	
	
	public static void main(String[] args) 
	{
		MyGameGUI gui = new MyGameGUI();
		gui.setVisible(true);
	}
	
	
	public MyGameGUI() 
	{
		init();
	}
	
	  private void init() 
	  {
		   
	        PaintRobots = false;
	        this.setSize(1300, 700);
	        setTitle("Best game ever");
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.addMouseListener(this);
			//init picture
			try 
			{
				robotImage = ImageIO.read(new File("C:\\Users\\Danielle2\\Desktop\\Eclipse-workspace\\Ex3OOP\\data\\robot.PNG"));
				bannaImage = ImageIO.read(new File("C:\\Users\\Danielle2\\Desktop\\Eclipse-workspace\\Ex3OOP\\data\\banana.PNG"));
				appleImage = ImageIO.read(new File("C:\\Users\\Danielle2\\Desktop\\Eclipse-workspace\\Ex3OOP\\data\\apple.PNG"));
			}

			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			
	        manualButton = new JButton("Manuel Game");
	        autoButton = new JButton("Auto Game");
	        manualButton.addActionListener(this);
	        autoButton.addActionListener(this);
	        this.getContentPane().setLayout(new GridLayout());
	        this.getContentPane().add(manualButton);
	        this.getContentPane().add(autoButton);
	        clientThread = new Thread(this);
	    }
/**
	public void init() 
	{
		//Thread newThread= new Thread(this);
		JPanel root = new JPanel();
		this.setSize(1000, 10000);
		setTitle("Best game ever");
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		center.add(timeLable, BorderLayout.NORTH);
		//center.add(timeLable, BorderLayout.NORTH);
		
		//run();
		int scenario=chooseScenario();
		game = Game_Server.getServer(scenario);
		currGraph = new DGraph();
		currGraph.init(game.getGraph());
		//addManualRobots();
		//game.startGame();
		//newThread.start();
		
		fruitArrayList= new ArrayList<Fruit>();
		if(!game.getFruits().isEmpty())
		{
			for (String fruit : game.getFruits()) 
			{
				Fruit currFruit = new Fruit(fruit);
				currFruit.setEdge(findFruitEdge(currFruit.getLocation()));
				fruitArrayList.add(currFruit);
			}
		}

		//init picture
		try 
		{
			robotImage = ImageIO.read(new File("C:\\Users\\Danielle2\\Desktop\\Eclipse-workspace\\Ex3OOP\\data\\robot.PNG"));
			bannaImage = ImageIO.read(new File("C:\\Users\\Danielle2\\Desktop\\Eclipse-workspace\\Ex3OOP\\data\\banana.PNG"));
			appleImage = ImageIO.read(new File("C:\\Users\\Danielle2\\Desktop\\Eclipse-workspace\\Ex3OOP\\data\\apple.PNG"));
		}

		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//get robots count for the scenario
		try 
		{
			String info = game.toString();
			JSONObject line;
			line = new JSONObject(info);
			JSONObject obj = line.getJSONObject("GameServer");
			robotsCount = obj.getInt("robots");
		}
		catch (JSONException e)
		{
			throw new RuntimeException("parse Json");
		}
		
		
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

		//let the player choose the type of the game
		String[] typeGame = {"Automatic", "Manual"};
		inputTypeGame = (String) JOptionPane.showInputDialog(null, "choose the type of the game: ", "Game type", JOptionPane.QUESTION_MESSAGE,null,typeGame,null);
		//repaint();
	}
	*/
	
	
	    @Override
	    public void actionPerformed(ActionEvent actionEvent) 
	    {
	        String str = actionEvent.getActionCommand();
	        if (str.equals("Manuel Game")) 
	        {
	            ManuelMode=true;
	            JFrame start = new JFrame();
	            try 
	            {
	                int level = chooseScenario();
	                if (level < 0 ) 
	                {
	                    JOptionPane.showMessageDialog(start, "Invalid level");
	                } 
	                else 
	                {
	                    game = Game_Server.getServer(level);
	                    String Graph_str = game.getGraph();
	                    currGraph = new DGraph();
	                    currGraph.init(Graph_str);
	                    this.remove(this.manualButton);
	                    this.remove(this.autoButton);
	                    repaint();
	                    
	                    game.startGame();
	        			//get robots count for the scenario
	        			try 
	        			{
	        				String info = game.toString();
	        				JSONObject line;
	        				line = new JSONObject(info);
	        				JSONObject obj = line.getJSONObject("GameServer");
	        				robotsCount = obj.getInt("robots");
	        			}
	        			catch (JSONException e)
	        			{
	        				throw new RuntimeException("parse Json");
	        			}
	        			Collection<node_data> listNodes = currGraph.getV();
	        			minX = getMinX(listNodes);
	        			maxX = getMaxX(listNodes);
	        			minY = getMinY(listNodes);
	        			maxY = getMaxY(listNodes);
	        			addManualRobots();
	                    clientThread.start();
	                }
	            } 
	            catch (Exception e) 
	            {
	                JOptionPane.showMessageDialog(start, "Invalid Pattern/Not entered any Number");
	                e.printStackTrace();
	            }
	        }
	        if (str.equals("Auto Game")) 
	        {
	            ManuelMode=false;
	            JFrame start = new JFrame();
	            try 
	            {
	            	int level = chooseScenario();
	                if (level < 0 ) 
	                {
	                    JOptionPane.showMessageDialog(start, "Invalid level");
	                } 
	                else 
	                {
	                    game = Game_Server.getServer(level);
	                    String Graph_str = game.getGraph();
	                    currGraph = new DGraph();
	                    currGraph.init(Graph_str);
	                    this.remove(this.manualButton);
	                    this.remove(this.autoButton);
	                    game.startGame();
	                    try 
	        			{
	        				String info = game.toString();
	        				JSONObject line;
	        				line = new JSONObject(info);
	        				JSONObject obj = line.getJSONObject("GameServer");
	        				robotsCount = obj.getInt("robots");
	        			}
	        			catch (JSONException e)
	        			{
	        				throw new RuntimeException("parse Json");
	        			}
	                    
	                    fruitArrayList= new ArrayList<Fruit>();
	            		if(!game.getFruits().isEmpty())
	            		{
	            			for (String fruit : game.getFruits()) 
	            			{
	            				Fruit currFruit = new Fruit(fruit);
	            				currFruit.setEdge(findFruitEdge(currFruit.getLocation()));
	            				fruitArrayList.add(currFruit);
	            			}
	            		}
	            		Collection<node_data> listNodes = currGraph.getV();
	        			minX = getMinX(listNodes);
	        			maxX = getMaxX(listNodes);
	        			minY = getMinY(listNodes);
	        			maxY = getMaxY(listNodes);
	                    drawAutoRobots(getGraphics());
	                    clientThread.start();
	                    
	                    repaint();

	                }
	            } 
	            catch (Exception e) 
	            {
	                JOptionPane.showMessageDialog(start, "Invalid Pattern/Not entered any Number");
	                e.printStackTrace();
	            }
	        }
	    }
  
	    /**
	@Override
	public void actionPerformed (ActionEvent event) 
	{
		
	}
		String action = event.getActionCommand();
		switch(action) 
		{
			case "move" : manualMove();
			break;
			
			/**default: //Default set to be kml
				Game_Server g = new Game_Server();
				//Game_Server g = new Game_Server(linePoints, fruitPoint ,robotPoint);
				try 
				{
					g.createKML(g, "C:\\Users\\danielle\\eclipse-workspace\\oop3\\data\\myGame.kml");
				}
				catch (ParseException e1) 
				{
					e1.printStackTrace();
				}
				break;
		}
		
 }*/
	@Override
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
			
			//draw nodes
			for (node_data currNode : currGraph.getV()) 
			{
				graph.setColor(Color.blue);
				Point3D srcNode = currNode.getLocation();
				double srcScaleXNode = scale(srcNode.x(),minX, maxX, 0+Offset, xRange);
				double srcScaleYNode = scale(srcNode.y(),minY, maxY, 0+Offset, yRange);
				graph.fillOval((int)srcScaleXNode, (int)srcScaleYNode, 12, 12);
				
				graph.setColor(Color.RED);
				graph.drawString(""+currNode.getKey(), (int)srcScaleXNode-4, (int)srcScaleYNode-4);
		    }
			
			//draw edge
			for (node_data currNode : currGraph.getV()) 
			{
				if ((currGraph.getE(currNode.getKey())!=null)) 
				{
					for (edge_data edge : currGraph.getE(currNode.getKey())) 
					{
						Point3D srcPoint = currNode.getLocation();
						Point3D destPoint = currGraph.getNode(edge.getDest()).getLocation();
						graph.setColor(Color.RED);
						double srcScaleX = scale(srcPoint.x(),minX, maxX, 0+Offset, xRange);
						double srcScaleY = scale(srcPoint.y(),minY, maxY, 0+Offset, yRange);
						double destScaleX = scale(destPoint.x(),minX, maxX, 0+Offset, xRange);
						double destScaleY = scale(destPoint.y(),minY, maxY, 0+Offset, yRange);
						
						((Graphics2D) graph).setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
						graph.drawLine((int)srcScaleX+5, (int)srcScaleY+5, (int)destScaleX+5, (int)destScaleY+5);
						
						graph.setColor(Color.black);
						graph.drawString(String.valueOf(Math.round((edge.getWeight()*100.0))/100.0), 1+(int)((srcScaleX*0.7)+(0.3*destScaleX)), (int)((srcScaleY*0.7)+(0.3*destScaleY)-2)); 
					}
				}	
			}
		
		//draw fruits
		if(game!=null && !game.getFruits().isEmpty())
		{
			for (String f:game.getFruits()) 
			{
				Fruit currFruit = new Fruit(f); 
				Point3D fruitLocation = currFruit.getLocation();
				double fruitScaleX = scale(fruitLocation.x() , minX, maxX, Offset, xRange);
				double fruitScaleY = scale(fruitLocation.y() , minY, maxY, Offset, yRange);
				if(currFruit.getType()<0)
				{
					//apple
					graph.drawImage(appleImage, (int)fruitScaleX, (int)fruitScaleY, this);
				}
				else
				{
					//banana
					graph.drawImage(bannaImage, (int)fruitScaleX, (int)fruitScaleY, this);
				}
				
			}
		}
		
		 if (PaintRobots) 
		 {
        		for (String r: game.getRobots()) 
        		{
    
        			Robot currRobot = new Robot (r);
        			Point3D robotLocation = currRobot.getLocation();
        			double robotScaleX = scale(robotLocation.x() , minX, maxX, Offset, xRange);
        			double robotScaleY = scale(robotLocation.y() , minY, maxY, Offset, yRange);
        			graph.drawImage(robotImage, (int)robotScaleX, (int)robotScaleY, this);
        		}
             }
         }
	}


	private void drawAutoRobots(Graphics graph)
	{
		int numFruit = game.getFruits().size();
		int robotKey;
		for (int i=0;i<numFruit&&robotsCount>0;i++) 
		{
			if (fruitArrayList.get(i).getType()==1) 
			{
				robotKey = fruitArrayList.get(i).getEdge().getSrc();
			}
			else 
			{
				
				robotKey = fruitArrayList.get(i).getEdge().getDest();
			}
			game.addRobot(robotKey);
			robotsCount--;
		}
		if (robotsCount>0 && numFruit<=0) 
		{
			int random = (int)(Math.random()*currGraph.getV().size());
			while (!game.getRobots().contains(random) && !currGraph.getV().contains(random)) 
			{
				robotsCount--;
				game.addRobot(random);	
			}
		}
		PaintRobots = true;
	}
	
	private void drawManRobots(Graphics graph)
	{
		for (String r: game.getRobots()) 
		{
			Robot currRobot = new Robot (r);
			Point3D robotLocation = currRobot.getLocation();
			double robotScaleX = scale(robotLocation.x() , minX, maxX, Offset, xRange);
			double robotScaleY = scale(robotLocation.y() , minY, maxY, Offset, yRange);
			graph.drawImage(robotImage, (int)robotScaleX, (int)robotScaleY, this);
		}
	}
	
	public int chooseScenario() 
	{
		JFrame window = new JFrame();
		int scenarioNum;
		try
		{
			String inputUser = JOptionPane.showInputDialog(window, "Please enter scenario number between 0-23");
			scenarioNum=Integer.parseInt(inputUser);
			
		}
		catch(Exception e)
		{
			throw new RuntimeException("The scenario must be a number, please try again");
		}
		if(scenarioNum<0 || scenarioNum>23) 
		{
			JOptionPane.showMessageDialog(window, "The number need to be between 0-23, please try again " );
			return -1;
		}
		else 
		{
			//game_service game = Game_Server.getServer(scenarioNum); 
			//String graphJson = game.getGraph();
			return scenarioNum;
		}
		
	}
	

	private static int nextNode(graph g, int src) 
	{
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	private edge_data findFruitEdge (Point3D fruitPoint) 
	{
		if(this.currGraph != null) 
		{
			if (this.currGraph.getV()!=null) 
			{
				for (node_data node: this.currGraph.getV()) 
				{
					if (this.currGraph.getE(node.getKey())!=null) 
					{
						for (edge_data edge : this.currGraph.getE(node.getKey())) 
						{
							Point3D src = this.currGraph.getNode(edge.getSrc()).getLocation();
							Point3D dest = this.currGraph.getNode(edge.getDest()).getLocation();
							double edgeDistance = Math.sqrt(Math.pow(src.x()-dest.x(), 2)+Math.pow(src.y()-dest.y(), 2));
							double fruitDistanceToPoints= Math.sqrt((Math.pow((src.x()-fruitPoint.x()), 2)+Math.pow((src.y()-fruitPoint.y()),2))) + Math.sqrt((Math.pow((fruitPoint.x()-dest.x()), 2)+Math.pow((fruitPoint.y()-dest.y()), 2)));
							if(Math.abs(fruitDistanceToPoints-edgeDistance)<epsilon)
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
	
	public ArrayList<Robot> nearestFruits(Game_Server g)
	{
		Double time;
		Fruit fruitEat = null;
		ArrayList<Fruit> leftFruitArray = new ArrayList<Fruit>();
		ArrayList<Robot> robotArray = new ArrayList<Robot>();
		for (String f: g.getFruits()) 
		{
			leftFruitArray.add(new Fruit (f));
		}
		while (!leftFruitArray.isEmpty()) 
		{
			int indexFruit = 0;
			int indexRobot = 0;
			
		}
		return null;
	}

	private double scale(double data, double r_min, double r_max, double t_min, double t_max) 
	{
		double res = ((data - r_min) / (r_max - r_min)) * (t_max - t_min) + t_min;
		return res;
	}
	

	public static double getMinX(Collection<node_data> nodes) 
	{
		for(node_data node : nodes) 
		{
			double temp = node.getLocation().x();
			if(temp<minX)
				minX=temp;
		}
		return minX;
	}
	public static double getMinY(Collection<node_data> nodes) 
	{
		for(node_data node : nodes) 
		{
			double temp = node.getLocation().y();
			if(temp<minY)
				minY=temp;
		}
		return minY;
	}
	public static double getMaxX(Collection<node_data> nodes) 
	{
		for(node_data node : nodes) 
		{
			double temp = node.getLocation().x();
			if(temp>maxX)
				maxX=temp;
		}
		return maxX;
	}
	public static double getMaxY(Collection<node_data> nodes) 
	{
		for(node_data node : nodes) 
		{
			double temp = node.getLocation().y();
			if(temp>maxY)
				maxY=temp;
		}
		return maxY;
	}
	
	private static void moveRobots(game_service game, graph gg) 
	{
		List<String> log = game.move();
		if(log!=null) 
		{
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) 
			{
				String robot_json = log.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
				
					if(dest==-1) 
					{	
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						//game.move();
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						System.out.println(ttt);
					}
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			}
		}
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
	
	
	private void timeLeft() 
	{
		long time=  this.game.timeToEnd()/1000;
		JFrame window = new JFrame();
		JOptionPane.showInputDialog(window, "The time left to the game is: "+ time);
				
	}
	
	private long startGame() 
	{
		return this.game.startGame();
	}
	
	private long stopGame() 
	{
		return this.game.stopGame();
	}
	
	private boolean isRunning() 
	{
		return game.isRunning();
	}
	
	@Override
	public void run() 
	{
		//timer();
		while (game!= null && game.isRunning()) 
		{
			try 
			{
				//synchronized (this)
				{
					//timer();
					game.move();
					repaint();
					//moveRobots(this.game,this.currGraph);
					Thread.sleep(200);
					//repaint();
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException("Exception in run time");
			}
			JOptionPane.showMessageDialog(null, "GameOver, Final Score is: ");
	    
		}
		
	}
	
	private void manualMove() 
	{
		String inputCurrKey = JOptionPane.showInputDialog("enter sorce key:");
		int currNode = -1;
		try 
		{
			currNode = Integer.parseInt(inputCurrKey);
		} 
		catch (Exception e)
		{
			throw new RuntimeException("Invalid key");
		}
		if(currNode==-1||!(currGraph.getV().contains(currNode))) 
		{
			JOptionPane.showInputDialog("the key doesnt exists");
		}
		String inputDestKey = JOptionPane.showInputDialog("Enter the key you went to move to:");
	    int destNode=-1;
		try 
		{
			destNode = Integer.parseInt(inputDestKey);
		} 
		catch (Exception e2) 
		{
			throw new RuntimeException("Invalid key");
		}
		if(currNode==-1||!(currGraph.getV().contains(currNode))) 
		{
			JOptionPane.showInputDialog("the key doesnt exists");
		}
		else 
		{
			game.chooseNextEdge(currNode, destNode);
		}
	}

	private void addManualRobots()
	{
		for(int i=0; i<robotsCount;i++)
		{
			int currNode = -1;
			try 
			{
				String inputCurrKey = JOptionPane.showInputDialog("enter sorce key:");
				currNode = Integer.parseInt(inputCurrKey);
				if(currNode==-1|| this.currGraph.getNode(currNode) == null) 
					{
						JOptionPane.showMessageDialog(null,"the key doesnt exists");
					}
				this.game.addRobot(currNode);
				PaintRobots = true;
			}
			catch (Exception e)
			{
				throw new RuntimeException("Invalid key");
			}
		}
		repaint();	
			
	}
	

	private void timer() 
	{
		long time = this.game.timeToEnd();
		if(this.game.isRunning())
		{
			timeLable= new JLabel("Time left" + (time/1000));
			
			//JOptionPane.showMessageDialog(null, time/1000);
			//StdDraw.textRight(StdDraw.xmax-0.0005, StdDraw.ymax-0.0005, "" + time/1000);
		}
		else 
		{
			JOptionPane.showMessageDialog(null, "game over");
			//StdDraw.textRight(StdDraw.xmax-0.0005, StdDraw.ymax-0.0005, "game over");
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
	
		Robot newRobot = null;
		if (ManuelMode) 
		{
			double x = scale(arg0.getX(),0,1300,0,1300);
			double y = scale(arg0.getY(),0,700,0,700);
			//int y = arg0.getY();
			Point3D mouseClickPoint= new Point3D(x, y);
			if(!firstpress) 
				
			{
				for (String rob: game.getRobots()) 
				{
					newRobot = new Robot(rob);
					double robotX = scale(newRobot.getLocation().x(),minX, maxX, 50, 1250);
					double robotY = scale(newRobot.getLocation().y(), minY, maxY, 50, 650);
                   
					
					//if(findPressedNode(mouseClickPoint)!=null) 
					//{
						firstpress=true;
						break;
					}
					
				}
			}
			if (firstpress) 
			{
				for(edge_data robotEdge: this.currGraph.getE(newRobot.getSrc()))
				{
					if(robotEdge.getDest()==newRobot.getDest())
					//if(findPressedNode(mouseClickPoint)!=null) 
					{
						game.chooseNextEdge(newRobot.getKey(), robotEdge.getDest());
						
						firstpress=false;
					//}
				}
				
			}
		}
	}
		
/**
		else if(this.robotsArrayList==null)
		{
			JOptionPane.showMessageDialog(null, "The is no Robot to move");
		}
		
		if(!canMove && this.robotsArrayList!= null)
		{
			JOptionPane.showMessageDialog(null, "The Robot can't move to where you chose");
		}
		
		
	}*/
	//return null if user pressed not on a node
	private node_data findPressedNode(Point3D mouseClickPoint) 
	{
		for(node_data currNode: this.currGraph.getV())
		{
			double distance= currNode.getLocation().distance3D(mouseClickPoint);
			if (distance<=epsilon)
			{
				node_data ansNode= currNode;
				return ansNode;
			}
		}
		return null;
	

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
		
	
}
