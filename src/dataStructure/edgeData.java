package dataStructure;

import java.io.Serializable;

public class edgeData implements edge_data ,Serializable
{

	int src;
	int dest;
	double weight;
	String info;
	int tag;
	
	public edgeData () 
	{
		this.src = 0;
		this.dest = 0;
		this.weight = 0;
		this.info = null;
		this.tag = 0;
	}
	
	public edgeData (int src,int dest,double weight) 
	{
		this.src = src;
		this.dest = dest;
		this.weight = weight;
		this.info = null;
		this.tag = 0;
	}
	
	public edgeData(nodeData src, nodeData dest, double weight, int tag) 
	{

		this.src = src.key;
		this.dest = dest.key;
		this.weight = weight;
		this.info = null;
		this.tag = tag;
	}
	
	public edgeData (edgeData edgeT) 
	{
		this.src = edgeT.src;
		this.dest = edgeT.dest;
		this.weight = edgeT.weight;
		this.info = edgeT.info;
		this.tag = edgeT.tag;
	}

	public int getSrc() 
	{
		return this.src;
	}

	@Override
	public int getDest() 
	{
		return this.dest;
	}

	@Override
	public double getWeight() 
	{
		return this.weight;
	}

	@Override
	public String getInfo() 
	{
		return this.info;
	}

	@Override
	public void setInfo(String s) 
	{
		this.info = s;
	}

	@Override
	public int getTag() 
	{
		return tag;
	}

	@Override
	public void setTag(int t) 
	{
		this.tag = t;
	}
	public void setWeight(double w) 
	{
		this.weight = w;
	}

}
