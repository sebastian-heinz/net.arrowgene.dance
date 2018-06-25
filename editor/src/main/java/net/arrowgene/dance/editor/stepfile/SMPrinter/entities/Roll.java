package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;

/**
 * An entity that represents the "roll" step in step mania. The "roll" type requires
 * the user to press the button repeatedly while the hold is active.
 *
 * @author Dan
 */

public class Roll extends Hold {
    public Roll(Settings settings, net.arrowgene.dance.editor.stepfile.SMPrinter.models.Step step, int x, int y, int width, int height) {
        super(settings, step, x, y, width, height);
    }
}
