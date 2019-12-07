package com.circuit_solver.calebbap.components;

import com.circuit_solver.calebbap.Component;
import com.circuit_solver.calebbap.*;

public final class Resistor extends Component {
    private double resistance;

    public Resistor(Coordinate newCoordinate, double newAngle, double newResistance) {
        super(newCoordinate, newAngle);

        resistance = newResistance;
    }
    
    @Override
    public Coordinate[] drawComponent(){
        Coordinate[] drawing;
        double dotSpacing = CircuitControl.getDotSpacing();

        double startX = componentEnds.getStartX();
        double startY = componentEnds.getStartY();
        double endX = componentEnds.getStartX() + (dotSpacing / 2);
        double endY = componentEnds.getStartY() - dotSpacing;

        Coordinate c0 = new Coordinate(startX, startY, endX, endY);
        startX = endX;
        startY = endY;
        endX += dotSpacing / 2;
        endY += dotSpacing * 2;
        Coordinate c1 = new Coordinate(startX, startY, endX, endY);
        startX = endX;
        startY = endY;
        endX += dotSpacing / 2;
        endY -= dotSpacing * 2;
        Coordinate c2 = new Coordinate(startX, startY, endX, endY);
        startX = endX;
        startY = endY;
        endX += dotSpacing / 2;
        endY += dotSpacing * 2;
        Coordinate c3 = new Coordinate(startX, startY, endX, endY);
        startX = endX;
        startY = endY;
        endX += dotSpacing / 2;
        endY -= dotSpacing * 2;
        Coordinate c4 = new Coordinate(startX, startY, endX, endY);
        startX = endX;
        startY = endY;
        endX += dotSpacing / 2;
        endY += dotSpacing * 2;
        Coordinate c5 = new Coordinate(startX, startY, endX, endY);
        startX = endX;
        startY = endY;
        endX += dotSpacing / 2;
        endY -= dotSpacing * 2;
        Coordinate c6 = new Coordinate(startX, startY, endX, endY);
        startX = endX;
        startY = endY;
        endX = componentEnds.getEndX();
        endY = componentEnds.getEndY();
        Coordinate c7 = new Coordinate(startX, startY, endX, endY);

        drawing = new Coordinate[] {c0, c1, c2, c3, c4, c5, c6, c7};
        //drawing = new Coordinate[] {};
        return drawing;
    }

    private void setResistance(double newResistance){
        resistance = newResistance;
    }

    public double getResistance(){
        return resistance;
    }

}