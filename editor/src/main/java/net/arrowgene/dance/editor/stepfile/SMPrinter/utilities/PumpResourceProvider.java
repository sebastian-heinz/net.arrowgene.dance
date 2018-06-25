package net.arrowgene.dance.editor.stepfile.SMPrinter.utilities;

import java.awt.image.BufferedImage;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Timing;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Type;

/**
 * A resource provider for the pump game mode.
 *
 * @author Dan
 */

public class PumpResourceProvider implements ResourceProvider {

    private BufferedImage freezeBody;
    private BufferedImage freezeEnd;
    private BufferedImage rollBody;
    private BufferedImage rollEnd;

    private BufferedImage[][] steps;
    //
    private BufferedImage mine;

    private boolean isLoaded = false;
    private Resources parent;

    public PumpResourceProvider(Resources parent) {
        this.parent = parent;
    }

    @Override
    public void loadImagesIfNotLoaded() {
        final String notesDir = parent.notesDir;

        if (isLoaded) {
            return;
        }

        freezeBody = Resources.loadImage(notesDir + "hold.png");
        freezeEnd = Resources.loadImage(notesDir + "hold_cap_bottom.png");
        rollBody = Resources.loadImage(notesDir + "roll.png");
        rollEnd = Resources.loadImage(notesDir + "roll_cap_bottom.png");
        mine = Resources.loadImage(notesDir + "mine.png");

        BufferedImage allNotes = Resources.loadImage(notesDir + "pump_notes.png");
        int stepDim = allNotes.getWidth() / 2;
        steps = Resources.getImageSubImages(allNotes, stepDim, stepDim);

        isLoaded = true;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public BufferedImage getStepImage(Type type, Orientation orientation, Timing timing) {
        switch (type) {
            //return normal step image
            case REGULAR:
            case FREEZE_START:
            case ROLL_START:
                switch (timing) {
                    case L1ST:
                    case L4TH:
                        return (orientation == Orientation.CENTER) ? steps[0][0] : steps[0][1];
                    case L8TH:
                        return (orientation == Orientation.CENTER) ? steps[1][0] : steps[1][1];
                    case L12TH:
                        return (orientation == Orientation.CENTER) ? steps[2][0] : steps[2][1];
                    case L16TH:
                        return (orientation == Orientation.CENTER) ? steps[3][0] : steps[3][1];
                    case L24TH:
                        return (orientation == Orientation.CENTER) ? steps[4][0] : steps[4][1];
                    case L32ND:
                        return (orientation == Orientation.CENTER) ? steps[5][0] : steps[5][1];
                    case L48TH:
                        return (orientation == Orientation.CENTER) ? steps[6][0] : steps[6][1];
                    default:
                        return (orientation == Orientation.CENTER) ? steps[7][0] : steps[7][1];
                }
                //mine image
            case MINE:
                return mine;
            default:
                return null;
        }
    }

    @Override
    public BufferedImage getHoldBackgroundImage(Type type, Orientation orientation, Timing timing) {
        switch (type) {
            case FREEZE_START:
            case HOLDING:
            case FREEZE_END:
                return freezeBody;
            case ROLL_START:
            case ROLLING:
            case ROLL_END:
                return rollBody;
            default:
                return empty;
        }
    }

    @Override
    public BufferedImage getHoldEndBackgroundImage(Type type, Orientation orientation, Timing timing) {
        switch (type) {
            case FREEZE_START:
            case HOLDING:
            case FREEZE_END:
                return freezeEnd;
            case ROLL_START:
            case ROLLING:
            case ROLL_END:
                return rollEnd;
            default:
                return empty;
        }
    }

}
