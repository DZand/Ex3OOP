package gameClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.TimeSpan;

import Server.Fruit;
import Server.Game_Server;
import Server.RobotG;

public class KML_Logger 
{
	
	public ArrayList<RobotG> robotArray;
	public ArrayList<Fruit> fruitArray;
	
	
	public static void createKML(Game_Server g, String f) throws ParseException 
	{

		ShortestPathAlgo algo = new ShortestPathAlgo();
		MyGameGUI tempGUI = new MyGameGUI();
		ArrayList<RobotG> robotArray = new ArrayList<RobotG>();
		ArrayList<RobotG> robotArray2 = new ArrayList<RobotG>();
		try 
		{
			robotArray = tempGUI.nearestFruits(g);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}

		Kml kml = new Kml();
		Document doc = kml.createAndSetDocument();

		RobotG pac = new RobotG(robotArray.get(robotArray.size()-1));
		boolean flag = true;
		int arr[] = new int[g.getRobots().size()];
		int ind = 0;
		robotArray2.add(pac);
		for (int i = robotArray.size()-1; i >= 0; i--) 
		{
			for (int j = 0; j < robotArray2.size(); j++)
			{
				if(robotArray.get(i).getID() == robotArray2.get(j).getID()) 
				{
					flag = false;
					break;	
				}
			}
			if(flag == true) 
			{
				robotArray2.add(robotArray.get(i));
				arr[ind] = i;
				ind++;
			}
			flag = true;
		}
		for (int i = 0; i < arr.length; i++) 
		{
			robotArray.remove(arr[i]);
		}

		for(RobotG it: robotArray2) 
		{
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//			long millis = Date2Millis(date);
			Placemark p = doc.createAndAddPlacemark();
//			TimeSpan s = p.createAndSetTimeSpan();
//			String str = Millis2Date(millis+(long)(it.getTime())*1000);
//			String[] strA = str.split(" ");
//			str = strA[0] + "T" + strA[1]+"Z"; 
//			s.setBegin(str);
			p.withDescription("Mac: " + it.getID() + "\nType: RobotG")
			.withOpen(Boolean.TRUE).createAndSetPoint().
			addToCoordinates(it.getLocation().x(),it.getLocation().y());
		}

		for (RobotG it: robotArray) 
		{ 
			Placemark p = doc.createAndAddPlacemark();
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			long millis = Date2Millis(date);
			millis += it.getTime()*100;
			date = Millis2Date(millis);
			String date1 = Millis2Date(Date2Millis(date)+1000);
			String[] date2 = date.split(" ");
			date = date2[0]+'T'+date2[1]+'Z';
			String[] date3 = date1.split(" ");
			date1 = date3[0]+'T'+date3[1]+'Z';
			TimeSpan s = p.createAndSetTimeSpan();
			s.setBegin(date);
			s.setEnd(date1);
			p.withDescription("Mac: " + it.getID() + "\nType: RobotG")
			.withOpen(Boolean.TRUE).createAndSetPoint().
			addToCoordinates(it.getLocation().x(),it.getLocation().y());
		}

		for(Fruit it: g.fruitArray) 
		{
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			long millis = Date2Millis(date);
			Placemark p = doc.createAndAddPlacemark();
			TimeSpan s = p.createAndSetTimeSpan();
			String str = Millis2Date(millis+(long)(it.endTime)*1000);
			String[] strA = str.split(" ");
			str = strA[0] + "T" + strA[1]+"Z"; 
			s.setEnd(str);
			p.withDescription("Mac: " + it.getID() + "\nType: fruit")
			.withOpen(Boolean.TRUE).createAndSetPoint().
			addToCoordinates(it.getLocation().x(),it.getLocation().y());
		}

		try 
		{
			kml.marshal(new File(f));
			/**
			 * write to kml file.
			 */
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
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
		Date time = (Date) format.parse(date.toString());
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
}
