package com.protocircuits.simulations;

import com.protocircuits.views.running.RunningView;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface GeneticCircuit {

    Logger logger = Logger.getLogger(RunningView.class.getName());

    SSDNA[] execute(SSDNA[] inputs) throws Exception;  // Command design pattern

    // default biological knowledge; it's just how it works
    default boolean toeholdMediatedStrandDisplacement(SSDNA toehold, SSDNA singleStrand) throws Exception {

        int toehold_size = toehold.sequence.length();

        // so queremos dar match das bases complementares do toehold (que he mais pequeno, e.g. 3 ou 4 bases)
        for (int i = 0; i < toehold_size; i++) {
            char c1 = toehold.sequence.charAt(i);
            char c2 = singleStrand.sequence.charAt(i);
            if (SSDNA.complementOf(c1) != c2) {
                return false;
            }
        }

        return true; // if the two sequences are complementary, then return true, meaning the process is successful.
    }

}
