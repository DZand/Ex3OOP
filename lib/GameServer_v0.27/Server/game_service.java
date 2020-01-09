// 
// Decompiled by Procyon v0.5.36
// 

package Server;

import java.util.List;
import java.io.Serializable;

public interface game_service extends Serializable
{
    String getGraph();
    
    List<String> getFruits();
    
    List<String> getRobots();
    
    boolean addRobot(final int p0);
    
    long startGame();
    
    boolean isRunning();
    
    long stopGame();
    
    long chooseNextEdge(final int p0, final int p1);
    
    long timeToEnd();
    
    List<String> move();
}
