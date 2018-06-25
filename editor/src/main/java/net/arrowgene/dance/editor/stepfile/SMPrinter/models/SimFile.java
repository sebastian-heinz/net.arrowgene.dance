package net.arrowgene.dance.editor.stepfile.SMPrinter.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A model for all the data within a sim file. Sim files contain multiple difficulties,
 * each can be of a different game mode. Meta data that is not specific to a difficulty
 * is modelled here.
 *
 * @author Dan
 */

public class SimFile {
    private String title;
    private String subtitle;
    private String artist;
    private String credit;
    private String displayBPM;

    private List<SimFileDifficulty> difficulties;

    public SimFile() {
        difficulties = new ArrayList<SimFileDifficulty>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDisplayBPM() {
        return displayBPM;
    }

    public void setDisplayBPM(String displayBPM) {
        this.displayBPM = displayBPM;
    }

    public List<SimFileDifficulty> getDifficulties() {
        return difficulties;
    }

    public void addDifficulty(SimFileDifficulty difficulty) {
        difficulties.add(difficulty);
    }

    public void sortDifficulties() {
        Collections.sort(difficulties);
    }

    @Override
    public String toString() {
        return title + " - " + artist + " [" + credit + "]";
    }
}
