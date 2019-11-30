package com.circuit_solver.calebbap;

public class Component{
    protected Coordinate componentEnds;
    protected double angle;

    // Protected class because this class will only be inheirited
    protected Component(Coordinate newComponentEnds, double newAngle){
        componentEnds = newComponentEnds;
        angle = newAngle;
    }

    public Coordinate[] drawComponent(){
        return null;
    }
}