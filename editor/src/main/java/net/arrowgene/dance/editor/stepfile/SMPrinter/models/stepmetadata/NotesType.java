package net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes.DanceCouple;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes.DanceDouble;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes.DanceSingle;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes.DanceSolo;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes.PumpCouple;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes.PumpDouble;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.notestypes.PumpSingle;

/**
 * Deals with the individual requirements for each game mode. All supported NotesTypes are
 * subclasses from this class, and implement their own methods for dealing with orientation
 * and line length.
 *
 * @author Dan
 */

public abstract class NotesType implements Comparable<NotesType> {
    protected static final NotesType[] notesTypes = new NotesType[]{
            new DanceSingle(GameMode.DANCE),
            new DanceDouble(GameMode.DANCE),
            new DanceCouple(GameMode.DANCE),
            new DanceSolo(GameMode.DANCE),
            new PumpSingle(GameMode.PUMP),
            new PumpDouble(GameMode.PUMP),
            new PumpCouple(GameMode.PUMP),
    };

    public static enum GameMode {DANCE, PUMP}

    ;

    public static NotesType fromMetaCode(String name) {
        for (NotesType notesType : notesTypes) {
            if (name.equals(notesType.getMetaCode())) {
                return notesType;
            }
        }
        System.err.println("The specified dance type could not be found! (" + name + ")");
        return null;
    }

    public NotesType(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    protected GameMode gameMode;

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public int compareTo(NotesType notesType) {
        return toString().compareTo(notesType.toString());
    }

    public abstract String getMetaCode(); //code found inside the simfile

    public abstract Orientation getStepOrientation(int stepIndex);

    public abstract int getLineLength();
}
