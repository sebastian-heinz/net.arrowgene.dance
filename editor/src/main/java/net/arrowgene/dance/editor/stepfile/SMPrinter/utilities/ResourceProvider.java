package net.arrowgene.dance.editor.stepfile.SMPrinter.utilities;

import java.awt.image.BufferedImage;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Orientation;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Timing;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.Type;

/**
 * An object that provides assets to steps that request them. The step does not care what
 * type of game mode it exists in, and the decision on what image to provide is left to
 * each implementation of the resource provider. By providing the step type, orientation
 * and timing, the resource provider can return the proper image.
 *
 * @author Dan
 */

public interface ResourceProvider {
    static BufferedImage empty = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    public void loadImagesIfNotLoaded();

    public boolean isLoaded();

    public BufferedImage getStepImage(Type type, Orientation orientation, Timing timing);

    public BufferedImage getHoldBackgroundImage(Type type, Orientation orientation, Timing timing);

    public BufferedImage getHoldEndBackgroundImage(Type type, Orientation orientation, Timing timing);
}
