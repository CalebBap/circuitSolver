package com.circuit_solver.calebbap;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Model{
    private File file;
    private PrintWriter writer;
    private Scanner reader;

    private int additionNum;

    Model(){
        additionNum = 0;
    }

    Boolean init(){
        // Is this needed cause writing to file will probably create it
            // Maybe define placement and name of file here
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Circuit File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Circuit", "*.crc"));
        file = fileChooser.showSaveDialog(View.getStage());

        if(file == null){
            return false; 
        }

        try{
            writer = new PrintWriter(file);
        }catch(FileNotFoundException e){
            return false;
        }

        try{
            reader = new Scanner(file);
        }catch(FileNotFoundException e){
            return false;
        }
        
        return true;
    }

    void write(double startX, double startY, double endX, double endY){
        writer.println("#" + Integer.toString(additionNum) + " " + Double.toString(startX) + " " + Double.toString(startY) + " " + 
            Double.toString(endX) + " " +  Double.toString(endY));
        writer.flush();
        additionNum++;
    }

    void read(){
        double[] values = {0, 0, 0, 0};
        int lineNum;

        int index = 0;

        while(reader.hasNext()){
            String read = reader.next();
            if(read.charAt(0) == '#'){
                System.out.println("Line number = " + read.substring(1));
                try{
                    lineNum = Integer.parseInt(read.substring(1));
                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
            }else{
                values[index] = Double.parseDouble(read);
            }
            
            if(index == 3){
                index = 0;
            }else{
                index++;
            }
        }
        
        double startX = values[0];
        double startY = values[1];
        double endX = values[2];
        double endY = values[3];

        System.out.println("startX = " + Double.toString(startX));
        System.out.println("startY = " + Double.toString(startY));
        System.out.println("endX = " + Double.toString(endX));
        System.out.println("endY = " + Double.toString(endY));


        //REMOVE TEST CALL FROM CLOSE() in View.java
    }

    void draw(){

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