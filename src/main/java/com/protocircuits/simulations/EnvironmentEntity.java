package com.protocircuits.simulations;

import com.protocircuits.views.running.RunningView;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract public class EnvironmentEntity {

    private final static Logger logger = Logger.getLogger(RunningView.class.getName());

    protected Position position;
    protected Size size;

    enum EntityState { INSIDE, BEING_RELEASED, OUTSIDE } // to keep track if some entities are inside others

    public EnvironmentEntity(Point center, Size size) {
        this.position = new Position(center, size);
        this.size = size;
    }

    public EnvironmentEntity(Point center, Size size, boolean roundedShape) {
        if (roundedShape)
            this.position = new Position(center, size, true);
        else
            this.position = new Position(center, size);

        this.size = size;
    }

    public abstract void agentDecision();


    public void move(int dx, int dy) {
        Environment.removeEntity(this.position); // remove its old position from the environment

        this.position.translate(dx, dy); // try translation

        boolean _clearToMOVE = true;

        for (Point p: position.getAreaPoints()) {
            if (Environment.getBlock(p) != null) { // check boundaries
                if (Environment.getEntity(p) != null) { // someone is already here
                    _clearToMOVE = false;
                    break;
                }
            }
            else { // crossed boundary
                _clearToMOVE = false;
                break;
            }
        }
        if (!_clearToMOVE)
            this.position.translate(-dx, -dy); // undo translation

        // commit final result to environment (changed or not)
        Environment.insertEntity(this);
    }

    protected void moveRandomly() {
        int r = Environment.random.nextInt(4);
        switch (r) {
            case 0: move(-1, 0); break;
            case 1: move(+1, 0); break;
            case 2: move(0, +1); break;
            case 3: move(0, -1); break;
            default: move(+1, -1); break;
        }
    }


    // Auxiliary methods

    Point[] getNeighborhoodPoints() {
        Point[] neighborhoodPositions = new Point[2*this.size.x + 2*this.size.y + 4];

        int counter = 0;

        // top row && bottom row
        for(int i = 0; i <= this.size.x; ++i) {
            neighborhoodPositions[counter++] = new Point(this.position.getX() - (int)(this.size.x / 2) - 1 + i,
                    this.position.getY() - (int)(this.size.y / 2) - 1);

            neighborhoodPositions[counter++] = new Point(this.position.getX() + (int)(this.size.x / 2) + 1 - i,
                    this.position.getY() + (int)(this.size.y / 2) + 1);
        }

        // right column && left column
        for(int i = 0; i <= this.size.y; ++i) {
            neighborhoodPositions[counter++] = new Point(this.position.getX() + (int)(this.size.x / 2) + 1,
                    this.position.getY() - (int)(this.size.y / 2) - 1 + i);

            neighborhoodPositions[counter++] = new Point(this.position.getX() - (int)(this.size.x / 2) - 1,
                    this.position.getY() + (int)(this.size.y / 2) + 1 - i);
        }

        return neighborhoodPositions;
    }

}
