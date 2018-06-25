package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Resources;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;


/**
 * A single page that contains all the graphics that from one page on the step chart.
 * These pages are a one to one mapping of the pages that are printed using the print
 * commandos. Draws text for the step chart title and the page number.
 *
 * @author Dan
 */

public class Page extends Container {
    private int PAGE_MARGIN = 48;

    private net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure[] measures;
    private int pageNumber;
    private String pageHeader;

    public Page(Settings settings, Measure[] measures, String pageHeader, int pageNumber, int x, int y, int width, int height) {
        super(settings, x, y, width, height);

        this.measures = measures;
        this.pageHeader = pageHeader;
        this.pageNumber = pageNumber;

        generateObjects();
    }

    private void generateObjects() {
        double columnWidth = (double) (width - 2 * PAGE_MARGIN) / settings.columnsPerPage;
        double currentX = x + PAGE_MARGIN;
        int currentMeasureIndex = pageNumber * settings.columnsPerPage * settings.measuresPerColumn;

        children = new Entity[settings.columnsPerPage];
        for (int i = 0; i < settings.columnsPerPage; i++) {
            Measure[] columnMeasures = Arrays.copyOfRange(measures, currentMeasureIndex, currentMeasureIndex + settings.measuresPerColumn);
            children[i] = new Column(settings, columnMeasures,
                    (int) currentX, y + PAGE_MARGIN, (int) columnWidth, height - 2 * PAGE_MARGIN);
            currentX += columnWidth;
            currentMeasureIndex += settings.measuresPerColumn;
        }

    }

    @Override
    public void draw(Graphics g) {
        drawChildren(g);

        g.setColor(Color.BLACK);
        g.setFont(Resources.getInstance().pageHeader);
        g.drawString(pageHeader, x + PAGE_MARGIN, y + PAGE_MARGIN - 10);

        String pageIndicator = "Page " + (pageNumber + 1) + " of " + settings.getNumberOfPages();
        int stringWidth = g.getFontMetrics().stringWidth(pageIndicator);
        g.drawString(pageIndicator, x + width - stringWidth - PAGE_MARGIN, y + PAGE_MARGIN - 10);
    }

    @Override
    public void drawMidground(Graphics g) {
        drawChildrenMidground(g);
    }

    @Override
    public void drawBackground(Graphics g) {
        highlightRegion(g, settings.pageColor);
        outlineRegion(g, settings.pageOutlineColor);
        drawChildrenBackground(g);
    }
}
