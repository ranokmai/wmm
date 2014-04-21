package models;

import com.jjoe64.graphview.GraphViewDataInterface;

/**
 * Represents a point of data on the line graph
 * @author Galen
 *
 */
public class GraphViewData implements GraphViewDataInterface {
    private double x,y;

    public GraphViewData(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }
}