package com.protocircuits.simulations;

public class RNAPolymerase {

    public RNA transcript(SSDNA dna) {

        String res_sequence = "";

        try {
            for (int i = 0; i < dna.sequence.length(); i++)
                res_sequence += RNA.complementOf(dna.sequence.charAt(i));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ssDNA is consumed, in case of that
        if (dna.position != null) {
            Environment.removeEntity(dna.position);
        }

        Environment.unregisterAgent(dna);

        return new RNA(res_sequence);
    }
}
