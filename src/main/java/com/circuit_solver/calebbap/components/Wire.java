package com.circuit_solver.calebbap.components;

import com.circuit_solver.calebbap.CircuitControl;
import com.circuit_solver.calebbap.Component;
import com.circuit_solver.calebbap.LineCoordinate;
import com.circuit_solver.calebbap.View;

public final class Wire extends Component{

    public Wire(LineCoordinate newStartPosition, LineCoordinate newEndPosition, double newAngle, 
            boolean newQuadrant) {
        super(newStartPosition, newEndPosition, newAngle, newQuadrant);
    }
    
    @Override
    public LineCoordinate[] drawComponent(){
        LineCoordinate[] drawing;
        
        double width = View.getRoot().getWidth();
        double height = View.getRoot().getHeight();

        double startX = relativeStartPosition.getEndX() * width;
        double startY = relativeStartPosition.getEndY() * height;
        double endX = startX + (2 * CircuitControl.getDotSpacing() * Math.cos(angle));
        double endY = startY + (2 * CircuitControl.getDotSpacing() * Math.sin(angle));

        LineCoordinate c0 = new LineCoordinate(startX, startY, endX, endY);

        drawing = new LineCoordinate[] {c0};
        return drawing;
    }
}

