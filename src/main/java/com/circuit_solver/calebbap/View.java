package com.circuit_solver.calebbap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import javafx.scene.control.Button;
import java.util.ArrayList;

public class View extends Application{

    public void start(Stage primaryStage){
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Circuit Solver");
        StackPane root = new StackPane();
        frameOne(root);   
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void frameOne(StackPane root){
        ArrayList<javafx.scene.control.Control> components = new ArrayList<javafx.scene.control.Control>();
        
        Button btn = new Button();
        btn.setText("Test");
        components.add(btn);
        
        addComponents(root, components);
    }

    // Method to add components to frame
    public void addComponents(StackPane root, ArrayList<javafx.scene.control.Control> components){
        for(int i = 0; i < components.size(); i++){
            root.getChildren().add(components.get(i));
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
