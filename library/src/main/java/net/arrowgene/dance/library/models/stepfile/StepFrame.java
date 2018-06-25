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


/**
 * A StepFrame can represent multiply notes(arrows) at one specific time slot.
 */
public class StepFrame {

    private int measurement;
    private int stepFrameType;
    private int interval;
    private ByteBuffer noteData;
    private ArrayList<StepNote> notes;


    public StepFrame(int measurement, int stepFrameType, int interval) {
        this.notes = new ArrayList<StepNote>();
        this.measurement = measurement;
        this.stepFrameType = stepFrameType;
        this.interval = interval;
    }

    public StepFrame(int measurement, int stepFrameType, int interval, byte[] data) {
        this(measurement, stepFrameType, interval);
        this.noteData = new ByteBuffer(data);
        this.parse();
    }

    private void parse() {

        for (int intervalPart = 0; intervalPart < this.interval; intervalPart++) {

            int unknown0 = this.noteData.getInt16();
            int unknown1 = this.noteData.getByte();
            int stepNoteType = this.noteData.getByte();

            if (unknown0 != 0) {
                StepNote note = new StepNote(stepNoteType, unknown0, unknown1, intervalPart);
                this.notes.add(note);
            }
        }
    }

    public int getNextFreeIntervalPart() {

        if (this.notes.size() < this.interval) {
            int[] intervalParts = new int[this.interval];
            for (StepNote stepNote : this.notes) {
                intervalParts[stepNote.getIntervalPart()] = 1;
            }
            for (int i = 0; i < this.interval; i++) {
                if (intervalParts[i] == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public byte[] getNotesForStepFile() {

        if (this.notes.size() > 0) {

            ByteBuffer sf = new ByteBuffer();

            for (int i = 0; i < this.interval; i++) {

                StepNote stepNote = this.TryGetStepNote(i);

                if (stepNote != null) {
                    sf.addInt16(stepNote.getUnknown0());
                    sf.addByte(stepNote.getUnknown1());
                    sf.addByte(stepNote.getStepNoteType());
                } else {
                    sf.addInt32(0);
                }
            }
            return sf.getAllBytes();
        } else {
            return null;
        }
    }

    public void addNote(StepNote stepNote) {
        if (this.canAddNote(stepNote.getIntervalPart())) {
            this.notes.add(stepNote);
        }
    }

    public int getNotesCount() {
        return this.notes.size();
    }


    public boolean canAddNote(int intervalPart) {
        return (this.notes.size() < this.getInterval() && intervalPart < this.interval && intervalPart >= 0);
    }

    public StepNote TryGetStepNote(int intervalPart) {
        for (StepNote stepNote : this.notes) {
            if (stepNote.getIntervalPart() == intervalPart) {
                return stepNote;
            }
        }
        return null;
    }

    public void clearNotes() {
        this.notes.clear();
    }

    public int getMeasurement() {
        return measurement;
    }

    public void setMeasurement(int measurement) {
        this.measurement = measurement;
    }

    public int getStepFrameType() {
        return stepFrameType;
    }

    public void setStepFrameType(int stepFrameType) {
        this.stepFrameType = stepFrameType;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public ArrayList<StepNote> getNotes() {
        return notes;
    }


}
