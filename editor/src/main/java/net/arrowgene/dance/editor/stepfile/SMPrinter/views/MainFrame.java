package net.arrowgene.dance.editor.stepfile.SMPrinter.views;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.SimFileDifficulty;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.Settings;
import net.arrowgene.dance.editor.stepfile.SMPrinter.utilities.SimFileReader;

/**
 * The main frame of the application. Contains all application panels, and initializations.
 * Contains a global settings instance that can be used in all panels. Every panel contains
 * a reference to an instance of the MainFrame.
 *
 * @author Dan
 */

public class MainFrame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Settings settings;

    private RenderPanel renderPanel;
    private SelectionInfoPanel selectionInfoPanel;
    private FileSelectorPanel fileSelectorPanel;

    public MainFrame() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //could not set look and feel; no problem, do nothing
        }

        settings = new Settings();
        renderPanel = new RenderPanel(this);
        selectionInfoPanel = new SelectionInfoPanel(this);

        String directory = getDirectoryToOpen();
        if (directory != null) {
            fileSelectorPanel = new FileSelectorPanel(this, new File(directory));
        } else {
            fileSelectorPanel = new FileSelectorPanel(this, null);
        }


        JSplitPane westSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fileSelectorPanel, selectionInfoPanel);
        westSplitPane.setOneTouchExpandable(true);
        westSplitPane.setDividerLocation(450);

        JSplitPane centreSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westSplitPane, new JScrollPane(renderPanel));
        centreSplitPane.setOneTouchExpandable(true);
        centreSplitPane.setDividerLocation(200);

        add(centreSplitPane);

        setJMenuBar(new MainMenu(this));

        Image img = Toolkit.getDefaultToolkit().createImage("icon.png");
        setIconImage(img);
        setTitle("Simfile Printer");

        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private String getDirectoryToOpen() {
        final String[] directories = {
                "C:/Program Files (x86)/StepMania 5/song/",
                "C:/Program Files (x86)/StepMania3.95/song/",
                "C:/Program Files (x86)/StepMania3.9b/song/",
                "C:/Program Files (x86)/OpenITG/song/",
                "C:/Program Files (x86)/ITG2/song/"
        };

        for (String directory : directories) {
            if (new File(directory).exists()) {
                return directory;
            }
        }
        return null;
    }

    public Settings getSettings() {
        return settings;
    }


    public void openFile(File file) {
        System.out.println(file);
        fileSelectorPanel.openFileOrDirectory(file);
        if (file.isFile()) {
            openSimFile(file.getAbsolutePath());
        }
    }

    public void openSimFile(String path) {
        SimFileReader reader = new SimFileReader(path);
        settings.simFile = reader.generateSimFile();
        settings.difficulty = settings.simFile.getDifficulties().get(0);

        selectionInfoPanel.notifyCurrentSimFileChanged();
        renderPanel.notifyCurrentSimFileChanged();
    }

    public void openDifficulty(SimFileDifficulty difficulty) {
        settings.difficulty = difficulty;

        renderPanel.notifyCurrentDifficultyChanged();
        selectionInfoPanel.notifyCurrentDifficultyChanged();
    }

    public void notifyPageDimensionsChanged() {
        renderPanel.notifyPageDimensionsChanged();
    }

    public void invertMeasureTrimming() {
        settings.hideLeadingAndTrailingWhiteSpace = !settings.hideLeadingAndTrailingWhiteSpace;

        renderPanel.notifyCurrentDifficultyChanged();
        selectionInfoPanel.notifyCurrentDifficultyChanged();
    }

    public void zoomIn() {
        renderPanel.zoomIn();
    }

    public void zoomOut() {
        renderPanel.zoomOut();
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
