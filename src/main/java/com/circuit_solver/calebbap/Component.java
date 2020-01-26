package com.circuit_solver.calebbap;

import java.io.Serializable;

public class Component implements Serializable{
    protected Coordinate componentEnds;
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

    public Coordinate[] drawComponent(){
        return null;
    }
}