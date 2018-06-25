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

package net.arrowgene.dance.library.models.wedding;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Keeps track of individual couples wedding status
 */
public class WeddingRecord {

    private static Format formatter = new SimpleDateFormat("yyyy-MM-dd");

    private int id;
    private int groomId;
    private int brideId;
    private long marriedDate;
    private long divorceDate;
    private long engageDate;
    private WeddingState groomState;
    private WeddingState brideState;
    private RingType ringType;
    private String brideCharacterName;
    private String groomCharacterName;

    public WeddingRecord() {
        this.id = -1;
        this.groomCharacterName = "";
        this.brideCharacterName = "";
        this.groomState = WeddingState.NOT_MARRIED;
        this.brideState = WeddingState.NOT_MARRIED;
        this.ringType = RingType.NONE;
        this.groomId = -1;
        this.brideId = -1;
        this.marriedDate = -1;
        this.divorceDate = -1;
        this.engageDate = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroomId() {
        return groomId;
    }

    public void setGroomId(int groomId) {
        this.groomId = groomId;
    }

    public int getBrideId() {
        return brideId;
    }

    public void setBrideId(int brideId) {
        this.brideId = brideId;
    }

    public long getMarriedDate() {
        return marriedDate;
    }

    public void setMarriedDate(long marriedDate) {
        this.marriedDate = marriedDate;
    }

    public long getDivorceDate() {
        return divorceDate;
    }

    public void setDivorceDate(long divorceDate) {
        this.divorceDate = divorceDate;
    }

    public long getEngageDate() {
        return engageDate;
    }

    public void setEngageDate(long engageDate) {
        this.engageDate = engageDate;
    }

    public WeddingState getGroomState() {
        return groomState;
    }

    public void setGroomState(WeddingState groomState) {
        this.groomState = groomState;
    }

    public WeddingState getBrideState() {
        return brideState;
    }

    public void setBrideState(WeddingState brideState) {
        this.brideState = brideState;
    }

    public String getBrideCharacterName() {
        return brideCharacterName;
    }

    public void setBrideCharacterName(String brideCharacterName) {
        this.brideCharacterName = brideCharacterName;
    }

    public String getGroomCharacterName() {
        return groomCharacterName;
    }

    public void setGroomCharacterName(String groomCharacterName) {
        this.groomCharacterName = groomCharacterName;
    }

    public RingType getRingType() {
        return ringType;
    }

    public void setRingType(RingType ringType) {
        this.ringType = ringType;
    }

    public String getMarriedDateString() {
        if (this.marriedDate > 0) {
            return WeddingRecord.formatter.format(this.marriedDate * 1000);
        } else {
            return "-";
        }
    }

    public String getDivorceDateString() {
        if (this.divorceDate > 0) {
            return WeddingRecord.formatter.format(this.divorceDate * 1000);
        } else {
            return "-";
        }
    }

    public String getEngageDateString() {
        if (this.engageDate > 0) {
            return WeddingRecord.formatter.format(this.engageDate * 1000);
        } else {
            return "-";
        }
    }

    public WeddingState getWeddingState(int characterId) {
        if (characterId == this.groomId) {
            return this.groomState;
        } else if (characterId == this.brideId) {
            return this.brideState;
        } else {
            return null;
        }
    }

    public WeddingState getPartnerWeddingState(int characterId) {
        if (characterId == this.groomId) {
            return this.brideState;
        } else if (characterId == this.brideId) {
            return this.groomState;
        } else {
            return null;
        }
    }

    public void setWeddingState(int characterId, WeddingState state) {
        if (characterId == this.groomId) {
            this.groomState = state;
        } else if (characterId == this.brideId) {
            this.brideState = state;
        }
    }

    public String getDate(WeddingState state) {
        String date = "";
        if (state != null) {
            switch (state) {
                case DIVORCE: {
                    date = this.getDivorceDateString();
                    break;
                }
                case ENGAGED: {
                    date = this.getEngageDateString();
                    break;
                }
                case MARRIED: {
                    date = this.getMarriedDateString();
                    break;
                }
            }
        }
        return date;
    }

    public int getPartnerCharacterId(int characterId) {
        if (characterId == this.groomId) {
            return this.brideId;
        } else if (characterId == this.brideId) {
            return this.groomId;
        } else {
            return -1;
        }
    }

    public String getPartnerCharacterName(int characterId) {
        if (characterId == this.groomId) {
            return this.brideCharacterName;
        } else if (characterId == this.brideId) {
            return this.groomCharacterName;
        } else {
            return "";
        }
    }

    public String getCharacterName(int characterId) {
        if (characterId == this.groomId) {
            return this.groomCharacterName;
        } else if (characterId == this.brideId) {
            return this.brideCharacterName;
        } else {
            return "";
        }
    }

    public boolean isMatchingRecord(int characterId, int partnerCharacterId) {
        return this.brideId == characterId && this.groomId == partnerCharacterId || this.brideId == partnerCharacterId && this.groomId == characterId;
    }
}
