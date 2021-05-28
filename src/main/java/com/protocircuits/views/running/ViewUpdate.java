package com.protocircuits.views.running;
import com.protocircuits.simulations.EnvironmentEntity;
import java.awt.Point;
import java.util.ArrayList;

public class ViewUpdate {
    private EnvironmentEntity entity;
    public enum TypeOfUpdate {MOVE, }
    private TypeOfUpdate typeOfUpdate;

    // MOVE
    private ArrayList<EnvironmentEntity> squaresToUpdate = new ArrayList<>();

    public ViewUpdate(EnvironmentEntity entity, TypeOfUpdate typeOfUpdate) {
        this.entity = entity;
        this.typeOfUpdate = typeOfUpdate;
    }

    public EnvironmentEntity getEntity() {
        return entity;
    }

    public TypeOfUpdate getTypeOfUpdate() {
        return typeOfUpdate;
    }

    public ArrayList<EnvironmentEntity> getSquaresToUpdate() {
        return squaresToUpdate;
    }

    public void setSquaresToUpdate(EnvironmentEntity entity) {
        //this.squaresToUpdate.add(newPosition);
    }
}