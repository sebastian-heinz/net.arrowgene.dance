package net.arrowgene.dance.editor.stepfile.SMPrinter.models;

import java.util.List;
import java.util.ArrayList;

/**
 * A model for all the data contained within a single measure of a step chart. A measure
 * is equivalent to a musical measure in the song: contains a set number of beats, and
 * step lines for each beat step (or fraction of a beat).
 *
 * @author Dan
 */

public class Measure {
    private List<SimFileLine> lines;
    private int measureNumber;

    private boolean isEmpty = true;

    public Measure(int measureNumber) {
        lines = new ArrayList<SimFileLine>();
        this.measureNumber = measureNumber;
    }


    public List<SimFileLine> getLines() {
        return lines;
    }

    public void setLines(List<SimFileLine> lines) {
        this.lines = lines;
    }

    public void addLine(SimFileLine line) {
        lines.add(line);
        isEmpty = line.isEmpty() && isEmpty;
    }

    public int getNumberOfLines() {
        return lines.size();
    }

    public int getMeasureNumber() {
        return measureNumber;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SimFileLine line : lines) {
            sb.append(line).append("\n");
        }
        sb.append("----");
        return sb.toString();
    }
}
