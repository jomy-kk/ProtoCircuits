package com.protocircuits.simulations;

import com.protocircuits.views.running.RunningView;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LivingCell extends EnvironmentEntity {

    private final static Logger logger = Logger.getLogger(RunningView.class.getName());

    // Knowledge about itself
    private String proteinSequenceThatTriggersApoptosis;
    private Molecule[] continuouslyProduces;

    // Organelles
    private LipidBilayer membrane;
    private RNAPolymerase[] rnaPolymerases;
    private Ribosome[] ribosomes;
    public LivingCellCondition condition;

    public enum LivingCellCondition { HEALTHY, TUMOROUS, DEAD };

    // Other agents it can contain
    int MAX_CARGO = 1;
    private Set<Protocell> cargo;
    private Set<Protocell> protocellsToOutput;
    private Stack<SSDNA> dnaToTranscript;
    private Stack<RNA> rnaToTranslate;
    private Stack<Protein> proteinsToMaturate;

    // Constructors

    public LivingCell(Point center, Size size) {
        super(center, size);
        this.condition = LivingCellCondition.HEALTHY;
        createOrganelles();
    }

    public LivingCell(Point center) {
        super(center, new Size(9,9));
        this.condition = LivingCellCondition.HEALTHY;
        createOrganelles();
    }

    public LivingCell(Point center, boolean tumorous) {
        super(center, new Size(9,9));
        if (tumorous) this.condition = LivingCellCondition.TUMOROUS;
        else this.condition = LivingCellCondition.HEALTHY;
        createOrganelles();
        if (tumorous)
            this.continuouslyProduces = new Molecule[] {new SSDNA("AACTGCGCATG")}; // just a default of tumorous cells
        else
            this.continuouslyProduces = new Molecule[] {};
        this.proteinSequenceThatTriggersApoptosis = "MPR-"; // just a default
    }

    public LivingCell(Point center, boolean tumorous, Molecule[] continuouslyProduces, String apoptosisProteinSequence) {
        super(center, new Size(9,9));
        if (tumorous) this.condition = LivingCellCondition.TUMOROUS;
        else this.condition = LivingCellCondition.HEALTHY;
        createOrganelles();
        this.continuouslyProduces = continuouslyProduces;
        this.proteinSequenceThatTriggersApoptosis = apoptosisProteinSequence;
    }



    private void createOrganelles() {
        this.membrane = new LipidBilayer();
        this.ribosomes = new Ribosome[] {new Ribosome(), new Ribosome(), new Ribosome()};
        this.rnaPolymerases = new RNAPolymerase[] {new RNAPolymerase(), new RNAPolymerase(), new RNAPolymerase()};

        cargo = new HashSet<>();
        protocellsToOutput = new HashSet<>();
        dnaToTranscript = new Stack<>();
        rnaToTranslate = new Stack<>();
        proteinsToMaturate = new Stack<>();
    }

    // Agent Decision at each epoch

    @Override
    public void agentDecision() {
        if (condition != LivingCellCondition.DEAD) {
            if (hasProteinsToMaturate()) {
                while (!proteinsToMaturate.isEmpty()) {
                    apoptosis(proteinsToMaturate.pop());
                }
            }
            if (hasProtocellsToOutput()) {
                for (Protocell p: protocellsToOutput) {
                    output(p);
                }
            }
            if (hasRNAToTranslate()) {
                while (!rnaToTranslate.isEmpty()) {
                    proteinsToMaturate.push(ribosomes[0].translate(rnaToTranslate.pop()));
                }
            }
            if (hasDNAToTranscript()) {
                while (!dnaToTranscript.isEmpty()) {
                    rnaToTranslate.push(rnaPolymerases[0].transcript(dnaToTranscript.pop()));
                    Environment.insertEntity(this); // just to recolor
                }
            }
            if (hasCargo()) { // cargo are protocells
                protocellsToOutput.addAll(cargo); // protocells have 1 epoch to release output and add it to dnaToTranscript.
            }
        }
    }


    // Sensors

    public boolean reachedCargoLimit() { return this.cargo.size() == MAX_CARGO; }

    private boolean hasProtocellsToOutput() { return !this.protocellsToOutput.isEmpty(); }

    private boolean hasProteinsToMaturate() { return !this.proteinsToMaturate.isEmpty(); }

    private boolean hasRNAToTranslate() { return !this.rnaToTranslate.isEmpty(); }

    private boolean hasDNAToTranscript() { return !this.dnaToTranscript.isEmpty(); }

    private boolean hasCargo() { return !this.cargo.isEmpty(); }

    public void addDNAToTranscript(SSDNA dna) {
        dnaToTranscript.add(dna);
    }


    // Actuators

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        if (hasCargo()){ // if it has some protocell inside, then they follow the movement
            for(Protocell p: cargo) {
                p.position = new Position(this.position.getCenter(), p.size);
                Environment.insertEntity(p); // commit change to environment
            }
        }
    }

    private void apoptosis(Protein p) {
        if(p.sequence.equals(proteinSequenceThatTriggersApoptosis)) {
            logger.log(Level.WARNING, "Protein of sequence " + p.sequence + " lead to " + condition.toString() + " cell dead");
            condition = LivingCellCondition.DEAD; // stops all machinery
        }
        else {
            logger.log(Level.INFO, "Protein of sequence " + p.sequence + " didn't lead to " + condition.toString() + " cell dead");
        }
    }

    public boolean input(EnvironmentEntity entity) {
        if (!reachedCargoLimit() && condition != LivingCellCondition.DEAD) { // ensure capacity, before opening membrane
            if (entity instanceof Protocell) { // the membrane opens for nanorobots
                this.membrane.open();
                Protocell p = ((Protocell)entity);
                cargo.add(p);
                p.moveInside(this);
                this.membrane.close();
                return true;
            }
            if (entity instanceof SSDNA) { // the membrane opens for molecules such as ssDNA
                this.membrane.open();
                dnaToTranscript.add((SSDNA)entity);
                ((Molecule)entity).moveInside(this);
                this.membrane.close();
                return true;
            }
        }
        return false; // input not successful
    }

    private boolean output(EnvironmentEntity entity) {
        // All ssDNAs that enter are transcribed and "destroyed", so only Protocells can be outputted
        for (Point p: getNeighborhoodPoints()) {
            if (Environment.getEntity(p) == null) {
                this.membrane.open();
                protocellsToOutput.remove(entity);
                cargo.remove(entity);
                ((Protocell)entity).moveOutside(p);
                this.membrane.close();
                move(p.x < position.getX() ? 2 : -2, p.y < position.getY() ? 2 : -2); // impulsion force
                return true;
            }
        }
        return false; // output not successful
    }

    public Molecule[] continuousProductionOfMolecules() {
        return continuouslyProduces;
    }


}
