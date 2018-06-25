package net.arrowgene.dance.editor.stepfile.SMPrinter.utilities;

import java.awt.image.BufferedImage;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Timing;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Type;

/**
 * A resource provider for assets of the dance game mode.
 *
 * @author Dan
 */

public class DanceResourceProvider implements ResourceProvider {

    private BufferedImage freezeBody;
    private BufferedImage freezeEnd;
    private BufferedImage rollBody;
    private BufferedImage rollEnd;
    private BufferedImage mine;

    private BufferedImage[] steps;

    private boolean isLoaded = false;
    private Resources parent;

    public DanceResourceProvider(Resources parent) {
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

        BufferedImage allNotes = Resources.loadImage(notesDir + "notes.png");
        int stepDim = allNotes.getWidth();
        steps = Resources.getImageSubImages(allNotes, stepDim);

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
                        return steps[0];
                    case L8TH:
                        return steps[1];
                    case L12TH:
                        return steps[2];
                    case L16TH:
                        return steps[3];
                    case L24TH:
                        return steps[4];
                    case L32ND:
                        return steps[5];
                    case L48TH:
                        return steps[6];
                    default:
                        return steps[7];
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
