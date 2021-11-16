package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;
/**
 * ShortestPath represents a class that finds the minimum-cost path between two nodes
 * from a weighted Graph with non-negative edges, where the minimum-cost path is defined as
 * one with the lowest total cost between the two nodes.
 *
 */
public class ShortestPath {

    // Because this class is not an ADT, there is no abstraction function and rep invariant, although
    // both of them would go here.
    /**
     * Finds the shortest path, by the weights of the edges, between the two provided nodes
     * within a weighted graph with non-negative weights.
     *
     * @param <N> the data that is stored within the Path (also the data stored within passed-in graph's node)
     * @param graph the weighted Graph to look for the shortest path in.
     * @param startPt The value that represents the starting node of this path.
     * @param endPt   The value that represents the ending node of this path.
     * @spec.requires graph is a valid, weighted graph with non-negative edges,
     *                  startPt and endPt are valid nodes within the graph are not null
     * @return A path between {@code startPt} and {@code endPt}, or {@literal null}
     * if none exists.
     */
    public static <N> Path<N> getShortestPath(Graph<N,Double> graph, N startPt, N endPt) {
        N start = startPt;
        N end = endPt;
        PriorityQueue<Path<N>> active = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(Path<N> o1, Path<N> o2) {
                int compare = Double.compare(o1.getCost(), o2.getCost());
                if (compare > 0) {
                    return 1;
                } else if (compare < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        );
        Set<N> finished = new HashSet<>();

        Path<N> startPath = new Path<>(start);
        active.add(startPath);

        while (!(active.isEmpty())) {
            Path<N> minPath = active.poll();
            N minDest = minPath.getEnd();
            if (minDest.equals(endPt)) {
                return minPath;
            }
            if (!(finished.contains(minDest))) {
                Set<Graph.Edge<N, Double>> children = graph.getChildrenOf(minDest);
                for (Graph.Edge<N, Double> e : children) {
                    if (!(finished.contains(e.getDestination()))) {
                        Path<N> newPath = minPath.extend(e.getDestination(), e.getLabel());
                        // extend path based off of new ending point and distance
                        active.add(newPath);
                    }
                }
                finished.add(minDest);
            }
        }
        return null;
    }

}
