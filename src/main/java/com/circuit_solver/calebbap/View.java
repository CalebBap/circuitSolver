package com.circuit_solver.calebbap;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.*; 
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.scene.canvas.*;

import javafx.stage.Stage;


import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

import javafx.scene.control.MenuItem;


public class View extends Application{

    public void start(Stage primaryStage){
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Circuit Solver");
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/primary.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        frameOne(root);   
    } 

    public void frameOne(GridPane gridPane){
        double frameHeight = gridPane.getHeight();
        double frameWidth =  gridPane.getWidth();
        System.out.println(frameHeight);
        System.out.println(frameWidth);
        
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
        gridPane.setGridLinesVisible(true);

        
        MenuBar menuBar = new MenuBar();
        menuBar.setMinHeight(frameHeight * 0.05);
        menuBar.setMaxHeight(frameWidth * 0.05);

        GridPane.setRowIndex(menuBar, 0);
        GridPane.setColumnIndex(menuBar, 0);
        GridPane.setColumnSpan(menuBar, 2);

        final Menu menuItem = new Menu("File");
        
        MenuItem menuFileNew = new MenuItem("New Circuit");
        MenuItem menuFileOpen = new MenuItem("Open Circuit");
        menuItem.getItems().addAll(menuFileNew, menuFileOpen);

        menuBar.getMenus().add(menuItem);

        GridPane tools = new GridPane();
        GridPane.setRowIndex(tools, 1);
        GridPane.setColumnIndex(tools, 0);

        tools.setId("toolGrid");

        final Canvas circuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        GraphicsContext gc = circuit.getGraphicsContext2D();

        gc.setFill(Color.BLUE);

        double dotSpacing = frameWidth * 0.01;

        for(double x = dotSpacing; x <= ((frameWidth * 0.85) - dotSpacing); x+=dotSpacing){
            for(double y = dotSpacing; y <= ((frameHeight * 0.95) - dotSpacing); y+=dotSpacing){
                gc.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
            }
        }

        GridPane.setRowIndex(circuit, 1);
        GridPane.setColumnIndex(circuit, 1);

        
        circuit.setId("circuitCanvas");

        gridPane.getChildren().addAll(menuBar, tools, circuit);

        
        /*
        // An array list to add components to in order to later add them to GUI
        ArrayList<javafx.scene.control.Control> components = new ArrayList<javafx.scene.control.Control>();
        components.add(mainToolBar);
        addComponentsToGUI(gridPane, components);
        */
    }
    
    /*
    // Method to add components to GUI
    public void addComponentsToGUI(Pane pane, ArrayList<javafx.scene.control.Control> components){
        for(int i = 0; i < components.size(); i++){
            pane.getChildren().add(components.get(i));
        }
    }
    */

    public static void main(String[] args){
        launch(args);
    }
}
