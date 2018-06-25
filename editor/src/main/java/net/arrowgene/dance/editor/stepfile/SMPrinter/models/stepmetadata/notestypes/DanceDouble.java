package net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;

/**
 * Implementation of NotesType for the dance double game mode. (8 buttons, 1 player)
 *
 * @author Dan
 */

public class DanceDouble extends NotesType {

    public DanceDouble(GameMode playType) {
        super(playType);
    }

    @Override
    public Orientation getStepOrientation(int lineIndex) {
        switch (lineIndex) {
            case 0:
            case 4:
                return Orientation.LEFT;
            case 1:
            case 5:
                return Orientation.DOWN;
            case 2:
            case 6:
                return Orientation.UP;
            case 3:
            case 7:
                return Orientation.RIGHT;
            default:
                return Orientation.NONE;
        }
    }

    @Override
    public int getLineLength() {
        return 8;
    }

    @Override
    public String getMetaCode() {
        return "dance-double";
    }

    @Override
    public String toString() {
        return "Dance Double";
    }

}
