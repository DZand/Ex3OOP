package dataStructure;

import java.io.Serializable;

import utils.Point3D;

public class nodeData implements node_data ,Serializable
{
	 
	int key; 
	Point3D location;
	double Weight;
	String info;
	int tag;
	
	public nodeData() 
	{
		this.key=0;
		this.location=null;
		this.Weight=0;
		this.tag=0;
		this.info=null;
	}
	
	public nodeData(nodeData node) 
	{
		this.key = node.key;
		this.location = new Point3D (node.location);
		this.Weight = node.Weight;
		this.info = node.info;
		this.tag = node.tag;
	}
	public nodeData (int key,double weight,int tag,Point3D p,String info) 
	{
		this.key= key;
		this.Weight = Weight;
        this.tag=tag;
        this.location=new Point3D(p);
        this.info=info;
	}

	public nodeData(int key,double weight,Point3D p,int tag) 
	{
		
		this.key=key;
		this.location=new Point3D(p);
		this.Weight=weight;
		this.tag=tag;
		this.info= null;
	}
	
	
	public nodeData(int key, Point3D p) 
	{
		this.key = key;
		this.location = new Point3D (p);
		this.Weight = 0;
		this.info = null;
		this.tag = 0;
	}

	@Override
	public int getKey() 
	{
		return this.key;
	}

	@Override
	public Point3D getLocation() 
	{
		return this.location;
	}

	@Override
	public void setLocation(Point3D p) 
	{
		this.location = new Point3D (p);
	}

	@Override
	public double getWeight() 
	{
		return this.Weight;
	}

	@Override
	public void setWeight(double w) 
	{
		this.Weight = w;
	}

	@Override
	public String getInfo() 
	{
		return this.info;
	}

	@Override
	public void setInfo(String s) 
	{
		this.info =s;
	}

	@Override
	public int getTag() 
	{
		return this.tag;
	}

	@Override
	public void setTag(int t) 
	{
		this.tag=t;
	}

}
