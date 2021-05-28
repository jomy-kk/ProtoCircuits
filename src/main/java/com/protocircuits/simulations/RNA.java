package com.protocircuits.simulations;

import java.awt.Point;

public class RNA extends NucleicAcidSequence {

    public RNA(Point center, Size size) {
        super(center, size);
    }

    public RNA(Point center) {
        super(center, new Size(1,1));
    }

    public RNA(String sequence) {
        super(new Point(0,0),  new Size(1,1), sequence);
    }

    @Override
    public void agentDecision() {

    }

    // this molecules hold this knowledge
    public static char complementOf(char nucleicAcid) throws Exception {
        switch (nucleicAcid) {
            case 'A':
                return 'U';
            case 'T':
                return 'A';
            case 'G':
                return 'C';
            case 'C':
                return 'G';
            default:
                throw new Exception("Invalid nucleic acid " + nucleicAcid);
        }
    }

}
