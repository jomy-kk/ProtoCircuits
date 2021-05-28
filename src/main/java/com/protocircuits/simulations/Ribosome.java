package com.protocircuits.simulations;

public class Ribosome {

    public Protein translate(RNA rna) {

        String res_sequence = "";

        String[] codons = rna.sequence.split("(?<=\\G...)"); // divides the sequence in groups of 3 bases, each called a codon

        try {
            for (String codon: codons)
                res_sequence += Protein.aminoacid(codon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Protein(res_sequence);

    }
}
