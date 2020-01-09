// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import oop_utils.OOP_Point3D;
import java.io.Serializable;

public interface robot extends Serializable
{
    int getID();
    
    boolean setNextNode(final int p0);
    
    int getNextNode();
    
    OOP_Point3D getLocation();
    
    boolean isMoving();
    
    boolean move();
    
    double getMoney();
    
    double getBatLevel();
    
    void randomWalk();
    
    double getSpeed();
    
    void setSpeed(final double p0);
    
    double doubleSpeedWeight();
    
    double turboSpeedWeight();
    
    void setDoubleSpeedWeight(final double p0);
    
    void setTurboSpeedWeight(final double p0);
    
    void addMoney(final double p0);
    
    int getSrcNode();
}
