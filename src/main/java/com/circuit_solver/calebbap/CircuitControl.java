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

    private double componentEndX;
    private double componentEndY;

    private Model model;

    CircuitControl(Canvas newCircuit, GraphicsContext newCircuitGraphics, Canvas newOverlayCircuit, 
        GraphicsContext newOverlayCircuitGraphics, double frameWidth, Model newModel){
            circuit = newCircuit;
            circuitGraphics = newCircuitGraphics;
            overlayCircuit = newOverlayCircuit;
            overlayCircuitGraphics = newOverlayCircuitGraphics;
            dotSpacing  = frameWidth * 0.01;
            model = newModel;
    }

    void drawCircuitBackground(double width, double height){
        int numXDots = 0;
        int numYDots = 0;

        circuitGraphics.setFill(Color.BLUE);
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

    void circuitHover(double x, double y){
        clearOverlay();

        double[] relativePosition = relativeMousePosition(x, y);
        dotXPosition = relativePosition[0];
        dotYPosition = relativePosition[1];

        overlayCircuitGraphics.setStroke(Color.BLACK);
        overlayCircuitGraphics.setLineWidth(2);
        overlayCircuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
    }

    void clearOverlay(){
        overlayCircuitGraphics.clearRect(0, 0, overlayCircuit.getWidth(), overlayCircuit.getHeight());
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

    void mouseControl(){
        final EventHandler<MouseEvent> mouseMoved = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(dotSpacing != 0){
                    circuitHover(event.getX(), event.getY());
                }
            }
        };
        
        final EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clearOverlay();
            }
        };

        final EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(withinBounds(event.getX(), event.getY())){
                    double[] relativePosition = relativeMousePosition(event.getX(), event.getY());
                    clickX = relativePosition[0];
                    clickY = relativePosition[1]; 
                }
            }
        };

        final EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(withinBounds(event.getX(), event.getY())){
                    drawOverlayComponent(event.getX(), event.getY());
                }
            }
        };

        final EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                drawComponent();
            }
        };

        circuit.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMoved);
        circuit.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExited);
        circuit.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
        circuit.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragged);
        circuit.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleased);
    }

    void drawOverlayComponent(double x, double y){
        double[] relativePosition = relativeMousePosition(x, y);
        componentEndX = relativePosition[0];
        componentEndY = relativePosition[1];
        clearOverlay();
        circuitHover(x, y);
        overlayCircuitGraphics.setStroke(Color.BLACK);
        overlayCircuitGraphics.setLineWidth(4);
        overlayCircuitGraphics.strokeLine(clickX, clickY + 1, componentEndX, componentEndY + 1);
    }

    void drawComponent(){
        clearOverlay();

        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);
        circuitGraphics.strokeLine(clickX, clickY, componentEndX, componentEndY);
        model.write(clickX, clickY, componentEndX, componentEndY);
    }

    void drawComponent(double startX, double startY, double endX, double endY){
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);
        circuitGraphics.strokeLine(clickX, clickY, componentEndX, componentEndY);
    }

    Boolean withinBounds(double x, double y){
        Bounds canvasBounds = circuit.getBoundsInLocal();

        if( x > canvasBounds.getMaxX() || x < canvasBounds.getMinX() || 
            y > canvasBounds.getMaxY() || y < canvasBounds.getMinY() ){
                return false;
        }
        
        return true;
    }
}