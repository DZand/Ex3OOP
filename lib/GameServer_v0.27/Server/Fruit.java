// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import oop_elements.OOP_Edge;
import oop_dataStructure.oop_edge_data;
import oop_utils.OOP_Point3D;

public class Fruit implements fruits
{
    private OOP_Point3D _pos;
    private double _value;
    private oop_edge_data _edge;
    
    public Fruit() {
    }
    
    public Fruit(final double v, final OOP_Point3D p, final oop_edge_data e) {
        this._value = v;
        this._pos = new OOP_Point3D(p);
        this._edge = e;
    }
    
    @Override
    public int getType() {
        int ans = 0;
        ans = this._edge.getDest() - this._edge.getSrc();
        return ans;
    }
    
    @Override
    public OOP_Point3D getLocation() {
        return new OOP_Point3D(this._pos);
    }
    
    public String toJSON1() {
        final String ans = "{\"Fruit\":{\"value\":10,\"type\":1,\"pos\":\"35.187615443099276,32.103800431932775,0.0\"}}";
        return ans;
    }
    
    @Override
    public String toString() {
        return this.toJSON();
    }
    
    public String toJSON() {
        int d = 1;
        if (this._edge.getSrc() > this._edge.getDest()) {
            d = -1;
        }
        final String ans = "{\"Fruit\":{\"value\":" + this._value + "," + "\"type\":" + d + "," + "\"pos\":\"" + this._pos.toString() + "\"" + "}" + "}";
        return ans;
    }
    
    @Override
    public double getValue() {
        return this._value;
    }
    
    @Override
    public double grap(final robot r, final double dist) {
        double ans = 0.0;
        if (this._edge != null && r != null) {
            final int d = r.getNextNode();
            if (this._edge.getDest() == d) {
                final OOP_Point3D rp = r.getLocation();
                if (dist > rp.distance2D(this._pos)) {
                    ans = this._value;
                }
            }
        }
        return ans;
    }
    
    public static void main(final String[] a) {
        final double v = 10.0;
        final oop_edge_data e = new OOP_Edge(5, 3, 2.0);
        final OOP_Point3D p = new OOP_Point3D(1.0, 2.0, 3.0);
        final Fruit f = new Fruit(v, p, e);
        final String s = f.toJSON();
        System.out.println(s);
    }
}
