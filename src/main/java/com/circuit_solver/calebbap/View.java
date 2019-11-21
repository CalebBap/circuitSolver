package com.circuit_solver.calebbap;

import javafx.application.Application;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.control.Hyperlink;

import javafx.stage.Stage;

public class View extends Application{

    private Canvas circuit;
    private GraphicsContext circuitGraphics;
    CircuitControl circuitControl;
    
    private Canvas overlayCircuit;
    private GraphicsContext overlayCircuitGraphics;
    CircuitControl overlayCircuitControl;

    public void start(Stage primaryStage){
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Circuit Solver");
        
        GridPane root = new GridPane();
        root.setId("root");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/primary.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();

        frameOne(root);   
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
        Button wireButton = new Button("Wire");
        tools.add(wireButton, 0, 0);

        circuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        circuitGraphics = circuit.getGraphicsContext2D();

        overlayCircuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        overlayCircuitGraphics = overlayCircuit.getGraphicsContext2D();
        
        circuitControl = new CircuitControl(circuit, circuitGraphics, overlayCircuit, overlayCircuitGraphics, frameWidth);

        Hyperlink newLink = new Hyperlink("new");
        newLink.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                newCircuit(frameWidth, frameHeight);
            }
            
        });

        Hyperlink openLink = new Hyperlink("open");
        openLink.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                openCircuit();
            }
            
        });

        TextFlow flow = new TextFlow(
            new Text("Create a"), newLink, new Text("circuit or"), openLink, new Text("an existing circuit to continue.")
        );
        flow.setId("noCircuitText");

        GridPane.setRowIndex(menuBar, 0);
        GridPane.setColumnIndex(menuBar, 0);
        GridPane.setColumnSpan(menuBar, 2);
        GridPane.setRowIndex(tools, 1);
        GridPane.setColumnIndex(tools, 0);
        /*GridPane.setRowIndex(overlayCircuit, 1);
        GridPane.setColumnIndex(overlayCircuit, 1);
        GridPane.setRowIndex(circuit, 1);
        GridPane.setColumnIndex(circuit, 1);*/
        GridPane.setRowIndex(flow, 1);
        GridPane.setColumnIndex(flow, 1);
        //gridPane.getChildren().addAll(menuBar, tools, overlayCircuit, circuit);   
        gridPane.getChildren().addAll(menuBar, tools, flow);   
    }

    void newCircuit(double frameWidth, double frameHeight){
        GridPane.setRowIndex(overlayCircuit, 1);
        GridPane.setColumnIndex(overlayCircuit, 1);
        GridPane.setRowIndex(circuit, 1);
        GridPane.setColumnIndex(circuit, 1);
        circuitControl.drawCircuitBackground(frameWidth, frameHeight);
        circuitControl.mouseControl();
    }

    void openCircuit(){
        
    }

    public static void main(String[] args){
        launch(args);
    }
}