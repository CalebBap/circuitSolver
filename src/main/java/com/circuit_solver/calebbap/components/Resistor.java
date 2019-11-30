package com.circuit_solver.calebbap.components;

import com.circuit_solver.calebbap.Component;
import com.circuit_solver.calebbap.*;

public final class Resistor extends Component {
    private double resistance;

    Resistor(Coordinate newCoordinate, double newAngle, double newResistance) {
        super(newCoordinate, newAngle);

        resistance = newResistance;
    }
    
    @Override
    public Coordinate[] drawComponent(){
        Coordinate[] drawing;

        return null;//drawing;
    }

    private void setResistance(double newResistance){
        resistance = newResistance;
    }

    public double getResistance(){
        return resistance;
    }

}