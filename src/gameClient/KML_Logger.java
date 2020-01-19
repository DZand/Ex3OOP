package gameClient;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.TimeSpan;



import Server.game_service;


public class KML_Logger 
{
	
	public static Kml kml;
	public ArrayList<Robot> robotArray;
	public static ArrayList<Fruit> fruitArray;
	public static Document doc;
	public static int i;
	public static MyGameGUI myGameGUI;
	
	public static void createKML(game_service g) throws ParseException, IOException, InterruptedException 
	{
		Kml kml = new Kml();
		doc = kml.createAndSetDocument().withName("kml file").withOpen(true);
		i = 0;
		MyGameGUI tempGUI = new MyGameGUI();
		ArrayList<Robot> robotArray = new ArrayList<Robot>();
		ArrayList<Fruit> fruitArray = new ArrayList<Fruit>();
		if(!g.getFruits().isEmpty())
		{
			for(String fruit: g.getFruits())
			{
				Fruit currFruit = new Fruit(fruit);
				currFruit.setEdge(tempGUI.findFruitEdge(currFruit.getLocation()));
				fruitArray.add(currFruit);	
			}
		}
		if(!g.getRobots().isEmpty())
		{
			for(String robot: g.getRobots())
			{
				Robot currRobot = new Robot(robot);
				robotArray.add(currRobot);	
			}
		}
		
		
		while(g.isRunning())
		{
			Thread.sleep(200);
			i++;
			kmlRobots(robotArray);
			kmlFruits(fruitArray);
		}
		System.out.println("arrived");
		CreatFile();
	}
	
	public static void kmlRobots(ArrayList<Robot> robotArray) throws ParseException
	{
		for(Robot robot: robotArray)
		{
			  Icon ff = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/parking_lot.png");
			  Placemark plmark = doc.createAndAddPlacemark();
              ff.setViewBoundScale(1);
              ff.setViewRefreshTime(1);
              ff.withRefreshInterval(1);
              IconStyle pp = new IconStyle();
              pp.setScale(1);
              pp.setHeading(1);
              pp.setColor("ff007db3");
              pp.setIcon(ff);
              plmark.createAndAddStyle().setIconStyle(pp);
              plmark.withDescription("Mac: " + "\nType: CAR").withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(robot.getLocation().x(), robot.getLocation().y());
              String time1 = Millis2Date(Date2Millis(TimeNow()) + i * 1000);
              String time2 = Millis2Date(Date2Millis(TimeNow()) + (i + 1) * 1000);
              String[] aa = time1.split(" ");
              time1 = aa[0] + "T" + aa[1] + "Z";
              String[] bb = time2.split(" ");
              time2 = bb[0] + "T" + bb[1] + "Z";
              TimeSpan a = plmark.createAndSetTimeSpan();
              a.setBegin(time1);
              a.setEnd(time2);
              
		}
	}
	
	public static void kmlFruits(ArrayList<Fruit> fruitArray) throws ParseException
	{
		for(Fruit fruit: fruitArray)
		{
		    Placemark placmark = doc.createAndAddPlacemark();
            Icon ff = new Icon();

            ff.setHref("https://img.favpng.com/0/3/15/super-mario-odyssey-super-mario-bros-luigi-mushroom-png-favpng-qEkUCTh1rLw3PeUCFxR7x3YKb.jpg");
            ff.setViewBoundScale(1);
            ff.setViewRefreshTime(1);
            ff.withRefreshInterval(1);
            IconStyle pp = new IconStyle();
            pp.setScale(1);
            pp.setHeading(1);
            pp.setColor("ff007db3");
            pp.setIcon(ff);
            placmark.createAndAddStyle().setIconStyle(pp);
            placmark.withDescription("MAC: " + "\nType: FRUIT").withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(fruit.getLocation().x(), fruit.getLocation().y());
            String time1 = Millis2Date(Date2Millis(TimeNow()) + i * 1000);
            String time2 = Millis2Date(Date2Millis(TimeNow()) + (i + 1) * 1000);
            String[] aa = time1.split(" ");
            time1 = aa[0] + "T" + aa[1] + "Z";
            String[] bb = time2.split(" ");
            time2 = bb[0] + "T" + bb[1] + "Z";
            TimeSpan b = placmark.createAndSetTimeSpan();
            b.setBegin(time1);
            b.setEnd(time2);
		}
	}
	
	
	public static void CreatFile() 
	{
		System.out.println("here");
		try 
		{
			kml.marshal(new File("running.kml"));
		    System.out.println("create kml file");
		}
		catch (Exception e)
		{
		    System.out.println("Fail create");
		}
	}
	

	/**
	 * This function converse a String date to milliseconds.
	 * @throws ParseException an exception while parsing.
	 * @param date represent the date.
	 * @return the date in milliseconds.
	 */
	public static long Date2Millis (String date) throws ParseException 
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		Date time = format.parse(date.toString());
		long millis = time.getTime();
		return millis;
	}

	/**
	 * This function converse milliseconds to a String date.
	 * @param millis represent the date in millisecond.
	 * @return the date.
	 */
	public static String Millis2Date(long millis) 
	{
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return date.format(new Date(millis));
	}
	
	private static String TimeNow()
	{
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}