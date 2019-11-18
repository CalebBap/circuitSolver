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
                if(dotSpacing != 0){
                    circuitHover(event.getX(), event.getY());
                }
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

    /*
        Use numXDots and numYDots as well as mouse location relative to Pane dimensions to figure
        out the x and y percentage of the mouse location relative to Pane closest to the dot placement relative
        to numXDots and numYDots.
    */

    void circuitHover(double x, double y){
        if((dotXPosition + dotYPosition) != 0 ){
            circuitCanvasGraphics.setFill(Color.TRANSPARENT);
            circuitCanvasGraphics.setStroke(Color.WHITE);
            circuitCanvasGraphics.setLineWidth(3);
            circuitCanvasGraphics.strokeArc(dotXPosition - 4, dotYPosition - 4, 8, 8, 0, 360, ArcType.OPEN);
        }

        Bounds canvasBounds = circuit.getBoundsInParent();
        double relativeXPostion = x / canvasBounds.getWidth();
        double relativeYPostion = y / canvasBounds.getHeight();
        
        int dotX = (int) (relativeXPostion * numDots[0]);
        int dotY = (int) (relativeYPostion * numDots[1]);

        dotXPosition = (dotSpacing * dotX) + 1;
        dotYPosition = (dotSpacing * dotY) + 1;

        circuitCanvasGraphics.setFill(Color.TRANSPARENT);
        circuitCanvasGraphics.setStroke(Color.BLACK);
        circuitCanvasGraphics.setLineWidth(2);
        circuitCanvasGraphics.strokeArc(dotXPosition - 4, dotYPosition - 4, 8, 8, 0, 360, ArcType.OPEN);
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

/*
        // An array list to add components to in order to later add them to GUI
        ArrayList<javafx.scene.control.Control> components = new ArrayList<javafx.scene.control.Control>();
        components.add(mainToolBar);
        addComponentsToGUI(gridPane, components);
        
    } (end of frame bracket)
    
    /*
    // Method to add components to GUI
    public void addComponentsToGUI(Pane pane, ArrayList<javafx.scene.control.Control> components){
        for(int i = 0; i < components.size(); i++){
            pane.getChildren().add(components.get(i));
        }
    }
    */