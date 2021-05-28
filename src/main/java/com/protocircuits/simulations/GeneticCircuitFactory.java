package com.protocircuits.simulations;

import com.protocircuits.views.running.RunningView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneticCircuitFactory {

    private final SSDNA output;
    private int N_copies;
    public int type;

    Logger logger = Logger.getLogger(RunningView.class.getName());

    public GeneticCircuitFactory(SSDNA output, int N_copies, int type) {
        this.output = output;
        this.N_copies = N_copies;
        this.type = type;
    }

    public SSDNA generateOutput() { // Factory design pattern
        if (N_copies > 0) {
            --N_copies;
            return new SSDNA(output.sequence, type);
        }
        return null;
    }

    public int getNCopies() {
        return N_copies;
    }
}
