package com.protocircuits.simulations;

public class TransducerGeneticCircuit implements GeneticCircuit {

    private final SSDNA toehold;
    private final GeneticCircuitFactory output;  // Factory design pattern

    public TransducerGeneticCircuit(SSDNA toehold, SSDNA output, int N_copies, int type) {
        this.toehold = toehold;
        this.output = new GeneticCircuitFactory(output, N_copies, type);
    }

    public TransducerGeneticCircuit(SSDNA toehold, SSDNA output, int N_copies) {
        this.toehold = toehold;
        this.output = new GeneticCircuitFactory(output, N_copies, 1); // transducers are type 1 by default
    }

    @Override
    public SSDNA[] execute(SSDNA[] inputs) throws Exception {
        if (inputs.length != 1)
            throw new Exception(); // one input at a time for this circuit
        else {
            if (toeholdMediatedStrandDisplacement(toehold, inputs[0]))
                if (output.getNCopies() > 0)
                    return new SSDNA[] {output.generateOutput()}; // generates one output per one input
                else
                    return null;
            else
                return null;
        }
    }
}
