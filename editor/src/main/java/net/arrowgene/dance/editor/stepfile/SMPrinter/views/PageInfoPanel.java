package net.arrowgene.dance.editor.stepfile.SMPrinter.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A panel provides controls to change the page metrics of a step chart, as well
 * as displaying information about the page setup of the step chart.
 *
 * @author Dan
 */

public class PageInfoPanel extends BasePanel implements ChangeListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final static int MAX_COLUMNS_PER_PAGE = 6;
    private final static int MIN_COLUMNS_PER_PAGE = 2;
    private final static int MAX_MEASURES_PER_COLUMN = 10;
    private final static int MIN_MEASURES_PER_COLUMN = 1;

    private JLabel pagesLabel;
    private JLabel pagesFieldLabel;

    private JLabel columnsLabel;
    private JSpinner columnsSpinner;

    private JLabel measuresLabel;
    private JSpinner measuresSpinner;

    public PageInfoPanel(MainFrame main) {
        super(main);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.ipady = 10;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        c.gridx = 0;
        c.gridy = 0;
        columnsLabel = new JLabel("Columns per page: ");
        add(columnsLabel, c);

        c.gridx = 1;
        columnsSpinner = new JSpinner();
        SpinnerModel spinnerModel = new SpinnerNumberModel(settings.columnsPerPage, MIN_COLUMNS_PER_PAGE, MAX_COLUMNS_PER_PAGE, 1);
        columnsSpinner.setModel(spinnerModel);
        columnsSpinner.addChangeListener(this);
        add(columnsSpinner, c);

        c.gridx = 0;
        c.gridy = 1;
        measuresLabel = new JLabel("Measure per column: ");
        add(measuresLabel, c);

        c.gridx = 1;
        measuresSpinner = new JSpinner();
        spinnerModel = new SpinnerNumberModel(settings.measuresPerColumn, MIN_MEASURES_PER_COLUMN, MAX_MEASURES_PER_COLUMN, 1);
        measuresSpinner.setModel(spinnerModel);
        measuresSpinner.addChangeListener(this);
        add(measuresSpinner, c);

        c.gridx = 0;
        c.gridy = 2;
        pagesLabel = new JLabel("Total pages: ");
        add(pagesLabel, c);

        c.gridx = 1;
        pagesFieldLabel = new JLabel();
        add(pagesFieldLabel, c);
    }

    @Override
    public void notifyCurrentSimFileChanged() {
        updateInfo();
    }

    @Override
    public void notifyCurrentDifficultyChanged() {

    }

    private void updateInfo() {
        pagesFieldLabel.setText(Integer.toString(settings.getNumberOfPages()));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        settings.measuresPerColumn = (Integer) measuresSpinner.getModel().getValue();
        settings.columnsPerPage = (Integer) columnsSpinner.getModel().getValue();
        updateInfo();
        main.notifyPageDimensionsChanged();
    }


}
