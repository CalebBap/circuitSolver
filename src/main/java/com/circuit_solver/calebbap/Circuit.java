package com.circuit_solver.calebbap;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Circuit{

    private Canvas mainCircuit;
    private GraphicsContext mainCircuitGraphics;
    private Canvas mainTempCircuit;
    private GraphicsContext mainTempCircuitGraphics;
    
    private int[] numDots = {0, 0};

    public double dotSpacing = 0;
    private double dotXPosition = 0;
    private double dotYPosition = 0;

    public double clickX;
    public double clickY;

    Circuit(Canvas circuit, GraphicsContext circuitGraphics, Canvas tempCircuit, GraphicsContext tempCircuitGraphics){
        mainCircuit = circuit;
        mainCircuitGraphics = circuitGraphics;
        mainTempCircuit = tempCircuit;
        mainTempCircuitGraphics = tempCircuitGraphics;
    }

    void drawComponent(double x, double y){
        double[] relativePosition = relativeMousePosition(x, y);
        x = relativePosition[0];
        y = relativePosition[1];
        mainTempCircuitGraphics.clearRect(0, 0, mainTempCircuit.getWidth(), mainTempCircuit.getHeight());
        mainTempCircuitGraphics.setStroke(Color.BLACK);
        mainTempCircuitGraphics.setLineWidth(4);
        mainTempCircuitGraphics.strokeLine(clickX, clickY, x, y);
    }

    void clearComponent(){

    }

    void circuitHover(double x, double y){
        clearHover();
        
        double[] relativePosition = relativeMousePosition(x, y);
        dotXPosition = relativePosition[0];
        dotYPosition = relativePosition[1];

        mainCircuitGraphics.setFill(Color.TRANSPARENT);
        mainCircuitGraphics.setStroke(Color.BLACK);
        mainCircuitGraphics.setLineWidth(2);
        mainCircuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
    }

    void clearHover(){
        if((dotXPosition + dotYPosition) != 0 ){
            mainCircuitGraphics.setFill(Color.TRANSPARENT);
            mainCircuitGraphics.setStroke(Color.WHITE);
            mainCircuitGraphics.setLineWidth(3);
            mainCircuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
        }
    }

    double[] relativeMousePosition(double x, double y){
        Bounds canvasBounds = mainCircuit.getBoundsInParent();
        double relativeXPostion = x / canvasBounds.getWidth();
        double relativeYPostion = y / canvasBounds.getHeight();
        
        int numDotX = (int) (relativeXPostion * numDots[0] + 1);
        int numDotY = (int) (relativeYPostion * numDots[1] + 1);

        double[] relativePosition = {(dotSpacing * numDotX), (dotSpacing * numDotY)};
        return relativePosition;
    }

    void drawCircuitBackground(double width, double height){
        int numXDots = 0;
        int numYDots = 0;

        for(double x = dotSpacing; x <= ((width * 0.85) - dotSpacing); x+=dotSpacing){
            for(double y = dotSpacing; y <= ((height * 0.95) - dotSpacing); y+=dotSpacing){
                mainCircuitGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                numYDots++;
            }
            numXDots++;
        }

        numYDots /= numXDots;

        numDots[0] = numXDots;
        numDots[1] = numYDots;
    }
}