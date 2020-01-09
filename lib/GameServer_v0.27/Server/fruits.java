// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import oop_utils.OOP_Point3D;
import java.io.Serializable;

public interface fruits extends Serializable
{
    OOP_Point3D getLocation();
    
    double getValue();
    
    double grap(final robot p0, final double p1);
    
    int getType();
}
