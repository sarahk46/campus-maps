package graph;

import java.util.*;

/**
 * <p>This mutable class represents a directed labeled graph with a collection of nodes and edges.
 * The Graph can have any number of edges between nodes, and no edges with same parent and child nodes
 * will have the same edge label.</p>
 *
 * @param <N> the node type
 * @param <E> the edge label type
 * @spec.specfield <p>nodes : a set of nodes where each node is a unique identifier of a specific location.
 *                                                                          // A collection of nodes in the graph.</p>
 * @spec.specfield <p>edges : a set of Edges where each Edge stores a starting location, destination, and label.
 *                                                                          // A collection of Edges in the graph.</p>
 */
public class Graph<N,E> {

    /** The Map between nodes as the keys and a Set of Edges as each key's corresponding value that represents the
     *  node's children as Edges. */
    private final Map<N, Set<Edge<N,E>>> graph;

    private static final boolean DEBUG = false; // MAKE SURE TO TURN OFF BEFORE END

    // Abstraction Function:
    //    AF(r) = directed labeled graph g such that
    //      g.nodes = r.graph.keySet()
    //      g.edges = union r.graph.get(n) for all nodes n in r
    //
    // Representation Invariant for every Graph g:
    //  graph != null &&
    //  for all nodes n such that n is in the graph, n != null &&
    //  for all Edges<N,E> e such that e is in the graph, (e != null && graph.contains(e.getStart()) && graph.contains(e.getDest()))
    //  In other words:
    //  - The graph is not null
    //  - No node in the graph is null
    //  - No Edge<N,E> in the graph is null and the graph contains both the source and destination nodes as a part of its nodes
    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert graph != null : "graph should be created";
        if (DEBUG) {
            for (N node : graph.keySet()) {
                assert node != null : "a node is null";
            }

            for (N node : graph.keySet()) {
                Set<Edge<N,E>> children = graph.get(node);
                assert children != null : "a set of children is null";
                for (Edge<N,E> edge : children) {
                    assert edge != null : "an edge is null";

                    boolean isStartInGraph = graph.containsKey(edge.getSource());
                    assert isStartInGraph : "graph does not contain the source of the edge as a node";

                    boolean isDestInGraph = graph.containsKey(edge.getDestination());
                    assert isDestInGraph : "graph does not contain the destination of the edge as a node";
                }
            }
        }
    }

    /**
     * <p>This immutable class represents an edge, which includes the node of where it starts, the node of where it goes, and
     * the label of such connection between them. An edge connects two nodes (i.e. two unique identifiers for locations), and an example of this
     * can be shown for edge e = (A, B) where e is an edge such that B is reachable from A.</p>
     *
     * @param <N> the node type
     * @param <E> the edge label type
     * @spec.specfield <p>source : N // The starting node of the edge.</p>
     * @spec.specfield <p>destination : N // The node that the starting node is directed to.</p>
     * @spec.specfield <p>label : E // The value contained in the edge.</p>
     */
    public static class Edge<N,E> {

        /** The starting node of the edge. */
        private final N source;

        /** The node that the starting node is directed to. */
        private final N destination;

        /** The value contained in the edge. */
        private final E label;


        // Abstraction function:
        //    AF(r) = Edge e such that
        //       e.source = r.source (which is the starting node of the edge)
        //       e.destination = r.destination (which is the node that the starting node is directed to)
        //       e.label = r.label (which is the value contained for the edge)
        // Representation Invariant:
        //      source != null && destination != null && label != null
        /**
         * Throws an exception if the representation invariant is violated.
         */
        private void checkRep() {
            if (DEBUG) {
                assert !(this.source.equals(null)) : "source is null";
                assert !(this.destination.equals(null)) : "dest is null";
                assert !(this.label.equals(null)) : "label is null";
            }
        }
        /**
         * Makes a new Edge with node of start as source, node of end as destination, and passed-in label as label.
         *
         * @param start represents source of this Edge to be constructed
         * @param end represents destination of this Edge to be constructed
         * @param label represents the value to assign to the this Edge
         * @spec.requires start != null, end != null, and label != null
         * @spec.effects Creates a new Edge x where x.source = start, x.destination = end, and x.label = label.
         */
        public Edge(N start, N end, E label) {
            this.source = start;
            this.destination = end;
            this.label = label;
            checkRep();
        }

        /**
         * Gets the source of the Edge.
         *
         * @return the source of this Edge.
         */
        public N getSource() {
            checkRep();
            return this.source;
        }

        /**
         * Gets the destination of the Edge.
         *
         * @return the destination of this Edge.
         */
        public N getDestination() {
            checkRep();
            return this.destination;
        }

        /**
         * Gets the label of the Edge.
         *
         * @return the label of this Edge.
         */
        public E getLabel() {
            checkRep();
            return this.label;
        }

        /**
         * Returns a string representation of this Edge shown as "(this.start, this.end, this.label)".
         *
         * @return a String representation of the Edge represented by this.
         */
        @Override
        public String toString() {
            checkRep();
            String rep = "(" + this.source.toString() + ", " + this.destination.toString() + ", " + this.label.toString() + ")";
            checkRep();
            return rep;
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also.
         */
        @Override
        public int hashCode() {
            checkRep();
            int value = this.source.hashCode() ^ this.destination.hashCode() ^ this.label.hashCode();
            checkRep();
            return value;
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true iff 'obj' is an instance of a Edge and 'this' and 'obj' represent the same
         * Edge (i.e. both Edges have the same source, destination, and label), false otherwise.
         */
        @Override
        public boolean equals(Object obj) {
            checkRep();
            if (!(obj instanceof Edge<?,?>)) {
                checkRep();
                return false;
            }
            Edge<?,?> other = (Edge<?,?>)obj;
            boolean equals = this.source.equals(other.getSource())
                    && this.destination.equals(other.getDestination()) && this.label.equals(other.getLabel());
            checkRep();
            return equals;
        }
    }

    /**
     * Creates a new empty Graph.
     *
     * @spec.effects sets both entries and nodes as new empty sets
     */
    public Graph() {
        this.graph = new HashMap<>();
        checkRep();
    }

    /**
     * Adds a node based off its name into the Graph if the node is not already present.
     *
     * @param name the name of the node to be created
     * @throws IllegalArgumentException if name == null or if this.contains(name)
     * @spec.modifies nodes
     * @spec.effects adds a node with passed-in name in nodes.
     */
    public void addNode(N name) throws IllegalArgumentException {
        checkRep();
        if (name == null || graph.containsKey(name)) {
            checkRep();
            throw new IllegalArgumentException();
        }
        Set<Edge<N,E>> edges = new HashSet<>();
        graph.put(name, edges);
        checkRep();
    }

    /**
     * Adds an Edge of the passed-in source, destination, and label into the Graph
     * if the Edge is not already present.
     *
     * @param source the name of the starting location of the Edge
     * @param destination the name of the ending location of the Edge
     * @param label the name of the label of the Edge
     * @throws IllegalArgumentException if source == null, destination == null, label == null,
     *                                          !(this.contains(source)), !(this.contains(destination)),
     *                                          or this.contains(Edge(source, destination, label))
     * @spec.modifies edges
     * @spec.effects adds an Edge with the passed-in source, destination, and label into edges.
     */
    public void addEdge(N source, N destination, E label) throws IllegalArgumentException {
        checkRep();
        if (source == null || destination == null || label == null
                                    || !(graph.containsKey(source)) || !(graph.containsKey(destination))) {
            checkRep();
            throw new IllegalArgumentException();
        }
        Edge<N,E> edge = new Edge<>(source, destination, label); // create an edge
        Set<Edge<N,E>> children = graph.get(source); // find the children at the source node
        if (children.contains(edge)) { // if the edge is already one of the children, throw exception
            throw new IllegalArgumentException();
        } else {
            children.add(edge); // else (edge isn't one of the children), simply add the edge
        }
        checkRep();
    }

    /**
     * Gets an unmodifiable view of the nodes of the Graph.
     *
     * @return an unmodifiable view of the set nodes
     */
    public Set<N> getNodeSet() {
        checkRep();
        Set<N> nodes = Collections.unmodifiableSet(graph.keySet());
        checkRep();
        return nodes;
    }

    /**
     * Gets an unmodifiable view of the edges of the Graph.
     *
     * @return an unmodifiable view of the set edges
     */
    public Set<Edge<N,E>> getEdgeSet() {
        checkRep();
        Set<Edge<N,E>> set = new HashSet<>();
        for (N node : graph.keySet()) {
            Set<Edge<N,E>> children = graph.get(node);
            set.addAll(children);
        }
        set = Collections.unmodifiableSet(set);
        checkRep();
        return set;
    }

    /**
     * Returns an unmodifiable view of the children of the node of the passed-in name in the Graph.
     * The children of the node of the passed-in name are Edges where there is an Edge from the node of the passed-in
     * name to another node in the graph.
     *
     * @param name the name of the node to search for in the Graph
     * @throws IllegalArgumentException if name == null or if !(this.contains(name))
     * @return an unmodifiable view of the set of children of a node of a specified passed-in name, where the set is
     * defined for Edges x such that there exists an Edge that connects the node of the passed-in name to a node y
     * in the Graph.
     */
    public Set<Edge<N,E>> getChildrenOf(N name) throws IllegalArgumentException {
        checkRep();
        if (name == null || !(graph.containsKey(name))) {
            checkRep();
            throw new IllegalArgumentException();
        }

        Set<Edge<N,E>> children = graph.get(name);
        children = Collections.unmodifiableSet(children);
        checkRep();
        return children;
    }

    /**
     * Returns true iff this node exists in the Graph. Returns false otherwise.
     *
     * @spec.requires name != null
     * @param name the name of the node to search for in the Graph
     * @return true iff the passed-in String is a node in the Graph. Otherwise, returns false.
     */
    public boolean containsNode(N name) {
        checkRep();
        boolean isInGraph = graph.containsKey(name);
        checkRep();
        return isInGraph;
    }

    /**
     * Returns true iff this edge with the passed-in source, destination, and label exists in the Graph.
     * Returns false otherwise.
     *
     * @spec.requires source != null && destination != null & label != null
     * @param source the name of the starting location of the Edge
     * @param destination the name of the ending location of the Edge
     * @param label the name of the label of the Edge
     * @return true iff the passed-in values represent an Edge that exists in the Graph.
     * Otherwise, returns false.
     */
    public boolean containsEdge(N source, N destination, E label) {
        checkRep();
        if (!(graph.containsKey(source)) || !(graph.containsKey(destination))) {
            checkRep();
            return false;
        } else {
            Set<Edge<N,E>> set = graph.get(source);
            Edge<N,E> edge = new Edge<>(source, destination, label);
            boolean isInGraph = set.contains(edge);
            checkRep();
            return isInGraph;
        }
    }


}
