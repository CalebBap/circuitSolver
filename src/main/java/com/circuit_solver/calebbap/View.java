package com.circuit_solver.calebbap;

import javafx.application.Application;

import javafx.stage.Stage;

import javafx.scene.control.MenuItem;

import javafx.event.*;
import javafx.geometry.Bounds;

import javafx.scene.Scene;
import javafx.scene.control.*; 
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.scene.canvas.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.input.*;


public class View extends Application{

    Canvas circuit;
    GraphicsContext circuitCanvasGraphics;
    
    int[] numDots = {0, 0};

    double dotSpacing = 0;
    double dotXPosition = 0;
    double dotYPosition = 0;

    double clickX;
    double clickY;
    double currentX;
    double currentY;
    
    boolean userDrawing = false;

    public void start(Stage primaryStage){
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Circuit Solver");
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/primary.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        frameOne(root);   
        
        
        root.setStyle("-fx-background-color: white;");
    } 

    public void frameOne(GridPane gridPane){
        double frameHeight = gridPane.getHeight();
        double frameWidth =  gridPane.getWidth();
        
        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(5);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(95);
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(15);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(85);

        gridPane.getRowConstraints().add(row0);
        gridPane.getColumnConstraints().add(col0);
        gridPane.getRowConstraints().add(row1);
        gridPane.getColumnConstraints().add(col1);
        gridPane.setGridLinesVisible(true); // For debug
        
        
        MenuBar menuBar = new MenuBar();
        menuBar.setMinHeight(frameHeight * 0.05);
        menuBar.setMaxHeight(frameWidth * 0.05);

        final Menu menuItem = new Menu("File");
        
        MenuItem menuFileNew = new MenuItem("New Circuit");
        menuFileNew.setOnAction(event -> { newCircuit(frameWidth, frameHeight); });

        MenuItem menuFileOpen = new MenuItem("Open Circuit");
        menuFileOpen.setOnAction(event -> { openCircuit(); });

        menuItem.getItems().addAll(menuFileNew, menuFileOpen);
        menuBar.getMenus().add(menuItem);

        
        GridPane tools = new GridPane();

        
        circuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        circuit.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(dotSpacing != 0 && !userDrawing){
                    circuitHover(event.getX(), event.getY());
                }else if(userDrawing){
                    drawComponent(event.getX(), event.getY());
                }
            }
        });

        circuit.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clearHover();
            }
        });

        circuit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clickX = event.getX();
                clickY = event.getY();
                userDrawing = true;
            }
        });

        circuit.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                userDrawing = false;
            }
        });

        circuitCanvasGraphics = circuit.getGraphicsContext2D();
        circuitCanvasGraphics.setFill(Color.BLUE);


        GridPane.setRowIndex(menuBar, 0);
        GridPane.setColumnIndex(menuBar, 0);
        GridPane.setColumnSpan(menuBar, 2);
        GridPane.setRowIndex(tools, 1);
        GridPane.setColumnIndex(tools, 0);
        GridPane.setRowIndex(circuit, 1);
        GridPane.setColumnIndex(circuit, 1);
        gridPane.getChildren().addAll(menuBar, tools, circuit);
    }

    void drawComponent(double x, double y){
        if( ((x < clickX) || (x > clickX)) && ((y < clickY) || (y > clickY)) ){
            // Draw component on angle
        }else if( (x < clickX) || (x > clickX) ){
            
        }else{
            // Draw component on vertically
        }
        // COPY circuit hover code so that component drawing updates each time. When user releases mouse
        // simply do nothing as last component position drawn should be kept.
    }

    void circuitHover(double x, double y){
        clearHover();
        
        double[] relativePosition = relativeMousePosition(x, y);
        dotXPosition = relativePosition[0];
        dotYPosition = relativePosition[1];

        circuitCanvasGraphics.setFill(Color.TRANSPARENT);
        circuitCanvasGraphics.setStroke(Color.BLACK);
        circuitCanvasGraphics.setLineWidth(2);
        circuitCanvasGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
    }

    void clearHover(){
        if((dotXPosition + dotYPosition) != 0 ){
            circuitCanvasGraphics.setFill(Color.TRANSPARENT);
            circuitCanvasGraphics.setStroke(Color.WHITE);
            circuitCanvasGraphics.setLineWidth(3);
            circuitCanvasGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
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

    void drawCircuitBackground(double dotSpacing, double width, double height){
        int numXDots = 0;
        int numYDots = 0;

        for(double x = dotSpacing; x <= ((width * 0.85) - dotSpacing); x+=dotSpacing){
            for(double y = dotSpacing; y <= ((height * 0.95) - dotSpacing); y+=dotSpacing){
                circuitCanvasGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                numYDots++;
            }
            numXDots++;
        }

        numYDots /= numXDots;

        numDots[0] = numXDots;
        numDots[1] = numYDots;
    }

    void newCircuit(double frameWidth, double frameHeight){
        dotSpacing = frameWidth * 0.01;
        drawCircuitBackground(dotSpacing, frameWidth, frameHeight);
        circuitCanvasGraphics.save();
    }

    void openCircuit(){
        
    }

    public static void main(String[] args){
        launch(args);
    }
}