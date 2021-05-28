package com.protocircuits.simulations;

public class LipidBilayer {
    private boolean isClosed = true;

    boolean isClosed()    { return isClosed;  }
    void open()           { isClosed = true;  }
    void close()          { isClosed = false; }
}
