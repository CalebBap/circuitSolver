package com.circuit_solver.calebbap.components;

import com.circuit_solver.calebbap.CircuitControl;
import com.circuit_solver.calebbap.Component;
import com.circuit_solver.calebbap.LineCoordinate;
import com.circuit_solver.calebbap.View;
import com.circuit_solver.calebbap.Point;

@SuppressWarnings("serial")
public final class Resistor extends Component{
    private double resistance;

    public Resistor(LineCoordinate newStartPosition, LineCoordinate newEndPosition, double newAngle, 
            boolean newQuadrant, double newResistance) {
        super(newStartPosition, newEndPosition, newAngle, newQuadrant);
        resistance = newResistance;
    }
    
    @Override
    public LineCoordinate[] drawComponent(){
        LineCoordinate[] drawing = new LineCoordinate[8];
        double dotSpacing = CircuitControl.getDotSpacing();
        double adjustedAngle = (Math.PI / 2) - angle;

        double x = relativeStartPosition.getEnd().getX() * View.getRoot().getWidth() * CircuitControl.getScale();
        double y = relativeStartPosition.getEnd().getY() * View.getRoot().getHeight() * CircuitControl.getScale();
        Point start = new Point(x, y);
        Point end = new Point(x, y);

        if(quadrantNE_SW){
            end.decrementY( ((dotSpacing / 2) * Math.cos(adjustedAngle)) + (dotSpacing * Math.sin(adjustedAngle)) );
            end.decrementX ( (dotSpacing * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
        }else{
            end.decrementY( (dotSpacing * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
            end.incrementX( ((dotSpacing / 2) * Math.sin(adjustedAngle)) + (dotSpacing * Math.cos(adjustedAngle)) );
        }
        drawing[0] = new LineCoordinate(new Point(start.getX(), start.getY()), new Point(end.getX(), end.getY()));

        for(int i = 1; i < (drawing.length - 1); i++){
            start.setX(end.getX());
            start.setY(end.getY());

            if(i % 2 != 0){
                if(quadrantNE_SW){
                    end.incrementY( ((dotSpacing * 2) * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
                    end.incrementX( ((dotSpacing * 2) * Math.cos(adjustedAngle)) + ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
                }else{
                    end.incrementY( ((dotSpacing * 2) * Math.sin(adjustedAngle)) + ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
                    end.decrementX( ((dotSpacing * 2) * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
                }
            }else{
                if(quadrantNE_SW){
                    end.decrementY( ((dotSpacing * 2) * Math.sin(adjustedAngle)) + ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
                    end.decrementX( ((dotSpacing * 2) * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
                }else{
                    end.decrementY( ((dotSpacing * 2) * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
                    end.incrementX( ((dotSpacing / 2) * Math.sin(adjustedAngle)) + ( (dotSpacing * 2) * Math.cos(adjustedAngle)) );
                }
            }
            drawing[i] = new LineCoordinate(new Point(start.getX(), start.getY()), new Point(end.getX(), end.getY()));
        }

        start.setX(end.getX());
        start.setY(end.getY());
        if(quadrantNE_SW){
            end.incrementY( (dotSpacing * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
            end.incrementX( (dotSpacing * Math.cos(adjustedAngle)) + ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
        }else{
            end.incrementY( ((dotSpacing / 2) * Math.cos(adjustedAngle)) + (dotSpacing * Math.sin(adjustedAngle)) );
            end.decrementX( (dotSpacing * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
        }
        drawing[7] = new LineCoordinate(new Point(start.getX(), start.getY()), new Point(end.getX(), end.getY()));
        return drawing;
    }

    @SuppressWarnings("unused")
    private void setResistance(double newResistance){ resistance = newResistance; }

    public double getResistance(){ return resistance; }    
}