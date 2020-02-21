package com.circuit_solver.calebbap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    Boolean init(final Boolean newCircuit) {
        final FileChooser fileChooser = new FileChooser();
        final File initialDirectory = new File(System.getProperty("user.home") + File.separator);

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
        } catch (final Exception e) {
            return false;
        }

        View.getStage().setTitle("Circuit Solver - " + file.getName());

        return true;
    }

    void write() {
        try{
            if(circuitComponents.size() > 0 || circuitNodes.size() > 0){
                final Map<String, ArrayList> circuitContents = new HashMap<String, ArrayList>();
                circuitContents.put("components", circuitComponents);
                circuitContents.put("nodes", circuitNodes);
                out.writeObject(circuitContents);
            }
        }catch(final Exception e){
            e.printStackTrace();
        }
    }

    void read(){
        try {
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);
            final Map<String, ArrayList> circuitContents = (Map<String, ArrayList>) in.readObject();
            circuitComponents = circuitContents.get("components");
            circuitNodes = circuitContents.get("nodes");
            in.close();
            fileIn.close();
        } catch (final Exception i) {
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

        }catch(final Exception e){
            e.printStackTrace();
        }
    }

    void drawFromFile() {
        for(final Component component : circuitComponents){
            View.getCircuitControl().drawComponent(component);
        }
        for(final Node node : circuitNodes){
            View.getCircuitControl().drawNode(node);
        }
    }

    public ArrayList<Component> getComponents(){
        return circuitComponents;
    }

    public ArrayList<Node> getNodes(){
        return circuitNodes;
    }

    public void addCircuitComponent(final Component newComponent){
        circuitComponents.add(newComponent);
    }

    public void addCircuitNode(final Node newNode){
        circuitNodes.add(newNode);
    }

    void undo() {}

    void redo() {}
}