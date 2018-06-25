package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Resources;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Type;

/**
 * An entity representing a single step on a step chart.
 *
 * @author Dan
 */

public class Step extends Entity {
    protected net.arrowgene.dance.editor.stepfile.SMPrinter.models.Step step;

    //same thing as super.width; just a more intuitive name
    protected int sideLength;

    protected Hold hold;
    protected Hold[] currentHolds;
    protected int holdIndex;

    protected BufferedImage stepImage;

    public Step(Settings settings, net.arrowgene.dance.editor.stepfile.SMPrinter.models.Step step, Hold[] currentHolds, int holdIndex,
                int x, int y, int sideLength, int lineHeight) {
        super(settings, x, y, sideLength, lineHeight);

        this.step = step;
        this.currentHolds = currentHolds;
        this.holdIndex = holdIndex;
        this.sideLength = sideLength;

        this.stepImage = Resources.getInstance().getProvider(settings.difficulty.getNotesType()).getStepImage(
                step.getType(), step.getOrientation(), step.getTiming());

        updateHolds();
    }

    private void updateHolds() {
        //TODO clean up this atrocity
        if (step.getType() == Type.ROLL_START) {
            hold = new Roll(settings, step, x, y, width, height);
            hold.start();
            currentHolds[holdIndex] = hold;
        } else if (step.getType() == Type.FREEZE_START) {
            hold = new Freeze(settings, step, x, y, width, height);
            hold.start();
            currentHolds[holdIndex] = hold;
        } else if (step.getType() == Type.ROLL_END
                || step.getType() == Type.FREEZE_END) {
            if (currentHolds[holdIndex] != null) {
                currentHolds[holdIndex].extend(height);
                currentHolds[holdIndex].end();
                currentHolds[holdIndex] = null;
            }
        } else if (step.getType() == Type.ROLLING
                || step.getType() == Type.HOLDING) {
            if (currentHolds[holdIndex] != null) {
                currentHolds[holdIndex].extend(height);
            } else {
                if (step.getType() == Type.ROLLING) {
                    hold = new Roll(settings, step, x, y, width, height);
                    currentHolds[holdIndex] = hold;
                } else if (step.getType() == Type.HOLDING) {
                    hold = new Freeze(settings, step, x, y, width, height);
                    currentHolds[holdIndex] = hold;
                }
            }
        }
    }


    @Override
    public void draw(Graphics g) {
        if (stepImage != null) {
            double imageWidth = (double) stepImage.getWidth();
            double imageHeight = (double) stepImage.getHeight();

            AffineTransform at = new AffineTransform();
            at.translate(x + sideLength / 2, y + sideLength / 2);
            at.rotate(step.getOrientation().getRotationAngle());
            at.scale(sideLength / imageWidth, sideLength / imageHeight);
            at.translate(-imageWidth / 2, -imageHeight / 2);

            ((Graphics2D) g).drawImage(stepImage, at, null);
        }
        if (hold != null) {
            hold.draw(g);
        }
    }

    @Override
    public void drawMidground(Graphics g) {
        if (hold != null) {
            hold.drawMidground(g);
        }
    }

    @Override
    public void drawBackground(Graphics g) {
        //nothing to draw in background
    }
}
