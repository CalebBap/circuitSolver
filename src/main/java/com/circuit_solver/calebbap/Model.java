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
    private PrintWriter writer;
    private Scanner reader;

    Boolean init(Boolean newCircuit){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Circuit File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Circuit", "*.crc"));
        
        if(newCircuit){
            file = fileChooser.showSaveDialog(View.getStage());
        }else{
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

        try{
            reader = new Scanner(file);
        }catch(FileNotFoundException e){
            return false;
        }
        
        return true;
    }

    void write(Coordinate coordinate){
        writer.println(Double.toString(coordinate.startX) + " " + Double.toString(coordinate.startY) + " " + 
            Double.toString(coordinate.endX) + " " +  Double.toString(coordinate.endY));
        writer.flush();
    }

    ArrayList<Coordinate> read(){
        double[] values = {0, 0, 0, 0};
        Coordinate coordinates;
        ArrayList<Coordinate> valueList = new ArrayList<Coordinate>();

        int index = 0;

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

    void draw(){
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
        writer.close();
    }
}