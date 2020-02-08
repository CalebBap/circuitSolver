package com.circuit_solver.calebbap;

import com.circuit_solver.calebbap.components.*;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
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
    private static double xShift = 0;
    private static double yShift = 0;

    private LineCoordinate backgroundShift = new LineCoordinate(0, 0, 0, 0);

    private static double scale = 1;
    private static double dotSpacing;

    private Model model;

    CircuitControl(Canvas newCircuit, GraphicsContext newCircuitGraphics, Canvas newOverlayCircuit,
            GraphicsContext newOverlayCircuitGraphics, Canvas newCircuitBackground,
            GraphicsContext newCircuitBackgroundGraphics, Model newModel) {
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
                if ((View.getRoot().getChildren().contains(circuit)) && (View.getTool() != View.Tool.MOVE)) {
                    circuitHover(event.getX(), event.getY());
                }
            }
        };

        final EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clearOverlay();
                View.getStage().getScene().setCursor(Cursor.DEFAULT);
            }
        };

        final EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                switch (View.getTool()) {
                case WIRE:
                    View.getStage().getScene().setCursor(Cursor.CROSSHAIR);
                    break;
                case MOVE:
                    View.getStage().getScene().setCursor(Cursor.MOVE);
                    break;
                default:
                    View.getStage().getScene().setCursor(Cursor.CROSSHAIR);
                    break;
                }
            }
        };

        final EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (withinBounds(event.getX(), event.getY())) {
                    if (View.getTool() != View.Tool.MOVE) {
                        double[] relativePosition = relativePosition(event.getX(), event.getY());
                        clickX = relativePosition[0];
                        clickY = relativePosition[1];
                    }
                    orginalMouseX = event.getX();
                    orginalMouseY = event.getY();
                }
            }
        };

        final EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (withinBounds(event.getX(), event.getY()) && (View.getTool() != View.Tool.MOVE)) {
                    drawOverlayComponent(event.getX(), event.getY());
                }
            }
        };

        final EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (View.getTool() != View.Tool.MOVE) {
                    drawComponent();
                }
            }
        };

        final EventHandler<MouseEvent> dragToMove = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (View.getTool() == View.Tool.MOVE) {
                    if (event.getX() > orginalMouseX) {
                        xShift -= (Math.abs(orginalMouseX - event.getX())) * 0.4;
                    } else {
                        xShift += (Math.abs(orginalMouseX - event.getX())) * 0.4;
                    }
                    if (event.getY() > orginalMouseY) {
                        yShift -= (Math.abs(orginalMouseY - event.getY())) * 0.4;
                    } else {
                        yShift += (Math.abs(orginalMouseY - event.getY())) * 0.4;
                    }

                    orginalMouseX = event.getX();
                    orginalMouseY = event.getY();

                    clearCircuit();
                    shiftBackground();
                    model.drawFromFile();
                }
            }
        };

        circuit.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMoved);
        circuit.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEntered);
        circuit.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExited);
        circuit.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
        circuit.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragged);
        circuit.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragToMove);
        circuit.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleased);
    }

    void drawCircuitBackground() {
        int numXDots = 0;
        int numYDots = 0;
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        dotSpacing = (width / height) * 8 * scale;

        circuitBackgroundGraphics.setFill(Color.BLUE);
        for (double x = 0; x <= (width * 0.85); x += dotSpacing) {
            for (double y = 0; y <= (height * 0.95); y += dotSpacing) {
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
        dotSpacing = (width / height) * 8 * scale;

        double xStart = 0;
        double xEnd = width * 0.85;
        double yStart = 0;
        double yEnd = height * 0.95;

        double xClipped = Math.abs(xShift) % dotSpacing;
        double yClipped = Math.abs(yShift) % dotSpacing;

        if (xShift < 0) {
            xStart += xClipped;
            xEnd -= xClipped;
        } else {
            xStart -= xClipped;
            xEnd += xClipped;
        }

        if (yShift < 0) {
            yStart += yClipped;
            yEnd -= yClipped;
        } else {
            yStart -= yClipped;
            yEnd += yClipped;
        }

        circuitBackgroundGraphics.setFill(Color.BLUE);
        for (double x = xStart; x <= xEnd; x += dotSpacing) {
            for (double y = yStart; y <= yEnd; y += dotSpacing) {
                circuitBackgroundGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                if ((y >= 0) && (y <= (height * 0.95))) {
                    numYDots++;
                }
            }
            if ((x >= 0) && (x <= (width * 0.85))) {
                numXDots++;
            }
        }

        numYDots /= numXDots;

        numDots[0] = numXDots;
        numDots[1] = numYDots;

        backgroundShift = new LineCoordinate(dotSpacing - xStart, dotSpacing - yStart, (width * 0.85) - xEnd,
                (height * 0.95) - yEnd);
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

    public double[] relativePosition(double x, double y) {
        double width = View.getRoot().getWidth();
        double height = View.getRoot().getHeight();
        dotSpacing = (width / height) * 8 * scale;
        Bounds canvasBounds = circuitBackground.getBoundsInParent();

        double relativeXPostion = x / canvasBounds.getWidth();
        double relativeYPostion = y / canvasBounds.getHeight();

        int numDotX = (int) (relativeXPostion * numDots[0] + 0.5);
        int numDotY = (int) (relativeYPostion * numDots[1] + 0.5);
        
        relativeXPostion = (dotSpacing * numDotX) - backgroundShift.startX;
        relativeYPostion = (dotSpacing * numDotY) - backgroundShift.startY;

        double[] relativePosition = { relativeXPostion, relativeYPostion };
        return relativePosition;
    }

    void drawOverlayComponent(double x, double y) {
        double[] relativePosition = relativePosition(x, y);
        componentEndX = relativePosition[0];
        componentEndY = relativePosition[1];
        clearOverlay();
        circuitHover(x, y);
        overlayCircuitGraphics.setStroke(Color.BLACK);
        overlayCircuitGraphics.setLineWidth(4);

        
        Component component = null;
        double width = View.getRoot().getWidth() * scale;
        double height = View.getRoot().getHeight() * scale;
        double angle = Math.atan(Math.abs(clickY - componentEndY) / Math.abs(clickX - componentEndX));
        LineCoordinate[] endLineCoordinates = drawComponentEnds(angle);
        LineCoordinate lowerEnd = endLineCoordinates[0];
        LineCoordinate higherEnd = endLineCoordinates[1];
        overlayCircuitGraphics.strokeLine(lowerEnd.startX, lowerEnd.startY, lowerEnd.endX, lowerEnd.endY);
        overlayCircuitGraphics.strokeLine(higherEnd.startX, higherEnd.startY, higherEnd.endX, higherEnd.endY);
        LineCoordinate relativeStartPosition = new LineCoordinate(lowerEnd.startX / width, lowerEnd.startY / height,
                            lowerEnd.endX / width, lowerEnd.endY / height);

        LineCoordinate relativeEndPosition = new LineCoordinate(higherEnd.startX / width, higherEnd.startY / height,
                        higherEnd.endX / width, higherEnd.endY / height);
                        boolean quadrantNE_SW = false;
        if( (lowerEnd.getStartX() < higherEnd.getEndX()) && (higherEnd.getEndY() < lowerEnd.getStartY()) || 
        lowerEnd.getStartY() == higherEnd.getEndY()){
            quadrantNE_SW = true;
        }
        switch (View.getTool()) {
            case WIRE:
                component = new Wire(relativeStartPosition, relativeEndPosition, angle, quadrantNE_SW);
                break;
            case RESISTOR:
                component = new Resistor(relativeStartPosition, relativeEndPosition, angle, quadrantNE_SW, 10);
                break;
            default:
                return;
        }

        LineCoordinate[] drawing = component.drawComponent();
        for (int z = 0; z < drawing.length; z++) {
            overlayCircuitGraphics.strokeLine(drawing[z].startX, drawing[z].startY, drawing[z].endX, drawing[z].endY);
        }

        //overlayCircuitGraphics.strokeLine(clickX, clickY + 1, componentEndX, componentEndY + 1);
    }

    void drawComponent(Component component) {
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);
        double width = View.getRoot().getWidth() * scale;
        double height = View.getRoot().getHeight() * scale;

        LineCoordinate[] drawing = component.drawComponent();
        for (int x = 0; x < drawing.length; x++) {
            drawing[x].startX -= xShift;
            drawing[x].startY -= yShift;
            drawing[x].endX -= xShift;
            drawing[x].endY -= yShift;
            circuitGraphics.strokeLine(drawing[x].startX, drawing[x].startY, drawing[x].endX, drawing[x].endY);
        }

        double xStart = ((component.relativeStartPosition.startX * width) - xShift);
        double yStart = ((component.relativeStartPosition.startY * height) - yShift);
        double xEnd = ((component.relativeEndPosition.endX * width) - xShift);
        double yEnd = ((component.relativeEndPosition.endY * height) - yShift);

        circuitGraphics.strokeLine(xStart, yStart, drawing[0].startX, drawing[0].startY);
        circuitGraphics.strokeLine(drawing[drawing.length - 1].endX, drawing[drawing.length - 1].endY, xEnd, yEnd);
    }

    void drawComponent() {
        clearOverlay();
        Component component = null;
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);

        double angle = Math.atan(Math.abs(clickY - componentEndY) / Math.abs(clickX - componentEndX));
        LineCoordinate[] endLineCoordinates = drawComponentEnds(angle);
        LineCoordinate lowerEnd = endLineCoordinates[0];
        LineCoordinate higherEnd = endLineCoordinates[1];
        double width = View.getRoot().getWidth() * scale;
        double height = View.getRoot().getHeight() * scale;

        if (((lowerEnd.startX < lowerEnd.endX) && (higherEnd.startX < higherEnd.endX))
                || ((lowerEnd.startX == higherEnd.startX) && (lowerEnd.startY < lowerEnd.endY)
                        && (higherEnd.startY < higherEnd.endY))) {

            circuitGraphics.strokeLine(lowerEnd.startX, lowerEnd.startY, lowerEnd.endX, lowerEnd.endY);
            circuitGraphics.strokeLine(higherEnd.startX, higherEnd.startY, higherEnd.endX, higherEnd.endY);

            LineCoordinate relativeStartPosition = new LineCoordinate(lowerEnd.startX / width, lowerEnd.startY / height,
                            lowerEnd.endX / width, lowerEnd.endY / height);

            LineCoordinate relativeEndPosition = new LineCoordinate(higherEnd.startX / width, higherEnd.startY / height,
                            higherEnd.endX / width, higherEnd.endY / height);
           
            boolean quadrantNE_SW = false;
            if( (lowerEnd.getStartX() < higherEnd.getEndX()) && (higherEnd.getEndY() < lowerEnd.getStartY()) || 
            lowerEnd.getStartY() == higherEnd.getEndY()){
                quadrantNE_SW = true;
            }

            switch (View.getTool()) {
                case WIRE:
                    component = new Wire(relativeStartPosition, relativeEndPosition, angle, quadrantNE_SW);
                    break;
                case RESISTOR:
                    component = new Resistor(relativeStartPosition, relativeEndPosition, angle, quadrantNE_SW, 10);
                    break;
                default:
                    return;
            }

            LineCoordinate[] drawing = component.drawComponent();
            for (int x = 0; x < drawing.length; x++) {
                circuitGraphics.strokeLine(drawing[x].startX, drawing[x].startY, drawing[x].endX, drawing[x].endY);
            }

            relativeStartPosition.applyRelativePosition();
            relativeEndPosition.applyRelativePosition();
            component.adjustPosition(relativeStartPosition, relativeEndPosition);

            model.addCircuitComponent(component);
        }
    }

    LineCoordinate[] drawComponentEnds(double angle) {
        LineCoordinate lowerEnd = new LineCoordinate();
        LineCoordinate higherEnd = new LineCoordinate();

        double middleX = Math.abs(clickX + componentEndX) / 2;
        double middleY = Math.abs(clickY + componentEndY) / 2;
;
        double componentRadius = dotSpacing * 2;

        if (clickX == componentEndX) {
            lowerEnd.startX = lowerEnd.endX = higherEnd.startX = higherEnd.endX = clickX;
            lowerEnd.endY = middleY - componentRadius;
            higherEnd.startY = middleY + componentRadius;
            if (clickY < componentEndY) {
                lowerEnd.startY = clickY;
                higherEnd.endY = componentEndY;
            } else {
                lowerEnd.startY = componentEndY;
                higherEnd.endY = clickY;
            }
        } else if (clickY == componentEndY) { // Straight horizontal line
            lowerEnd.startY = lowerEnd.endY = higherEnd.startY = higherEnd.endY = clickY;
            lowerEnd.endX = middleX - componentRadius;
            higherEnd.startX = middleX + componentRadius;
            if (clickX < componentEndX) {
                lowerEnd.startX = clickX;
                higherEnd.endX = componentEndX;
            } else {
                lowerEnd.startX = componentEndX;
                higherEnd.endX = clickX;
            }
        } else if ((clickX < componentEndX) && (clickY < componentEndY)) {
            lowerEnd.startX = clickX;
            lowerEnd.startY = clickY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY - (componentRadius * Math.sin(angle));

            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY + (componentRadius * Math.sin(angle));
            higherEnd.endX = componentEndX;
            higherEnd.endY = componentEndY;
        } else if ((componentEndX < clickX) && (componentEndY < clickY)) {
            lowerEnd.startX = componentEndX;
            lowerEnd.startY = componentEndY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY - (componentRadius * Math.sin(angle));

            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY + (componentRadius * Math.sin(angle));
            higherEnd.endX = clickX;
            higherEnd.endY = clickY;
        } else if ((clickX < componentEndX) && (componentEndY < clickY)) {
            lowerEnd.startX = clickX;
            lowerEnd.startY = clickY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY + (componentRadius * Math.sin(angle));

            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY - (componentRadius * Math.sin(angle));
            higherEnd.endX = componentEndX;
            higherEnd.endY = componentEndY;
        } else if ((componentEndX < clickX) && (clickY < componentEndY)) {
            lowerEnd.startX = componentEndX;
            lowerEnd.startY = componentEndY;
            lowerEnd.endX = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.endY = middleY + (componentRadius * Math.sin(angle));

            higherEnd.startX = middleX + (componentRadius * Math.cos(angle));
            higherEnd.startY = middleY - (componentRadius * Math.sin(angle));
            higherEnd.endX = clickX;
            higherEnd.endY = clickY;
        }

        return (new LineCoordinate[] { lowerEnd, higherEnd });
    }

    Boolean withinBounds(double x, double y) {
        Bounds canvasBounds = circuit.getBoundsInLocal();

        if (x > canvasBounds.getMaxX() || x < canvasBounds.getMinX() || y > canvasBounds.getMaxY()
                || y < canvasBounds.getMinY()) {
            return false;
        }

        return true;
    }

    public void resizeCircuit() {
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        circuit.setHeight(height * 0.95);
        circuit.setWidth(width * 0.85);
        overlayCircuit.setHeight(height * 0.95);
        overlayCircuit.setWidth(width * 0.85);
    }

    public void zoom() {
        clearCircuit();
        shiftBackground();
        model.drawFromFile();
    }

    void clearOverlay() {
        overlayCircuitGraphics.clearRect(0, 0, overlayCircuit.getWidth(), overlayCircuit.getHeight());
    }

    public void clearCircuit() {
        circuitGraphics.clearRect(0, 0, circuit.getWidth(), circuit.getHeight());
        circuitBackgroundGraphics.clearRect(0, 0, circuitBackground.getWidth(), circuitBackground.getHeight());
    }

    public static double getScale() {
        return scale;
    }

    public void setScale(double multiplier) {
        scale *= multiplier;
    }

    public Canvas getCircuit() {
        return circuit;
    }

    public static double getXShift() {
        return xShift;
    }

    public static double getYShift() {
        return yShift;
    }

    public void resetXShift(){
        xShift = 0;
    }

    public void resetYShift(){
        yShift = 0;
    }

    public static double getDotSpacing(){
        return dotSpacing;
    }
}