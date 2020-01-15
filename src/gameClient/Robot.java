package gameClient;


import org.json.JSONObject;
import utils.Point3D;

public class Robot 
{
		private Point3D _robotLocation;
		private double _robotValue;
	    private int _robotKey;
	    private int _robotSrc;
	    private int _robotDest;
	    private  double _robotSpeed;

	    public  Robot()
	    {
	    	this._robotLocation=null;
	    }

	    
	    public Robot (String jsonRobot) 
		{
			try 
			{
				JSONObject objRob = new JSONObject(jsonRobot);
				JSONObject currntRobot = objRob.getJSONObject("Robot");
				String[] positionRob = currntRobot.getString("pos").split(",");
				this._robotLocation = new Point3D(Double.parseDouble(positionRob[0]), Double.parseDouble(positionRob[1]));
				this._robotKey = currntRobot.getInt("id");
				this._robotValue = currntRobot.getDouble("value");
		        this._robotSrc=currntRobot.getInt("src");
		        this._robotDest=currntRobot.getInt("dest");
		        this._robotSpeed=currntRobot.getDouble("speed");
			} 
			catch (Exception e)
			{
				System.out.println("The parse from json file failed.");
			}
		}

		public String toJSON() {
			String ans = "{\"Robot\":{\"id\":" + this._robotKey + "," + "\"value\":" + this._robotValue + "," + "\"src\":" + this._robotSrc
					+ "," + "\"dest\":" + this._robotDest + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\""
					+ this._robotLocation.toString() + "\"" + "}" + "}";
			return ans;
		}
	    
	    public Point3D getLocation() 
	    {
	        return this._robotLocation;
	    }

	    public void setLocation(Point3D location) 
	    {
	        this._robotLocation = location;
	    }
	    
	    public int getKey() 
	    {
	        return _robotKey;
	    }

	    public void setKey(int id) 
	    {
	        this._robotKey = id;
	    }

	    public double getValue() 
	    {
	        return _robotValue;
	    }

	    public void setValue(double value) 
	    {
	        this._robotValue = value;
	    }

	    public int getSrc() 
	    {
	        return _robotSrc;
	    }

	    public void setSrc(int src) 
	    {
	        this._robotSrc = src;
	    }

	    public int getDest() 
	    {
	        return this._robotDest;
	    }

	    public void setDest(int dest) 
	    {
	        this._robotDest = dest;
	    }

	    public double getSpeed() 
	    {
	        return this._robotSpeed;
	    }

	    public void setSpeed(double speed) 
	    {
	        this._robotSpeed = speed;
	    }


	}

