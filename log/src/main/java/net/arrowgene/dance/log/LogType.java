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

package net.arrowgene.dance.log;


public enum LogType {

    NONE(0),
    CLIENT(1),
    SERVER(2),
    INFO(3),
    WARNING(4),
    HACK(5),
    ERROR(7),
    NOTICE(8),
    DEBUG(9),
    REQUEST_PACKET(10),
    RESPONSE_PACKET(11),
    UNKNOWN_PACKET(12),
    ITEM(13),
    CHAT(14),
    GROUP(15);

    private int numValue;

    LogType(int numValue) {
        this.numValue = numValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public static LogType getType(int id) {
        for (LogType pt : LogType.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }


}
