// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import java.util.Iterator;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_graph;
import oop_dataStructure.oop_node_data;
import oop_dataStructure.oop_edge_data;
import oop_utils.OOP_Point3D;

public class RobotG implements robot
{
    public static final double EPS;
    public static final double StartMoney = 0.0;
    public static final double EC = 1.0;
    public static final double DEFAULT_SPEED = 1.0;
    public static final double DS = 50.0;
    public static final double TS = 100.0;
    private static int _count;
    private static int _seed;
    private int _id;
    private long _key;
    private OOP_Point3D _pos;
    private double _speed;
    private oop_edge_data _curr_edge;
    private oop_node_data _curr_node;
    private oop_graph _gg;
    private long _start_move;
    private double _money;
    private double _DoubleSpeedW;
    private double _TurboleSpeedW;
    
    static {
        EPS = OOP_DGraph.EPS;
        RobotG._count = 0;
        RobotG._seed = 3331;
    }
    
    public RobotG(final oop_graph g, final int start_node) {
        this(g, start_node, 50.0, 100.0);
    }
    
    public RobotG(final oop_graph g, final int start_node, final double ds, final double ts) {
        this._gg = g;
        this.setMoney(0.0);
        this._curr_node = this._gg.getNode(start_node);
        this._pos = this._curr_node.getLocation();
        this._id = RobotG._count++;
        this._key = this.getKey(this._id);
        this.setSpeed(1.0);
        this.setDoubleSpeedWeight(ds);
        this.setTurboSpeedWeight(ts);
    }
    
    @Override
    public int getSrcNode() {
        return this._curr_node.getKey();
    }
    
    public String toJSON() {
        final int d = this.getNextNode();
        final String ans = "{\"Robot\":{\"id\":" + this._id + "," + "\"value\":" + this._money + "," + "\"src\":" + this._curr_node.getKey() + "," + "\"dest\":" + d + "," + "\"speed\":" + this.getSpeed() + "," + "\"pos\":\"" + this._pos.toString() + "\"" + "}" + "}";
        return ans;
    }
    
    void setMoney(final double v) {
        this._money = v;
    }
    
    @Override
    public void addMoney(final double d) {
        this._money += d;
    }
    
    private long getKey(final int id) {
        final long k0 = new Random(this._id).nextLong();
        final long k2 = new Random(RobotG._seed).nextLong();
        final long key = k0 ^ k2;
        return key;
    }
    
    @Override
    public boolean setNextNode(final int dest) {
        boolean ans = false;
        final int src = this._curr_node.getKey();
        final boolean reset_time = !this.isMoving();
        this._curr_edge = this._gg.getEdge(src, dest);
        if (this._curr_edge != null) {
            ans = true;
            if (reset_time) {
                this._start_move = new Date().getTime();
            }
        }
        return ans;
    }
    
    @Override
    public boolean isMoving() {
        return this._curr_edge != null;
    }
    
    @Override
    public boolean move() {
        boolean ans = false;
        if (this._curr_edge != null) {
            this.updateSpeed();
            final long now = new Date().getTime();
            final double dt = (now - this._start_move) / 1000.0;
            final double v = this.getSpeed();
            final double pr = v * dt / this._curr_edge.getWeight();
            final int dest = this._curr_edge.getDest();
            final oop_node_data dd = this._gg.getNode(dest);
            final OOP_Point3D ddd = dd.getLocation();
            if (pr >= 1.0) {
                this._pos = ddd;
                this._curr_node = dd;
                this._curr_edge = null;
                ans = true;
            }
            else {
                final OOP_Point3D src = this._curr_node.getLocation();
                final double dx = ddd.x() - src.x();
                final double dy = ddd.y() - src.y();
                final double dz = ddd.z() - src.z();
                final double x = src.x() + dx * pr;
                final double y = src.y() + dy * pr;
                final double z = src.z() + dz * pr;
                final OOP_Point3D cr = new OOP_Point3D(x, y, z);
                if (ddd.distance2D(cr) < ddd.distance2D(this._pos)) {
                    this._pos = cr;
                    ans = true;
                }
            }
        }
        return ans;
    }
    
    private void updateSpeed() {
        final double cs = this.getSpeed();
        final double w = this.getMoney();
        if (cs == 1.0 && w >= this.doubleSpeedWeight()) {
            this.setSpeed(2.0);
        }
        if (cs == 2.0 && w >= this.turboSpeedWeight()) {
            this.setSpeed(5.0);
        }
    }
    
    @Override
    public void randomWalk() {
        if (!this.isMoving()) {
            final Collection<oop_edge_data> ee = this._gg.getE(this._curr_node.getKey());
            final int t = ee.size();
            final int ii = (int)(Math.random() * t);
            final Iterator<oop_edge_data> itr = ee.iterator();
            for (int i = 0; i < ii; ++i) {
                itr.next();
            }
            this.setNextNode(itr.next().getDest());
        }
        else {
            this.move();
        }
    }
    
    @Override
    public String toString() {
        return this.toJSON();
    }
    
    public String toString1() {
        final String ans = this.getID() + "," + this._pos + ", " + this.isMoving() + "," + this.getMoney();
        return ans;
    }
    
    @Override
    public int getID() {
        return this._id;
    }
    
    public long getKey() {
        return this._key;
    }
    
    @Override
    public OOP_Point3D getLocation() {
        return this._pos;
    }
    
    @Override
    public double getMoney() {
        return this._money;
    }
    
    @Override
    public double getBatLevel() {
        return 0.0;
    }
    
    @Override
    public int getNextNode() {
        int ans = -2;
        if (this._curr_edge == null) {
            ans = -1;
        }
        else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }
    
    @Override
    public double getSpeed() {
        return this._speed;
    }
    
    @Override
    public void setSpeed(final double v) {
        this._speed = v;
    }
    
    @Override
    public double doubleSpeedWeight() {
        return this._DoubleSpeedW;
    }
    
    @Override
    public double turboSpeedWeight() {
        return this._TurboleSpeedW;
    }
    
    @Override
    public void setDoubleSpeedWeight(final double w) {
        this._DoubleSpeedW = w;
    }
    
    @Override
    public void setTurboSpeedWeight(final double w) {
        this._TurboleSpeedW = w;
    }
}
