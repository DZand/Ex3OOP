// 
// Decompiled by Procyon v0.5.36
// 

package oop_utils;

public class OOP_Range
{
    private double _min;
    private double _max;
    
    public OOP_Range(final double min, final double max) {
        this.set_min(min);
        this.set_max(max);
    }
    
    public boolean isIn(final double d) {
        boolean inSide = false;
        if (d >= this.get_min() && d <= this.get_max()) {
            inSide = true;
        }
        return inSide;
    }
    
    @Override
    public String toString() {
        String ans = "[" + this.get_min() + "," + this.get_max() + "]";
        if (this.isEmpty()) {
            ans = "Empty Range";
        }
        return ans;
    }
    
    public boolean isEmpty() {
        return this.get_min() > this.get_max();
    }
    
    public double get_max() {
        return this._max;
    }
    
    public double get_length() {
        return this._max - this._min;
    }
    
    private void set_max(final double _max) {
        this._max = _max;
    }
    
    public double get_min() {
        return this._min;
    }
    
    private void set_min(final double _min) {
        this._min = _min;
    }
}
