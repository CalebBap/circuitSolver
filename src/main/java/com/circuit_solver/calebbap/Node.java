package com.circuit_solver.calebbap;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable{
    private ArrayList<Component> components;
    private double[] location = new double[2];

    public Node(ArrayList<Component> newComponents, double[] newLocation){
        components = newComponents;
        location = newLocation;
    }

    public void applyRelativePosition(){
        double width = View.getRoot().getWidth() * CircuitControl.getScale();
        double height = View.getRoot().getHeight() * CircuitControl.getScale();

        location[0] = location[0] + (CircuitControl.getXShift() / width);
        location[1] = location[1] + (CircuitControl.getYShift() / height);
    }

    public void applyAbsolutePosition(){
        double width = View.getRoot().getWidth() * CircuitControl.getScale();
        double height = View.getRoot().getHeight() * CircuitControl.getScale();

        location[0] = location[0] - (CircuitControl.getXShift() / width);
        location[1] = location[1] - (CircuitControl.getYShift() / height);
    }

    public double[] getLocation(){
        return location;
    }

    public void addComponent(Component newComponent){
        components.add(newComponent);
    }
}