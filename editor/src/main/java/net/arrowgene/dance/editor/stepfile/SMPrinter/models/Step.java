package net.arrowgene.dance.editor.stepfile.SMPrinter.models;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Timing;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Type;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;

/**
 * A model for a single step of any orientation, type, game mode or timing.
 *
 * @author Dan
 */

public class Step {

    private Type type;
    private Orientation orientation;
    private Timing timing;

    public Step(Type type, Orientation orientation, Timing timing) {
        this.type = type;
        this.orientation = orientation;
        this.timing = timing;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Timing getTiming() {
        return timing;
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }
}
