package com.circuit_solver.calebbap;

public class Component{
    protected Coordinate componentEnds;
    protected double angle;
    protected static double radius;
    protected enum Quadrant{
        A, B
    }
    protected Quadrant quadrant;

    // Protected class because this class will only be inheirited
    protected Component(Coordinate newComponentEnds, double newAngle){
        componentEnds = newComponentEnds;
        angle = newAngle;
        setQuadrant();
    }

    private void setQuadrant(){
        if( (componentEnds.getStartX() < componentEnds.getEndX()) && (componentEnds.getEndY() < componentEnds.getStartY()) || 
            componentEnds.getStartY() == componentEnds.getEndY()){
                quadrant = Quadrant.A;
        }else{
            quadrant = Quadrant.B;
        }
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