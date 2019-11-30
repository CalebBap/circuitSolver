package com.circuit_solver.calebbap;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

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

    private Component component = null;

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
                if( /*(component != null) &&*/ (withinBounds(event.getX(), event.getY())) ){
                    double[] relativePosition = relativeMousePosition(event.getX(), event.getY());
                    clickX = relativePosition[0];
                    clickY = relativePosition[1]; 
                }
            }
        };

        final EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if( /*(component != null) &&*/ (withinBounds(event.getX(), event.getY())) ){
                    drawOverlayComponent(event.getX(), event.getY());
                }
            }
        };

        final EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //if(component != null){
                    drawComponent();
                //}
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

        //circuitGraphics.strokeLine(clickX, clickY, componentEndX, componentEndY);

        // USE COORDINATE CLASS HERE AND IN REST OF THE CODE
        double componentRadius = 20;

        double startLowerX = 0;
        double startLowerY = 0;
        double endLowerX = 0;
        double endLowerY = 0;

        double startHigherX = 0;
        double startHigherY = 0;
        double endHigherX = 0;
        double endHigherY = 0;

        double middleX = Math.abs(clickX + componentEndX) / 2;
        double middleY = Math.abs(clickY + componentEndY) / 2;

        double angle = Math.atan( Math.abs(clickY - componentEndY) / Math.abs(clickX - componentEndX) );
        
        if(clickX == componentEndX){  // Straight vertical line
            startLowerX = endLowerX = startHigherX = endHigherX = clickX;
            endLowerY = middleY - componentRadius;
            startHigherY = middleY + componentRadius;
            if(clickY < componentEndY){
                startLowerY = clickY;
                endHigherY = componentEndY;
            }else{
                startLowerY = componentEndY;
                endHigherY = clickY;
            }
        }else if(clickY == componentEndY){  // Straight horizontal line
            startLowerY = endLowerY = startHigherY = endHigherY = clickY;
            endLowerX = middleX - componentRadius;
            startHigherX = middleX + componentRadius;
            if(clickX < componentEndX){
                startLowerX = clickX;
                endHigherX = componentEndX;
            }else{
                startLowerX = componentEndX;
                endHigherX = clickX;
            }
        }else if( (clickX < componentEndX) && (clickY < componentEndY) ){
            startLowerX = clickX;
            startLowerY = clickY;
            endLowerX = middleX - (componentRadius * Math.cos(angle));
            endLowerY = middleY - (componentRadius * Math.sin(angle));
            
            startHigherX = middleX + (componentRadius * Math.cos(angle));
            startHigherY = middleY + (componentRadius * Math.sin(angle));
            endHigherX = componentEndX;
            endHigherY = componentEndY;
        }else if( (componentEndX < clickX) && (componentEndY < clickY) ){
            startLowerX = componentEndX;
            startLowerY = componentEndY;
            endLowerX = middleX - (componentRadius * Math.cos(angle));
            endLowerY = middleY - (componentRadius * Math.sin(angle));
            
            startHigherX = middleX + (componentRadius * Math.cos(angle));
            startHigherY = middleY + (componentRadius * Math.sin(angle));
            endHigherX = clickX;
            endHigherY = clickY;
        }else if( (clickX < componentEndX) && (componentEndY < clickY) ){
            startLowerX = clickX;
            startLowerY = clickY;
            endLowerX = middleX - (componentRadius * Math.cos(angle));
            endLowerY = middleY + (componentRadius * Math.sin(angle));
            
            startHigherX = middleX + (componentRadius * Math.cos(angle));
            startHigherY = middleY - (componentRadius * Math.sin(angle));

            endHigherX = componentEndX;
            endHigherY = componentEndY;            
        }else if( (componentEndX < clickX) && (clickY < componentEndY) ){
            startLowerX = componentEndX;
            startLowerY = componentEndY;
            endLowerX = middleX - (componentRadius * Math.cos(angle));
            endLowerY = middleY + (componentRadius * Math.sin(angle));
            
            startHigherX = middleX + (componentRadius * Math.cos(angle));
            startHigherY = middleY - (componentRadius * Math.sin(angle));
            endHigherX = clickX;
            endHigherY = clickY;            
        }

        circuitGraphics.strokeLine(startLowerX, startLowerY, endLowerX, endLowerY);
        circuitGraphics.strokeLine(startHigherX, startHigherY, endHigherX, endHigherY);
        
        Coordinate coordinate = new Coordinate(clickX, clickY, componentEndX, componentEndY);
        model.write(coordinate);
    }

    void drawComponent(Coordinate coordinate){
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);
        circuitGraphics.strokeLine(coordinate.startX, coordinate.startY, coordinate.endX, coordinate.endY);
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