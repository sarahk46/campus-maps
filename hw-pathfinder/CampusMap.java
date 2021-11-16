/*
 * Copyright (C) 2021 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;

/**
 * <p>This immutable class represents the concept of a map of a campus. The points along the map represent all locations
 * across campus, including buildings and intermediary walkways, and there are connections that are between each of these
 * locations that are represented by the distance that it takes to travel from one place to another.</p>
 *
 * @spec.specfield <p> locations: a set of points along the campus, including the campus buildings and the walkways in between.
 *                                          // A collection of locations across the entire campus. </p>
 * @spec.specfield <p> paths: a set of all mappings between two locations of the map, represented by the distance between the locations.
 *                                      // A collection of the paths for pairs of locations along the campus. </p>
 */
public class CampusMap implements ModelAPI {

    /** The file that represents the the buildings of the campus.*/
    private static final String buildingsFile = "campus_buildings.tsv";

    /** The file that represents the the paths of the campus that also store intermediary pathways along campus. */
    private static final String pathsFile = "campus_paths.tsv";

    private static final boolean DEBUG = false; // MAKE SURE TO TURN OFF BEFORE END

    /** The Graph is a bi-directional weighted graph that represents the CampusMap where the nodes
     *  are the points of locations along the campus and the edges represent straight-line walking
     *  segments connecting two locations. */
    private Graph<Point,Double> campusGraph;

    /** The Map represents the short names of each building matched with
     * their corresponding CampusBuilding objects. */
    private Map<String,CampusBuilding> shortToBuilding;


    //  Abstraction Function:
    //    AF(r) = map of the campus m such that
    //      m.locations = r.campusGraph.getNodeSet()
    //      m.paths = union r.campusGraph.getChildrenOf(n) for all nodes n in the campusGraph
    //
    //  Representation Invariant for every CampusMap m:
    //  campusGraph != null &&
    //  shortToBuilding != null
    //  for each short name s inside shortToBuilding, s != null &&
    //  for each CampusBuilding b as a value inside shortToBuilding, b != null &&
    //                      Point(b.getX(), b.getY()) is a node of the campusGraph
    //
    //  In other words:
    //  - campusGraph is not null
    //  - shortToBuilding is not null
    //  - No short name of shortToBuilding is null
    //  - No CampusBuilding inside shortToBuilding is null
    //  - Each (x, y) location of the CampusBuildings stored inside shortToBuilding is a node in the campusGraph
    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert campusGraph != null : "graph should be created";
        assert shortToBuilding != null : "map from short names to buildings should be created";
        if (DEBUG) {
            for (String shortName : shortToBuilding.keySet()) {
                assert shortName != null : "a short name is null";
            }

            for (String shortName : shortToBuilding.keySet()) {
                CampusBuilding building = shortToBuilding.get(shortName);
                assert building != null : "a CampusBuilding is null";

                Point pointOfBuilding = new Point(building.getX(), building.getY());
                boolean isLocationOfMap = campusGraph.containsNode(pointOfBuilding);
                assert isLocationOfMap : "a building is not inside the map!";
            }

        }
    }

    /**
     * Creates a new CampusMap that represents a map between all locations of the campus
     * mapped together by their distances.
     *
     * @spec.modifies locations, paths
     * @spec.effects sets locations as all of the points of buildings and walkways of the map
     *               and sets up paths as all of the two-way walkways between each location of the map
     */
    public CampusMap() {
        this.campusGraph = CampusMap.createGraph();
        this.shortToBuilding = CampusMap.mapShortToBuilding();
        checkRep();
    }

    /**
     * Returns a Graph where its nodes are each point along the campus and its edges represent a segment from one
     * point of the campus to another point, which can be represented as (Point1, Point2), with an edge label
     * that represents the distance between the two points (helper procedure).
     *
     * @return a Graph with nodes for each distinct point on campus and its edges are segments
     * between two points with an edge label that stores the distance between the two points.
     */
    private static Graph<Point,Double> createGraph() {
        Graph<Point, Double> campusGraph = new Graph<>();

        List<CampusBuilding> listOfBuildings = CampusPathsParser.parseCampusBuildings(buildingsFile);
        for (CampusBuilding building : listOfBuildings) {
            Point buildingPt = new Point(building.getX(), building.getY());
            if (!(campusGraph.containsNode(buildingPt))) {
                campusGraph.addNode(buildingPt);
            }
        }
        List<CampusPath> listOfPaths = CampusPathsParser.parseCampusPaths(pathsFile);
        for (CampusPath path : listOfPaths) {
            Point startPt = new Point(path.getX1(), path.getY1());
            Point endPt = new Point(path.getX2(), path.getY2());

            if (!(campusGraph.containsNode(startPt))) {
                campusGraph.addNode(startPt);
            }
            if (!(campusGraph.containsNode(endPt))) {
                campusGraph.addNode(endPt);
            }

            if (!(campusGraph.containsEdge(startPt, endPt, path.getDistance()))) {
                campusGraph.addEdge(startPt, endPt, path.getDistance());
            }
            if (!(campusGraph.containsEdge(endPt, startPt, path.getDistance()))) {
                campusGraph.addEdge(endPt, startPt, path.getDistance());
            }
        }
        return campusGraph;
    }

    /**
     * Returns a Map between the short names of locations on campus and the corresponding CampusBuilding objects
     * of each location (helper procedure). Refer to CampusBuilding.java for more information about these objects.
     *
     * @return a Map with short names of the locations on campus mapped to their respective CampusBuilding objects.
     */
    private static Map<String,CampusBuilding> mapShortToBuilding() {
        Map<String,CampusBuilding> mapShortToBuilding = new HashMap<>();
        List<CampusBuilding> listOfBuildings = CampusPathsParser.parseCampusBuildings(buildingsFile);
        for (CampusBuilding building : listOfBuildings) {
            mapShortToBuilding.put(building.getShortName(), building);
        }
        return mapShortToBuilding;
    }

    @Override
    public boolean shortNameExists(String shortName) {
        checkRep();
        boolean isShortName = shortToBuilding.containsKey(shortName);
        checkRep();
        return isShortName;
    }

    @Override
    public String longNameForShort(String shortName) throws IllegalArgumentException {
        checkRep();
        boolean isShortName = shortToBuilding.containsKey(shortName);
        if (!(isShortName)) {
            checkRep();
            throw new IllegalArgumentException();
        }
        String longName = shortToBuilding.get(shortName).getLongName();
        checkRep();
        return longName;
    }

    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        Map<String,String> mapBuildingNames = new HashMap<>();
        for (String shortName : shortToBuilding.keySet()) {
            mapBuildingNames.put(shortName, shortToBuilding.get(shortName).getLongName());
        }
        mapBuildingNames = Collections.unmodifiableMap(mapBuildingNames);
        checkRep();
        return mapBuildingNames;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) throws IllegalArgumentException{
        if (startShortName == null || endShortName == null ||
                !(shortToBuilding.containsKey(startShortName)) || !(shortToBuilding.containsKey(endShortName))) {
            checkRep();
            throw new IllegalArgumentException();
        }
        // obtain the starting point and the ending point
        Point startPt = new Point(shortToBuilding.get(startShortName).getX(), shortToBuilding.get(startShortName).getY());
        Point endPt = new Point(shortToBuilding.get(endShortName).getX(), shortToBuilding.get(endShortName).getY());
        Path<Point> shortestPath = ShortestPath.getShortestPath(campusGraph, startPt, endPt);
        checkRep();
        return shortestPath;

    }

}
