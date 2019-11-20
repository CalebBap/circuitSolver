package com.circuit_solver.calebbap;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CircuitControl{

    private Canvas circuit;
    private GraphicsContext circuitGraphics;
    private Canvas overlayCircuit;
    private GraphicsContext overlayCircuitGraphics;
    
    private int[] numDots = {0, 0};

    private double dotSpacing = 0;
    private double dotXPosition = 0;
    private double dotYPosition = 0;

    private double clickX;
    private double clickY;

    CircuitControl(Canvas newCircuit, GraphicsContext newCircuitGraphics, Canvas newOverlayCircuit, 
        GraphicsContext newOverlayCircuitGraphics, double frameWidth){
            circuit = newCircuit;
            circuitGraphics = newCircuitGraphics;
            overlayCircuit = newOverlayCircuit;
            overlayCircuitGraphics = newOverlayCircuitGraphics;
            dotSpacing  = frameWidth * 0.01;
    }

    void drawComponent(double x, double y){
        double[] relativePosition = relativeMousePosition(x, y);
        x = relativePosition[0];
        y = relativePosition[1];
        overlayCircuitGraphics.clearRect(0, 0, overlayCircuit.getWidth(), overlayCircuit.getHeight());
        overlayCircuitGraphics.setStroke(Color.BLACK);
        overlayCircuitGraphics.setLineWidth(4);
        overlayCircuitGraphics.strokeLine(clickX, clickY, x, y);
    }

    void clearComponent(){

    }

    void circuitHover(double x, double y){
        clearHover();
        
        double[] relativePosition = relativeMousePosition(x, y);
        dotXPosition = relativePosition[0];
        dotYPosition = relativePosition[1];

        circuitGraphics.setFill(Color.TRANSPARENT);
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(2);
        circuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
    }

    void clearHover(){
        if((dotXPosition + dotYPosition) != 0 ){
            circuitGraphics.setFill(Color.TRANSPARENT);
            circuitGraphics.setStroke(Color.WHITE);
            circuitGraphics.setLineWidth(3);
            circuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
        }
    }

    double[] relativeMousePosition(double x, double y){
        Bounds canvasBounds = circuit.getBoundsInParent();
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
                circuitGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                numYDots++;
            }
            numXDots++;
        }

        numYDots /= numXDots;

        numDots[0] = numXDots;
        numDots[1] = numYDots;
    }

    void mouseControl(){
        circuit.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(dotSpacing != 0){
                    circuitHover(event.getX(), event.getY());
                }
            }
        });

        circuit.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clearHover();
            }
        });

        circuit.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                double[] relativePosition = relativeMousePosition(event.getX(), event.getY());
                clickX = relativePosition[0];
                clickY = relativePosition[1]; 
                circuit.setMouseTransparent(true);
            }
        });

        circuit.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                drawComponent(event.getX(), event.getY());
            }
        });

        circuit.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Mouse Released");
            }
        });
    }
}