package net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;

/**
 * Implementation of NotesType for the pump couple game mode. (10 buttons, 2 players)
 *
 * @author Dan
 */

public class PumpCouple extends NotesType {

    public PumpCouple(GameMode playType) {
        super(playType);
    }

    @Override
    public Orientation getStepOrientation(int lineIndex) {
        switch (lineIndex) {
            case 0:
            case 5:
                return Orientation.DOWN_LEFT;
            case 1:
            case 6:
                return Orientation.UP_LEFT;
            case 2:
            case 7:
                return Orientation.CENTER;
            case 3:
            case 8:
                return Orientation.UP_RIGHT;
            case 4:
            case 9:
                return Orientation.DOWN_RIGHT;
            default:
                return Orientation.NONE;
        }
    }

    @Override
    public int getLineLength() {
        return 10;
    }

    @Override
    public String getMetaCode() {
        return "pump-couple";
    }

    @Override
    public String toString() {
        return "Pump Couple";
    }

}
