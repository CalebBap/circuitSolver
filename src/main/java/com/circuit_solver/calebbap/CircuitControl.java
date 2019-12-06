package com.circuit_solver.calebbap;

import com.circuit_solver.calebbap.components.*;

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
    private Canvas circuitBackground;
    private GraphicsContext circuitBackgroundGraphics;

    private int[] numDots = { 0, 0 };

    private double dotXPosition = 0;
    private double dotYPosition = 0;

    private double clickX;
    private double clickY;

    private double componentEndX;
    private double componentEndY;

    private double orginalMouseX;
    private double orginalMouseY;
    private double xShift = 0;
    private double yShift = 0;

    private static double scale = 1;

    private Model model;

    private Component component = null;
    double componentRadius = 20;

    CircuitControl(Canvas newCircuit, GraphicsContext newCircuitGraphics, Canvas newOverlayCircuit,
            GraphicsContext newOverlayCircuitGraphics, Canvas newCircuitBackground, GraphicsContext newCircuitBackgroundGraphics, 
            Model newModel) {
        circuit = newCircuit;
        circuitGraphics = newCircuitGraphics;
        overlayCircuit = newOverlayCircuit;
        overlayCircuitGraphics = newOverlayCircuitGraphics;
        circuitBackground = newCircuitBackground;
        circuitBackgroundGraphics = newCircuitBackgroundGraphics;
        model = newModel;
    }

    void mouseControl() {
        final EventHandler<MouseEvent> mouseMoved = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if ( (View.getRoot().getChildren().contains(circuit)) && (View.getTool() != View.Tool.MOVE) ) {
                    circuitHover(event.getX() + xShift, event.getY() + yShift);
                }else{
                    clearOverlay();
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
                    if(View.getTool() != View.Tool.MOVE){
                        double[] relativePosition = relativePosition(event.getX() + xShift, event.getY() + yShift);
                        clickX = relativePosition[0];
                        clickY = relativePosition[1];
                    }
                    orginalMouseX = event.getY();
                    orginalMouseY = event.getY();
                }
            }
        };

        final EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (withinBounds(event.getX(), event.getY()) && (View.getTool() != View.Tool.MOVE) ) {
                    drawOverlayComponent(event.getX() + xShift, event.getY() + yShift);
                }
            }
        };

        final EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(View.getTool() != View.Tool.MOVE){
                    drawComponent();
                }else{
                    clearCircuit();
                    shiftBackground();
                    model.drawFromFile();
                }
            }
        };

        final EventHandler<MouseEvent> dragToMove = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(View.getTool() == View.Tool.MOVE){
                    double height = View.getRoot().getHeight();
                    double width = View.getRoot().getWidth();
                    xShift += (orginalMouseX - event.getX()) * 0.4 * (height / width);
                    yShift += (orginalMouseY - event.getY()) * 0.4 * (width / height); 
                    orginalMouseX = event.getX();
                    orginalMouseY = event.getY();

                    clearCircuit();
                    shiftBackground();
                    model.drawFromFile();
                }
            }
        };

        circuit.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMoved);
        circuit.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExited);
        circuit.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
        circuit.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragged);
        circuit.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragToMove);
        circuit.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleased);
    }

    public double getXShift(){
        return xShift;
    }

    public double getYShift(){
        return yShift;
    }

    public void resetXShift(){
        xShift = 0;
    }

    public void resetYShift(){
        yShift = 0;
    }

    void drawCircuitBackground() {
        int numXDots = 0;
        int numYDots = 0;
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        double dotSpacing = (width / height) * 8 * scale;
        
        circuitBackgroundGraphics.setFill(Color.BLUE);
        for (double x = dotSpacing; x <= ((width * 0.85) - dotSpacing); x += dotSpacing) {
            for (double y = dotSpacing; y <= ((height * 0.95) - dotSpacing); y += dotSpacing) {
                circuitBackgroundGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                numYDots++;
            }
            numXDots++;
        }

        numYDots /= numXDots;

        numDots[0] = numXDots;
        numDots[1] = numYDots;
    }

    void shiftBackground() {
        int numXDots = 0;
        int numYDots = 0;
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        double dotSpacing = (width / height) * 8 * scale;

        double xStart = dotSpacing;
        double yStart = dotSpacing;
        double xEnd = (width * 0.85) - dotSpacing;
        double yEnd = (height * 0.95) - dotSpacing;

        if(xShift < 0){
            xStart += xShift;
            xEnd -= xShift;
        }else if(xShift > 0){
            xStart -= xShift;
            xEnd += xShift;
        }
        if(yShift < 0){
            yStart += yShift;
            yEnd -= yShift;
        }else if(yShift > 0){
            yStart -= yShift;
            yEnd += yShift;
        }     

        circuitBackgroundGraphics.setFill(Color.BLUE);
        for (double x = xStart; x <= xEnd; x += dotSpacing) {
            for (double y = yStart; y <= yEnd; y += dotSpacing) {
                circuitBackgroundGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
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

        double[] relativePosition = relativePosition(x, y);
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
        circuitBackgroundGraphics.clearRect(0, 0, circuitBackground.getWidth(), circuitBackground.getHeight());
    }

    public void resizeCircuit() {
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        circuit.setHeight(height * 0.95);
        circuit.setWidth(width * 0.85);
        overlayCircuit.setHeight(height * 0.95);
        overlayCircuit.setWidth(width * 0.85);
    }

    public double[] relativePosition(double x, double y){
        
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        double dotSpacing = (width / height) * 8 * scale;

        Bounds canvasBounds = circuit.getBoundsInParent();
        double relativeXPostion = x / canvasBounds.getWidth();
        double relativeYPostion = y / canvasBounds.getHeight();
        
        int numDotX = (int) (relativeXPostion * numDots[0] + 1);
        int numDotY = (int) (relativeYPostion * numDots[1] + 1);

        double[] relativePosition = {(dotSpacing * numDotX), (dotSpacing * numDotY)};
        return relativePosition;
    }

    void drawOverlayComponent(double x, double y){
        double[] relativePosition = relativePosition(x, y);
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
        double width = circuitBackground.getWidth();
        double height = circuitBackground.getHeight();

        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);
        
        double xStart = coordinate.startX * width;
        double yStart = coordinate.startY * height;
        double xEnd = coordinate.endX * width;
        double yEnd = coordinate.endY * height;

        if(xShift < 0){
            xStart += xShift;
            xEnd += xShift;
        }else if(xShift > 0){
            xStart -= xShift;
            xEnd -= xShift;
        }
        if(yShift < 0){
            yStart += yShift;
            yEnd += yShift;
        }else if(yShift > 0){
            yStart -= yShift;
            yEnd -= yShift;
        } 

        circuitGraphics.strokeLine(xStart, yStart, xEnd, yEnd);
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
                
                Coordinate coordinate = new Coordinate(clickX / circuit.getWidth(), clickY / circuit.getHeight(), 
                    componentEndX / circuit.getWidth(), componentEndY / circuit.getHeight());
                model.write(coordinate);

            }
        }else if(View.getTool() == View.Tool.WIRE){
            circuitGraphics.strokeLine(clickX, clickY, componentEndX, componentEndY);
            Coordinate coordinate = new Coordinate(clickX / circuit.getWidth(), clickY / circuit.getHeight(), 
                    componentEndX / circuit.getWidth(), componentEndY / circuit.getHeight());
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

    public double getScale(){
        return scale;
    }

    public void setScale(double multiplier){
        scale *= multiplier;
    }

    public Canvas getCircuit(){
        return circuit;
    }

    public void zoom(){
        clearCircuit();
        drawCircuitBackground();
        model.drawFromFile();
    }
}