package com.circuit_solver.calebbap;

import java.io.Serializable;

public class LineCoordinate implements Serializable {
    double startX;
    double startY;
    double endX;
    double endY;

    public LineCoordinate(){ }

    public LineCoordinate(double newStartX, double newStartY, double newEndX, double newEndY){
        startX = newStartX;
        startY = newStartY;
        endX = newEndX;
        endY = newEndY;
    }

    public double getStartX(){
        return startX;
    }

    public double getEndX(){
        return endX;
    }

    public double getStartY(){
        return startY;
    }

    public double getEndY(){
        return endY;
    }

    public void setValues(double newStartX, double newStartY, double newEndX, double newEndY){
        startX = newStartX;
        startY = newStartY;
        endX = newEndX;
        endY = newEndY;
    }

    public void applyRelativePosition(){
        double width = View.getRoot().getWidth() * CircuitControl.getScale();
        double height = View.getRoot().getHeight() * CircuitControl.getScale();

        startX = startX + (CircuitControl.getXShift() / width);
        startY = startY + (CircuitControl.getYShift() / height);
        endX = endX + (CircuitControl.getXShift() / width);
        endY = endY + (CircuitControl.getYShift() / height);
    }

    public void applyAbsolutePosition(){
        double width = View.getRoot().getWidth() * CircuitControl.getScale();
        double height = View.getRoot().getHeight() * CircuitControl.getScale();

        startX = startX - (CircuitControl.getXShift() / width);
        startY = startY - (CircuitControl.getYShift() / height);
        endX = endX - (CircuitControl.getXShift() / width);
        endY = endY - (CircuitControl.getYShift() / height);
    }
}