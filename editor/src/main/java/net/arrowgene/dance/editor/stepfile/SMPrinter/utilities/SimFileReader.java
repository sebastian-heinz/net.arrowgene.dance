package net.arrowgene.dance.editor.stepfile.SMPrinter.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.arrowgene.dance.editor.stepfile.SMPrinter.models.Measure;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.SimFile;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.SimFileDifficulty;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.SimFileLine;
import net.arrowgene.dance.editor.stepfile.SMPrinter.models.stepmetadata.NotesType;

/**
 * The utility that reads and parses sim files, and creates the proper models.
 *
 * @author Dan
 */

public class SimFileReader {
    private static final String STEP_FILE_REGEX = "#.+?;";

    private File systemSimFile;
    private Pattern metaDataRegex;
    private Pattern stepLineRegex;

    private SimFileLine previouslyAddedLine = null;

    public SimFileReader(String stepFilePath) {
        this(new File(stepFilePath));
    }

    public SimFileReader(File systemSimFile) {
        this.systemSimFile = systemSimFile;

        metaDataRegex = Pattern.compile(STEP_FILE_REGEX, Pattern.DOTALL);
        stepLineRegex = Pattern.compile("[0-9MLF]{4,10}"); //huehuehue
    }

    public SimFile generateSimFile() {
        SimFile result = new SimFile();
        String[] fileParts = splitSimFile(readSimFileData());
        if (fileParts != null) {
            generateMetaData(fileParts, result);
        }
        return result;
    }

    private String readSimFileData() {
        String result = null;
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(systemSimFile.getAbsolutePath()));
            result = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(fileBytes)).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] splitSimFile(String fileData) {
        return getRegexMatchingList(metaDataRegex, fileData).toArray(new String[0]);
    }

    private void generateMetaData(String[] stepFileParts, SimFile accumulator) {
        for (String part : stepFileParts) {
            setDataBasedOnTag(part, accumulator);
        }
    }

    private void setDataBasedOnTag(String part, SimFile accumulator) {
        if (getTag(part).equals("#TITLE")) {
            accumulator.setTitle(stripTag(part));
        } else if (getTag(part).equals("#SUBTITLE")) {
            accumulator.setSubtitle(stripTag(part));
        } else if (getTag(part).equals("#ARTIST")) {
            accumulator.setArtist(stripTag(part));
        } else if (getTag(part).equals("#CREDIT")) {
            accumulator.setCredit(stripTag(part));
        } else if (getTag(part).equals("#BPMS")) {
            parseDisplayBPM(stripTag(part), accumulator);
        } else if (getTag(part).equals("#DISPLAYBPM")) {
            accumulator.setDisplayBPM(stripTag(part));
        } else if (getTag(part).equals("#NOTES")) {
            generateStepData(part, accumulator);
        }
    }

    private void parseDisplayBPM(String bpmValues, SimFile accumulator) {
        final String[] bpmCodes = bpmValues.split(",");
        double min = Integer.MAX_VALUE;
        String minText = "";
        double max = Integer.MIN_VALUE;
        String maxText = "";

        for (String bpmCode : bpmCodes) {
            String bpmText = bpmCode.substring(bpmCode.indexOf('=') + 1);
            double bpm = Double.parseDouble(bpmText);
            if (bpm < min) {
                min = bpm;
                minText = bpmText;
            }
            if (bpm > max) {
                max = bpm;
                maxText = bpmText;
            }
        }

        String displayBPM;
        if (max == min) {
            displayBPM = minText;
        } else {
            displayBPM = minText + " - " + maxText;
        }

        accumulator.setDisplayBPM(displayBPM);
    }

    private void generateStepData(String notes, SimFile accumulator) {
        previouslyAddedLine = null;
        //for all note data
        notes = stripTag(notes);

        String[] difficultyParts = notes.split(":");
        if (difficultyParts.length != 6) {
            System.err.println("there was an error reading the difficulty: expected 6 got " + difficultyParts.length);
            return;
        }

        SimFileDifficulty difficulty = new SimFileDifficulty();
        difficulty.setNotesType(difficultyParts[0].trim());
        difficulty.setDescription(difficultyParts[1].trim());
        difficulty.setDifficultyClass(difficultyParts[2].trim());
        difficulty.setDifficultyMeter(difficultyParts[3].trim());
        difficulty.setRadarValues(difficultyParts[4].trim());

        if (difficulty.getNotesType() != null) {
            String[] measureData = difficultyParts[5].split(",");
            for (int i = 0; i < measureData.length; i++) {
                difficulty.addMeasure(parseMeasure(measureData[i], i, difficulty.getNotesType()));
            }

            difficulty.trimMeasures();
            accumulator.addDifficulty(difficulty);

            System.out.println("SET DIFFICULTY: " + difficulty);
        }

        accumulator.sortDifficulties();
    }

    private Measure parseMeasure(String measure, int measureNumber, NotesType notesType) {
        Measure result = new Measure(measureNumber);
        List<String> rawLines = getRegexMatchingList(stepLineRegex, measure);
        int lineIndex = 0;
        int numberLinesInMeasure = rawLines.size();
        for (String rawLine : rawLines) {
            //create new step line given the previously added line
            previouslyAddedLine = new SimFileLine(rawLine, previouslyAddedLine, notesType, lineIndex, numberLinesInMeasure);
            result.addLine(previouslyAddedLine);
            lineIndex++;
        }

        return result;
    }

    private String getTag(String part) {
        int indexHash = part.indexOf('#');
        int indexColon = part.indexOf(':');
        if (indexHash != -1 && indexColon != -1) {
            return part.substring(indexHash, indexColon);
        }
        return "";
    }

    private String stripTag(String part) {
        return part.substring(part.indexOf(':') + 1, part.length() - 1).trim();
    }

    private List<String> getRegexMatchingList(Pattern pattern, String data) {
        Matcher match = pattern.matcher(data);
        List<String> matches = new ArrayList<String>();
        while (match.find()) {
            matches.add(match.group());
        }
        return matches;
    }
}
