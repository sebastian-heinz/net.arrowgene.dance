package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import java.awt.Graphics;

/**
 * An object that, given a Graphics instance, can draw itself onto the canvas.
 *
 * @author Dan
 */

public interface Drawable {
    public void draw(Graphics g);

    public void drawMidground(Graphics g);

    public void drawBackground(Graphics g);
}
