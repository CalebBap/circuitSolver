package com.circuit_solver.calebbap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Model{
    private static File file;

    private static FileOutputStream fileOut;
    private static ObjectOutputStream out;

    FileInputStream fileIn;
    ObjectInputStream in;

    ArrayList<Component> circuitComponents = new ArrayList<>();
    ArrayList<Node> circuitNodes = new ArrayList<>();

    Boolean init(Boolean newCircuit) {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File(System.getProperty("user.home") + File.separator);

        fileChooser.getExtensionFilters().add(new ExtensionFilter("Circuit", "*.crc"));
        fileChooser.setInitialDirectory(initialDirectory);

        if (newCircuit) {
            fileChooser.setTitle("Save Circuit File");
            file = fileChooser.showSaveDialog(View.getStage());
        } else {
            fileChooser.setTitle("Open Circuit File");
            file = fileChooser.showOpenDialog(View.getStage());
        }

        if (file == null) {
            return false;
        }
         
        if(circuitComponents.size() > 0)
            closeFile();

        if (!newCircuit)
            read();
            
        try {
            fileOut = new FileOutputStream(file);
            out = new ObjectOutputStream(fileOut);
        } catch (Exception e) {
            return false;
        }

        View.getStage().setTitle("Circuit Solver - " + file.getName());

        return true;
    }

    void write() {
        try{
            out.writeObject(circuitComponents);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void read(){
        try {
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);
            circuitComponents = (ArrayList<Component>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    void closeFile() {
        write();
        circuitComponents.clear();
        circuitNodes.clear();
        try{
            if(fileOut != null)
                fileOut.close();
                
            if(out != null)
                out.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void drawFromFile() {
        for(Component component : circuitComponents){
            View.getCircuitControl().drawComponent(component);
        }
        for(Node node : circuitNodes){
            View.getCircuitControl().drawNode(node);
        }
    }

    public ArrayList<Component> getComponents(){
        return circuitComponents;
    }

    public ArrayList<Node> getNodes(){
        return circuitNodes;
    }

    public void addCircuitComponent(Component newComponent){
        circuitComponents.add(newComponent);
    }

    public void addCircuitNode(Node newNode){
        circuitNodes.add(newNode);
    }

    void undo() {}

    void redo() {}
}