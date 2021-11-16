package graph.junitTests;

import static org.junit.Assert.*;
import graph.*;
import graph.Graph.Edge;
import org.junit.Test;

public final class GraphTest {

    private final static String node1 = "node1";
    private final static String node2 = "node2";
    private final static String label = "number";
    private final static int NUM_OF_NODES = 20;

    @Test
    public void createEmptyGraph() {
        Graph<String,String> graph = new Graph<>();
        assertTrue(graph.getEdgeSet().isEmpty());
    }


    @Test(expected=IllegalArgumentException.class)
    public void checkAddDuplicateNode() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addNode(node1); // should throw exception
        assertEquals("graph has added both nodes!", 1, graph.getNodeSet().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkAddNullNode() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(null);
        assertTrue(graph.getNodeSet().isEmpty()); // nodes should not contain anything, since still empty
    }

    @Test
    public void checkAddOneEdge() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addNode(node2);

        graph.addEdge(node1, node2, label);

        Edge<String,String> edge = new Edge<>(node1, node2, label);
        assertTrue(graph.getEdgeSet().contains(edge));
        assertEquals("graph has not added the edge!", 1, graph.getEdgeSet().size());
    }

    @Test
    public void checkAddMultipleEdges() {
        Graph<String,String> graph = new Graph<>();
        for (int i = 0; i < NUM_OF_NODES; i++) {
            graph.addNode("node" + i);
        }

        for (int i = 0; i < NUM_OF_NODES - 1; i++) {
            graph.addEdge("node" + i, "node" + (i + 1), label);
            Edge<String,String> edge = new Edge<>("node" + i, "node" + (i + 1), label);
            assertTrue(graph.getEdgeSet().contains(edge));
        }
        assertEquals("graph has not added all edges!", NUM_OF_NODES - 1, graph.getEdgeSet().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkAddDuplicateEdge() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1, node2, label);
        Edge<String,String> edge = new Edge<>(node1, node2, label);

        assertTrue(graph.getEdgeSet().contains(edge));
        assertEquals("graph has not added the first edge!", 1, graph.getEdgeSet().size());

        graph.addEdge(node1, node2, label); // should not be able to add this, so edge set remains same
        assertEquals("graph has added the duplicate edge!", 1, graph.getEdgeSet().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkAddNullSourceForEdge() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node2);
        graph.addEdge(null, node2, label);
        Edge<String,String> edge = new Edge<>(null, node2, label);

        assertFalse(graph.getEdgeSet().contains(edge)); // should not contain this invalid node
        assertTrue(graph.getEdgeSet().isEmpty()); // should not contain passed-in edge from prev. statement
                                                     //(i.e. is empty)
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkAddNullDestinationForEdge() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addEdge(node1, null, label);
        Edge<String,String> edge = new Edge<>(node1, null, label); // invalid Edges

        assertFalse(graph.getEdgeSet().contains(edge)); // should not have invalid Edge since not part of graph
        assertTrue(graph.getEdgeSet().isEmpty()); // should be empty since there are no Edges to graph
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkAddNullLabelForEdge() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1, node2, null);
        Edge<String,String> edge = new Edge<>(node1, node2, null);

        assertFalse(graph.getEdgeSet().contains(edge)); // should not have invalid Edge since not part of graph
        assertTrue(graph.getEdgeSet().isEmpty()); // should be empty since there are no Edges to graph
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkAddEdgeToEmptyGraph() {
        Graph<String,String> graph = new Graph<>();
        graph.addEdge(node1, node2, label);
        Edge<String,String> edge = new Edge<>(node1, node2, label); // Edge can be valid

        // However, it should not be added to the Graph because it comes from non-existing nodes.
        assertFalse(graph.getEdgeSet().contains(edge));
        assertTrue(graph.getEdgeSet().isEmpty());
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkAddEdgeForSingleNodeGraph() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addEdge(node1, node2, label);
        Edge<String,String> edge = new Edge<>(node1, node2, label); // Edge can be valid

        // However, it should not be added to the Graph because it comes from a non-existing node
        // in the second argument.
        assertFalse(graph.getEdgeSet().contains(edge));
        assertTrue(graph.getEdgeSet().isEmpty());
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkGetChildrenOfNullNode() {
        Graph<String,String> graph = new Graph<>();
        graph.getChildrenOf(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkGetChildrenOfNodeNotInGraph() {
        Graph<String,String> graph = new Graph<>();
        graph.getChildrenOf(node1);
    }

    @Test
    public void checkContainsNode(){
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        assertTrue(graph.containsNode(node1));
    }

    @Test
    public void checkContainsEdge() {
        Graph<String,String> graph = new Graph<>();
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addEdge(node1, node2, label);
        assertTrue(graph.containsEdge(node1, node2, label));
    }

}