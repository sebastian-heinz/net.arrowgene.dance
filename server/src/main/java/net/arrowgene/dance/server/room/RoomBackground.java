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

package net.arrowgene.dance.server.room;


public enum RoomBackground {
    RANDOM(-1), //04 00 00 00 01 00
    WALK_STREET(0),
    CLUB_ST(1),
    BACK_STREET(2),
    STAGE(3),
    TAHITI(4),
    XMAS(5),
    CARNIVAL(6),
    SECRET_GDN(7),
    DANCE_TRIP(8),
    CASABLANCA(9),
    PARADE(10),
    SOCCER_FIELD(12),
    SEABED(14),
    MAGIC_HOUSE(15),
    FLOURISHING_STREET(16),
    ;

    private int numValue;

    RoomBackground(int numValue) {
        this.numValue = numValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public static RoomBackground getType(int id) {
        for (RoomBackground pt : RoomBackground.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }
}
