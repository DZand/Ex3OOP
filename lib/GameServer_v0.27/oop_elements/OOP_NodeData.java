// 
// Decompiled by Procyon v0.5.36
// 

package oop_elements;

import oop_utils.OOP_Point3D;
import java.io.Serializable;
import oop_dataStructure.oop_node_data;

public class OOP_NodeData implements oop_node_data, Serializable
{
    private OOP_Point3D _pos;
    private double _weight;
    private String _msg;
    private int _tag;
    private int _id;
    private static int _count;
    
    static {
        OOP_NodeData._count = 0;
    }
    
    public static void resetCount() {
        OOP_NodeData._count = 0;
    }
    
    public OOP_NodeData() {
        this.set_id(OOP_NodeData._count);
        ++OOP_NodeData._count;
    }
    
    public OOP_NodeData(final int id, final OOP_Point3D p) {
        this.set_id(id);
        this.setLocation(p);
    }
    
    public OOP_NodeData(final int t, final double w, final OOP_Point3D p, final String msg) {
        this();
        this.setTag(t);
        this.setWeight(w);
        this.setLocation(p);
        this.setInfo(msg);
    }
    
    @Override
    public OOP_Point3D getLocation() {
        return this._pos;
    }
    
    @Override
    public void setLocation(final OOP_Point3D p) {
        this._pos = new OOP_Point3D(p);
    }
    
    @Override
    public double getWeight() {
        return this._weight;
    }
    
    @Override
    public void setWeight(final double w) {
        this._weight = w;
    }
    
    @Override
    public String getInfo() {
        return this._msg;
    }
    
    @Override
    public void setInfo(final String s) {
        this._msg = s;
    }
    
    public void appendInfo(final String s) {
        this._msg = String.valueOf(this._msg) + s;
    }
    
    @Override
    public int getTag() {
        return this._tag;
    }
    
    @Override
    public void setTag(final int t) {
        this._tag = t;
    }
    
    @Override
    public String toString() {
        return this.getKey() + "," + this.getTag() + "," + this.getWeight() + ":" + this.getInfo();
    }
    
    public String toJSON() {
        String ans = "";
        ans = String.valueOf(ans) + "{id:" + this.getKey() + ",info:" + this.getInfo() + "}";
        return ans;
    }
    
    @Override
    public int getKey() {
        return this._id;
    }
    
    private void set_id(final int _id) {
        this._id = _id;
    }
}
