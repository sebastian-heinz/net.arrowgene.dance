package net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;

/**
 * Implementation of NotesType for the pump single game mode. (5 buttons, 1 player)
 *
 * @author Dan
 */

public class PumpSingle extends NotesType {

    public PumpSingle(GameMode playType) {
        super(playType);
    }

    @Override
    public Orientation getStepOrientation(int lineIndex) {
        switch (lineIndex) {
            case 0:
                return Orientation.DOWN_LEFT;
            case 1:
                return Orientation.UP_LEFT;
            case 2:
                return Orientation.CENTER;
            case 3:
                return Orientation.UP_RIGHT;
            case 4:
                return Orientation.DOWN_RIGHT;
            default:
                return Orientation.NONE;
        }
    }

    @Override
    public int getLineLength() {
        return 5;
    }

    @Override
    public String getMetaCode() {
        return "pump-single";
    }

    @Override
    public String toString() {
        return "Pump Single";
    }

}
