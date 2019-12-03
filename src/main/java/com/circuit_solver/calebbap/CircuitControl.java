package com.circuit_solver.calebbap;

import com.circuit_solver.calebbap.components.Wire;

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

    private int[] numDots = { 0, 0 };

    private double dotSpacing = 0;
    private double dotXPosition = 0;
    private double dotYPosition = 0;

    private double clickX;
    private double clickY;

    private double componentEndX;
    private double componentEndY;

    private Model model;

    private Component component = null;
    double componentRadius = 20;

    CircuitControl(Canvas newCircuit, GraphicsContext newCircuitGraphics, Canvas newOverlayCircuit,
            GraphicsContext newOverlayCircuitGraphics, Model newModel) {
        circuit = newCircuit;
        circuitGraphics = newCircuitGraphics;
        overlayCircuit = newOverlayCircuit;
        overlayCircuitGraphics = newOverlayCircuitGraphics;
        model = newModel;
    }

    void mouseControl() {
        final EventHandler<MouseEvent> mouseMoved = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (dotSpacing != 0) {
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
                if (withinBounds(event.getX(), event.getY())) {
                    double[] relativePosition = relativeMousePosition(event.getX(), event.getY());
                    clickX = relativePosition[0];
                    clickY = relativePosition[1];
                }
            }
        };

        final EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (withinBounds(event.getX(), event.getY())) {
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

    void drawCircuitBackground() {
        int numXDots = 0;
        int numYDots = 0;
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        dotSpacing = (width / height) * 8;
        
        circuitGraphics.setFill(Color.BLUE);
        for (double x = dotSpacing; x <= ((width * 0.85) - dotSpacing); x += dotSpacing) {
            for (double y = dotSpacing; y <= ((height * 0.95) - dotSpacing); y += dotSpacing) {
                circuitGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                numYDots++;
            }
            numXDots++;
        }

        numYDots /= numXDots;

        numDots[0] = numXDots;
        numDots[1] = numYDots;
    }

    void circuitHover(double x, double y) {
        clearOverlay();

        double[] relativePosition = relativeMousePosition(x, y);
        dotXPosition = relativePosition[0];
        dotYPosition = relativePosition[1];

        overlayCircuitGraphics.setStroke(Color.BLACK);
        overlayCircuitGraphics.setLineWidth(2);
        overlayCircuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
    }

    void clearOverlay() {
        overlayCircuitGraphics.clearRect(0, 0, overlayCircuit.getWidth(), overlayCircuit.getHeight());
    }

    public void clearCircuit() {
        circuitGraphics.clearRect(0, 0, circuit.getWidth(), circuit.getHeight());
    }

    public void resizeCircuit() {
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        circuit.setHeight(height * 0.95);
        circuit.setWidth(width * 0.85);
        overlayCircuit.setHeight(height * 0.95);
        overlayCircuit.setWidth(width * 0.85);
        /*double width = View.getStage().getWidth() * 0.85; 
        double height = View.getStage().getHeight() * 0.95;
        circuit.setHeight(height);
        circuit.setWidth(width);
        overlayCircuit.setHeight(height);
        overlayCircuit.setWidth(width);*/
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

    // Update with gap between wires for component?
    void drawComponent(Coordinate coordinate){
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);
        circuitGraphics.strokeLine(coordinate.startX, coordinate.startY, coordinate.endX, coordinate.endY);
    }

    void drawComponent(){
        clearOverlay();

        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);

        if(View.getTool() != View.Tool.WIRE){
            double angle = Math.atan( Math.abs(clickY - componentEndY) / Math.abs(clickX - componentEndX) );

            Coordinate[] endCoordinates = drawComponentEnds(angle);
            Coordinate lowerEnd = endCoordinates[0];
            Coordinate higherEnd = endCoordinates[1];

            if( ( (lowerEnd.startX < lowerEnd.endX) && (higherEnd.startX < higherEnd.endX) ) ||
                ( (lowerEnd.startX == higherEnd.startX) && (lowerEnd.startY < lowerEnd.endY) 
                && (higherEnd.startY < higherEnd.endY) ) ){

                circuitGraphics.strokeLine(lowerEnd.startX, lowerEnd.startY, lowerEnd.endX, lowerEnd.endY);
                circuitGraphics.strokeLine(higherEnd.startX, higherEnd.startY, higherEnd.endX, higherEnd.endY);

                // Make component using start and end coordinates of gap
                switch(View.getTool()){
                    case WIRE:
                        component = new Wire(new Coordinate(lowerEnd.endX, lowerEnd.endY, 
                        higherEnd.startX, higherEnd.startY), angle);
                        break;
                    case RESISTOR:
                        break;
                    default:
                        return;
                }
                
                Coordinate[] drawing = component.drawComponent();
                for(int x = 0; x < drawing.length; x++){
                    circuitGraphics.strokeLine(drawing[x].startX, drawing[x].startY, drawing[x].endX, drawing[x].endY);  
                }
                
                Coordinate coordinate = new Coordinate(clickX, clickY, componentEndX, componentEndY);
                model.write(coordinate);

            }
        }else if(View.getTool() == View.Tool.WIRE){
            circuitGraphics.strokeLine(clickX, clickY, componentEndX, componentEndY);
            Coordinate coordinate = new Coordinate(clickX, clickY, componentEndX, componentEndY);
            model.write(coordinate);
        }
    }

    Coordinate[] drawComponentEnds(double angle){
        Coordinate lowerEnd = new Coordinate();
        Coordinate higherEnd = new Coordinate();

        double middleX = Math.abs(clickX + componentEndX) / 2;
        double middleY = Math.abs(clickY + componentEndY) / 2;
        
        if(clickX == componentEndX){
            lowerEnd.startX = lowerEnd.endX = higherEnd.startX = higherEnd.endX = clickX;
            lowerEnd.endY = middleY - componentRadius;
            higherEnd.startY = middleY + componentRadius;
            if(clickY < componentEndY){
                lowerEnd.startY = clickY;
                higherEnd.endY = componentEndY;
            }else{
                lowerEnd.startY = componentEndY;
                higherEnd.endY = clickY;
            }
        }else if(clickY == componentEndY){  // Straight horizontal line
            lowerEnd.startY = lowerEnd.endY = higherEnd.startY = higherEnd.endY = clickY;
            lowerEnd.endX = middleX - componentRadius;
            higherEnd.startX = middleX + componentRadius;
            if(clickX < componentEndX){
                lowerEnd.startX = clickX;
                higherEnd.endX = componentEndX;
            }else{
                lowerEnd.startX = componentEndX;
                higherEnd.endX = clickX;
            }
        }else if( (clickX < componentEndX) && (clickY < componentEndY) ){
            lowerEnd.startX = clickX;
            lowerEnd.startY = clickY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY - (componentRadius * Math.sin(angle));
            
            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY + (componentRadius * Math.sin(angle));
            higherEnd.endX = componentEndX;
            higherEnd.endY = componentEndY;
        }else if( (componentEndX < clickX) && (componentEndY < clickY) ){
            lowerEnd.startX = componentEndX;
            lowerEnd.startY = componentEndY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY - (componentRadius * Math.sin(angle));
            
            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY + (componentRadius * Math.sin(angle));
            higherEnd.endX = clickX;
            higherEnd.endY = clickY;
        }else if( (clickX < componentEndX) && (componentEndY < clickY) ){
            lowerEnd.startX = clickX;
            lowerEnd.startY = clickY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY + (componentRadius * Math.sin(angle));
            
            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY - (componentRadius * Math.sin(angle));
            higherEnd.endX = componentEndX;
            higherEnd.endY = componentEndY;            
        }else if( (componentEndX < clickX) && (clickY < componentEndY) ){
            lowerEnd.startX = componentEndX;
            lowerEnd.startY = componentEndY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY + (componentRadius * Math.sin(angle));
            
            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY - (componentRadius * Math.sin(angle));
            higherEnd.endX = clickX;
            higherEnd.endY = clickY;            
        }

        return (new Coordinate[] {lowerEnd, higherEnd});
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