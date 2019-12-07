package com.circuit_solver.calebbap;

public class Component{
    protected Coordinate componentEnds;
    protected double angle;
    protected static double radius;

    // Protected class because this class will only be inheirited
    protected Component(Coordinate newComponentEnds, double newAngle){
        componentEnds = newComponentEnds;
        angle = newAngle;
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