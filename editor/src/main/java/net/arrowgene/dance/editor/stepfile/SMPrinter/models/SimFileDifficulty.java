package net.arrowgene.dance.editor.stepfile.SMPrinter.models;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;

/**
 * A model for a single difficulty in a sim file. Contains step data, and meta data
 * specific to the difficulty.
 *
 * @author Dan
 */

public class SimFileDifficulty implements Comparable<SimFileDifficulty> {
    private String description;
    private String difficultyClass;
    private String difficultyMeter;
    private String radarValues;

    private NotesType notesType;

    private List<Measure> measures;
    private List<Measure> trimmedMeasures;

    public SimFileDifficulty() {
        measures = new ArrayList<Measure>();
    }

    public void trimMeasures() {
        trimmedMeasures = new ArrayList<Measure>(measures);

        ListIterator<Measure> itr = trimmedMeasures.listIterator();
        while (itr.hasNext()) {
            if (itr.next().isEmpty()) {
                itr.remove();
            } else {
                break;
            }
        }

        itr = trimmedMeasures.listIterator(trimmedMeasures.size());
        while (itr.hasPrevious()) {
            if (itr.previous().isEmpty()) {
                itr.remove();
            } else {
                break;
            }
        }
    }

    public NotesType getNotesType() {
        return notesType;
    }

    public void setNotesType(String metaCode) {
        this.notesType = NotesType.fromMetaCode(metaCode);
    }

    public void setNotesType(NotesType notesType) {
        this.notesType = notesType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficultyClass() {
        return difficultyClass;
    }

    public void setDifficultyClass(String difficultyClass) {
        this.difficultyClass = difficultyClass;
    }

    public String getDifficultyMeter() {
        return difficultyMeter;
    }

    public void setDifficultyMeter(String difficultyMeter) {
        this.difficultyMeter = difficultyMeter;
    }

    public String getRadarValues() {
        return radarValues;
    }

    public void setRadarValues(String radarValues) {
        this.radarValues = radarValues;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public List<Measure> getTrimmedMeasures() {
        return trimmedMeasures;
    }

    public void addMeasure(Measure measure) {
        measures.add(measure);
//		//don't want to take out measures empty measures in the middle of the song
//		if (!measure.isEmpty() || trimmedMeasures.size() > 0) {
//			trimmedMeasures.add(measure);
//		}
    }

    public int getNumberOfMeasures() {
        return measures.size();
    }

    public int getNumberOfMeasuresTrimmed() {
        return trimmedMeasures.size();
    }

    @Override
    public String toString() {
        return notesType + " - " + difficultyClass + " (" + difficultyMeter + ")";
    }

    @Override
    public int compareTo(SimFileDifficulty difficulty) {
        int temp;
        if ((temp = notesType.compareTo(difficulty.getNotesType())) != 0) {
            return temp;
        }
        return Integer.parseInt(difficultyMeter) - Integer.parseInt(difficulty.getDifficultyMeter());
    }
}
