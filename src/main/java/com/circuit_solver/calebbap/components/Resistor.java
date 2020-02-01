package com.circuit_solver.calebbap.components;

import com.circuit_solver.calebbap.CircuitControl;
import com.circuit_solver.calebbap.Component;
import com.circuit_solver.calebbap.LineCoordinate;
import com.circuit_solver.calebbap.View;

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

        double startX, endX;
        startX = endX = relativeStartPosition.getEndX() * View.getRoot().getWidth() * CircuitControl.getScale();
        double startY, endY;
        startY = endY = relativeStartPosition.getEndY() * View.getRoot().getHeight() * CircuitControl.getScale();

        if(quadrantNE_SW){
            endY -= ( ((dotSpacing / 2) * Math.cos(adjustedAngle)) + (dotSpacing * Math.sin(adjustedAngle)) );
            endX -= ( (dotSpacing * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
        }else{
            endY -= ((dotSpacing * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)));
            endX += ((dotSpacing / 2) * Math.sin(adjustedAngle)) + (dotSpacing * Math.cos(adjustedAngle));
        }
        drawing[0] = new LineCoordinate(startX, startY, endX, endY);

        for(int i = 1; i < (drawing.length - 1); i++){
            startX = endX;
            startY = endY;

            if(i % 2 != 0){
                if(quadrantNE_SW){
                    endY += ( ((dotSpacing * 2) * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
                    endX += ( ((dotSpacing * 2) * Math.cos(adjustedAngle)) + ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
                }else{
                    endY += ((dotSpacing * 2) * Math.sin(adjustedAngle)) + ((dotSpacing / 2) * Math.cos(adjustedAngle));
                    endX -= ( ((dotSpacing * 2) * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
                }
            }else{
                if(quadrantNE_SW){
                    endY -= ( ((dotSpacing * 2) * Math.sin(adjustedAngle)) + ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
                    endX -= ( ((dotSpacing * 2) * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
                }else{
                    endY -= ( ((dotSpacing * 2) * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
                    endX += ( ((dotSpacing / 2) * Math.sin(adjustedAngle)) + ( (dotSpacing * 2) * Math.cos(adjustedAngle)) );
                }
            }
            drawing[i] = new LineCoordinate(startX, startY, endX, endY);
        }

        startX = endX;
        startY = endY;
        if(quadrantNE_SW){
            endY += ( (dotSpacing * Math.sin(adjustedAngle)) - ((dotSpacing / 2) * Math.cos(adjustedAngle)) );
            endX += ( (dotSpacing * Math.cos(adjustedAngle)) + ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
        }else{
            endY += ((dotSpacing / 2) * Math.cos(adjustedAngle)) + (dotSpacing * Math.sin(adjustedAngle));
            endX -= ( (dotSpacing * Math.cos(adjustedAngle)) - ((dotSpacing / 2) * Math.sin(adjustedAngle)) );
        }
        drawing[7] = new LineCoordinate(startX, startY, endX, endY);

        return drawing;
    }

    private void setResistance(double newResistance){ resistance = newResistance; }

    public double getResistance(){ return resistance; }    
}