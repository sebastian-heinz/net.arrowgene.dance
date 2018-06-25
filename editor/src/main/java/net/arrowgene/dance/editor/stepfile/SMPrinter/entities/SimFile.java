package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure;

/**
 * An entity that represents the highest level of objects that draw the step chart.
 * The overall structure of the sim file is: SimFile > Column > Measure > Line > Step/Hold.
 *
 * @author Dan
 */

public class SimFile extends Container implements Printable {
    private final int pageWidth;
    private final int pageHeight;

    public SimFile(Settings settings, int x, int y, int pageWidth, int pageHeight) {
        super(settings, x, y, 0, 0);
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;

        generateObjects();
    }

    private void generateObjects() {
        final String pageHeader = settings.simFile.getTitle() + " - " + settings.simFile.getArtist() + " (" + settings.difficulty + ")";

        net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure[] measures;
        if (settings.hideLeadingAndTrailingWhiteSpace) {
            measures = padMeasures(settings.difficulty.getTrimmedMeasures());
        } else {
            measures = padMeasures(settings.difficulty.getMeasures());
        }

        int numberOfPages = settings.getNumberOfPages();

        children = new Entity[numberOfPages];
        for (int i = 0; i < numberOfPages; i++) {
            children[i] = new Page(settings, measures, pageHeader, i, x, i * pageHeight, pageWidth, pageHeight);
        }

        width = pageWidth;
        height = numberOfPages * pageHeight;
    }

    private Measure[] padMeasures(List<net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure> measureList) {
        int measureSize = (int) Math.ceil((double) measureList.size() / settings.getMeasuresPerPage()) * settings.getMeasuresPerPage();
        Measure[] result = new net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure[measureSize];
        return measureList.toArray(result);
    }

    public int getNumberOfPages() {
        return children.length;
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
        drawChildrenBackground(g);
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageNumber)
            throws PrinterException {
        if (pageNumber < children.length) {

        } else {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2D = (Graphics2D) g;
        final double xAxisScale = (double) pageFormat.getWidth() / pageWidth;
        final double yAxisScale = (double) pageFormat.getHeight() / pageHeight;
        if (Math.abs(xAxisScale - yAxisScale) > 1E7) {
            System.err.println("WARNING: The print page has a different aspect ratio than the sim file painter");
        }
        g2D.scale(xAxisScale, yAxisScale);
        g2D.translate(0, -pageNumber * pageHeight);
        Page page = (Page) children[pageNumber];
        page.drawBackground(g2D);
        page.drawMidground(g2D);
        page.draw(g2D);

        return PAGE_EXISTS;
    }

}
