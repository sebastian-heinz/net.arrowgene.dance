package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.SimFileLine;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Resources;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;

/**
 * An entity that represents a single measure in a step chart. Each column contains multiple
 * measures, each containing multiple step lines.
 *
 * @author Dan
 */

public class Measure extends Container {
    protected net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure measure;
    protected Hold[] currentHolds;

    public Measure(Settings settings, net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure measure, Hold[] currentHolds, int x, int y, int width, int height) {
        super(settings, x, y, width, height);

        this.measure = measure;
        this.currentHolds = currentHolds;

        generateChildren();
    }

    private void generateChildren() {
        if (measure != null) {
            List<SimFileLine> lines = measure.getLines();
            children = new Entity[lines.size()];

            double currentY = y;
            double lineHeight = (double) height / children.length;

            for (int i = 0; i < children.length; i++) {
                children[i] = new Line(settings, lines.get(i), currentHolds,
                        x, (int) currentY, width, (int) lineHeight);
                currentY += lineHeight;
            }
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
        highlightRegion(g, settings.measureColor);
        drawChildrenBackground(g);

        if (measure != null) {
            g.setColor(Color.BLACK);
            g.setFont(Resources.getInstance().pageHeader);
            g.drawString(measure.getMeasureNumber() + "", x, y + 5);
        }
    }

}
