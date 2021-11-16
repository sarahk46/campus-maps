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

interface PathListForCampusState {
    start: string; // the selection of the starting location's drop-down box
    end: string; // the selection of the destination location's drop-down box
    campusBuildings: Record<string, string>; // stores mappings between each campus building's short name with its long name
}

interface PathListForCampusProps {
    onChange(coordinatePointsForShortestPath: [number, number][]): void;  // stores the shortest path as coordinate points
}

/**
 * A component that stores the two drop-down menus associated with the user's response
 * of the starting and ending locations to find the shortest path of.
 */
class PathListForCampus extends Component<PathListForCampusProps, PathListForCampusState> {

    constructor(props: PathListForCampusProps) {
        super(props);
        this.state = {
            start: "Select...",
            end: "Select...",
            campusBuildings: {},
        };
    }

    componentDidMount() {
        this.makeDropDownList();
    }

    //  Handles the selection of the dropdown box that represents the starting location.
    //  Parameter:
    //      event: contains information about the new contents of the dropdown box based upon user selection.
    handleChangeStart = (event: React.ChangeEvent<HTMLSelectElement>) => {
        this.setState({
            start: event.target.value,
        });
    }

    //  Handles the selection of the dropdown box that represents the destination location.
    //  Parameter:
    //      event: contains information about the new contents of the dropdown box based upon user selection.
    handleChangeEnd = (event: React.ChangeEvent<HTMLSelectElement>) => {
        this.setState({
            end: event.target.value,
        });
    }


    //  Creates a continuous list of every two coordinate points within the shortest path.
    //  Parameters:
    //      start: the starting location of the path
    //      end: the destination location of the path
    makeShortestPath= async (start: string, end: string) => {
        let startingPt: string = start;
        let destination: string = end;

        if (startingPt ===  "Select..." || destination ===  "Select...") {
            // if the user has not selected a starting or ending location
            alert("Make sure to choose a starting location and/or destination!");
            return;
        }

        try {
            let response = await fetch("http://localhost:4567/find-path?start="+ startingPt +"&end="+ destination);
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return;
            }

            let result = await response.json();
            let shortestPath: string[] = result["path"]; // refers to the path field within result object
                                    // recall path stores an array with a starting point, ending point, and cost per element
            let coordinatePointsForShortestPath: [number, number][] = [];
                                // represents the coordinate points that we will populate

            let i: number = 0;
            while (i < shortestPath.length) {
                let startX: number = result["path"][i]["start"]["x"];
                let startY: number = result["path"][i]["start"]["y"];
                coordinatePointsForShortestPath.push([startX, startY]);
                            // store the starting coordinate pair of each point along the path
                if (i === shortestPath.length - 1) {
                            // if we have reached the end of the path
                    let endX: number = result["path"][i]["end"]["x"];
                    let endY: number = result["path"][i]["end"]["y"];
                    coordinatePointsForShortestPath.push([endX, endY]); // store the last coordinate pair of the path
                }
                i++; // go to the next element
            }

            this.props.onChange(coordinatePointsForShortestPath);
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    // Creates the mapping between the short names of the buildings along the campus map and their long names.
    makeDropDownList = async () => {
        try {
            let response = await fetch("http://localhost:4567/get-map");
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return;
            }
            let result = (await response.json()) as Record<string, string>;
            this.setState({
                campusBuildings: result
            });
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    // Clears drop-down selection and any paths displayed on the map.
    // Note: Returns dropdown selection to its default "Select..." option
    clearPath = () => {
        let coordinatePointsForShortestPath: [number, number][] = [];
        this.setState({start: "Select...", end: "Select..."});
        this.props.onChange(coordinatePointsForShortestPath);
    }

    render() {
        let options : JSX.Element[] = [];
        for (let key of Object.keys(this.state.campusBuildings)) {
            let longName = this.state.campusBuildings[key];
            let htmlTag = <option value={key}>{longName}</option>;
            options.push(htmlTag);
        }
        return (
            <div id="edge-list">
                <label>
                    Pick your starting location:
                    <select value = {this.state.start} onChange = {this.handleChangeStart}>
                        <option value={"Select"}> Select... </option>
                        {options}
                    </select>
                </label>
                    <label>
                        Pick your destination:
                        <select value = {this.state.end} onChange={this.handleChangeEnd}>
                            <option value={"Select"}> Select... </option>
                            {options}
                        </select>
                    </label>
                <button onClick={() => this.makeShortestPath(this.state.start, this.state.end)}>Find Path</button>
                <button onClick={() => this.clearPath()}>Clear Path</button>
            </div>
        );
    }
}

export default PathListForCampus;