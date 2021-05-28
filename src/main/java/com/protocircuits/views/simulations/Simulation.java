package com.protocircuits.views.simulations;

import com.vaadin.flow.component.grid.Grid;

public class Simulation {

    private int id;
    private String name;
    private String population1;
    private int population1Amount;
    private String population2;
    private int population2Amount;
    private String population3;
    private int population3Amount;
    private String initialSSDNA;
    private int initialSSDNAAmount;
    private String environment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPopulation1() {
        return population1;
    }

    public void setPopulation1(String population1) {
        this.population1 = population1;
    }

    public int getPopulation1Amount() {
        return population1Amount;
    }

    public void setPopulation1Amount(int population1Amount) {
        this.population1Amount = population1Amount;
    }

    public String getPopulation2() {
        return population2;
    }

    public void setPopulation2(String population2) {
        this.population2 = population2;
    }

    public int getPopulation2Amount() {
        return population2Amount;
    }

    public void setPopulation2Amount(int population2Amount) {
        this.population2Amount = population2Amount;
    }

    public String getPopulation3() {
        return population3;
    }

    public void setPopulation3(String population3) {
        this.population3 = population3;
    }

    public int getPopulation3Amount() {
        return population3Amount;
    }

    public void setPopulation3Amount(int population3Amount) {
        this.population3Amount = population3Amount;
    }

    public String getInitialSSDNA() {
        return initialSSDNA;
    }

    public void setInitialSSDNA(String initialSSDNA) {
        this.initialSSDNA = initialSSDNA;
    }

    public int getInitialSSDNAAmount() {
        return initialSSDNAAmount;
    }

    public void setInitialSSDNAAmount(int initialSSDNAAmount) {
        this.initialSSDNAAmount = initialSSDNAAmount;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
