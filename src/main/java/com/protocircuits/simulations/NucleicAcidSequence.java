package com.protocircuits.simulations;

import java.awt.Point;

public abstract class NucleicAcidSequence extends Molecule {

    protected String sequence;

    public NucleicAcidSequence(Point center, Size size) {
        super(center, size);
    }

    public NucleicAcidSequence(Point center, Size size, String sequence) {
        super(center, size);
        this.sequence = sequence;
    }

}
