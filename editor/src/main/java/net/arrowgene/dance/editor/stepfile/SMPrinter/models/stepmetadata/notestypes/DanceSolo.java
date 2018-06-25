package net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;

/**
 * Implementation of NotesType for the dance solo game mode. (4 buttons, 1 player)
 *
 * @author Dan
 */

public class DanceSolo extends NotesType {

    public DanceSolo(GameMode playType) {
        super(playType);
    }

    @Override
    public Orientation getStepOrientation(int lineIndex) {
        switch (lineIndex) {
            case 0:
                return Orientation.LEFT;
            case 1:
                return Orientation.DOWN;
            case 2:
                return Orientation.UP;
            case 3:
                return Orientation.RIGHT;
            default:
                return Orientation.NONE;
        }
    }

    @Override
    public int getLineLength() {
        return 4;
    }

    @Override
    public String getMetaCode() {
        return "dance-solo";
    }

    @Override
    public String toString() {
        return "Dance Solo";
    }

}
