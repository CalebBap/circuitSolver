package com.circuit_solver.calebbap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Model{
    private File file;
    private PrintWriter writer = null;
    private Scanner reader;

    Boolean init(Boolean newCircuit){
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File(System.getProperty("user.home") + File.separator);
        
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Circuit", "*.crc"));
        fileChooser.setInitialDirectory(initialDirectory);
        
        if(newCircuit){
            fileChooser.setTitle("Save Circuit File");
            file = fileChooser.showSaveDialog(View.getStage());
        }else{
            fileChooser.setTitle("Open Circuit File");
            file = fileChooser.showOpenDialog(View.getStage());
        }

        if(file == null){
            return false; 
        }

        if(newCircuit){
            try{
                writer = new PrintWriter(file);
            }catch(FileNotFoundException e){
                return false;
            }
        }else{
            try{
                writer = new PrintWriter(new FileWriter(file, true));
            }catch(Exception e){
                return false;
            }
        }
        
        View.getStage().setTitle("Circuit Solver - " + file.getName());

        return true;
    }

    void write(Coordinate coordinate){
        writer.println(Double.toString(coordinate.startX) + " " + Double.toString(coordinate.startY) + " " + 
            Double.toString(coordinate.endX) + " " +  Double.toString(coordinate.endY));
        writer.flush();
    }

    ArrayList<Coordinate> read(){

        ArrayList<Coordinate> valueList = new ArrayList<Coordinate>();

        try{
            reader = new Scanner(file);
        }catch(FileNotFoundException e){
            return valueList;
        }

        double[] values = {0, 0, 0, 0};
        int index = 0;
        Coordinate coordinates;

        while(reader.hasNext()){
            values[index] = Double.parseDouble(reader.next());
            if(index == 3){
                index = 0;
                coordinates = new Coordinate(values[0], values[1], values[2], values[3]);
                valueList.add(coordinates);
            }else{
                index++;
            }
        }

        return valueList;
    }

    void drawFromFile(){
        ArrayList<Coordinate> coordinates = read();
        for(int x = 0; x < coordinates.size(); x++){
            Coordinate coordinate = coordinates.get(x);
            View.getCircuitControl().drawComponent(coordinate);
        }
    }

    void undo(){
        // Perhaps save these to a temp file for redo()?
    }

    void redo(){

    }

    void closeFile(){
        if(writer != null){
            writer.close();
        }
    }
}