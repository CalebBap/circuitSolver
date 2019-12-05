package com.circuit_solver.calebbap;

public class Coordinate {
    double startX;
    double startY;
    double endX;
    double endY;

    public Coordinate(){ }

    public Coordinate(double newStartX, double newStartY, double newEndX, double newEndY){
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

    public Coordinate scale(double scale){
        double width = View.getRoot().getWidth();
        double height = View.getRoot().getHeight();
        double xShift = View.getCircuitControl().getXShift();
        double yShift = View.getCircuitControl().getYShift();

        double scaledStartX = (startX * scale) + (xShift / width);
        double scaledStartY = (startY * scale) + (yShift / height);
        double scaledEndX = (endX * scale) + (xShift / width);
        double scaledEndY = (endY * scale) + (yShift / height);
        
        return (new Coordinate(scaledStartX, scaledStartY, scaledEndX, scaledEndY));
    }
}