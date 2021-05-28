package com.protocircuits.simulations;

import com.protocircuits.views.running.RunningView;

import java.awt.Point;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Nanorobot extends Protocell{

    private final static Logger logger = Logger.getLogger(RunningView.class.getName());

    public static int outputMoleculeCount = 0;

    public GeneticCircuit circuit;

    public Nanorobot(Point center) {
        super(center);
    }

    public Nanorobot(Point center, int max_cargo) {
        super(center, max_cargo);
    }

    public Nanorobot(Point center, int max_cargo, GeneticCircuit circuit) {
        super(center, max_cargo);
        this.circuit = circuit;
    }

    @Override
    public void agentDecision() {
        if(getState() == EntityState.INSIDE && hasCargo()) {
            tryActivateCircuit();
        }
        if(getState() == EntityState.BEING_RELEASED) {
            super.agentDecision();
            setState(EntityState.OUTSIDE);
        }
        if(getState() == EntityState.OUTSIDE) {
            if (hasOutputsToRelease()) { tryOutput(); }
            else if(hasCargo()) { tryActivateCircuit(); }
            else {
                super.agentDecision();
                perceiveBodies();
            }
        }
    }

    // Additional sensor
    private void perceiveRadiation() {
        // TODO
    }

    // Actuator
    private void tryActivateCircuit() {
        SSDNA[] inputMolecules = getAllCargo().toArray(new SSDNA[0]);
        try {
            SSDNA[] outputMolecules = circuit.execute(inputMolecules); // { x, x, x, ... } where x might be null
            if (outputMolecules == null) { // strand was not displaced (bc strand does not match, or no more copies are available)
                addOutput(inputMolecules); // send (all) the input molecule back to the environment
                removeCargo(inputMolecules); // remove (all) the input molecule as cargo
            }
            else { // strand was displaced
                for (SSDNA outputMolecule: outputMolecules) { // e.g. in AND and OR circuits, outputMolecules.length will be 0, so this is not executed
                    logger.log(Level.INFO, "A circuit generated new output molecule " + ++Nanorobot.outputMoleculeCount);
                    if (getState() == EntityState.INSIDE || getState() == EntityState.BEING_RELEASED)
                        carrier.addDNAToTranscript(outputMolecule);
                    else
                        addOutput(outputMolecule);
                }
                // e.g. but this is, which is necessary
                removeCargo(inputMolecules); // remove the input molecule as cargo
                Environment.removeEntity(inputMolecules[0].position); // generally they are superimposed

                // FIXME: for 2+ input molecules, we need to know each one was consumed to remove just that one
                for (Molecule a: inputMolecules)
                    Environment.unregisterAgent(a); // the input molecule was "consumed"  // e.g. in this example the input was also consumed and "stays inside"
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception while executing circuit was raised:" + e.toString());
        }
    }

    private void tryOutput() {
        for (SSDNA outputMolecule: getAllOutputs()) {
            if (output(outputMolecule))
                break; // one release successful (one release per step)
        }
    }

}
