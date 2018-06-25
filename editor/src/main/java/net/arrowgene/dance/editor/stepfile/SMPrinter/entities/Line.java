package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import java.awt.Color;
import java.awt.Graphics;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.SimFileLine;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Timing;

/**
 * An entity that represents one line on a step chart. Each measure contains multiple lines
 * (or rows) that display specific timing button presses.
 *
 * @author Dan
 */

public class Line extends Container {
    protected SimFileLine line;

    protected Hold[] currentHolds;

    protected double stepSideLength;

    public Line(Settings settings, SimFileLine line, Hold[] currentHolds, int x, int y, int width, int height) {
        super(settings, x, y, width, height);

        this.line = line;
        this.currentHolds = currentHolds;

        generateObjects();
    }

    private void generateObjects() {
        net.arrowgene.dance.editor.stepfile.SMPrinter.models.Step[] steps = line.getSteps();
        children = new Entity[steps.length];

        stepSideLength = (double) width / steps.length;
        double currentX = x;
        for (int i = 0; i < steps.length; i++) {
            children[i] = new Step(settings, steps[i], currentHolds, i, (int) currentX, y, (int) stepSideLength, height);
            currentX += stepSideLength;
        }
    }

    @Override
    public void draw(Graphics g) {
        drawChildren(g);
    }

    @Override
    public void drawMidground(Graphics g) {
        drawChildrenMidground(g);
    }

    @Override
    public void drawBackground(Graphics g) {
        //	highlightRegion(g, Color.RED);

        if (line.getTiming() == Timing.L1ST) {
            g.setColor(Color.BLACK);
            g.fillRect(x, y + (int) (stepSideLength / 2) - 2, width, 4);
        } else if (line.getTiming() == Timing.L4TH) {
            g.setColor(Color.BLACK);
            g.fillRect(x, y + (int) (stepSideLength / 2) - 1, width, 2);
        }

        drawChildrenBackground(g);
    }
}
