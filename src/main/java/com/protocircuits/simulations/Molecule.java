package com.protocircuits.simulations;

import java.awt.Point;

abstract public class Molecule extends EnvironmentEntity {

    private EnvironmentEntity.EntityState _state = EnvironmentEntity.EntityState.OUTSIDE;

    public Molecule(Point center, Size size) {
        super(center, size);
    }

    public EnvironmentEntity.EntityState getState() {
        return _state;
    }

    public void setState(EnvironmentEntity.EntityState s) {
        _state = s;
    }

    void moveInside(EnvironmentEntity entity) {
        setState(EnvironmentEntity.EntityState.INSIDE);
        Environment.removeEntity(this.position); // remove its old position from the environment
        Point center = entity.position.getCenter();;
        if (entity instanceof LivingCell)
            center = entity.position.getAreaPoints()[entity.size.x+1]; // top left corner of living cells
        this.position = new Position(center, this.size);
        Environment.insertEntity(this); // commit change to environment
    }

    void moveOutside(Point p) {
        setState(EnvironmentEntity.EntityState.BEING_RELEASED);
        Environment.removeEntity(this.position); // remove its old position from the environment, if any
        this.position = new Position(p, this.size);
        Environment.insertEntity(this); // commit change to environment
    }

    void perceiveBodies() {
        // check for Protocells and Living cells in the nearest neighborhood
        for (Point p: getNeighborhoodPoints()) {
            EnvironmentEntity entity = Environment.getEntity(p);
            if (entity instanceof Protocell) {
                Protocell protocell = (Protocell)entity;
                if (protocell.input(this))
                    break; // if successful, then end
                // otherwise, try other neighbors
            }
            if (entity instanceof LivingCell) {
                LivingCell cell = (LivingCell)entity;
                if (cell.input(this))
                    break; // if successful, then end
                // otherwise, try other neighbors
                break;
            }
        }
    }
}
