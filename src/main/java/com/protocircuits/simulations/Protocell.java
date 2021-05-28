package com.protocircuits.simulations;

import com.protocircuits.views.running.RunningView;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public abstract class Protocell extends EnvironmentEntity {

    private final static Logger logger = Logger.getLogger(RunningView.class.getName());

    // Biological properties
    int MAX_CARGO = 2;
    private Set<SSDNA> cargo;
    private Set<SSDNA> outputs;
    private final LipidBilayer membrane;
    private float gibbsEnergy;

    private EnvironmentEntity.EntityState _state = EnvironmentEntity.EntityState.OUTSIDE;
    public LivingCell carrier = null; // keep a pointer to the cell it is inside of

    // Constructors

    public Protocell(Point center) {
        super(center, new Size(3,3));
        this.membrane = new LipidBilayer();
        this.gibbsEnergy = -40;
        cargo = new HashSet<>(MAX_CARGO);
        outputs = new HashSet<>(MAX_CARGO);
    }
    public Protocell(Point center, int max_cargo) {
        super(center, new Size(3,3));
        this.membrane = new LipidBilayer();
        this.gibbsEnergy = -40;
        MAX_CARGO = max_cargo;
        cargo = new HashSet<>(MAX_CARGO);
        outputs = new HashSet<>(MAX_CARGO);
    }

    public Protocell(Point position, float gibbsEnergy) {
        super(position, new Size(3,3));
        this.membrane = new LipidBilayer();
        this.gibbsEnergy = gibbsEnergy;
    }

    public Protocell(Point position, Size size) {
        super(position, size);
        this.membrane = new LipidBilayer();
        this.gibbsEnergy = -40;
    }

    public Protocell(Point position, Size size, float gibbsEnergy) {
        super(position, size);
        this.membrane = new LipidBilayer();
        this.gibbsEnergy = gibbsEnergy;
    }

    Set<SSDNA> getAllCargo() {
        return this.cargo;
    }

    void addCargo(SSDNA mol) {
        this.cargo.add(mol);
    }

    void removeCargo(SSDNA mol) {
        this.cargo.remove(mol);
    }

    void removeCargo(SSDNA[] molecules) {
        this.cargo.removeAll(Arrays.asList(molecules));
    }


    Set<SSDNA> getAllOutputs() {
        return this.outputs;
    }

    void addOutput(SSDNA mol) {
        this.outputs.add(mol);
    }

    void addOutput(SSDNA[] molecules) {
        this.outputs.addAll(Arrays.asList(molecules));
    }

    void removeOutput(SSDNA mol) {
        this.outputs.remove(mol);
    }

    public void agentDecision() {
        moveRandomly();
    }

    public EnvironmentEntity.EntityState getState() {
        return _state;
    }

    public void setState(EnvironmentEntity.EntityState s) {
        _state = s;
    }


    // Sensors

    public boolean hasCargo() {
        return !this.cargo.isEmpty();
    }

    public boolean hasOutputsToRelease() {
        return !this.outputs.isEmpty();
    }

    public boolean reachedCargoLimit() {
        return this.cargo.size() == MAX_CARGO;
    }

    private void perceiveSSDNAInsideLivingCell(LivingCell cell) {
        for (Molecule mol: cell.continuousProductionOfMolecules()) {
            if (mol instanceof SSDNA)
                addCargo((SSDNA) mol);
        }
    }

    void perceiveBodies() {
        // check for Living cells in the nearest neighborhood
        for (Point p: getNeighborhoodPoints()) {
            EnvironmentEntity entity = Environment.getEntity(p);
            if (entity instanceof LivingCell) {
                LivingCell cell = (LivingCell)entity;
                if (cell.input(this))
                    break; // if successful, then end
                // otherwise, try other neighbor cells
            }
        }
    }


    // Actuators

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        if (hasCargo()){ // if it has some molecule inside, then they follow the movement
            for(SSDNA mol: cargo) {
                mol.position = new Position(this.position.getCenter(), mol.size);
                Environment.insertEntity(mol); // commit change to environment
            }
        }
    }

    boolean input(Molecule mol) {
        if (!reachedCargoLimit()) { // ensure capacity, before opening membrane
            if (mol instanceof SSDNA) { // the membrane cannot open unless it is a tiny ssDNA
                this.membrane.open();
                addCargo((SSDNA)mol);
                mol.moveInside(this);
                this.membrane.close();
                return true;
            }
        }
        return false; // input not successful
    }

    boolean output(Molecule mol) {
        // check if any neighbor position is free
        for (Point p: getNeighborhoodPoints()) {
             if (Environment.getEntity(p) == null) {
                this.membrane.open();
                removeOutput((SSDNA)mol);
                mol.moveOutside(p);
                Environment.registerAgent(mol); // if this is a new molecule, now autonomous, then it should be added
                this.membrane.close();
                return true;
            }
        }
        return false; // output not successful
    }

    void moveInside(LivingCell cell) {
        _state = EntityState.INSIDE;
        Environment.removeEntity(this.position); // remove its old position from the environment
        Point center = cell.position.getCenter();
        this.position = new Position(center, this.size);
        Environment.insertEntity(this); // commit change to environment
        perceiveSSDNAInsideLivingCell(cell);
        carrier = cell;
    }

    void moveOutside(Point p) {
        _state = EntityState.BEING_RELEASED;
        Environment.removeEntity(this.position); // remove its old position from the environment, if any
        this.position = new Position(p, this.size);
        Environment.insertEntity(this); // commit change to environment
        carrier = null;
    }





}
