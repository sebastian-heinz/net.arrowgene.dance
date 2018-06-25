package net.arrowgene.dance.editor.stepfile.SMPrinter.entities;

/**
 * An object that represents a step that is held. Holds have starts, ends and
 * can be extends as the sim file is parsed.
 *
 * @author Dan
 */

public interface Holdable {
    public void extend(int byHeight);

    public void start();

    public void end();
}
