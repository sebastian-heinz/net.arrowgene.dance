package net.arrowgene.dance.editor.stepfile.SMPrinter.views;

import javax.swing.JPanel;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;

/**
 * A base class that all panels within the application extend. Contains notification
 * methods that are used when certain states are changed.
 *
 * @author Dan
 */

public abstract class BasePanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected MainFrame main;
    protected Settings settings;

    public BasePanel(MainFrame main) {
        this.main = main;
        this.settings = main.getSettings();
    }

    public void notifyCurrentSimFileChanged() {

    }

    public void notifyCurrentDifficultyChanged() {

    }

    public void notifyPageDimensionsChanged() {

    }
}
