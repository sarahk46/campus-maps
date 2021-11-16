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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Map;

/**
 * This is the main class that runs the server for the CampusMap GUI, it includes a main method
 * that sets up the server and allows it to accept requests to find the shortest path between
 * two valid buildings given their short names and the mapping between short names and their long names.
 */
public class SparkServer {

    private static CampusMap campusMap = new CampusMap(); // CampusMap to fulfill requests of server

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        /**
         * Finds shortest path between two buildings via their short names in following HTML path
         * "/find-path?start=BLDG1&end=BLDG2".
         */
        Spark.get("/find-path", new Route() {
            @Override
            /**
             * Handles finding shortest path between two buildings via their short names in following HTML path
             * "/find-path?start=BLDG1&end=BLDG2". Returns a JSON object that stores the shortest path.
             *
             * @spec.requires BLDG1 and BLDG 2 are non-null building names, represent actual locations on the campus map,
             *                and are short names for the locations that they represent.
             * @param request: the Request that represents the properly formatted HTML that sends a "/find-path"
             *                request with the start and end buildings' short names
             * @param response: the Response to set information about
             * @return a JSON object that stores the shortest path between BLDG1 and BLDG2.
             */
            public Object handle(Request request, Response response) throws Exception {
                String startingPt = request.queryParams("start");
                String destination = request.queryParams("end");

                if (startingPt == null || destination == null || !(campusMap.shortNameExists(startingPt)) ||
                                !(campusMap.shortNameExists(destination))) {
                    // If any of the pre-conditions are not met, this halts the current request
                    // and sends back just the status code we provide.
                    Spark.halt(400, "must have valid start and end");
                }

                Path<Point> shortestPath = campusMap.findShortestPath(startingPt, destination);
                Gson gson = new Gson();
                return gson.toJson(shortestPath);
            }
        });

        /**
         *  Obtains a mapping between the campus map's buildings' short names with their corresponding long names
         *  in following HTML path "/get-map".
         */
        Spark.get("/get-map", new Route() {
            @Override
            /**
             * Handles finding the map between each building's short name with its long names on campus via the HTML path
             * "/get-map". Returns a JSON object that stores the mapping between each building's short name with its long name.
             *
             * @param request: the Request that represents the properly formatted HTML that sends a "/get-map"
             *                request
             * @param response: the Response to set information about
             * @return a JSON object that stores each building's short name on the campus map mapped to its long name.
             */
            public Object handle(Request request, Response response) throws Exception {
                Map<String, String> buildingsForMap = campusMap.buildingNames();
                Gson gson = new Gson();
                return gson.toJson(buildingsForMap);
            }
        });
    }

}
