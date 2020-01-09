// 
// Decompiled by Procyon v0.5.36
// 

package oop_dataStructure;

import java.util.Collection;

public interface oop_graph
{
    oop_node_data getNode(final int p0);
    
    oop_edge_data getEdge(final int p0, final int p1);
    
    void addNode(final oop_node_data p0);
    
    void connect(final int p0, final int p1, final double p2);
    
    Collection<oop_node_data> getV();
    
    Collection<oop_edge_data> getE(final int p0);
    
    oop_node_data removeNode(final int p0);
    
    oop_edge_data removeEdge(final int p0, final int p1);
    
    int nodeSize();
    
    int edgeSize();
    
    int getMC();
}
