package net.arrowgene.dance.editor.stepfile.SMPrinter.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.SimFileDifficulty;

/**
 * A panel that displays the meta data on the currently selected sim file. Also provides
 * controls to access different difficulties within a sim file.
 *
 * @author Dan
 */

public class SimFileInfoPanel extends BasePanel implements ItemListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JLabel titleLabel;
    private JLabel titleFieldLabel;
    private JLabel subtitleLabel;
    private JLabel subtitleFieldLabel;
    private JLabel artistLabel;
    private JLabel artistFieldLabel;
    private JLabel creditLabel;
    private JLabel creditFieldLabel;
    private JLabel bpmLabel;
    private JLabel bpmFieldLabel;

    private JComboBox<SimFileDifficulty> difficultySelector;
    private DefaultComboBoxModel<SimFileDifficulty> difficultyList;

    public SimFileInfoPanel(MainFrame main) {
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


        titleLabel = new JLabel("Title: ");
        titleFieldLabel = new JLabel();
        addLabelAndField(c, 0, titleLabel, titleFieldLabel);

        subtitleLabel = new JLabel("Subtitle: ");
        subtitleFieldLabel = new JLabel();
        addLabelAndField(c, 1, subtitleLabel, subtitleFieldLabel);

        artistLabel = new JLabel("Artist: ");
        artistFieldLabel = new JLabel();
        addLabelAndField(c, 2, artistLabel, artistFieldLabel);

        creditLabel = new JLabel("Credit: ");
        creditFieldLabel = new JLabel();
        addLabelAndField(c, 3, creditLabel, creditFieldLabel);

        bpmLabel = new JLabel("BPM: ");
        bpmFieldLabel = new JLabel();
        addLabelAndField(c, 4, bpmLabel, bpmFieldLabel);

        c.weighty = 1;
        c.gridy = 5;
        c.gridx = 0;
        c.gridwidth = 2;
        difficultyList = new DefaultComboBoxModel<SimFileDifficulty>();
        difficultySelector = new JComboBox<SimFileDifficulty>(difficultyList);
        difficultySelector.addItemListener(this);
        add(difficultySelector, c);
    }

    private void addLabelAndField(GridBagConstraints c, int row, JLabel label, JLabel field) {
        c.gridx = 0;
        c.gridy = row;
        add(label, c);
        c.gridx = 1;
        add(field, c);
    }


    @Override
    public void notifyCurrentSimFileChanged() {
        updateInfo();
    }

    private void updateInfo() {
        Settings settings = main.getSettings();
        updateAndHideField(titleLabel, titleFieldLabel, settings.simFile.getTitle());
        updateAndHideField(subtitleLabel, subtitleFieldLabel, settings.simFile.getSubtitle());
        updateAndHideField(artistLabel, artistFieldLabel, settings.simFile.getArtist());
        updateAndHideField(creditLabel, creditFieldLabel, settings.simFile.getCredit());
        updateAndHideField(bpmLabel, bpmFieldLabel, settings.simFile.getDisplayBPM());

        difficultyList = new DefaultComboBoxModel<SimFileDifficulty>(settings.simFile.getDifficulties().toArray(new SimFileDifficulty[0]));
        difficultySelector.setModel(difficultyList);
    }

    private void updateAndHideField(JLabel label, JLabel fieldLabel, String text) {
        if (text != null && !text.equals("")) {
            label.setVisible(true);
            fieldLabel.setVisible(true);
            fieldLabel.setText(text);
        } else {
            label.setVisible(false);
            fieldLabel.setVisible(false);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            SimFileDifficulty difficulty = (SimFileDifficulty) event.getItem();
            main.openDifficulty(difficulty);
            System.out.println("opened " + difficulty);
        }
    }

}
