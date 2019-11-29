package com.circuit_solver.calebbap;

import javafx.application.Application;

import javafx.geometry.Pos;

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
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Hyperlink;

import javafx.stage.Stage;

public class View extends Application{

    private Canvas circuit;
    private GraphicsContext circuitGraphics;
    CircuitControl circuitControl;
    
    private Canvas overlayCircuit;
    private GraphicsContext overlayCircuitGraphics;
    CircuitControl overlayCircuitControl;

    GridPane root;
    VBox noCircuit;

    private Model model;

    private static Stage stage;

    public void start(Stage primaryStage){
        stage = primaryStage;
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Circuit Solver");
        
        root = new GridPane();
        root.setId("root");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/primary.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();

        frameOne();   
    } 

    public void frameOne(){
        double frameHeight = root.getHeight();
        double frameWidth =  root.getWidth();
        
        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(5);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(95);
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(15);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(85);
        
        root.getRowConstraints().add(row0);
        root.getColumnConstraints().add(col0);
        root.getRowConstraints().add(row1);
        root.getColumnConstraints().add(col1);
        
        
        MenuBar menuBar = new MenuBar();
        //menuBar.setMinHeight(frameHeight * 0.05);
        //menuBar.setMaxHeight(frameWidth * 0.05);

        final Menu menuItem = new Menu("File");
        
        MenuItem menuFileNew = new MenuItem("New Circuit");
        menuFileNew.setOnAction(event -> { newCircuit(frameWidth, frameHeight); });

        MenuItem menuFileOpen = new MenuItem("Open Circuit");
        menuFileOpen.setOnAction(event -> { openCircuit(); });

        menuItem.getItems().addAll(menuFileNew, menuFileOpen);
        menuBar.getMenus().add(menuItem);
        
        GridPane tools = new GridPane();
        Button wireButton = new Button("Wire");
        wireButton.setId("wire");
        tools.add(wireButton, 0, 0);

        circuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        circuitGraphics = circuit.getGraphicsContext2D();

        overlayCircuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        overlayCircuitGraphics = overlayCircuit.getGraphicsContext2D();
        
        /*circuitControl = new CircuitControl(circuit, circuitGraphics, overlayCircuit, overlayCircuitGraphics, 
            frameWidth);*/

        model = new Model();

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

        noCircuit = new VBox();
        noCircuit.setAlignment(Pos.CENTER);
        TextFlow noCircuitText = new TextFlow(
            new Text("Create a"), newLink, new Text("circuit or"), openLink, 
            new Text("an existing circuit to continue.")
        );
        noCircuitText.setId("noCircuitText");
        noCircuitText.setTextAlignment(TextAlignment.CENTER);
        noCircuit.getChildren().add(noCircuitText);

        GridPane.setRowIndex(menuBar, 0);
        GridPane.setColumnIndex(menuBar, 0);
        GridPane.setColumnSpan(menuBar, 2);
        GridPane.setRowIndex(tools, 1);
        GridPane.setColumnIndex(tools, 0);
        GridPane.setRowIndex(noCircuit, 1);
        GridPane.setColumnIndex(noCircuit, 1);  
        root.getChildren().addAll(menuBar, tools, noCircuit);   
    }

    void newCircuit(double frameWidth, double frameHeight){
        if(model.init()){
            root.getChildren().remove(noCircuit);
            GridPane.setRowIndex(overlayCircuit, 1);
            GridPane.setColumnIndex(overlayCircuit, 1);
            GridPane.setRowIndex(circuit, 1);
            GridPane.setColumnIndex(circuit, 1);
            root.getChildren().addAll(overlayCircuit, circuit);
            circuitControl = new CircuitControl(circuit, circuitGraphics, overlayCircuit, overlayCircuitGraphics, 
                frameWidth, model);
            circuitControl.drawCircuitBackground(frameWidth, frameHeight);
            circuitControl.mouseControl();
        }
    }

    void openCircuit(){
        
    }

    public static Stage getStage(){
        return stage;
    }

    public void stop(){

        model.read(); // TEMP FOR TESTING

        model.closeFile();
    }

    public static void main(String[] args){
        launch(args);
    }
}