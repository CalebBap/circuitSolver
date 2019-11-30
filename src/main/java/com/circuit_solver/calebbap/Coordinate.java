package com.circuit_solver.calebbap;

public class Coordinate{
    double startX;
    double startY;
    double endX;
    double endY;

    Coordinate(){ }

    Coordinate(double newStartX, double newStartY, double newEndX, double newEndY){
        startX = newStartX;
        startY = newStartY;
        endX = newEndX;
        endY = newEndY;
    }
}