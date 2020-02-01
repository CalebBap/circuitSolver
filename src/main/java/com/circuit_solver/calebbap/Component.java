package com.circuit_solver.calebbap;

import java.io.Serializable;

public abstract class Component implements Serializable{
    protected Coordinate componentEnds;
    protected Coordinate relativePosition;
    protected double angle;
    protected static double radius;
    protected boolean quadrantNE_SW;

    // Protected class because this class will only be inheirited
    protected Component(Coordinate newComponentEnds, double newAngle){
        componentEnds = newComponentEnds;
        angle = newAngle;
        quadrantNE_SW = setQuadrant();
    }

    private Boolean setQuadrant(){
        if( (componentEnds.getStartX() < componentEnds.getEndX()) && (componentEnds.getEndY() < componentEnds.getStartY()) || 
            componentEnds.getStartY() == componentEnds.getEndY()){
                return true;
        }
        return false;
    }

    public static double getRadius(){
        return radius;
    }

    public static void setRadius(double newRadius){
        radius = newRadius;
    }

    public abstract Coordinate[] drawComponent();

    public void setRelativePosition(Coordinate coordinate){
        relativePosition = coordinate;
    }

    public Coordinate getRelativePosition(){
        return relativePosition;
    }

    public void shiftComponentEnds(){
        componentEnds.startX -= CircuitControl.getXShift();
        componentEnds.startY -= CircuitControl.getYShift();
        componentEnds.endX -= CircuitControl.getXShift();
        componentEnds.endY -= CircuitControl.getYShift();
    }

    public void applyShift(){
        componentEnds.startX += CircuitControl.getXShift();
        componentEnds.startY += CircuitControl.getYShift();
        componentEnds.endX += CircuitControl.getXShift();
        componentEnds.endY += CircuitControl.getYShift();
    }
}