package com.circuit_solver.calebbap;

import javafx.scene.canvas.Canvas;

public class ResizableCanvas extends Canvas{

    ResizableCanvas(double width, double height){
        super(width, height);
    }

    @Override
    public boolean isResizable()
    {
        return true;
    }
}