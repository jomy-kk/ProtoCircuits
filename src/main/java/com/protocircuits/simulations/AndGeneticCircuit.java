package com.protocircuits.simulations;

public class AndGeneticCircuit implements GeneticCircuit {

    enum State {NONE_DISPLACED, FIRST_DISPLACED}

    private SSDNA toehold1, toehold2;
    private final GeneticCircuitFactory output;  // Factory design pattern
    private State state;

    public AndGeneticCircuit(SSDNA toehold1, SSDNA toehold2, SSDNA output, int N_copies, int type) {
        this.toehold1 = toehold1;
        this.toehold2 = toehold2;
        this.output = new GeneticCircuitFactory(output, N_copies, type);
        this.state = State.NONE_DISPLACED;
    }

    public AndGeneticCircuit(SSDNA toehold1, SSDNA toehold2, SSDNA output, int N_copies) {
        this.toehold1 = toehold1;
        this.toehold2 = toehold2;
        this.output = new GeneticCircuitFactory(output, N_copies, 2); // and gates are type 2 by default
        this.state = State.NONE_DISPLACED;
    }

    @Override
    public SSDNA[] execute(SSDNA[] inputs) throws Exception {
        if (output.getNCopies() >= 1) { // check here to reduce temporal complexity
            switch (state) {
                case NONE_DISPLACED:
                    for (SSDNA input : inputs)
                        if (toeholdMediatedStrandDisplacement(toehold1, input)) {
                            state = State.FIRST_DISPLACED;
                            return new SSDNA[]{}; // returns empty array => successful displacement, but no output yey
                        }
                    break;
                case FIRST_DISPLACED:
                    for (SSDNA input : inputs)
                        if (toeholdMediatedStrandDisplacement(toehold2, input)) {
                            state = State.NONE_DISPLACED; // to repeat the cycle
                            return new SSDNA[]{output.generateOutput()}; // returns array with one output
                        }
                    break;
            }

            return null; // returns null => no successful displacement, discard all these inputs
        }
        else
            return null; // returns null => no more copies are available
    }
}
