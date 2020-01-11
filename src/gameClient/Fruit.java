package gameClient;

import org.json.JSONObject;
import utils.Point3D;

public class Fruit 
{
	
	
    private Point3D _fruitLocation;
    private double _fruitValue;
    private int _fruitType;
    //private edge_data _fruitEdge;


    public Fruit() 
    {
    	this._fruitLocation =null;
    }

    public Fruit(double v, Point3D p,int type) 
    {
    	this._fruitType=type;
        this._fruitValue = v;
        this._fruitLocation = new Point3D(p);
    }
    

    
	public Fruit (String fruitJson) 
	{

		try 
		{
			JSONObject objFru = new JSONObject(fruitJson);
			JSONObject currntFruit = objFru.getJSONObject("Fruit");
			String[] positionFruit = currntFruit.getString("pos").split(",");
			this._fruitLocation = new Point3D(Double.parseDouble(positionFruit[0]), Double.parseDouble(positionFruit[1]));
			this._fruitType = currntFruit.getInt("type");
			this._fruitValue = currntFruit.getDouble("value");
			
					
		} 
		catch (Exception e)
		{
			System.out.println("The parse from json failed.");
		}
	}
    
  

    public int getType() 
    {
      return this._fruitType;
    }

    public Point3D getLocation() 
    {
        return new Point3D(this._fruitLocation);
    }



    public double getValue() 
    {
        return this._fruitValue;
    }


}
	
	