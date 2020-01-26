package com.circuit_solver.calebbap.components;

import com.circuit_solver.calebbap.Component;
import com.circuit_solver.calebbap.Coordinate;

public final class Wire extends Component{

    public Wire(Coordinate newCoordinate, double newAngle) {
        super(newCoordinate, newAngle);
    }
    
    @Override
    public Coordinate[] drawComponent(){
        Coordinate[] drawing;
        
        Coordinate c0 = new Coordinate(componentEnds.getStartX(), componentEnds.getStartY(), 
            componentEnds.getEndX(), componentEnds.getEndY());

        drawing = new Coordinate[] {c0};
        return drawing;
    }
}

