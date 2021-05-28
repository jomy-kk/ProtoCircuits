package com.protocircuits.simulations;
import com.protocircuits.views.running.RunningView;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSDNA extends NucleicAcidSequence {

    static Logger logger = Logger.getLogger(RunningView.class.getName());

    public int type; // type for color-coded maps in the view

    public SSDNA(Point center, Size size, int type) {
        super(center, size);
        this.type = type;
    }

    public SSDNA(Point center, int type) {
        super(center,  new Size(1,1));
        this.type = type;
    }

    public SSDNA(String sequence, int type) {
        super(new Point(0,0),  new Size(1,1), sequence);
        this.type = type;
    }

    public SSDNA(String sequence) { // special constructor for toeholds; do not use otherwise!
        super(new Point(0,0),  new Size(1,1), sequence);
    }

    public SSDNA(Point center, String sequence, int type) {
        super(center,  new Size(1,1), sequence);
        this.type = type;
    }


    @Override
    public void agentDecision() {
        switch (getState()) {
            case OUTSIDE:
                moveRandomly();
                perceiveBodies();
                break;
            case BEING_RELEASED:
                moveRandomly();
                setState(EntityState.OUTSIDE);
                break;
            case INSIDE:
                // left empty intentionally
                break;

        }
    }

    // this molecules hold this knowledge
    public static char complementOf(char nucleicAcid) throws Exception {
        switch (nucleicAcid) {
            case 'A':
                return 'T';
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
