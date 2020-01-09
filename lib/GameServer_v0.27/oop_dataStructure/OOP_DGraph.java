// 
// Decompiled by Procyon v0.5.36
// 

package oop_dataStructure;

import java.util.Iterator;
import oop_elements.OOP_Edge;
import java.util.Collection;
import org.json.JSONArray;
import oop_utils.OOP_Point3D;
import org.json.JSONObject;
import java.util.Scanner;
import java.io.File;
import oop_elements.OOP_NodeData;
import java.util.HashMap;

public class OOP_DGraph implements oop_graph
{
    public static final double EPS1 = 1.0E-4;
    public static final double EPS2;
    public static final double EPS;
    private HashMap<Integer, oop_node_data> _V;
    private HashMap<Integer, HashMap<Integer, oop_edge_data>> _E;
    public static final int UNDIRECTED = 0;
    public static final int DIRECTED = 1;
    private int _mc;
    private int _type;
    private int e_count;
    
    static {
        EPS2 = Math.pow(1.0E-4, 2.0);
        EPS = OOP_DGraph.EPS2;
    }
    
    public OOP_DGraph() {
        this(1);
    }
    
    public OOP_DGraph(final int type) {
        this.init();
        this._type = type;
        this.e_count = 0;
        this._mc = 0;
    }
    
    public OOP_DGraph(final String file_name) {
        try {
            this.init();
            OOP_NodeData.resetCount();
            final Scanner scanner = new Scanner(new File(file_name));
            final String jsonString = scanner.useDelimiter("\\A").next();
            scanner.close();
            final JSONObject graph = new JSONObject(jsonString);
            final JSONArray nodes = graph.getJSONArray("Nodes");
            final JSONArray edges = graph.getJSONArray("Edges");
            for (int i = 0; i < nodes.length(); ++i) {
                final int id = nodes.getJSONObject(i).getInt("id");
                final String pos = nodes.getJSONObject(i).getString("pos");
                final OOP_Point3D p = new OOP_Point3D(pos);
                this.addNode(new OOP_NodeData(id, p));
            }
            for (int i = 0; i < edges.length(); ++i) {
                final int s = edges.getJSONObject(i).getInt("src");
                final int d = edges.getJSONObject(i).getInt("dest");
                final double w = edges.getJSONObject(i).getDouble("w");
                this.connect(s, d, w);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getType() {
        return this._type;
    }
    
    @Override
    public int getMC() {
        return this._mc;
    }
    
    @Override
    public int nodeSize() {
        return this._V.size();
    }
    
    @Override
    public int edgeSize() {
        return this.e_count;
    }
    
    @Override
    public Collection<oop_node_data> getV() {
        return this._V.values();
    }
    
    @Override
    public oop_edge_data getEdge(final int a, final int b) {
        return this._E.get(a).get(b);
    }
    
    @Override
    public void addNode(final oop_node_data n) {
        final HashMap<Integer, oop_edge_data> tt = new HashMap<Integer, oop_edge_data>();
        this._V.put(n.getKey(), n);
        this._E.put(n.getKey(), tt);
        ++this._mc;
    }
    
    @Override
    public oop_node_data getNode(final int key) {
        return this._V.get(key);
    }
    
    public void connect(final oop_node_data a, final oop_node_data b, final OOP_Edge e) {
        this.put(e);
    }
    
    @Override
    public void connect(final int a, final int b, final double w) {
        final oop_node_data aa = this.getNode(a);
        final oop_node_data bb = this.getNode(b);
        if (aa != null && bb != null) {
            final OOP_Edge e = new OOP_Edge(a, b, w);
            this.put(e);
            return;
        }
        throw new RuntimeException("ERR: can NOT connect nodes which are not in the graph! got(" + a + "," + b + ")");
    }
    
    private void put(final oop_edge_data e) {
        final HashMap<Integer, oop_edge_data> v_out = this._E.get(e.getSrc());
        v_out.put(e.getDest(), e);
        ++this.e_count;
        ++this._mc;
    }
    
    public oop_node_data getNode(final oop_node_data n) {
        return this._V.get(n.getKey());
    }
    
    @Override
    public oop_node_data removeNode(final int key) {
        final oop_node_data v = this._V.remove(key);
        final int t = this._E.remove(key).size();
        this.e_count -= t;
        for (final oop_node_data d : this.getV()) {
            final int dd = d.getKey();
            final HashMap<Integer, oop_edge_data> ni = this._E.get(dd);
            if (ni.containsKey(key)) {
                final oop_edge_data e = ni.remove(key);
                if (e == null) {
                    continue;
                }
                --this.e_count;
            }
        }
        ++this._mc;
        return v;
    }
    
    @Override
    public oop_edge_data removeEdge(final int src, final int dest) {
        final oop_edge_data ans = this._E.get(src).remove(dest);
        if (ans != null) {
            --this.e_count;
            ++this._mc;
        }
        return ans;
    }
    
    oop_node_data getNodeByKey(final int k) {
        return this._V.get(k);
    }
    
    @Override
    public String toString() {
        return this.toJSON();
    }
    
    public String toString1() {
        String ans = this.getClass().getName() + "|V|=" + this.getV().size() + ", |E|=" + this.edgeSize() + "\n";
        for (final oop_node_data c : this.getV()) {
            ans = String.valueOf(ans) + c.toString() + "\n";
            for (final oop_edge_data ee : this.getE(c.getKey())) {
                ans = String.valueOf(ans) + ee.toString() + "\n";
            }
            ans = String.valueOf(ans) + " *** new Node ***\n";
        }
        return ans;
    }
    
    public void init(final String jsonSTR) {
        try {
            OOP_NodeData.resetCount();
            this.init();
            this.e_count = 0;
            final JSONObject graph = new JSONObject(jsonSTR);
            final JSONArray nodes = graph.getJSONArray("Nodes");
            final JSONArray edges = graph.getJSONArray("Edges");
            for (int i = 0; i < nodes.length(); ++i) {
                final int id = nodes.getJSONObject(i).getInt("id");
                final String pos = nodes.getJSONObject(i).getString("pos");
                final OOP_Point3D p = new OOP_Point3D(pos);
                this.addNode(new OOP_NodeData(id, p));
            }
            for (int i = 0; i < edges.length(); ++i) {
                final int s = edges.getJSONObject(i).getInt("src");
                final int d = edges.getJSONObject(i).getInt("dest");
                final double w = edges.getJSONObject(i).getDouble("w");
                this.connect(s, d, w);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String toJSON() {
        final JSONObject allEmps = new JSONObject();
        final JSONArray VArray = new JSONArray();
        final JSONArray EArray = new JSONArray();
        final Collection<oop_node_data> V = this.getV();
        final Iterator<oop_node_data> iter = V.iterator();
        final Collection<oop_edge_data> E = null;
        Iterator<oop_edge_data> itr = null;
        try {
            while (iter.hasNext()) {
                final oop_node_data nn = iter.next();
                final int n = nn.getKey();
                final String p = nn.getLocation().toString();
                final JSONObject node = new JSONObject();
                node.put("id", n);
                node.put("pos", (Object)p);
                VArray.put((Object)node);
                itr = this.getE(n).iterator();
                while (itr.hasNext()) {
                    final oop_edge_data ee = itr.next();
                    final JSONObject edge = new JSONObject();
                    edge.put("src", ee.getSrc());
                    edge.put("dest", ee.getDest());
                    edge.put("w", ee.getWeight());
                    EArray.put((Object)edge);
                }
            }
            allEmps.put("Nodes", (Object)VArray);
            allEmps.put("Edges", (Object)EArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return allEmps.toString();
    }
    
    private void init() {
        this._V = new HashMap<Integer, oop_node_data>();
        this._E = new HashMap<Integer, HashMap<Integer, oop_edge_data>>();
    }
    
    @Override
    public Collection<oop_edge_data> getE(final int node_id) {
        return this._E.get(node_id).values();
    }
}
