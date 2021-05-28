package com.protocircuits.simulations;

import java.awt.Point;
import java.util.ArrayList;

public class Position {
    private Point center; // center of object
    private Point[] areaPoints;


    protected Position(Point center, Size size) {
        this.center = center;

        // Compute area positions
        this.areaPoints = new Point[size.x*size.y]; // area of rectangles
        int counter = 0;
        for(int i = 0; i < size.x; ++i) {
            for(int j = 0; j < size.y; ++j) {
                areaPoints[counter++] = new Point(this.center.x - (size.x / 2) + i,
                        this.center.y - (size.y / 2) + j);
            }
        }
    }

    protected Position(Point center, Size size, boolean rounded) {
        this.center = center;

        if(rounded) {

            // Compute area positions as a circle
            ArrayList<Point> res = new ArrayList<Point>();
            double midPoint = (size.x-1)/2.0;
            for (int col = 0; col < size.y; col++)
            {
                double yy = col-midPoint;
                for (int x=0; x<size.x; x++)
                {
                    double xx = x-midPoint;
                    if (Math.sqrt(xx*xx+yy*yy) <= midPoint) { // area of a circle
                        res.add(new Point(x, col));
                    }
                }
            }

            this.areaPoints = res.toArray(new Point[0]);
        }

    }

    public Point getCenter() {
        return center;
    }

    public int getX() {
        return center.x;
    }

    public int getY() {
        return center.y;
    }

    public Point[] getAreaPoints() {
        return areaPoints;
    }

    protected void translate(int dx, int dy) {
        center.translate(dx, dy);
        for(Point p: areaPoints) {
            p.translate(dx, dy);
        }
    }


}
