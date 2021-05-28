package com.protocircuits.simulations;

public class AmplifierGeneticCircuit implements GeneticCircuit {

    private final SSDNA toehold;
    private final GeneticCircuitFactory output;  // Factory design pattern

    public AmplifierGeneticCircuit(SSDNA toehold, SSDNA output, int N_copies, int type) {
        this.toehold = toehold;
        this.output = new GeneticCircuitFactory(output, N_copies, type);
    }

    public AmplifierGeneticCircuit(SSDNA toehold, SSDNA output, int N_copies) {
        this.toehold = toehold;
        this.output = new GeneticCircuitFactory(output, N_copies, 0); // amplifiers are type 0 by default
    }

    @Override
    public SSDNA[] execute(SSDNA[] inputs) throws Exception {
        if (inputs.length != 1)
            throw new Exception();
        else {
            if (toeholdMediatedStrandDisplacement(toehold, inputs[0]))
                if (output.getNCopies() >= 2)
                    return new SSDNA[] {output.generateOutput(), output.generateOutput()};
                else
                    return null;
            else
                return null;
        }
    }
}
