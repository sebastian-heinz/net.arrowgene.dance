package net.arrowgene.dance.editor.stepfile.SMPrinter.views;

import java.awt.BorderLayout;

import javax.swing.border.EmptyBorder;

/**
 * A panel that contains info about a selected sim file.
 *
 * @author Dan
 */

public class SelectionInfoPanel extends BasePanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private PageInfoPanel pageInfoPanel;
    private SimFileInfoPanel stepFileInfoPanel;

    public SelectionInfoPanel(MainFrame main) {
        super(main);

        pageInfoPanel = new PageInfoPanel(main);
        pageInfoPanel.setBorder(new EmptyBorder(10, 10, 5, 10));

        stepFileInfoPanel = new SimFileInfoPanel(main);
        stepFileInfoPanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        setLayout(new BorderLayout());
        add(stepFileInfoPanel, BorderLayout.NORTH);
        add(pageInfoPanel, BorderLayout.SOUTH);
    }

    @Override
    public void notifyCurrentSimFileChanged() {
        pageInfoPanel.notifyCurrentSimFileChanged();
        stepFileInfoPanel.notifyCurrentSimFileChanged();
    }
}
