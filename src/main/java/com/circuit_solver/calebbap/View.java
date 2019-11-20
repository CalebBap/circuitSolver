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

import com.circuit_solver.calebbap.Circuit;

public class View extends Application{

    Canvas circuit;
    GraphicsContext circuitGraphics;
    Canvas tempCircuit;
    GraphicsContext tempCircuitGraphics;
    
    int[] numDots = {0, 0};

    double dotSpacing = 0;
    double dotXPosition = 0;
    double dotYPosition = 0;

    double clickX;
    double clickY;
    
    double tempCompX;
    double tempCompY;
    
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
                }
            }
        });

        circuit.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                userDrawing = false;
                clearHover();
            }
        });

        circuit.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                userDrawing = true;
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
                userDrawing = false;
            }
        });

        circuitGraphics = circuit.getGraphicsContext2D();
        circuitGraphics.setFill(Color.BLUE);

        
        tempCircuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        tempCircuitGraphics = tempCircuit.getGraphicsContext2D();

        GridPane.setRowIndex(menuBar, 0);
        GridPane.setColumnIndex(menuBar, 0);
        GridPane.setColumnSpan(menuBar, 2);
        GridPane.setRowIndex(tools, 1);
        GridPane.setColumnIndex(tools, 0);
        GridPane.setRowIndex(tempCircuit, 1);
        GridPane.setColumnIndex(tempCircuit, 1);
        GridPane.setRowIndex(circuit, 1);
        GridPane.setColumnIndex(circuit, 1);
        gridPane.getChildren().addAll(menuBar, tools, tempCircuit, circuit);
    }

    public static void main(String[] args){
        launch(args);
    }
}