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

import React, {Component} from 'react';
import Map from "./Map";
import PathListForCampus from "./PathListForCampus";

interface AppState {
    coordinatePointsForShortestPath: [number, number][];
                // list of coordinate points that build the shortest path
}
class App extends Component<{}, AppState> {

    constructor(props: any) {
        super(props);
        this.state = {
            coordinatePointsForShortestPath: [],
        };
    }

    // Updates the shortest path based off of the new list of coordinate points passed in.
    // Parameter:
    //  newCoordinatePoints: the list of coordinate pairs that represent the shortest path between two buildings
    updatePath = (newCoordinatePoints : [number, number][]) => {
        this.setState({
            coordinatePointsForShortestPath: newCoordinatePoints,
        });
    };

    render() {
        return (
            <div>
                <h1> Find the shortest path!</h1>
                <p> Welcome to the UW campus! To find the shortest path between two buildings, fill in the drop-down menu below. </p>
                <PathListForCampus onChange={this.updatePath}/>
                <Map coordinatePointsForShortestPath={this.state.coordinatePointsForShortestPath}/>
            </div>
        );
    }

}

export default App;
