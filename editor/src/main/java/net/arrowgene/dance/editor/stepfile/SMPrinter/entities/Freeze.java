package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;

/**
 * An entity that represents the "freeze" step type in step mania. The "freeze" step type is a
 * hold where the user must hold the button down throughout the hold.
 *
 * @author Dan
 */

public class Freeze extends Hold {
    public Freeze(Settings settings, net.arrowgene.dance.editor.stepfile.SMPrinter.models.Step step, int x, int y, int width, int height) {
        super(settings, step, x, y, width, height);
    }
}
