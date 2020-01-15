package dataStructure;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.Node;

import utils.Point3D;

public class DGraph implements graph,Serializable
{
	public Hashtable<Integer,node_data> node;
	public Hashtable<Integer , Hashtable <Integer,edge_data>> edge;
	public int mc;
	public int edgeSize;

	public DGraph() 
	{
		node = new Hashtable<Integer,node_data>();
		edge = new Hashtable<Integer , Hashtable <Integer,edge_data>>();
		mc=0;
		edgeSize=0;
	}
	
	public DGraph(graph graph) 
	{
		node=((DGraph)graph).getNodeHash();
		edge=((DGraph)graph).getNodeHash();
		mc=((DGraph)graph).getMC();
		edgeSize=((DGraph)graph).edgeSize();
	}
	
	@Override
	public node_data getNode(int key) 
	{
		if(!this.node.containsKey(key))
		{
			return null;
		}
		return node.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) 
	{
		if(edge.get(src)!=null)
		{
			return edge.get(src).get(dest);
		}
		return null;
		
	}

	@Override
	public void addNode(node_data n) 
	{
		node.put(n.getKey(), n);
		mc++;
	
	}

	@Override
	public void connect(int src, int dest, double w) 
	{
		if (getNode(src)!=null && getNode(dest)!=null) 
		{
			edgeData tempEdge = new edgeData (src,dest,w);
			if (edge.get(src)==null)
			{
				Hashtable<Integer, edge_data> temp = new Hashtable<Integer,edge_data>();
				temp.put(dest,tempEdge);
				this.edge.put(src, temp);
			}
			else 
			{
				if (edge.get(src).get(dest)!=null) 
				{
					//if the edge already exist and the same weight
					if(edge.get(src).get(dest).getWeight()==w)
					{
						throw new RuntimeException ("the edge alredy exist");
					}
					else
					{
						((edgeData) this.edge.get(src).get(dest)).setWeight(w);
					}
					
				}
				this.edge.get(src).put(dest,tempEdge);
			}
		mc ++; 
		edgeSize++;
		}
	}

	@Override
	public Collection<node_data> getV() 
	{
		return node.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) 
	{
		if(edge.get(node_id)!=null)
		{
			return edge.get(node_id).values();
		}
		return null;
	}

	@Override
	public node_data removeNode(int key) 
	{
		node_data removeNode = getNode(key);
		if (removeNode!=null) 
		{
			mc++;
			node.remove(key);
			Iterator it = this.edge.entrySet().iterator();
			//remove all edges with key as dest
			while(it.hasNext()) 
			{
				Map.Entry cureentEdge = (Entry) it.next();
				int keySrc = (int) cureentEdge.getKey();
				if(edge.get(keySrc).get(key)!=null) 
				{
					removeEdge(keySrc, key);
				}
			}
			//remove all edges with key as src
			if (edge.get(key)!=null) 
			{
				edge.remove(key);
			}
		}
		return removeNode;
	}

	@Override
	public edge_data removeEdge(int src, int dest) 
	{
		edge_data removeEdge = getEdge(src, dest);
		if (removeEdge !=null) 
		{
			edge.get(src).remove(dest);
			edgeSize--;
			mc++;
			if (edge.get(src).values().isEmpty()) 
			{
				edge.remove(src);
			}
		}
		return removeEdge;
	}

	@Override
	public int nodeSize() 
	{
		return node.size();
	}

	@Override
	public int edgeSize() 
	{
		return edgeSize;
	}

	@Override
	public int getMC() 
	{
		return mc;
	}
	
	public Hashtable getNodeHash()
	{
		return this.node;
	}

	public Hashtable getEdgeHash()
	{
		return this.edge;
	}
	

	public void setEdgeHash(Hashtable<Integer, Hashtable<Integer, edge_data>> hashedges) 
	{
		this.edge = hashedges;
		
	}
	public void setHashnodes(Hashtable<Integer, node_data> hashnodes) 
	{
		this.node = hashnodes;
	}
	
	public void setEdgeSize(Hashtable<Integer, Hashtable<Integer, edge_data>> hashedges)
	{
		this.edgeSize=hashedges.size();
	}
	public void init(String g) 
	{
		try 
		{
			JSONObject obj = new JSONObject(g);
			JSONArray nodesArray = obj.getJSONArray("Nodes");
			JSONArray edgeArray = obj.getJSONArray("Edges");
			for (int i =0; i< nodesArray.length(); i++) 
			{
				JSONObject currNode = (JSONObject) nodesArray.get(i);
				String pointString = currNode.getString("pos");
				int keyNode = currNode.getInt("id");
				Point3D point = null;
				String[] listOfPos =  pointString.split(",");
				try 
				{
					point = new Point3D(Double.parseDouble(listOfPos[0]),Double.parseDouble(listOfPos[1]),Double.parseDouble(listOfPos[2]));
				}
				catch (Exception e)
				{
					throw new  RuntimeException("the parse has been faild, wrong corrdinates");
				}
				nodeData newNode = new nodeData(keyNode,point);
				this.addNode(newNode);
			}
			
			for (int i =0; i< edgeArray.length(); i++) 
			{
				JSONObject currEdge = (JSONObject) edgeArray.get(i);
				int src  = currEdge.getInt("src");
				double weight = currEdge.getDouble("w"); 
				int dest  = currEdge.getInt("dest");
				this.connect(src, dest, weight);
			}
			
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	
}
