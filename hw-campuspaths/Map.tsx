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
import "./Map.css";

interface MapProps {
    coordinatePointsForShortestPath: [number, number][]; // list of coordinate points that build the shortest path
}

interface MapState {
    backgroundImage: HTMLImageElement | null;  // image object rendered into the canvas (once loaded)
}

/**
 * A component that displays the map of campus and draws the shortest path on the map.
 */
class Map extends Component<MapProps, MapState> {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    canvas: React.RefObject<HTMLCanvasElement>;

    constructor(props: MapProps) {
        super(props);
        this.state = {
            backgroundImage: null
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.fetchAndSaveImage();
        this.drawBackgroundImage();
    }

    componentDidUpdate() {
        this.drawBackgroundImage();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background: HTMLImageElement = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    drawBackgroundImage() {
        let canvas = this.canvas.current;
        if (canvas === null) throw Error("Unable to draw, no canvas ref.");
        let ctx = canvas.getContext("2d");
        if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
        this.drawPath(ctx, this.props.coordinatePointsForShortestPath);
    }


    //  Given a list of coordinate points that create the shortest path between two buildings, draws the path upon the grid.
    //  Parameters:
    //   CanvasRenderingContext2D: for the purpose of drawing onto the canvas
    //   coordinatePointsForShortestPath: the list of coordinate points that represent the shortest path
    drawPath = (ctx: CanvasRenderingContext2D, coordinatePointsForShortestPath: [number, number][]) => {
        let i: number = 0;
        while (i < coordinatePointsForShortestPath.length - 1) {
            let startPt: [number, number] = [coordinatePointsForShortestPath[i][0], coordinatePointsForShortestPath[i][1]];
                    // get the first location of a segment within the shortest path
            let endPt : [number, number] = [coordinatePointsForShortestPath[i + 1][0], coordinatePointsForShortestPath[i + 1][1]];
                    // get the next location of a segment within the shortest path
            ctx.strokeStyle = "yellow";
            ctx.lineWidth = 10;
            ctx.beginPath();
            ctx.moveTo(startPt[0], startPt[1]); // move your pen to the first location
            ctx.lineTo(endPt[0], endPt[1]); // draw a line to the next location
            ctx.stroke();
            i++;
        }
    }

    render() {
        return (
            <canvas ref={this.canvas}/>
        )
    }
}

export default Map;