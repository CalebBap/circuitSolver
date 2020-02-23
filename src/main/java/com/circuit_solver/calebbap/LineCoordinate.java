package com.circuit_solver.calebbap;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LineCoordinate implements Serializable {
    Point start = new Point();
    Point end = new Point();

    public LineCoordinate(){ }

    public LineCoordinate(Point newStart, Point newEnd){
        start = newStart;
        end = newEnd;
    }

    public Point getStart(){
        return start;
    }

    public Point getEnd(){
        return end;
    }

    public void setValues(Point newStart, Point newEnd){
        start = newStart;
        end = newEnd;
    }

    public void applyRelativePosition(){
        double width = View.getRoot().getWidth() * CircuitControl.getScale();
        double height = View.getRoot().getHeight() * CircuitControl.getScale();

        start.x += CircuitControl.getShift().x / width;
        start.y += CircuitControl.getShift().y / height;
        end.x += CircuitControl.getShift().x / width;
        end.y += CircuitControl.getShift().y / height;
    }

    public void applyAbsolutePosition(){
        double width = View.getRoot().getWidth() * CircuitControl.getScale();
        double height = View.getRoot().getHeight() * CircuitControl.getScale();

        start.x -= CircuitControl.getShift().x / width;
        start.y -= CircuitControl.getShift().y / height;
        end.x -= CircuitControl.getShift().x / width;
        end.y -= CircuitControl.getShift().y / height;
    }
}