package com.circuit_solver.calebbap.components;

import com.circuit_solver.calebbap.CircuitControl;
import com.circuit_solver.calebbap.Component;
import com.circuit_solver.calebbap.LineCoordinate;
import com.circuit_solver.calebbap.View;
import com.circuit_solver.calebbap.Point;

@SuppressWarnings("serial")
public final class Wire extends Component{

    public Wire(LineCoordinate newStartPosition, LineCoordinate newEndPosition, double newAngle, 
            boolean newQuadrant) {
        super(newStartPosition, newEndPosition, newAngle, newQuadrant);
    }
    
    @Override
    public LineCoordinate[] drawComponent(){
        LineCoordinate[] drawing;
        double width = View.getRoot().getWidth() * CircuitControl.getScale();
        double height = View.getRoot().getHeight() * CircuitControl.getScale();

        Point start = new Point(relativeStartPosition.getEnd().getX() * width, relativeStartPosition.getEnd().getY() * height);
        Point end = new Point(relativeEndPosition.getStart().getX() * width, relativeEndPosition.getStart().getY() * height);

        LineCoordinate c0 = new LineCoordinate(start, end);
        drawing = new LineCoordinate[] {c0};
        return drawing;
    }
}

