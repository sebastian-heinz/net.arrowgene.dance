package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Resources;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.Step;

/**
 * An entity that represents any of the types of holds in stepmania. Holds are a
 * repeated graphic that extends until the hold is complete. Holds that are ended
 * using the <code>end</code> method have their tails shown as a special graphic.
 *
 * @author Dan
 */

public abstract class Hold extends Entity implements Holdable {
    protected boolean started = false;
    protected boolean ended = false;

    protected net.arrowgene.dance.editor.stepfile.SMPrinter.models.Step step;

    protected BufferedImage body;
    protected BufferedImage end;

    public Hold(Settings settings, Step step, int x, int y, int width, int height) {
        super(settings, x, y, width, height);

        this.step = step;
        body = Resources.getInstance().getProvider(settings.difficulty.getNotesType())
                .getHoldBackgroundImage(step.getType(), step.getOrientation(), step.getTiming());
        end = Resources.getInstance().getProvider(settings.difficulty.getNotesType())
                .getHoldEndBackgroundImage(step.getType(), step.getOrientation(), step.getTiming());
    }

    @Override
    public void extend(int byHeight) {
        height += byHeight;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void end() {
        ended = true;
    }

    @Override
    public void draw(Graphics g) {
        //nothing to draw in foreground
    }

    @Override
    public void drawMidground(Graphics g) {
        //TODO refactor this method to work as expected
        double scaledImageHeight = (double) width / body.getWidth() * body.getHeight();
        double currentX = x;
        double currentY = y;
        double usableHeight = height;
        if (started) {
            currentY += width / 2.0;
            usableHeight -= width / 2.0;
        }
        //not sure why this works: it just does
        if (ended && started) {
            usableHeight -= width / 2.0;
        }

        int numberOfFullImages = (int) (usableHeight / scaledImageHeight);
        for (int i = 0; i < numberOfFullImages; i++) {
            g.drawImage(body, (int) currentX, (int) currentY, (int) currentX + width, (int) (currentY + scaledImageHeight), 0, 0, body.getWidth(), body.getHeight(), null);
            currentY += scaledImageHeight;
        }

        double leftOverHeight = usableHeight - numberOfFullImages * scaledImageHeight;
        double fractionHeight = leftOverHeight / scaledImageHeight;
        g.drawImage(body, (int) currentX, (int) currentY, (int) currentX + width, (int) (currentY + leftOverHeight), 0, 0, body.getWidth(), (int) (body.getHeight() * fractionHeight), null);
        currentY += leftOverHeight;

        if (ended) {
            g.drawImage(end, (int) currentX, (int) currentY, (int) currentX + width, (int) (currentY + scaledImageHeight), 0, 0, end.getWidth(), end.getHeight(), null);
        }
    }

    @Override
    public void drawBackground(Graphics g) {
        //nothing to draw in background
    }
}
