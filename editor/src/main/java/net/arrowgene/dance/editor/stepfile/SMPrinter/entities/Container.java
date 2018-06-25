package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import java.awt.Graphics;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;

/**
 * A container that holds other entities within itself. Has methods to draw the foreground, midground and
 * background of the contained entities.
 *
 * @author Dan
 */

public abstract class Container extends Entity {
    protected Entity[] children;


    public Container(Settings settings, int x, int y, int width, int height) {
        super(settings, x, y, width, height);
    }

    protected void drawChildren(Graphics g) {
        if (children != null) {
            for (Entity child : children) {
                child.draw(g);
            }
        }
    }

    protected void drawChildrenMidground(Graphics g) {
        if (children != null) {
            for (Entity child : children) {
                child.drawMidground(g);
            }
        }
    }

    protected void drawChildrenBackground(Graphics g) {
        if (children != null) {
            for (Entity child : children) {
                child.drawBackground(g);
            }
        }
    }
}
