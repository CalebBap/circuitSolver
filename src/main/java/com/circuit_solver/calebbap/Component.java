package com.circuit_solver.calebbap;

import java.io.Serializable;

public abstract class Component implements Serializable{
    protected LineCoordinate relativeStartPosition;
    protected LineCoordinate relativeEndPosition;
    protected double angle;
    protected boolean quadrantNE_SW;

    protected Component(LineCoordinate newStartPosition, LineCoordinate newEndPosition, 
            double newAngle, boolean newQuadrant){
        relativeStartPosition = newStartPosition;
        relativeEndPosition = newEndPosition;
        angle = newAngle;
        quadrantNE_SW = newQuadrant;
    }

    public void adjustPosition(LineCoordinate newStartPosition, LineCoordinate newEndPosition){
        relativeStartPosition = newStartPosition;
        relativeEndPosition = newEndPosition;
    }

    public abstract LineCoordinate[] drawComponent();

    public LineCoordinate getRelativeEndPositions(){
        return new LineCoordinate(relativeStartPosition.startX, relativeStartPosition.startY, 
            relativeEndPosition.endX, relativeEndPosition.endY);
    }
}