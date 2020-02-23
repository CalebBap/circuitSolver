package com.circuit_solver.calebbap;

import java.io.File;
import java.util.ArrayList;

import com.circuit_solver.calebbap.View.Tool;
import com.circuit_solver.calebbap.components.Resistor;
import com.circuit_solver.calebbap.components.Wire;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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

    private Point dotPosition = new Point();

    private Point click = new Point();

    private Point componentEnd = new Point();

    private Point orginalMouse = new Point();
    private static Point shift = new Point();

    private LineCoordinate backgroundShift = new LineCoordinate(new Point(), new Point());

    private static double scale = 1;
    private static double dotSpacing;

    File file = new File(getClass().getClassLoader().getResource("icons/deleteCursor.png").getFile());
    private Image deleteCursor = new Image(file.toURI().toString());

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
                if ( (View.getRoot().getChildren().contains(circuit)) && (View.getTool() != View.Tool.MOVE) )
                    circuitHover(new Point(event.getX(), event.getY()));
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
                    case MOVE:
                        View.getStage().getScene().setCursor(Cursor.MOVE);
                        break;
                    default:
                        View.getStage().getScene().setCursor(Cursor.NONE);
                        break;
                }
            }
        };

        final EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (withinBounds(new Point(event.getX(), event.getY()))) {
                    if (View.getTool() != View.Tool.MOVE && View.getTool() != View.Tool.DELETE) {
                        double[] relativePosition = relativePosition(new Point(event.getX(), event.getY()));
                        click.setXY(relativePosition[0], relativePosition[1]);
                    }
                    orginalMouse.setXY(event.getX(), event.getY());
                }
            }
        };

        final EventHandler<MouseEvent> dragged = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(View.getTool() == View.Tool.DELETE){
                    circuitHover(new Point(event.getX(), event.getY()));
                }else if (withinBounds(new Point(event.getX(), event.getY())) && (View.getTool() != View.Tool.MOVE) ){
                        clearOverlay();
                        circuitHover(new Point(event.getX(), event.getY()));
                        double[] relativePosition = relativePosition(new Point(event.getX(), event.getY()));
                        componentEnd.setXY(relativePosition[0], relativePosition[1]);
                        drawComponent(overlayCircuitGraphics, null);
                }
            }
        };

        final EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if ( (View.getTool() != View.Tool.MOVE) && (View.getTool() != View.Tool.DELETE) ) {
                    clearOverlay();
                    drawComponent(circuitGraphics, null);
                }
            }
        };

        final EventHandler<MouseEvent> dragToMove = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (View.getTool() == View.Tool.MOVE) {
                    if (event.getX() > orginalMouse.x) {
                        shift.x -= (Math.abs(orginalMouse.x - event.getX())) * 0.4;
                    } else {
                        shift.x += (Math.abs(orginalMouse.x - event.getX())) * 0.4;
                    }
                    if (event.getY() > orginalMouse.y) {
                        shift.y -= (Math.abs(orginalMouse.y - event.getY())) * 0.4;
                    } else {
                        shift.y += (Math.abs(orginalMouse.y - event.getY())) * 0.4;
                    }

                    orginalMouse.x = event.getX();
                    orginalMouse.y = event.getY();

                    clearCircuit();
                    shiftBackground();
                    model.drawFromFile();
                }
            }
        };

        final EventHandler<MouseEvent> selectComponent = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(View.getTool() == View.Tool.DELETE){
                    Component component = selectComponent(new Point(event.getX(), event.getY()));
                    System.out.println(component);
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
        circuit.addEventHandler(MouseEvent.MOUSE_CLICKED, selectComponent);
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
        Point start = new Point();
        Point end = new Point(width * 0.85, height * 0.95);

        dotSpacing = (width / height) * 8 * scale;
        Point clipped = new Point(Math.abs(shift.x) % dotSpacing, Math.abs(shift.y) % dotSpacing);

        circuitBackgroundGraphics.setFill(Color.BLUE);

        if (shift.x < 0) {
            start.x += clipped.x;
            end.x -= clipped.x;
        } else {
            start.x -= clipped.x;
            end.x += clipped.x;
        }

        if (shift.y < 0) {
            start.y += clipped.y;
            end.y -= clipped.y;
        } else {
            start.y -= clipped.y;
            end.y += clipped.y;
        }

        for (double x = start.x; x <= end.x; x += dotSpacing) {
            for (double y = start.y; y <= end.y; y += dotSpacing) {
                circuitBackgroundGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                if ((y >= 0) && (y <= (height * 0.95)))
                    numYDots++;
            }
            if ((x >= 0) && (x <= (width * 0.85)))
                numXDots++;
        }

        numYDots /= numXDots;
        numDots[0] = numXDots;
        numDots[1] = numYDots;

        backgroundShift = new LineCoordinate(new Point(dotSpacing - start.x, dotSpacing - start.y), 
                                             new Point((width * 0.85) - end.x, (height * 0.95) - end.y));
    }

    void circuitHover(Point point) {
        clearOverlay();

        if(View.getTool() != Tool.DELETE){
            double[] relativePosition = relativePosition(point);
            dotPosition.setXY(relativePosition[0], relativePosition[1]);
            overlayCircuitGraphics.setStroke(Color.BLACK);
            overlayCircuitGraphics.setLineWidth(2);
            overlayCircuitGraphics.strokeArc(dotPosition.x - 3, dotPosition.y - 3, 8, 8, 0, 360, ArcType.OPEN);
        }else{
            overlayCircuitGraphics.drawImage(deleteCursor, point.x - 10, point.y - 10);
        }
    }

    public double[] relativePosition(Point point) {
        double width = View.getRoot().getWidth();
        double height = View.getRoot().getHeight();
        dotSpacing = (width / height) * 8 * scale;
        Bounds canvasBounds = circuitBackground.getBoundsInParent();

        double relativeXPostion = point.x / canvasBounds.getWidth();
        double relativeYPostion = point.y / canvasBounds.getHeight();

        int numDotX = (int) (relativeXPostion * numDots[0] + 0.5);
        int numDotY = (int) (relativeYPostion * numDots[1] + 0.5);
        
        relativeXPostion = (dotSpacing * numDotX) - backgroundShift.start.x;
        relativeYPostion = (dotSpacing * numDotY) - backgroundShift.start.y;

        double[] relativePosition = { relativeXPostion, relativeYPostion };
        return relativePosition;
    }

    void drawComponent(GraphicsContext graphicsContext, Component component){
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(4);

        double angle = Math.atan(Math.abs(click.y - componentEnd.y) / Math.abs(click.x - componentEnd.x));
        LineCoordinate[] endLineCoordinates = drawComponentEnds(angle);
        LineCoordinate lowerEnd = endLineCoordinates[0];
        LineCoordinate higherEnd = endLineCoordinates[1];
        double width = View.getRoot().getWidth() * scale;
        double height = View.getRoot().getHeight() * scale;

        graphicsContext.strokeLine(lowerEnd.start.x, lowerEnd.start.y, lowerEnd.end.x, lowerEnd.end.y);
        graphicsContext.strokeLine(higherEnd.start.x, higherEnd.start.y, higherEnd.end.x, higherEnd.end.y);

        LineCoordinate relativeStartPosition = new LineCoordinate(new Point(lowerEnd.start.x / width, lowerEnd.start.y / height),
                            new Point(lowerEnd.end.x / width, lowerEnd.end.y / height));

        LineCoordinate relativeEndPosition = new LineCoordinate(new Point(higherEnd.start.x / width, higherEnd.start.y / height),
                        new Point(higherEnd.end.x / width, higherEnd.end.y / height));
        
        boolean quadrantNE_SW = false;
        if( (lowerEnd.getStart().x < higherEnd.getEnd().x) && (higherEnd.getEnd().y < lowerEnd.getStart().y) || 
        lowerEnd.getStart().y == higherEnd.getEnd().y){
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
            graphicsContext.strokeLine(drawing[x].start.x, drawing[x].start.y, drawing[x].end.x, drawing[x].end.y);
        }

        Node[] nodes = {checkForNode(component, new double[]{lowerEnd.start.x / width, lowerEnd.start.y / height}), 
                        checkForNode(component, new double[]{higherEnd.end.x / width, higherEnd.end.y / height})};
        graphicsContext.setFill(Color.BLACK);
        for(Node node : nodes){
            if(node != null)
                graphicsContext.fillArc((node.getLocation()[0] * width) - (5 * scale), (node.getLocation()[1] * height) - (5 * scale), 10 * scale, 10 * scale, 0, 360, ArcType.ROUND);
        }

        if(graphicsContext == circuitGraphics){
            relativeStartPosition.applyRelativePosition();
            relativeEndPosition.applyRelativePosition();
            component.adjustPosition(relativeStartPosition, relativeEndPosition);
            for(Node node : nodes){
                if(node != null){
                    node.applyRelativePosition();
                    model.addCircuitNode(node);
                }
            }
            model.addCircuitComponent(component);
        }
    }

    void drawComponent(Component component) {
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(4);
        double width = View.getRoot().getWidth() * scale;
        double height = View.getRoot().getHeight() * scale;

        LineCoordinate[] drawing = component.drawComponent();
        for (int x = 0; x < drawing.length; x++) {
            drawing[x].start.x -= shift.x;
            drawing[x].start.y -= shift.y;
            drawing[x].end.x -= shift.x;
            drawing[x].end.y -= shift.y;
            circuitGraphics.strokeLine(drawing[x].start.x, drawing[x].start.y, drawing[x].end.x, drawing[x].end.y);
        }

        Point start = new Point((component.relativeStartPosition.start.x * width) - shift.x,
                                (component.relativeStartPosition.start.y * height) - shift.y);
        Point end = new Point((component.relativeEndPosition.end.x * width) - shift.x,
                                (component.relativeEndPosition.end.y * height) - shift.y);

        circuitGraphics.strokeLine(start.x, start.y, drawing[0].start.x, drawing[0].start.y);
        circuitGraphics.strokeLine(drawing[drawing.length - 1].end.x, drawing[drawing.length - 1].end.y, end.x, end.y);
    }

    void drawNode(Node node){
        double width = View.getRoot().getWidth() * scale;
        double height = View.getRoot().getHeight() * scale;
        circuitGraphics.setFill(Color.BLACK);
        Point point = new Point((node.getLocation()[0] * width) - shift.x, (node.getLocation()[1] * height) - shift.y);
        circuitGraphics.fillArc(point.x - (5 * scale), point.y - (5 * scale), 10 * scale, 10 * scale, 0, 360, ArcType.ROUND);
    }

    LineCoordinate[] drawComponentEnds(double angle) {
        LineCoordinate lowerEnd = new LineCoordinate();
        LineCoordinate higherEnd = new LineCoordinate();

        double middleX = Math.abs(click.x + componentEnd.x) / 2;
        double middleY = Math.abs(click.y + componentEnd.y) / 2;
;
        double componentRadius = dotSpacing * 2;

        if (click.x == componentEnd.x) {
            lowerEnd.start.x = lowerEnd.end.x = higherEnd.start.x = higherEnd.end.x = click.x;
            lowerEnd.end.y = middleY - componentRadius;
            higherEnd.start.y = middleY + componentRadius;
            if (click.y < componentEnd.y) {
                lowerEnd.start.y = click.y;
                higherEnd.end.y = componentEnd.y;
            } else {
                lowerEnd.start.y = componentEnd.y;
                higherEnd.end.y = click.y;
            }
        } else if (click.y == componentEnd.y) { // Straight horizontal line
            lowerEnd.start.y = lowerEnd.end.y = higherEnd.start.y = higherEnd.end.y = click.y;
            lowerEnd.end.x = middleX - componentRadius;
            higherEnd.start.x = middleX + componentRadius;
            if (click.x < componentEnd.x) {
                lowerEnd.start.x = click.x;
                higherEnd.end.x = componentEnd.x;
            } else {
                lowerEnd.start.x = componentEnd.x;
                higherEnd.end.x = click.x;
            }
        } else if ((click.x < componentEnd.x) && (click.y < componentEnd.y)) {
            lowerEnd.start.x = click.x;
            lowerEnd.start.y = click.y;
            lowerEnd.end.x = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.end.y = middleY - (componentRadius * Math.sin(angle));

            higherEnd.start.x = middleX + (componentRadius * Math.cos(angle));
            higherEnd.start.y = middleY + (componentRadius * Math.sin(angle));
            higherEnd.end.x = componentEnd.x;
            higherEnd.end.y = componentEnd.y;
        } else if ((componentEnd.x < click.x) && (componentEnd.y < click.y)) {
            lowerEnd.start.x = componentEnd.x;
            lowerEnd.start.y = componentEnd.y;
            lowerEnd.end.x = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.end.y = middleY - (componentRadius * Math.sin(angle));

            higherEnd.start.x = middleX + (componentRadius * Math.cos(angle));
            higherEnd.start.y = middleY + (componentRadius * Math.sin(angle));
            higherEnd.end.x = click.x;
            higherEnd.end.y = click.y;
        } else if ((click.x < componentEnd.x) && (componentEnd.y < click.y)) {
            lowerEnd.start.x = click.x;
            lowerEnd.start.y = click.y;
            lowerEnd.end.x = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.end.y = middleY + (componentRadius * Math.sin(angle));

            higherEnd.start.x = middleX + (componentRadius * Math.cos(angle));
            higherEnd.start.y = middleY - (componentRadius * Math.sin(angle));
            higherEnd.end.x = componentEnd.x;
            higherEnd.end.y = componentEnd.y;
        } else if ((componentEnd.x < click.x) && (click.y < componentEnd.y)) {
            lowerEnd.start.x = componentEnd.x;
            lowerEnd.start.y = componentEnd.y;
            lowerEnd.end.x = middleX - (componentRadius * Math.cos(angle));
            lowerEnd.end.y = middleY + (componentRadius * Math.sin(angle));

            higherEnd.start.x = middleX + (componentRadius * Math.cos(angle));
            higherEnd.start.y = middleY - (componentRadius * Math.sin(angle));
            higherEnd.end.x = click.x;
            higherEnd.end.y = click.y;
        }

        return (new LineCoordinate[] { lowerEnd, higherEnd });
    }

    Node checkForNode(Component checkComponent, double[] checkEnd){
        double width = View.getRoot().getWidth();
        double height = View.getRoot().getHeight();

        checkEnd[0] += getShift().x / width;
        checkEnd[1] += getShift().y / height;

        for(Component component : model.getComponents()){
            LineCoordinate ends = component.getRelativeEndPositions();
            double[] location = null;
            if( (checkEnd[0] == ends.start.x && checkEnd[1] == ends.start.y) || 
                (checkEnd[0] == ends.end.x &&  checkEnd[1] == ends.end.y) ){
                    location = new double[]{checkEnd[0], checkEnd[1]};
            }

            if(location != null){
                for(Node node : model.getNodes()){
                    double[] nodeLocation = node.getLocation();
                    if( location[0] == nodeLocation[0] && location[1] == nodeLocation[1]){
                        node.addComponent(component);
                        return null; // No need to draw Node again
                    }
                }
                ArrayList<Component> components = new ArrayList<>();
                components.add(checkComponent);
                components.add(component);
                return new Node(components, location); 
            }
        }

        return null;
    }

    Boolean withinBounds(Point point) {
        Bounds canvasBounds = circuit.getBoundsInLocal();

        if (point.x > canvasBounds.getMaxX() || point.x < canvasBounds.getMinX() || point.y > canvasBounds.getMaxY()
                || point.y < canvasBounds.getMinY()) {
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

    private Component selectComponent(Point point){
        double height = View.getRoot().getHeight();
        double width = View.getRoot().getWidth();
        dotSpacing = (width / height) * 8 * scale;

        point.x /= width;
        point.y /= height;

        for(Component component : model.getComponents()){
            if(component.angle == 0){   // Horizontal line
                if( Math.abs(point.y - component.relativeEndPosition.end.y) < dotSpacing && 
                    (Math.abs(point.x - component.relativeStartPosition.start.x) < dotSpacing ||
                    Math.abs(point.x - component.relativeEndPosition.end.x) > dotSpacing) )
                        return component;
            }else if(component.angle == Math.PI / 2){   // Vertical line
                if( Math.abs(point.x - component.relativeEndPosition.end.x) < dotSpacing && 
                    (Math.abs(point.y - component.relativeStartPosition.start.y ) < dotSpacing ||
                    Math.abs(point.y - component.relativeEndPosition.end.y) < dotSpacing) )
                        return component;
            }
        }

        return null;
    }

    public static double getScale() {
        return scale;
    }

    public void setScale(double multiplier) {
        scale *= multiplier;
    }

    public static Point getShift() {
        return shift;
    }

    public void resetShift(){
        shift.setX(0);
        shift.setY(0);
    }

    public static double getDotSpacing(){
        return dotSpacing;
    }
}