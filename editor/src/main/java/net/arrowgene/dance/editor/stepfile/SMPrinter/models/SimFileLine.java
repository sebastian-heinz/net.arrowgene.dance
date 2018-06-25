package net.arrowgene.dance.editor.stepfile.SMPrinter.models;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Type;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Timing;

/**
 * A model for a single line in a step chart. Contains several steps, the number
 * depending on what game mode the line is for. There is a line at every fraction
 * of a beat.
 *
 * @author Dan
 */

public class SimFileLine {

    private Step[] steps;
    private Timing timing;

    private boolean isEmpty = true;

    public SimFileLine(String rawData, SimFileLine previousLine, NotesType notesType,
                       int lineIndex, int numberLinesInMeasure) {
        timing = Timing.fromLineIndexAndMeasureSize(lineIndex, numberLinesInMeasure);

        steps = new Step[rawData.trim().length()];
        if (previousLine != null && previousLine.steps != null) {
            for (int i = 0; i < steps.length; i++) {
                steps[i] = makeStep(rawData.charAt(i), i, notesType, previousLine.getSteps()[i]);
            }
        } else {
            for (int i = 0; i < steps.length; i++) {
                steps[i] = makeStep(rawData.charAt(i), i, notesType, null);
            }
        }
    }

    private Step makeStep(char rawCharacter, int stepIndex, NotesType notesType, Step previousStep) {
        Type type = Type.fromChar(rawCharacter, previousStep);
        Orientation orientation = notesType.getStepOrientation(stepIndex);
        if (type != Type.NONE) {
            isEmpty = false;
        }
        return new Step(type, orientation, timing);
    }

    public Step[] getSteps() {
        return steps;
    }

    public Timing getTiming() {
        return timing;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    //debug strings
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Step step : steps) {
            sb.append(getStepRepr(step));
        }
        return sb.toString();
    }

    private String getStepRepr(Step step) {
        if (step == null) {
            return "";
        }

        switch (step.getType()) {
            case REGULAR:
                return "#";
            case FREEZE_START:
                return "%";
            case HOLDING:
                return "|";
            case ROLLING:
                return "!";
            default:
                return " ";
        }
    }
}
