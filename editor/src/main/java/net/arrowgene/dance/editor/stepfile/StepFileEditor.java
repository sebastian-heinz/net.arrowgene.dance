/*
 * This file is part of net.arrowgene.dance.
 *
 * net.arrowgene.dance is a server implementation for the game "Dance! Online".
 * Copyright (C) 2013-2018  Sebastian Heinz (github: sebastian-heinz)
 * Copyright (C) 2013-2018  Daniel Neuendorf
 *
 * Github: https://github.com/Arrowgene/net.arrowgene.dance
 * Web: https://arrowgene.net
 *
 * net.arrowgene.dance is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * net.arrowgene.dance is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.arrowgene.dance.editor.stepfile;

import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;
import net.arrowgene.dance.library.crypto.GNDecrypt;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.library.models.stepfile.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class StepFileEditor extends EditorFrame implements ActionListener {

    private static String musicPath = "C:\\dance\\music";

    private JPanel panelMain;
    private JComboBox<String> comboBoxStepFiles;
    private JButton buttonEncrypt;
    private JTextField textFieldWriter;
    private JTextField textFieldProducer;
    private JCheckBox checkBoxUpdateDb;
    private JTextField textFieldTitle;
    private JButton buttonSaveDecrypt;
    private JTextField textFieldFileName;
    private JRadioButton radioButtonEasy;
    private JRadioButton radioButtonNormal;
    private JRadioButton radioButtonHard;
    private JTextField textFieldLevel;
    private JTextField textFieldNoteCount;
    private JTextField textFieldFileType;
    private JTextField textFieldDuration;
    private JTextField textFieldAddress;
    private JScrollPane scrollPaneValues;
    private JTextField textFieldMeasurements;
    private JTextField textFieldAddressEnd;
    private JTextField textFieldFileId;
    private JTextField textFieldUnknown0;
    private JTextField textFieldUnknown1;
    private JTextField textFieldUnknown2;
    private JTextField textFieldUnknown3;
    private JTextField textFieldUnknown4;
    private JTextField textFieldUnknown5;
    private JTextField textFieldUnknown6;
    private JTextField textFieldUnknown7;
    private JTextField textFieldUnknown8;
    private JTextField textFieldUnknown9;
    private JTextField textFieldUnknown10;
    private JTextField textFieldUnknown19;
    private JTextField textFieldUnknown18;
    private JTextField textFieldUnknown17;
    private JTextField textFieldUnknown16;
    private JTextField textFieldUnknown12;
    private JTextField textFieldUnknown11;
    private JTextField textFieldUnknownString0;
    private JTextField textFieldUnknownString1;
    private JButton buttonToggleUnknown;
    private JScrollPane scrollPaneUnknown;
    private JButton buttonToggleFileDetails;
    private JPanel panelFileDetails;
    private JPanel panelNotes;
    private JTabbedPane tabbedPaneView;
    private JPanel panelVisual;
    private JScrollPane scrollPaneVisual;
    private JButton buttonplay;
    private JButton buttonStop;
    private JPanel panelNotesValues;
    private Timer timerPlay;

    private GNDecrypt gnDecrypt;
    private StepFile stepFile;
    private StepFileDifficultType lastSelectedDifficult;
    private ArrayList<StepFrameView> currentStepFrameViews;

    public StepFileEditor(EditorConfig config) {
        super(config, "Stepfile Editor");
    }

    private StepFileDifficultType getSelectedDifficult() {
        if (this.radioButtonEasy.isSelected()) {
            return StepFileDifficultType.Easy;
        } else if (this.radioButtonNormal.isSelected()) {
            return StepFileDifficultType.Normal;
        } else {
            return StepFileDifficultType.Hard;
        }
    }

    @Override
    public void menuClicked() {
        this.setVisible(true);
        this.updateComboBoxStepFiles();
    }

    @Override
    public void init() {
        this.currentStepFrameViews = new ArrayList<StepFrameView>();

        this.pack();
        this.setMinimumSize(new Dimension(600, 400));
        this.setSize(new Dimension(600, 400));
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setLocation(0, 0);

        //   this.panelMain = new JPanel();
        this.add(panelMain);

        this.panelNotesValues = new JPanel();
        this.panelNotesValues.setLayout(new GridLayout(0, 1));
        this.panelNotesValues.setAutoscrolls(true);

        //  this.scrollPaneValues = new JScrollPane();
        this.scrollPaneValues.setViewportView(this.panelNotesValues);

        this.timerPlay = new Timer(1000, this);
        this.timerPlay.setActionCommand("play_scroll_timer");

        // this.buttonStop = new JButton();
        this.buttonStop.setActionCommand("stop");
        this.buttonStop.addActionListener(this);

        //  this.buttonToggleUnknown = new JButton();
        this.buttonToggleUnknown.setActionCommand("toggle_unknown");
        this.buttonToggleUnknown.addActionListener(this);

        //  this.buttonSaveDecrypt = new JButton();
        this.buttonSaveDecrypt.setActionCommand("save_decrypt");
        this.buttonSaveDecrypt.addActionListener(this);

        //  this.buttonEncrypt = new JButton();
        this.buttonEncrypt.setActionCommand("encrypt");
        this.buttonEncrypt.addActionListener(this);

        //  this.comboBoxStepFiles = new JComboBox<>();
        this.comboBoxStepFiles.setActionCommand("selection_changed");
        this.comboBoxStepFiles.addActionListener(this);

        //  this.radioButtonEasy = new JRadioButton();
        this.radioButtonEasy.setActionCommand("difficult_changed");
        this.radioButtonEasy.addActionListener(this);

        //  this.radioButtonNormal = new JRadioButton();
        this.radioButtonNormal.setActionCommand("difficult_changed");
        this.radioButtonNormal.addActionListener(this);

        //  this.radioButtonHard = new JRadioButton();
        this.radioButtonHard.setActionCommand("difficult_changed");
        this.radioButtonHard.addActionListener(this);

        //   this.buttonToggleFileDetails = new JButton();
        this.buttonToggleFileDetails.setActionCommand("toggle_file_details");
        this.buttonToggleFileDetails.addActionListener(this);

        //   this.buttonplay = new JButton();
        this.buttonplay.setActionCommand("play");
        this.buttonplay.addActionListener(this);

        //  this.tabbedPaneView = new JTabbedPane();
        this.tabbedPaneView.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPaneView.getSelectedIndex() != 0) {
                    scrollPaneVisual.setViewportView(new StepFileVisual(stepFile));
                }
            }
        });

        this.gnDecrypt = new GNDecrypt();
        this.lastSelectedDifficult = this.getSelectedDifficult();

        super.init();
    }

    @Override
    public String getMenuCategory() {
        return "Tools";
    }

    private void updateComboBoxStepFiles() {
        File[] files = new File(musicPath).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".gn");
            }
        });

        for (File file : files) {
            if (file.isFile()) {
                int songFileId = this.getSongFileId(file.getName());
                if (getSongByFileId(songFileId) != null) {
                    comboBoxStepFiles.addItem(file.getName());
                }
            }
        }
    }

    private Song getSongByFileId(int fileId) {
        Song song = null;
        /*        for (Song s : this.gnDecrypt.getSongs()) {
            if (s.getFileId() == fileId) {
                song = s;
                break;
            }
        }*/
        return song;
    }

    private void updateStepFileView() {

        if (this.stepFile != null) {

            this.panelNotesValues.removeAll();
            this.currentStepFrameViews.clear();
            this.lastSelectedDifficult = this.getSelectedDifficult();

            this.textFieldUnknownString0.setText(this.stepFile.getUnknownString0());
            this.textFieldUnknownString1.setText(this.stepFile.getUnknownString0());

            this.textFieldUnknown0.setText(String.valueOf(this.stepFile.getUnknown0()));
            this.textFieldUnknown1.setText(String.valueOf(this.stepFile.getUnknown1()));
            this.textFieldUnknown2.setText(String.valueOf(this.stepFile.getUnknown2()));
            this.textFieldUnknown3.setText(String.valueOf(this.stepFile.getUnknown3()));
            this.textFieldUnknown4.setText(String.valueOf(this.stepFile.getUnknown4()));
            this.textFieldUnknown5.setText(String.valueOf(this.stepFile.getUnknown5()));
            this.textFieldUnknown6.setText(String.valueOf(this.stepFile.getUnknown6()));
            this.textFieldUnknown7.setText(String.valueOf(this.stepFile.getUnknown7()));
            this.textFieldUnknown8.setText(String.valueOf(this.stepFile.getUnknown8()));
            this.textFieldUnknown9.setText(String.valueOf(this.stepFile.getUnknown9()));
            this.textFieldUnknown10.setText(String.valueOf(this.stepFile.getUnknown10()));
            this.textFieldUnknown11.setText(String.valueOf(this.stepFile.getUnknown11()));
            this.textFieldUnknown12.setText(String.valueOf(this.stepFile.getUnknown12()));
            this.textFieldUnknown16.setText(String.valueOf(this.stepFile.getUnknown16()));
            this.textFieldUnknown17.setText(String.valueOf(this.stepFile.getUnknown17()));
            this.textFieldUnknown18.setText(String.valueOf(this.stepFile.getUnknown18()));
            this.textFieldUnknown19.setText(String.valueOf(this.stepFile.getUnknown19()));

            this.textFieldFileId.setText(String.valueOf(this.stepFile.getFileId()));
            this.textFieldProducer.setText(this.stepFile.getProducer());
            this.textFieldWriter.setText(this.stepFile.getWriter());
            this.textFieldTitle.setText(this.stepFile.getTitle());
            this.textFieldFileName.setText(this.stepFile.getFileName());
            this.textFieldFileType.setText(this.stepFile.getFileType());
            this.textFieldAddressEnd.setText(String.valueOf(this.stepFile.getAddressEnd()));

            if (this.lastSelectedDifficult == StepFileDifficultType.Easy) {
                this.textFieldAddress.setText(String.valueOf(this.stepFile.getAddressEasy()));
                this.textFieldDuration.setText(String.valueOf(this.stepFile.getDurationEasy()));
                this.textFieldNoteCount.setText(String.valueOf(this.stepFile.getNoteCountEasy()));
                this.textFieldLevel.setText(String.valueOf(this.stepFile.getLevelEasy()));
                this.textFieldMeasurements.setText(String.valueOf(this.stepFile.getMeasurementsEasy()));

                for (StepFrame sFrame : this.stepFile.getFramesEasy()) {
                    StepFrameView stepFrameView = new StepFrameView(sFrame);
                    this.panelNotesValues.add(stepFrameView);
                    this.currentStepFrameViews.add(stepFrameView);
                }
            } else if (this.lastSelectedDifficult == StepFileDifficultType.Normal) {
                this.textFieldAddress.setText(String.valueOf(this.stepFile.getAddressNormal()));
                this.textFieldDuration.setText(String.valueOf(this.stepFile.getDurationNormal()));
                this.textFieldNoteCount.setText(String.valueOf(this.stepFile.getNoteCountNormal()));
                this.textFieldLevel.setText(String.valueOf(this.stepFile.getLevelNormal()));
                this.textFieldMeasurements.setText(String.valueOf(this.stepFile.getMeasurementsNormal()));

                for (StepFrame sFrame : this.stepFile.getFramesNormal()) {
                    StepFrameView stepFrameView = new StepFrameView(sFrame);
                    this.panelNotesValues.add(stepFrameView);
                    this.currentStepFrameViews.add(stepFrameView);
                }
            } else {
                this.textFieldAddress.setText(String.valueOf(this.stepFile.getAddressHard()));
                this.textFieldDuration.setText(String.valueOf(this.stepFile.getDurationHard()));
                this.textFieldNoteCount.setText(String.valueOf(this.stepFile.getNoteCountHard()));
                this.textFieldLevel.setText(String.valueOf(this.stepFile.getLevelHard()));
                this.textFieldMeasurements.setText(String.valueOf(this.stepFile.getMeasurementsHard()));

                for (StepFrame sFrame : this.stepFile.getFramesHard()) {
                    StepFrameView stepFrameView = new StepFrameView(sFrame);
                    this.panelNotesValues.add(stepFrameView);
                    this.currentStepFrameViews.add(stepFrameView);
                }
            }
        }
        this.updateUI();
    }

    private void updateStepFile(StepFileDifficultType difficultType) {

        if (this.stepFile != null) {

            this.stepFile.setFileId(Integer.valueOf(this.textFieldFileId.getText()));
            this.stepFile.setProducer(this.textFieldProducer.getText());
            this.stepFile.setWriter(this.textFieldWriter.getText());
            this.stepFile.setTitle(this.textFieldTitle.getText());
            this.stepFile.setFileName(this.textFieldFileName.getText());
            this.stepFile.setFileType(this.textFieldFileType.getText());
            this.stepFile.setAddressEnd(Integer.valueOf(this.textFieldAddressEnd.getText()));

            if (difficultType == StepFileDifficultType.Easy) {
                this.stepFile.setAddressEasy(Integer.valueOf(this.textFieldAddress.getText()));
                this.stepFile.setDurationEasy(Integer.valueOf(this.textFieldDuration.getText()));
                this.stepFile.setNoteCountEasy(Integer.valueOf(this.textFieldNoteCount.getText()));
                this.stepFile.setLevelEasy(Integer.valueOf(this.textFieldLevel.getText()));
                this.stepFile.setMeasurementsEasy(Integer.valueOf(this.textFieldMeasurements.getText()));

            } else if (difficultType == StepFileDifficultType.Normal) {
                this.stepFile.setAddressNormal(Integer.valueOf(this.textFieldAddress.getText()));
                this.stepFile.setDurationNormal(Integer.valueOf(this.textFieldDuration.getText()));
                this.stepFile.setNoteCountNormal(Integer.valueOf(this.textFieldNoteCount.getText()));
                this.stepFile.setLevelNormal(Integer.valueOf(this.textFieldLevel.getText()));
                this.stepFile.setMeasurementsNormal(Integer.valueOf(this.textFieldMeasurements.getText()));

            } else {
                this.stepFile.setAddressHard(Integer.valueOf(this.textFieldAddress.getText()));
                this.stepFile.setDurationHard(Integer.valueOf(this.textFieldDuration.getText()));
                this.stepFile.setNoteCountHard(Integer.valueOf(this.textFieldNoteCount.getText()));
                this.stepFile.setLevelHard(Integer.valueOf(this.textFieldLevel.getText()));
                this.stepFile.setMeasurementsHard(Integer.valueOf(this.textFieldMeasurements.getText()));

            }

            for (StepFrameView stepFrameView : this.currentStepFrameViews) {
                stepFrameView.writeValues();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("encrypt")) {
            this.encrypt();
        } else if (e.getActionCommand().equals("decrypt")) {
            this.decrypt();
        } else if (e.getActionCommand().equals("selection_changed")) {
            this.decrypt();
        } else if (e.getActionCommand().equals("save_decrypt")) {
            this.saveDecrypt();
        } else if (e.getActionCommand().equals("difficult_changed")) {
            this.updateStepFile(this.lastSelectedDifficult);
            this.updateStepFileView();
        } else if (e.getActionCommand().equals("toggle_unknown")) {
            if (this.scrollPaneUnknown.isVisible()) {
                this.scrollPaneUnknown.setVisible(false);
            } else {
                this.scrollPaneUnknown.setVisible(true);
            }
            this.updateUI();
        } else if (e.getActionCommand().equals("toggle_file_details")) {
            if (this.panelFileDetails.isVisible()) {
                this.panelFileDetails.setVisible(false);
            } else {
                this.panelFileDetails.setVisible(true);
            }
            this.updateUI();
        } else if (e.getActionCommand().equals("play")) {
            this.timerPlay.setDelay(100);
            this.timerPlay.start();
        } else if (e.getActionCommand().equals("play_scroll_timer")) {
            JScrollBar scrollBar = this.scrollPaneVisual.getVerticalScrollBar();
            int a = scrollBar.getValue() + 10;
            scrollBar.setValue(a);
        } else if (e.getActionCommand().equals("stop")) {
            this.timerPlay.stop();
        }

    }

    private void saveDecrypt() {
        File file = this.getSelectedFile();
        int songFileId = this.getSongFileId(file.getName());
        Song song = this.getSongByFileId(songFileId);
        StepFileType stepFileType = StepFileType.getTypeByFileName(file.getName());
        //this.gnDecrypt.decryptToDisk(file.getAbsolutePath(), song.getSongId(), stepFileType);
    }

    private void decrypt() {
        File file = this.getSelectedFile();
        int songFileId = this.getSongFileId(file.getName());
        Song song = this.getSongByFileId(songFileId);
        StepFileType stepFileType = StepFileType.getTypeByFileName(file.getName());
        /*
        if (song.getFileId() > -1 && stepFileType != null) {
            byte[] data = this.gnDecrypt.decryptGN(file.getAbsolutePath(), song.getSongId(), stepFileType);
            if (data != null) {
                this.stepFile = new StepFile(data, song.getSongId(), stepFileType);
                this.updateStepFileView();
            }
        }
        */
    }

    private void encrypt() {
        /*
        if (this.stepFile != null) {
            this.updateStepFile(this.getSelectedDifficult());
            this.gnDecrypt.encryptGN(this.getSelectedFile().getAbsolutePath(), this.stepFile.getStepFile(), this.stepFile.getSongId(), this.stepFile.getStepFileType(), this.checkBoxUpdateDb.isSelected());
            JOptionPane.showMessageDialog(this, "File written!", "File Written", JOptionPane.INFORMATION_MESSAGE);
        }
        */
    }

    private int getSongFileId(String fileName) {
        fileName = fileName.replaceAll("[^-?0-9]+", "");
        int songId = -1;
        try {
            songId = Integer.parseInt(fileName);
        } catch (Exception ex) {

        }
        return songId;
    }

    private File getSelectedFile() {
        String fileName = String.valueOf(this.comboBoxStepFiles.getSelectedItem());
        return new File(musicPath, fileName);
    }

    private class StepFrameView extends JPanel {

        private boolean isReady;
        private StepFrame stepFrame;
        private JTextField textFieldSecond;
        private JTextField textFieldType;
        private JTextField textFieldInterval;
        private JButton buttonAddNote;
        private JComboBox<ComboBoxIntervalItem> comboBoxAddNote;
        private ArrayList<StepNoteView> stepNoteViews;
        private boolean comboBoxAddNoteAddNoteCanRaiseSelectionChanged;

        public StepFrameView(final StepFrame stepFrame) {
            this.isReady = false;
            this.stepFrame = stepFrame;
            this.comboBoxAddNoteAddNoteCanRaiseSelectionChanged = true;
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            this.stepNoteViews = new ArrayList<StepNoteView>();

            this.textFieldSecond = new JTextField();
            this.textFieldSecond.setMaximumSize(new Dimension(35, this.textFieldSecond.getMinimumSize().height));
            this.textFieldSecond.setPreferredSize(new Dimension(35, this.textFieldSecond.getMinimumSize().height));
            this.textFieldSecond.setText(String.valueOf(stepFrame.getMeasurement()));
            this.add(textFieldSecond);

            this.textFieldType = new JTextField();
            this.textFieldType.setMaximumSize(new Dimension(35, this.textFieldType.getMinimumSize().height));
            this.textFieldType.setPreferredSize(new Dimension(35, this.textFieldType.getMinimumSize().height));
            this.textFieldType.setText(String.valueOf(stepFrame.getStepFrameType()));
            this.add(textFieldType);

            this.textFieldInterval = new JTextField();
            this.textFieldInterval.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (textFieldInterval.getText().length() > 0 && isReady) {
                        changeInterval();
                    }
                }
            });
            textFieldInterval.setMaximumSize(new Dimension(35, textFieldInterval.getMinimumSize().height));
            textFieldInterval.setPreferredSize(new Dimension(35, textFieldInterval.getMinimumSize().height));
            this.textFieldInterval.setText(String.valueOf(stepFrame.getInterval()));
            this.add(textFieldInterval);

            this.buttonAddNote = new JButton();
            this.buttonAddNote.setText("+♫");
            this.buttonAddNote.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int nextInterval = stepFrame.getNextFreeIntervalPart();
                    if (stepFrame.canAddNote(nextInterval)) {
                        comboBoxAddNoteAddNoteCanRaiseSelectionChanged = false;
                        comboBoxAddNote.removeAllItems();
                        int interval = stepFrame.getInterval();
                        for (int i = 0; i < interval; i++) {
                            if (stepFrame.TryGetStepNote(i) == null) {
                                ComboBoxIntervalItem item = new ComboBoxIntervalItem(i, interval);
                                comboBoxAddNote.addItem(item);
                            }
                        }
                        int count = comboBoxAddNote.getItemCount();
                        for (int i = 0; i < count; i++) {
                            ComboBoxIntervalItem selectedItem = (ComboBoxIntervalItem) comboBoxAddNote.getItemAt(i);
                            if (selectedItem.getIntervalPart() == nextInterval) {
                                comboBoxAddNote.setSelectedItem(selectedItem);
                                break;
                            }
                        }
                        comboBoxAddNote.setVisible(true);
                        buttonAddNote.setVisible(false);
                        comboBoxAddNoteAddNoteCanRaiseSelectionChanged = true;
                    }
                }
            });
            this.add(buttonAddNote);

            this.comboBoxAddNote = new JComboBox<ComboBoxIntervalItem>();
            this.comboBoxAddNote.setMaximumSize(new Dimension(75, this.comboBoxAddNote.getMinimumSize().height));
            this.comboBoxAddNote.setPreferredSize(new Dimension(75, this.comboBoxAddNote.getMinimumSize().height));
            this.comboBoxAddNote.setVisible(false);
            this.comboBoxAddNote.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("comboBoxChanged") && isReady
                            && comboBoxAddNoteAddNoteCanRaiseSelectionChanged) {

                        ComboBoxIntervalItem item = (ComboBoxIntervalItem) comboBoxAddNote.getSelectedItem();

                        addStepNote(item.getIntervalPart());
                        comboBoxAddNote.setVisible(false);
                        buttonAddNote.setVisible(true);
                    }
                }
            });
            this.add(this.comboBoxAddNote);
            this.reloadNotes();
            this.isReady = true;
        }

        private void reloadNotes() {

            for (StepNoteView stepNoteView : this.stepNoteViews) {
                this.remove(stepNoteView);
            }
            this.stepNoteViews.clear();

            ArrayList<StepNote> stepNotes = this.stepFrame.getNotes();

            // TODO sort...
            //stepNotes.sort(new Comparator<StepNote>() {
            //    @Override
            //    public int compare(StepNote o1, StepNote o2) {
            //        return o1.getIntervalPart() - o2.getIntervalPart();
            //    }
            //});

            for (StepNote stepNote : stepNotes) {
                StepNoteView stepNoteView = new StepNoteView(stepNote, this.stepFrame.getInterval());
                stepNoteView.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                this.stepNoteViews.add(stepNoteView);
                this.add(stepNoteView);
            }
            this.updateUI();
        }

        private void addStepNote(int intervalPart) {
            StepNote stepNote = new StepNote(0, 0, 0, intervalPart);
            this.stepFrame.addNote(stepNote);
            this.reloadNotes();
        }

        private void changeInterval() {
            this.stepFrame.setInterval(Integer.valueOf(this.textFieldInterval.getText()));
            this.stepFrame.clearNotes();
            this.reloadNotes();
        }

        public void writeValues() {

            this.stepFrame.setMeasurement(Integer.valueOf(this.textFieldSecond.getText()));
            this.stepFrame.setStepFrameType(Integer.valueOf(this.textFieldType.getText()));
            this.stepFrame.setInterval(Integer.valueOf(this.textFieldInterval.getText()));

            for (StepNoteView stepNoteView : this.stepNoteViews) {
                stepNoteView.writeValues();
            }
        }
    }

    private class StepNoteView extends JPanel {

        private StepNote stepNote;
        private JComboBox<ComboBoxIntervalItem> comboBoxIntervalPart;
        private JTextField textFieldType;
        private JTextField textFieldUnknown0;
        private JTextField textFieldUnknown1;

        public StepNoteView(StepNote stepNote, int interval) {
            this.stepNote = stepNote;

            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            this.comboBoxIntervalPart = new JComboBox<ComboBoxIntervalItem>();
            this.comboBoxIntervalPart
                    .setMaximumSize(new Dimension(75, this.comboBoxIntervalPart.getMinimumSize().height));
            this.comboBoxIntervalPart
                    .setPreferredSize(new Dimension(75, this.comboBoxIntervalPart.getMinimumSize().height));
            for (int i = 0; i < interval; i++) {
                ComboBoxIntervalItem item = new ComboBoxIntervalItem(i, interval);
                this.comboBoxIntervalPart.addItem(item);
            }
            this.comboBoxIntervalPart.setSelectedIndex(this.stepNote.getIntervalPart());
            this.add(this.comboBoxIntervalPart);

            this.textFieldType = new JTextField();
            this.textFieldType.setMaximumSize(new Dimension(35, this.textFieldType.getMinimumSize().height));
            this.textFieldType.setPreferredSize(new Dimension(35, this.textFieldType.getMinimumSize().height));
            this.textFieldType.setText(String.valueOf(this.stepNote.getStepNoteType()));
            this.add(textFieldType);

            this.textFieldUnknown0 = new JTextField();
            this.textFieldUnknown0.setMaximumSize(new Dimension(35, this.textFieldUnknown0.getMinimumSize().height));
            this.textFieldUnknown0.setPreferredSize(new Dimension(35, this.textFieldUnknown0.getMinimumSize().height));
            this.textFieldUnknown0.setText(String.valueOf(this.stepNote.getUnknown0()));
            this.add(textFieldUnknown0);

            this.textFieldUnknown1 = new JTextField();
            this.textFieldUnknown1.setMaximumSize(new Dimension(35, this.textFieldUnknown1.getMinimumSize().height));
            this.textFieldUnknown1.setPreferredSize(new Dimension(35, this.textFieldUnknown1.getMinimumSize().height));
            this.textFieldUnknown1.setText(String.valueOf(this.stepNote.getUnknown1()));
            this.add(textFieldUnknown1);
        }

        public void writeValues() {

            this.stepNote.setStepNoteType(Integer.valueOf(this.textFieldType.getText()));
            this.stepNote.setUnknown0(Integer.valueOf(this.textFieldUnknown0.getText()));
            this.stepNote.setUnknown1(Integer.valueOf(this.textFieldUnknown1.getText()));
            ComboBoxIntervalItem item = (ComboBoxIntervalItem) this.comboBoxIntervalPart.getSelectedItem();
            this.stepNote.setIntervalPart(item.getIntervalPart());
        }

    }

    private class ComboBoxIntervalItem {

        private int intervalPart;
        private int interval;

        public ComboBoxIntervalItem(int intervalPart, int interval) {
            this.interval = interval;
            this.intervalPart = intervalPart;
        }

        public int getInterval() {
            return interval;
        }

        @Override
        public String toString() {
            return intervalPart + 1 + "/" + interval;
        }

        public int getIntervalPart() {
            return intervalPart;
        }
    }

    private class StepFileVisual extends JComponent {

        private int width;
        private int zoom;
        private int duration;
        private double measurementHeight;
        private double measurements;
        private Dimension dimension;
        private Color m_tRed = new Color(255, 0, 0, 150);
        private Color m_tGreen = new Color(0, 255, 0, 150);
        private Color m_tBlue = new Color(0, 0, 255, 150);
        private Font monoFont = new Font("Monospaced", Font.BOLD | Font.ITALIC, 36);
        private Font sanSerifFont = new Font("SanSerif", Font.PLAIN, 12);
        private Font serifFont = new Font("Serif", Font.BOLD, 24);
        private ImageIcon java2sLogo = new ImageIcon("java2s.gif");
        private StepFile stepFile;

        public StepFileVisual(StepFile stepFile) {
            this.stepFile = stepFile;
            this.zoom = 100;
            this.width = 400;
            this.reloadValues();
        }

        private void reloadValues() {
            this.measurements = this.stepFile.getMeasurementsEasy();
            this.duration = this.stepFile.getDurationEasy();

            this.measurementHeight = (this.duration / this.measurements) * this.zoom;
            this.dimension = new Dimension(this.width, this.duration * this.zoom);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw entire component white
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());

            this.DrawTimeLine(g);
            this.DrawMeasurementsLine(g);
            this.DrawNotes(g);
        }

        private void DrawMeasurementsLine(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;

            g.setColor(Color.RED);
            g.drawLine(0, 0, 0, this.dimension.height);

            for (double i = 0; i < this.dimension.height; i += this.measurementHeight) {
                g2.draw(new Line2D.Double(this.dimension.width, i, this.dimension.width - 400, i));
            }
        }

        private void DrawNotes(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            int c = 0; // is also an unknown value in meta info of net.arrowgene.dance.editor.stepfile, true note count?

            int test = 0; //note count

            int xMod = this.dimension.width / 4;

            for (StepFrame stepFrame : this.stepFile.getFramesEasy()) {

                for (StepNote stepNote : stepFrame.getNotes()) {
                    c++;
                    if (stepFrame.getStepFrameType() > 1 && stepFrame.getStepFrameType() < 6) {

                        test++; //visible notes

                    }

                    double measurementStartY = this.measurementHeight * (stepFrame.getMeasurement() - 1);
                    double yHeight = this.measurementHeight / stepFrame.getInterval();
                    double measurementPartStart = yHeight * stepNote.getIntervalPart();

                    double y = measurementStartY + measurementPartStart;
                    double x = 0;

                    if (stepFrame.getStepFrameType() == 2) {
                        x = 0;
                    } else if (stepFrame.getStepFrameType() == 3) {
                        x = xMod;
                    } else if (stepFrame.getStepFrameType() == 4) {
                        x = xMod * 2;
                    } else if (stepFrame.getStepFrameType() == 5) {
                        x = xMod * 3;
                    } else {
                        x = xMod * 4;
                    }

                    Rectangle2D rect = new Rectangle2D.Double(x, y, xMod, yHeight);
                    if (stepFrame.getStepFrameType() < 1 || stepFrame.getStepFrameType() > 6) {
                        g.setColor(Color.ORANGE);
                    } else {
                        if (stepNote.getStepNoteType() == StepNoteType.HOLD_START.getNumValue()) {
                            g.setColor(Color.BLUE);
                        } else if (stepNote.getStepNoteType() == StepNoteType.HOLD_END.getNumValue()) {
                            g.setColor(Color.cyan);
                        } else if (stepNote.getStepNoteType() == StepNoteType.ARROW.getNumValue()) {
                            g.setColor(Color.GREEN);
                        } else {
                            g.setColor(Color.magenta); //should not happen
                        }
                    }

                    g2.draw(rect);
                    g2.fill(rect);

                }
            }
        }

        private void DrawTimeLine(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawLine(this.dimension.width, 0, this.dimension.width, this.dimension.height);

            for (int i = 0; i < this.dimension.height; i += this.zoom) {
                g.drawLine(0, i, 10, i);
            }
        }

        @Override
        public Dimension getSize() {
            return this.dimension;
        }

        @Override
        public Dimension getPreferredSize() {
            return getSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getSize();
        }

    }

}
