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

    public abstract LineCoordinate[] drawComponent();
}