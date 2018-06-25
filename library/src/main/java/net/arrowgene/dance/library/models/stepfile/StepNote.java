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

/**
 * Created by Railgun on 19.06.2015.
 */
public class StepNote {

    private int stepNoteType;
    private int unknown0;
    private int unknown1;
    private int intervalPart;

    public StepNote(int stepNoteType, int unknown0, int unknown1, int intervalPart) {
        this.stepNoteType = stepNoteType;
        this.unknown0 = unknown0;
        this.unknown1 = unknown1;
        this.intervalPart = intervalPart;
    }

    public int getStepNoteType() {
        return stepNoteType;
    }

    public void setStepNoteType(int stepNoteType) {
        this.stepNoteType = stepNoteType;
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

    public int getIntervalPart() {
        return intervalPart;
    }

    public void setIntervalPart(int intervalPart) {
        this.intervalPart = intervalPart;
    }
}
