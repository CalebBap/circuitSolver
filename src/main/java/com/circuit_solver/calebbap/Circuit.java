package com.circuit_solver.calebbap;

public class Circuit{
    void drawComponent(double x, double y){
        double[] relativePosition = relativeMousePosition(x, y);
        x = relativePosition[0];
        y = relativePosition[1];
        tempCircuitGraphics.clearRect(0, 0, tempCircuit.getWidth(), tempCircuit.getHeight());
        tempCompX = x;
        tempCompY = y;
        tempCircuitGraphics.setStroke(Color.BLACK);
        tempCircuitGraphics.setLineWidth(4);
        tempCircuitGraphics.strokeLine(clickX, clickY, x, y);
    }

    void clearComponent(){

    }

    void circuitHover(double x, double y){
        clearHover();
        
        double[] relativePosition = relativeMousePosition(x, y);
        dotXPosition = relativePosition[0];
        dotYPosition = relativePosition[1];

        circuitGraphics.setFill(Color.TRANSPARENT);
        circuitGraphics.setStroke(Color.BLACK);
        circuitGraphics.setLineWidth(2);
        circuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
    }

    void clearHover(){
        if((dotXPosition + dotYPosition) != 0 ){
            circuitGraphics.setFill(Color.TRANSPARENT);
            circuitGraphics.setStroke(Color.WHITE);
            circuitGraphics.setLineWidth(3);
            circuitGraphics.strokeArc(dotXPosition - 3, dotYPosition - 3, 8, 8, 0, 360, ArcType.OPEN);
        }
    }

    double[] relativeMousePosition(double x, double y){
        Bounds canvasBounds = circuit.getBoundsInParent();
        double relativeXPostion = x / canvasBounds.getWidth();
        double relativeYPostion = y / canvasBounds.getHeight();
        
        int numDotX = (int) (relativeXPostion * numDots[0] + 1);
        int numDotY = (int) (relativeYPostion * numDots[1] + 1);

        double[] relativePosition = {(dotSpacing * numDotX), (dotSpacing * numDotY)};
        return relativePosition;
    }

    void drawCircuitBackground(double dotSpacing, double width, double height){
        int numXDots = 0;
        int numYDots = 0;

        for(double x = dotSpacing; x <= ((width * 0.85) - dotSpacing); x+=dotSpacing){
            for(double y = dotSpacing; y <= ((height * 0.95) - dotSpacing); y+=dotSpacing){
                circuitGraphics.fillArc(x, y, 2, 2, 0, 360, ArcType.ROUND);
                numYDots++;
            }
            numXDots++;
        }

        numYDots /= numXDots;

        numDots[0] = numXDots;
        numDots[1] = numYDots;
    }

    void newCircuit(double frameWidth, double frameHeight){
        dotSpacing = frameWidth * 0.01;
        drawCircuitBackground(dotSpacing, frameWidth, frameHeight);
        circuitGraphics.save();
    }

    void openCircuit(){
        
    }
}