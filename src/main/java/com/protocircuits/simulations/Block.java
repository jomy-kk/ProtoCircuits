package com.protocircuits.simulations;


import com.vaadin.flow.component.html.Div;

import java.awt.Point;

public class Block {

    public enum Shape { PLASMA, TUNICA_MEDIA, TUNICA_EXTERNA, ECM, CELL, FREE }

    public Shape shape;
    public Point position;
    public Div UISquare;

    public Block(Shape shape, Point position) {
        this.shape = shape;
        this.position = position;
    }

    public Block(Shape shape) {
        this.shape = shape;
    }

    public void setUISquare(Div UISquare) {
        this.UISquare = UISquare;
    }
}
