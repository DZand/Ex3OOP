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
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import gameClient.Fruit;
import Server.Game_Server;
import gameClient.Robot;
import Server.game_service;
import utils.Point3D;
import dataStructure.*;
import algorithms.*;
import gameClient.KML_Logger;


public class MyGameGUI extends JFrame implements ActionListener , MouseListener, Runnable 
{
	public BufferedImage robotImage; //robot icon.
	public BufferedImage appleImage; 
	public BufferedImage bannaImage; 
	public ArrayList<Fruit> fruitArrayList;//list of fruits.
	public ArrayList<Robot> robotsArrayList;//list of robots.
	private DGraph currGraph;
	private game_service game;
	private static double maxX = Double.NEGATIVE_INFINITY;
	private static double maxY = Double.NEGATIVE_INFINITY;
	private static double minX = Double.POSITIVE_INFINITY;
	private static double minY = Double.POSITIVE_INFINITY;
	private final int Offset = 50;
	private final int xRange = 600;
	private final int yRange = 600;
	private int robotsCount;
	private double epsilon = 0.001;
	private Boolean PaintRobots;
	boolean ManuelMode;
	Thread clientThread;
	private boolean firstpress=false;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private static int score=0;
	public static Graph_Algo algo;
	public boolean AutoMode=false;
	public int level;
	
	
	public static void main(String[] args) 
	{
		MyGameGUI gui = new MyGameGUI();
		gui.setVisible(true);
	}
	
	
	public MyGameGUI() 
	{
		init();
		//KML_Logger kml = new KML_Logger();
	}
	
	public  void guiCreateKML() throws JSONException, ParseException, InterruptedException, IOException 
	{
		KML_Logger.createKML(this.game);
	}

	
	  private void init() 
	  {
		   
	        PaintRobots = false;
	        this.setSize(1700, 10000);
	        setTitle("Best game ever");
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.addMouseListener(this);
			//init picture
			try 
			{
				robotImage = ImageIO.read(new File("data/robot.png"));
				bannaImage = ImageIO.read(new File("data/banana.png"));
				appleImage = ImageIO.read(new File("data/apple.png"));
			}

			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			MenuBar menuBar = new MenuBar();
			Menu type = new Menu("Type");
			menuBar.add(type);
			
			Menu save = new Menu("Save");
			menuBar.add(save);
			
			MenuItem auto = new MenuItem("Auto Game");
			auto.addActionListener(this);
			
			MenuItem manuel = new MenuItem("Manuel Game");
			manuel.addActionListener(this);
			
			type.add(auto);
			type.add(manuel);
			
			MenuItem saveKml = new MenuItem("Save To KML");
			saveKml.addActionListener(this);
			
			save.add(saveKml);
			
			this.setMenuBar(menuBar);
			
			this.addMouseListener(this);

	        clientThread = new Thread(this);
	    }

	
	    @Override
	    public void actionPerformed(ActionEvent event) 
	    {
	        String action = event.getActionCommand();
	        if (action.equals("Manuel Game")) 
	        {
	            ManuelMode=true;
	            JFrame start = new JFrame();
	            try 
	            {
	                level = chooseScenario();
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
	                    repaint();
	        			addManualRobots();
	        			game.startGame();
	        			clientThread.start();
	                    
	                }
	            } 
	            catch (Exception e) 
	            {
	                JOptionPane.showMessageDialog(start, "Error in manual game");
	                e.printStackTrace();
	            }
	        }
	        if (action.equals("Auto Game")) 
	        {
	            ManuelMode=false;
	            JFrame start = new JFrame();
	            try 
	            {
	            	level = chooseScenario();
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
	                    drawAutoRobots();
	                    PaintRobots=true;
	                    game.startGame();
	                    clientThread.start();
	                    //repaint();

	                }
	            } 
	            catch (Exception e) 
	            {
	                JOptionPane.showMessageDialog(start, "Error in automatic game");
	                e.printStackTrace();
	            }
	        }
	        
	        if(action.equals("Same To KML"))
	        {
	        	
	        }
	    }
  
	@Override
	public void paint(Graphics graph) 
	{
		super.paint(graph);
		graph.setFont(new Font ("Courier", Font.PLAIN,20));
		if (currGraph == null && game !=null ) 
		{
			JFrame mesg = new JFrame(); 
			JOptionPane.showMessageDialog(mesg, "There Isn't Graph To Show");
		}
		else if(game !=null  && currGraph!=null)
		{
			
			//draw nodes
			for (node_data currNode : currGraph.getV()) 
			{
				graph.setColor(Color.blue);
				Point3D srcNode = currNode.getLocation();
				double srcScaleXNode = scale(srcNode.x(),minX, maxX, 0+Offset, (double)xRange);
				double srcScaleYNode = scale(srcNode.y(),minY, maxY, 0+Offset, (double)yRange);
				graph.fillOval((int)srcScaleXNode, (int)srcScaleYNode, 12, 12);
				
				graph.setColor(Color.blue);
				graph.drawString("" + currNode.getKey(), (int)srcScaleXNode, (int)srcScaleYNode+20);

			//draw edge
				if ((currGraph.getE(currNode.getKey())!=null)) 
				{
					for (edge_data edge : currGraph.getE(currNode.getKey())) 
					{
						Point3D srcPoint = currNode.getLocation();
						Point3D destPoint = currGraph.getNode(edge.getDest()).getLocation();
						graph.setColor(Color.GRAY);
						double destScaleX = scale(destPoint.x(),minX, maxX, 0+Offset, (double)xRange);
						double destScaleY = scale(destPoint.y(),minY, maxY, 0+Offset, (double)yRange);
						 Graphics2D g2 = (Graphics2D) graph;
	                     g2.setStroke(new BasicStroke(2));
	                     g2.drawLine((int) srcScaleXNode, (int) srcScaleYNode, (int) destScaleX, (int) destScaleY);

	                     graph.setColor(Color.BLACK);
	                        int directed_x = (int) (srcScaleXNode * 0.15 + destScaleX * 0.85);
	                        int directed_y = (int) (srcScaleYNode * 0.15 + destScaleY * 0.85);
	                        graph.fillOval(directed_x - 4, directed_y - 2, 7, 7);
	                        graph.setColor(Color.DARK_GRAY);
	                        graph.drawString("" + df2.format(edge.getWeight()), directed_x, directed_y);
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
				double fruitScaleX = scale(fruitLocation.x() , minX, maxX, 0+Offset, (double)xRange);
				double fruitScaleY = scale(fruitLocation.y() , minY, maxY, 0+Offset, (double)yRange);
				if(currFruit.getType()<0)
				{
					//apple
					graph.drawString(currFruit.getValue() + " v", (int) fruitScaleX - 9, (int) fruitScaleY + 11);
					graph.drawImage(appleImage, (int)fruitScaleX-15, (int)fruitScaleY-10, null);
				}
				else
				{
					//banana
					graph.drawImage(bannaImage, (int)fruitScaleX-15, (int)fruitScaleY-10, null);
					graph.drawString(currFruit.getValue() + " ^", (int) fruitScaleX - 9, (int) fruitScaleY + 11);
				}
				
			}
		}
		
		 if (PaintRobots) 
		 {
        		for (String r: game.getRobots()) 
        		{
    
        			Robot currRobot = new Robot (r);
        			Point3D robotLocation = currRobot.getLocation();
        			double robotScaleX = scale(robotLocation.x() , minX, maxX, 0+Offset, (double)xRange);
        			double robotScaleY = scale(robotLocation.y() , minY, maxY, 0+Offset, (double)yRange);
        			graph.drawImage(robotImage, (int)robotScaleX-15, (int)robotScaleY-10, null);
        		}
          }
		 //draw timer
		 timer(graph);
		 //calculate score
		 calcScore();
       }
	}
	
	private void calcScore()
	{
		 try 
         {
             String info = game.toString();
             JSONObject line = new JSONObject(info);
             JSONObject GameServer = line.getJSONObject("GameServer");
             score = GameServer.getInt("grade");
         }
         catch (JSONException e) 
         {
             e.printStackTrace();
         } 
	}
	
	private void drawAutoRobots()
	{
		int numFruit = game.getFruits().size();
		ArrayList<Fruit> fruitsTemp = fruitArrayList;
		int robotKey=0;
		Fruit fRemove=null;
		for (int i =0; i<robotsCount;i++) 
		{
			if (fruitsTemp==null) 
			{
				robotKey = (int)(Math.random()*currGraph.getV().size());
				while (!game.getRobots().contains(robotKey) && !currGraph.getV().contains(robotKey)) 
				{
					game.addRobot(robotKey);
				}	
			}
			else 
			{
				double maxF = Integer.MIN_VALUE;
				for (Fruit f: fruitsTemp) 
				{
					if (f.getValue()>maxF) 
					{
						maxF = f.getValue();
						if (f.getType()==1) 
						{
							robotKey = f.getEdge().getSrc();
							fRemove=f;
						}
						else 
						{
							robotKey = f.getEdge().getDest();
							fRemove=f;
						}
					}
				}
				game.addRobot(robotKey);
				fruitsTemp.remove(fRemove);
			}
		}
		PaintRobots=true;
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
			return scenarioNum;
		}
		
	}
	


	public edge_data findFruitEdge (Point3D fruitPoint) 
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
	
	public void moveRobots(game_service game , DGraph g) 
	{
	    List<String> log = game.move();
	    if(log!=null) {
	    	long t = game.timeToEnd();
	        for(int i=0;i<log.size();i++) 
	        {
	            String robot_json = log.get(i);
	            try 
	            {
	                JSONObject line = new JSONObject(robot_json);
	                JSONObject ttt = line.getJSONObject("Robot");
	                int rid = ttt.getInt("id");
	                int src = ttt.getInt("src");
	                int dest = ttt.getInt("dest");
	                if(dest==-1) {
	                    dest = nextNode(src,game);
	                    game.chooseNextEdge(rid, dest);
	                }
	                game.move();
	            }
	            catch (JSONException e) {e.printStackTrace();}
	        }
		 }
	}



	@Override
	public void run() 
	{
		while (game!= null && game.isRunning()) 
		{
			try 
			{
				{
					if(ManuelMode==false)
					{
						repaint();
						moveRobots(this.game,this.currGraph);
						game.move();
						//moveRobots(this.game,this.currGraph);
						//game.move();
						//repaint();
						Thread.sleep(1000);
						
					}
					else if(ManuelMode==true)
					{
						game.move();
						repaint();
						Thread.sleep(1000);
					}
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException("Exception in run time");
			}
			
		}
		JOptionPane.showMessageDialog(null, "GameOver, Final Score is: "+ score);
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
	

	private void timer(Graphics graph) 
	{
		 graph.drawString("TIME TO END: "+ (int)(game.timeToEnd()/1000), (int)minX+1000,(int)maxY+100);
	}
	

	
	 public void mouseClicked(MouseEvent e1) 
	 {
		 Robot newRobot = null;
		 boolean canMove=false;
	        if(ManuelMode)
	        {
	            if(game.getRobots().size()==1)
	            {
	                Robot r = new Robot(game.getRobots().get(0));
	                double e1_get_y=e1.getY();
	                double e1_get_x=e1.getX();
	                for (edge_data ed : currGraph.getE(r.getSrc())) 
	                {
	                    double ndlocationX = scale(currGraph.getNode(ed.getDest()).getLocation().x(), minX, maxX, 0+Offset, (double)xRange);
	                    double ndlocationY = scale(currGraph.getNode(ed.getDest()).getLocation().y(), minY, maxY,  0+Offset, (double)yRange);
	                    if (Math.abs(e1_get_x - ndlocationX) < 25 && Math.abs(e1_get_y - ndlocationY) < 25) 
	                    {
	                        game.chooseNextEdge(0, ed.getDest());
	                        canMove=true;
	                    }
	                }
	            }
	            else 
	            {
	                if (!firstpress) 
	                {
	                    for (int i = 0; i < game.getRobots().size(); i++) 
	                    {
	                    	newRobot = new Robot(game.getRobots().get(i));
	                        double RobotlocationX = scale(currGraph.getNode(newRobot.getSrc()).getLocation().x(), minX, maxX, 0+Offset, (double)xRange);
	                        double RobotlocationY = scale(currGraph.getNode(newRobot.getSrc()).getLocation().y(),  minY, maxY,  0+Offset, (double)yRange);
	                        double e1_get_y=e1.getY();
	    	                double e1_get_x=e1.getX();
	                        if (Math.abs(RobotlocationX - e1_get_x) < 35 && Math.abs(RobotlocationY - e1_get_y) < 35) 
	                        {
	                            firstpress = true;
	                            break;
	                        }
	                    }
	                }
	                if (firstpress) 
	                {
	                	double e1_get_y=e1.getY();
    	                double e1_get_x=e1.getX();
	                    for (edge_data ed : currGraph.getE(newRobot.getSrc())) 
	                    {
	                        double ndlocationX = scale(currGraph.getNode(ed.getDest()).getLocation().x(), minX, maxX, 0+Offset, (double)yRange);
	                        double ndlocationY = scale(currGraph.getNode(ed.getDest()).getLocation().y(), minY, maxY, 0+Offset, (double)xRange);
	                        if (Math.abs(e1_get_x - ndlocationX) < 25 && Math.abs(e1_get_y - ndlocationY) < 25) 
	                        {
	                            game.chooseNextEdge(newRobot.getKey(), ed.getDest());
	                            firstpress = false;
	                            canMove=true;
	                        }
	                    }
	                }
	            }
	        }
	        if(!canMove)
            {
            	 JOptionPane.showMessageDialog(null, "The choosen node is not a neighbor, it is not possible to move");
            }
	    }

	
	 private int nextNode(int src,game_service game) 
	 {
	        Graph_Algo graphAlgo=new Graph_Algo();
	        int key =-1;
	        double shortestpathdist=Integer.MAX_VALUE;
	        graphAlgo.init(currGraph);
	        for (int j = 0; j <fruitArrayList.size() ; j++)
	        {
	            edge_data edge=fruitArrayList.get(j).getEdge();
	            System.out.println("edge: "+edge.getSrc());
	            double returnshortst = graphAlgo.shortestPathDist(src, edge.getDest());
	            System.out.println("ahortest: "+returnshortst);
	            if (returnshortst < shortestpathdist) 
	            {
	                try 
	                {
	                    shortestpathdist = graphAlgo.shortestPathDist(src, edge.getDest());
	                    key = graphAlgo.shortestPath(src, edge.getDest()).get(1).getKey();
	                }
	                catch (Exception e)
	                {
	                    key=edge.getSrc();
	                }
	            }
	        }
	        return key;
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