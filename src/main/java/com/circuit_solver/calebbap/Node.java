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

    public double[] getLocation(){
        return location;
    }

    public void addComponent(Component newComponent){
        components.add(newComponent);
    }
}