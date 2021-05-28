package com.protocircuits.simulations;

public class OrGeneticCircuit implements GeneticCircuit {

    private SSDNA strandA, strandB, output;

    @Override
    public SSDNA[] execute(SSDNA[] inputs) throws Exception {
        if (inputs.length != 2)
            throw new Exception();
        else {
            if (toeholdMediatedStrandDisplacement(inputs[0], strandA) ||
                    toeholdMediatedStrandDisplacement(strandA, inputs[0]))
                return new SSDNA[] {output};
            else
                return null;
        }
    }
}
