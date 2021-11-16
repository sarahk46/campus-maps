package graph.junitTests;

import static org.junit.Assert.*;
import graph.Graph.Edge;
import org.junit.Test;

public final class EdgeTest {

    private final static String node1 = "node1";
    private final static String node2 = "node2";
    private final static String label = "number";

    @Test
    public void createEdgeAndTestSource() {
        Edge<String,String> edge = new Edge<>(node1, node2, label);
        assertEquals("graph does not have right source!", node1, edge.getSource());
    }

    @Test
    public void createEdgeAndTestDestination() {
        Edge<String,String> edge = new Edge<>(node1, node2, label);
        assertEquals("graph does not have right destination!", node2, edge.getDestination());
    }

    @Test
    public void createEdgeAndTestLabel() {
        Edge<String,String> edge = new Edge<>(node1, node2, label);
        assertEquals("graph does not have right label!", label, edge.getLabel());
    }

    @Test
    public void checkEqualForReflexive() {
        Edge<String,String> edge1 = new Edge<>(node1, node2, label);

        // Tests reflexive property for equals()
        assertTrue("not reflexive for edge1!", edge1.equals(edge1));
    }

    @Test
    public void checkEqualForSymmetric() {
        Edge<String,String> edge1 = new Edge<>(node1, node2, label);
        Edge<String,String> edge2 = new Edge<>(node1, node2, label);

        // Tests symmetric property of equals()
        assertTrue("edge1 is not equal to edge2!", edge1.equals(edge2));
        assertTrue("edge2 is not equal to edge1", edge2.equals(edge1));
    }

    @Test
    public void checkEqualForTransitive() {
        Edge<String,String> edge1 = new Edge<>(node1, node2, label);
        Edge<String,String> edge2 = new Edge<>(node1, node2, label);
        Edge<String,String> edge3 = new Edge<>(node1, node2, label);

        // Tests transitive property of equals()
        assertTrue("edge1 is not equal to edge2!", edge1.equals(edge2));
        assertTrue("edge2 is not equal to edge3!", edge2.equals(edge3));
        assertTrue("edge1 is not equal to edge3!", edge1.equals(edge3));
    }

    @Test
    public void checkHashSelfConsistency() {
        Edge<String,String> edge1 = new Edge<>(node1, node2, label);

        // Tests self-consistency with hashCode()
        assertEquals("hash code isn't self-consistent!", edge1.hashCode(), edge1.hashCode());
    }

    @Test
    public void checkHashConsistencyWithEquals() {
        Edge<String,String> edge1 = new Edge<>(node1, node2, label);
        Edge<String,String> edge2 = new Edge<>(node1, node2, label);

        //Tests consistency with equality since edge1.equals(edge2)
        assertEquals("hash codes are not the same with two equal edges!",
                edge1.hashCode(), edge2.hashCode());
    }

    @Test
    public void checkToString() {
        String target = "(node1, node2, number)";
        Edge<String,String> edge1 = new Edge<>(node1, node2, label);

        assertEquals("toString does not match expected!", target, edge1.toString());
    }


}
