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

package net.arrowgene.dance.library.models.stepfile;


import net.arrowgene.dance.library.common.ByteBuffer;

import java.util.ArrayList;


public class StepFile {

    private StepFileType stepFileType;
    private ByteBuffer stepFile;
    private int songId;
    private ArrayList<StepFrame> framesEasy;
    private ArrayList<StepFrame> framesNormal;
    private ArrayList<StepFrame> framesHard;

    private String fileName;
    private String fileType;
    private String writer;
    private String producer;
    private String title;
    private int fileId;
    private int levelEasy;
    private int levelNormal;
    private int levelHard;
    private int noteCountEasy;
    private int noteCountNormal;
    private int noteCountHard;
    private int addressEasy;
    private int addressNormal;
    private int addressHard;
    private int addressEnd;
    private int durationEasy;
    private int durationNormal;
    private int durationHard;

    private String unknownString0;
    private String unknownString1;
    private int unknown0;
    private int unknown1;
    private int unknown2;
    private int unknown3;
    private int unknown4;
    private int unknown5;
    private int unknown6;
    private int unknown7;
    private int unknown8;
    private int unknown9;
    private int unknown10;
    private int unknown11;
    private int unknown12;
    private int measurementsEasy;
    private int measurementsNormal;
    private int measurementsHard;
    private int unknown16;
    private int unknown17;
    private int unknown18;
    private int unknown19;


    public StepFile(byte[] stepFile, int songId, StepFileType stepFileType) {
        this.stepFile = new ByteBuffer(stepFile);
        this.songId = songId;
        this.stepFileType = stepFileType;
        this.parse();
    }

    private void parse() {

        this.fileId = this.stepFile.getInt32();
        this.fileType = this.getFixedString(4);

        this.unknown0 = this.stepFile.getInt16();
        this.unknown1 = this.stepFile.getInt16();
        this.unknown2 = this.stepFile.getInt16();

        this.unknown3 = this.stepFile.getInt16();
        this.unknown4 = this.stepFile.getInt16();
        this.unknown5 = this.stepFile.getInt16();

        this.levelEasy = this.stepFile.getInt16();
        this.levelNormal = this.stepFile.getInt16();
        this.levelHard = this.stepFile.getInt16();

        this.unknown6 = this.stepFile.getInt16();
        this.unknown7 = this.stepFile.getInt16();

        this.unknown8 = this.stepFile.getInt16();
        this.unknown9 = this.stepFile.getInt16();

        this.unknown10 = this.stepFile.getInt16();
        this.unknown11 = this.stepFile.getInt16();

        this.unknown12 = this.stepFile.getInt16();

        this.noteCountEasy = this.stepFile.getInt32();
        this.noteCountNormal = this.stepFile.getInt32();
        this.noteCountHard = this.stepFile.getInt32();

        this.measurementsEasy = this.stepFile.getInt32();
        this.measurementsNormal = this.stepFile.getInt32();
        this.measurementsHard = this.stepFile.getInt32();

        this.unknown16 = this.stepFile.getInt32();
        this.unknown17 = this.stepFile.getInt32();
        this.unknown18 = this.stepFile.getInt32();

        this.unknownString0 = this.getFixedString(32);
        this.title = this.getFixedString(32);
        this.unknownString1 = this.getFixedString(32);
        this.writer = this.getFixedString(32);
        this.producer = this.getFixedString(32);
        this.fileName = this.getFixedString(32);

        this.unknown19 = this.stepFile.getInt32();

        this.durationEasy = this.stepFile.getInt32();
        this.durationNormal = this.stepFile.getInt32();
        this.durationHard = this.stepFile.getInt32();

        this.addressEasy = this.stepFile.getInt32();
        this.addressNormal = this.stepFile.getInt32();
        this.addressHard = this.stepFile.getInt32();
        this.addressEnd = this.stepFile.getInt32();


        this.framesEasy = new ArrayList<StepFrame>();
        while (this.stepFile.getCurrentPos() < this.addressNormal) {
            int second = this.stepFile.getInt32();
            int stepFrameType = this.stepFile.getInt16();
            int interval = this.stepFile.getInt16();
            byte[] data = this.stepFile.getBytes(interval * 4);
            StepFrame stepFrame = new StepFrame(second, stepFrameType, interval, data);
            this.framesEasy.add(stepFrame);
        }

        this.framesNormal = new ArrayList<StepFrame>();
        while (this.stepFile.getCurrentPos() < this.addressHard) {
            int second = this.stepFile.getInt32();
            int stepFrameType = this.stepFile.getInt16();
            int interval = this.stepFile.getInt16();
            byte[] data = this.stepFile.getBytes(interval * 4);
            StepFrame stepFrame = new StepFrame(second, stepFrameType, interval, data);
            this.framesNormal.add(stepFrame);
        }

        this.framesHard = new ArrayList<StepFrame>();
        while (this.stepFile.getCurrentPos() < this.addressEnd) {
            int second = this.stepFile.getInt32();
            int stepFrameType = this.stepFile.getInt16();
            int interval = this.stepFile.getInt16();
            byte[] data = this.stepFile.getBytes(interval * 4);
            StepFrame stepFrame = new StepFrame(second, stepFrameType, interval, data);
            this.framesHard.add(stepFrame);
        }


    }

    public byte[] getStepFile() {
        ByteBuffer sf = new ByteBuffer();

        sf.addInt32(this.fileId);
        sf.addBytes(this.createFixedString(this.fileType, 4));

        sf.addInt16(this.unknown0);
        sf.addInt16(this.unknown1);
        sf.addInt16(this.unknown2);

        sf.addInt16(this.unknown3);
        sf.addInt16(this.unknown4);
        sf.addInt16(this.unknown5);

        sf.addInt16(this.levelEasy);
        sf.addInt16(this.levelNormal);
        sf.addInt16(this.levelHard);

        sf.addInt16(this.unknown6);
        sf.addInt16(this.unknown7);
        sf.addInt16(this.unknown8);

        sf.addInt16(this.unknown9);
        sf.addInt16(this.unknown10);
        sf.addInt16(this.unknown11);

        sf.addInt16(this.unknown12);

        sf.addInt32(this.noteCountEasy);
        sf.addInt32(this.noteCountNormal);
        sf.addInt32(this.noteCountHard);

        sf.addInt32(this.measurementsEasy);
        sf.addInt32(this.measurementsNormal);
        sf.addInt32(this.measurementsHard);

        sf.addInt32(this.unknown16);
        sf.addInt32(this.unknown17);
        sf.addInt32(this.unknown18);

        sf.addBytes(this.createFixedString(this.unknownString0, 32));
        sf.addBytes(this.createFixedString(this.title, 32));
        sf.addBytes(this.createFixedString(this.unknownString1, 32));
        sf.addBytes(this.createFixedString(this.writer, 32));
        sf.addBytes(this.createFixedString(this.producer, 32));
        sf.addBytes(this.createFixedString(this.fileName, 32));

        sf.addInt32(this.unknown19);

        sf.addInt32(this.durationEasy);
        sf.addInt32(this.durationNormal);
        sf.addInt32(this.durationHard);

        sf.addInt32(this.addressEasy);
        sf.addInt32(this.addressNormal);
        sf.addInt32(this.addressHard);
        sf.addInt32(this.addressEnd);

        this.addStepFrames(sf, this.framesEasy);
        this.addStepFrames(sf, this.framesNormal);
        this.addStepFrames(sf, this.framesHard);

        return sf.getAllBytes();
    }

    private void addStepFrames(ByteBuffer buffer, ArrayList<StepFrame> frames) {
        for (StepFrame stepFrame : frames) {
            byte[] notes = stepFrame.getNotesForStepFile();
            if (notes != null) {
                buffer.addInt32(stepFrame.getMeasurement());
                buffer.addInt16(stepFrame.getStepFrameType());
                buffer.addInt16(stepFrame.getInterval());
                buffer.addBytes(notes);
            }
        }
    }

    private String getFixedString(int len) {

        int pos = this.stepFile.getCurrentPos();

        // TODO check if this is correct..
        String s = this.stepFile.getStringNulTerminated();
        this.stepFile.setCurrentPos(pos + len);
        return s;
    }

    private byte[] createFixedString(String s, int len) {

        ByteBuffer byteBuffer = new ByteBuffer(len);
        byteBuffer.addString(s);

        //TODO check if this is correct..
        byteBuffer.setCurrentPos(len);
        return byteBuffer.getAllBytes();
    }

    public StepFileType getStepFileType() {
        return stepFileType;
    }

    public void setStepFileType(StepFileType stepFileType) {
        this.stepFileType = stepFileType;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getLevelEasy() {
        return levelEasy;
    }

    public void setLevelEasy(int levelEasy) {
        this.levelEasy = levelEasy;
    }

    public int getLevelNormal() {
        return levelNormal;
    }

    public void setLevelNormal(int levelNormal) {
        this.levelNormal = levelNormal;
    }

    public int getLevelHard() {
        return levelHard;
    }

    public void setLevelHard(int levelHard) {
        this.levelHard = levelHard;
    }

    public int getNoteCountEasy() {
        return noteCountEasy;
    }

    public void setNoteCountEasy(int noteCountEasy) {
        this.noteCountEasy = noteCountEasy;
    }

    public int getNoteCountNormal() {
        return noteCountNormal;
    }

    public void setNoteCountNormal(int noteCountNormal) {
        this.noteCountNormal = noteCountNormal;
    }

    public int getNoteCountHard() {
        return noteCountHard;
    }

    public void setNoteCountHard(int noteCountHard) {
        this.noteCountHard = noteCountHard;
    }

    public int getAddressEasy() {
        return addressEasy;
    }

    public void setAddressEasy(int addressEasy) {
        this.addressEasy = addressEasy;
    }

    public int getAddressNormal() {
        return addressNormal;
    }

    public void setAddressNormal(int addressNormal) {
        this.addressNormal = addressNormal;
    }

    public int getAddressHard() {
        return addressHard;
    }

    public void setAddressHard(int addressHard) {
        this.addressHard = addressHard;
    }

    public int getDurationEasy() {
        return durationEasy;
    }

    public void setDurationEasy(int durationEasy) {
        this.durationEasy = durationEasy;
    }

    public int getDurationNormal() {
        return durationNormal;
    }

    public void setDurationNormal(int durationNormal) {
        this.durationNormal = durationNormal;
    }

    public int getDurationHard() {
        return durationHard;
    }

    public void setDurationHard(int durationHard) {
        this.durationHard = durationHard;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<StepFrame> getFramesEasy() {
        return framesEasy;
    }

    public void setFramesEasy(ArrayList<StepFrame> framesEasy) {
        this.framesEasy = framesEasy;
    }

    public ArrayList<StepFrame> getFramesNormal() {
        return framesNormal;
    }

    public void setFramesNormal(ArrayList<StepFrame> framesNormal) {
        this.framesNormal = framesNormal;
    }

    public ArrayList<StepFrame> getFramesHard() {
        return framesHard;
    }

    public void setFramesHard(ArrayList<StepFrame> framesHard) {
        this.framesHard = framesHard;
    }

    public int getAddressEnd() {
        return addressEnd;
    }

    public void setAddressEnd(int addressEnd) {
        this.addressEnd = addressEnd;
    }

    public int getMeasurementsEasy() {
        return measurementsEasy;
    }

    public void setMeasurementsEasy(int measurementsEasy) {
        this.measurementsEasy = measurementsEasy;
    }

    public int getMeasurementsNormal() {
        return measurementsNormal;
    }

    public void setMeasurementsNormal(int measurementsNormal) {
        this.measurementsNormal = measurementsNormal;
    }

    public int getMeasurementsHard() {
        return measurementsHard;
    }

    public void setMeasurementsHard(int measurementsHard) {
        this.measurementsHard = measurementsHard;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }


    public String getUnknownString0() {
        return unknownString0;
    }

    public void setUnknownString0(String unknownString0) {
        this.unknownString0 = unknownString0;
    }

    public String getUnknownString1() {
        return unknownString1;
    }

    public void setUnknownString1(String unknownString1) {
        this.unknownString1 = unknownString1;
    }

    public int getUnknown0() {
        return unknown0;
    }

    public void setUnknown0(int unknown0) {
        this.unknown0 = unknown0;
    }

    public int getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(int unknown1) {
        this.unknown1 = unknown1;
    }

    public int getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(int unknown2) {
        this.unknown2 = unknown2;
    }

    public int getUnknown3() {
        return unknown3;
    }

    public void setUnknown3(int unknown3) {
        this.unknown3 = unknown3;
    }

    public int getUnknown4() {
        return unknown4;
    }

    public void setUnknown4(int unknown4) {
        this.unknown4 = unknown4;
    }

    public int getUnknown5() {
        return unknown5;
    }

    public void setUnknown5(int unknown5) {
        this.unknown5 = unknown5;
    }

    public int getUnknown6() {
        return unknown6;
    }

    public void setUnknown6(int unknown6) {
        this.unknown6 = unknown6;
    }

    public int getUnknown7() {
        return unknown7;
    }

    public void setUnknown7(int unknown7) {
        this.unknown7 = unknown7;
    }

    public int getUnknown8() {
        return unknown8;
    }

    public void setUnknown8(int unknown8) {
        this.unknown8 = unknown8;
    }

    public int getUnknown9() {
        return unknown9;
    }

    public void setUnknown9(int unknown9) {
        this.unknown9 = unknown9;
    }

    public int getUnknown10() {
        return unknown10;
    }

    public void setUnknown10(int unknown10) {
        this.unknown10 = unknown10;
    }

    public int getUnknown11() {
        return unknown11;
    }

    public void setUnknown11(int unknown11) {
        this.unknown11 = unknown11;
    }

    public int getUnknown12() {
        return unknown12;
    }

    public void setUnknown12(int unknown12) {
        this.unknown12 = unknown12;
    }

    public int getUnknown16() {
        return unknown16;
    }

    public void setUnknown16(int unknown16) {
        this.unknown16 = unknown16;
    }

    public int getUnknown17() {
        return unknown17;
    }

    public void setUnknown17(int unknown17) {
        this.unknown17 = unknown17;
    }

    public int getUnknown18() {
        return unknown18;
    }

    public void setUnknown18(int unknown18) {
        this.unknown18 = unknown18;
    }

    public int getUnknown19() {
        return unknown19;
    }

    public void setUnknown19(int unknown19) {
        this.unknown19 = unknown19;
    }
}
