package com.protocircuits.simulations;
import java.awt.Point;

public class Protein extends Molecule {

    protected String sequence;

    public Protein(Point position, Size size, String sequence) {
        super(position, size);
        this.sequence = sequence;
    }

    public Protein(Point center, String sequence) {
        super(center, new Size(1,1));
        this.sequence = sequence;
    }

    public Protein(String sequence) {
        super(new Point(0,0), new Size(1,1));
        this.sequence = sequence;
    }

    // this molecules hold this knowledge
    public static char aminoacid(String codon) throws Exception {
        switch (codon.charAt(0)) {
            case 'U':
                switch (codon.charAt(1)) {
                    case 'U':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                                return 'F';
                            case 'A':
                            case 'G':
                                return 'L';
                        }
                        break;
                    case 'C':
                        return 'S';
                    case 'A':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                                return 'Y';
                            case 'A':
                            case 'G':
                                return '-';
                        }
                    case 'G':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                                return 'C';
                            case 'A':
                                return '-';
                            case 'G':
                                return 'W';
                        }
                }
                break;
            case 'C':
                switch (codon.charAt(1)) {
                    case 'U':
                        return 'L';
                    case 'C':
                        return 'P';
                    case 'A':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                                return 'H';
                            case 'A':
                            case 'G':
                                return 'Q';
                        }
                    case 'G':
                        return 'R';
                }
                break;
            case 'A':
                switch (codon.charAt(1)) {
                    case 'U':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                            case 'A':
                                return 'I';
                            case 'G':
                                return 'M';
                        }
                    case 'C':
                        return 'T';
                    case 'A':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                                return 'N';
                            case 'A':
                            case 'G':
                                return 'K';
                        }
                    case 'G':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                                return 'S';
                            case 'A':
                            case 'G':
                                return 'R';
                        }
                }
                break;
            case 'G':
                switch (codon.charAt(1)) {
                    case 'U':
                        return 'V';
                    case 'C':
                        return 'A';
                    case 'A':
                        switch (codon.charAt(2)) {
                            case 'U':
                            case 'C':
                                return 'D';
                            case 'A':
                            case 'G':
                                return 'E';
                        }
                    case 'G':
                        return 'G';
                }
                break;
        }

        throw new Exception("Invalid codon " + codon);
    }

    @Override
    public void agentDecision() {

    }
}
