package com.circuit_solver.calebbap;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class View extends Application {
    private Canvas circuit;
    private GraphicsContext circuitGraphics;
    private static CircuitControl circuitControl;

    private Canvas overlayCircuit;
    private GraphicsContext overlayCircuitGraphics;

    private Canvas circuitBackground;
    private GraphicsContext circuitBackgroundGraphics;

    static GridPane root;
    private ScrollPane circuitScrollPane;
    private Pane circuitPane;
    VBox noCircuit;

    public enum Tool {
        WIRE, RESISTOR, MOVE
    }

    private static Tool currentTool = Tool.WIRE;

    private Model model;

    private static Stage stage; 

    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Circuit Solver");
        /*final EventHandler<MouseEvent> testPress = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println(event.getSource());
            }
        };

        primaryStage.addEventHandler(MouseEvent.MOUSE_PRESSED, testPress);*/

        root = new GridPane();
        root.setId("root");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/primary.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        frameOne();
    }

    public void frameOne() {
        double frameHeight = root.getHeight();
        double frameWidth = root.getWidth();

        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(4);
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

        final Menu menuItem = new Menu("File");

        MenuItem menuFileNew = new MenuItem("New Circuit");
        menuFileNew.setOnAction(event -> {
            newCircuit();
        });

        MenuItem menuFileOpen = new MenuItem("Open Circuit");
        menuFileOpen.setOnAction(event -> {
            openCircuit();
        });

        menuItem.getItems().addAll(menuFileNew, menuFileOpen);
        menuBar.getMenus().add(menuItem);

        GridPane tools = new GridPane();
        tools.setId("toolsGrid");

        Button wireButton = new Button("Wire");
        wireButton.setId("wire");
        tools.add(wireButton, 0, 0);
        wireButton.setOnAction(a -> {
            getStage().getScene().setCursor(Cursor.CROSSHAIR);
            currentTool = Tool.WIRE;
        });

        Button zoomIn = new Button("Zoom In");
        tools.add(zoomIn, 0, 1);
        zoomIn.setOnAction(a -> {
            if( (circuitControl.getScale() < 5) && (root.getChildren().contains(circuit)) ){
                circuitControl.setScale(1.2);
                circuitControl.zoom();
            }
        });

        Button zoomOut = new Button("Zoom Out");
        tools.add(zoomOut, 1, 1);
        zoomOut.setOnAction(a -> {
            if( (circuitControl.getScale() > 0.4) && (root.getChildren().contains(circuit)) ){
                circuitControl.setScale(0.8);
                circuitControl.zoom();
            }
        });

        Button move = new Button("Move");
        tools.add(move, 0, 2);
        move.setOnAction(a -> {
            getStage().getScene().setCursor(Cursor.MOVE);
            currentTool = Tool.MOVE;
        });

        circuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        circuitGraphics = circuit.getGraphicsContext2D();

        circuitBackground = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        circuitBackgroundGraphics = circuit.getGraphicsContext2D();

        overlayCircuit = new Canvas(frameWidth * 0.85, frameHeight * 0.95);
        overlayCircuitGraphics = overlayCircuit.getGraphicsContext2D();

        circuitPane.getChildren().addAll(overlayCircuit, circuit);
        circuitScrollPane.setContent(circuitPane);

        model = new Model();

        Hyperlink newLink = new Hyperlink("new");
        newLink.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                newCircuit();
            }

        });

        Hyperlink openLink = new Hyperlink("open");
        openLink.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                openCircuit();
            }

        });

        noCircuit = new VBox();
        noCircuit.setAlignment(Pos.CENTER);
        TextFlow noCircuitText = new TextFlow(new Text("Create a"), newLink, new Text("circuit or"), openLink,
                new Text("an existing circuit to continue."));
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

        ChangeListener<Number> windowResizeListener = (observable, oldValue, newValue) -> 
            windowResized();
        root.widthProperty().addListener(windowResizeListener);
        root.heightProperty().addListener(windowResizeListener);
    }

    void windowResized() {
        if (root.getChildren().contains(circuit)){
            circuitControl.resizeCircuit();
            circuitControl.clearCircuit();
            circuitControl.drawCircuitBackground();
            model.drawFromFile();
        }
    }

    void initCircuit() {
        Boolean firstCircuit = !(root.getChildren().contains(circuit));

        if (firstCircuit) {
            root.getChildren().remove(noCircuit);
            GridPane.setRowIndex(overlayCircuit, 1);
            GridPane.setColumnIndex(overlayCircuit, 1);
            GridPane.setRowIndex(circuitBackground, 1);
            GridPane.setColumnIndex(circuitBackground, 1);
            GridPane.setRowIndex(circuit, 1);
            GridPane.setColumnIndex(circuit, 1);
            root.getChildren().addAll(overlayCircuit, circuitBackground, circuit);

            circuitControl = new CircuitControl(circuit, circuitGraphics, overlayCircuit, overlayCircuitGraphics,
                circuitBackground, circuitBackgroundGraphics, model);
            circuitControl.drawCircuitBackground();
            circuitControl.mouseControl();
        } else {
            circuitGraphics.clearRect(0, 0, circuit.getWidth(), circuit.getHeight());
            circuitControl.drawCircuitBackground();
        }
    }

    void newCircuit() {
        if (model.init(true)) {
            initCircuit();
        }
    }

    void openCircuit() {
        if (model.init(false)) {
            initCircuit();
            model.drawFromFile();
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static GridPane getRoot() {
        return root;
    }

    public static CircuitControl getCircuitControl() {
        return circuitControl;
    }

    public static Tool getTool() {
        return currentTool;
    }

    public void stop(){
        model.closeFile();
    }

    public static void main(String[] args){
        launch(args);
    }
}