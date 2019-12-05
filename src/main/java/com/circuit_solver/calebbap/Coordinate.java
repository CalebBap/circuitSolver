package com.circuit_solver.calebbap;

import javafx.geometry.Bounds;

public class Coordinate{
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
        //double[] relativeStartPosition = View.getCircuitControl().relativePosition(scaledStartX, scaledStartY); 
        //scaledStartX = relativeStartPosition[0];
        //scaledStartY = relativeStartPosition[1];
        double scaledEndX = (endX * scale) + (xShift / width);
        double scaledEndY = (endY * scale) + (yShift / height);
        //double[] relativeEndPosition = View.getCircuitControl().relativePosition(scaledEndX, scaledEndY); 
        //scaledEndX = relativeEndPosition[0];
        //scaledEndY = relativeEndPosition[1];
        //View.getCircuitControl().resetXShift();
        //View.getCircuitControl().resetYShift();

        /*if(scaledStartX < 0){
            scaledStartX = 0.01;
        }else if(scaledStartX > 1){
            scaledStartX = 1;
        }
        
        if(scaledStartY < 0){
            scaledStartY = 0.01;
        }else if(scaledStartY > 1){
            scaledStartY = 1;
        }

        if(scaledEndX < 0){
            scaledEndX = 0.01;
        }else if(scaledEndX > 1){
            scaledEndX = 1;
        }
        
        if(scaledEndY < 0){
            scaledEndY = 0.01;
        }else if(scaledEndY > 1){
            scaledEndY = 1;
        }

        /*System.out.println("(" + Double.toString(startX) + ", " + Double.toString(startY) + ") -> (" +
            Double.toString(endX) + ", " + Double.toString(endY) + ")");
        System.out.println("(" + Double.toString(scaledStartX) + ", " + Double.toString(scaledStartY) + ") -> (" +
            Double.toString(scaledEndX) + ", " + Double.toString(scaledEndY) + ")");
        System.out.println("");*/

        return (new Coordinate(scaledStartX, scaledStartY, scaledEndX, scaledEndY));
    }
}