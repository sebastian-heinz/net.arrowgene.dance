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

package net.arrowgene.dance.library.models.mail;

public enum MailType {
    NONE((byte)0),
    WEDDING_BG((byte)1),
    HAPPY_BIRTHDAY((byte)2),
    PINK_HEARTS((byte)3),
    PUFFY_CLOUDS((byte)4),
    LOVE_YOU_HEART((byte)5),
    HEARTS_N_STARS((byte)6),
    SUNNY_DAY((byte)7),
    LEAFY_LOVE((byte)8),
    PINK_HEARTS_2((byte)9),
    PINK_HEARTS_3((byte)10),
    DAISY_MAE((byte)11),
    STARRY_NIGHT((byte)12),
    NOTEBOOK((byte)13),
    DAYDREAMS((byte)14),
    SDO_GRAFFITI((byte)15),
    DIVORCE_REQUEST((byte)99),
    PROPOSE_REQUEST((byte)100),
    CHANGE_RING_REQUEST((byte)120),
    PAY_BILL((byte)200);


    private byte numValue;

    MailType(byte numValue) {
        this.numValue = numValue;
    }

    public byte getNumValue() {
        return numValue;
    }

    public static MailType getType(byte id) {
        for (MailType pt : MailType.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }
}
