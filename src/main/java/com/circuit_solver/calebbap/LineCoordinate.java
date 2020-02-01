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

    public LineCoordinate scale(double scale){

        double scaledStartX = (startX * scale);
        double scaledStartY = (startY * scale);
        double scaledEndX = (endX * scale);
        double scaledEndY = (endY * scale);
        
        return (new LineCoordinate(scaledStartX, scaledStartY, scaledEndX, scaledEndY));
    }

    public LineCoordinate applyShift(){
        double shiftedStartX = startX + CircuitControl.getXShift();
        double shiftedStartY = startY + CircuitControl.getYShift();
        double shiftedEndX = endX + CircuitControl.getXShift();
        double shiftedEndY = endY + CircuitControl.getYShift();
     
        return (new LineCoordinate(shiftedStartX, shiftedStartY, shiftedEndX, shiftedEndY));
    }
}