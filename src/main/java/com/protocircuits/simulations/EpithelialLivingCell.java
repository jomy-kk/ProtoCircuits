package com.protocircuits.simulations;

import java.awt.*;

public class EpithelialLivingCell extends LivingCell{

    public EpithelialLivingCell(Point position, Size size) {
        super(position, size);
    }

    public EpithelialLivingCell(Point center, boolean tumorous) {
        super(center, tumorous);
    }


}
