package com.circuit_solver.calebbap;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Point implements Serializable{
    protected double x;
    protected double y;

    public Point(double newX, double newY){
        x = newX;
        y = newY;
    }

    public Point(){
        x = 0;
        y = 0;
    }

    public double getX(){ return x; }
    public double getY(){ return y; }
    public void setX(double newX){ x = newX; }
    public void setY(double newY){ y = newY; }
    public void setXY(double newX, double newY){ x = newX; y = newY; }

    public void decrementX(double value){ x -= value; }
    public void decrementY(double value){ y -= value; }
    public void incrementX(double value){ x += value; }
    public void incrementY(double value){ y += value; }
}