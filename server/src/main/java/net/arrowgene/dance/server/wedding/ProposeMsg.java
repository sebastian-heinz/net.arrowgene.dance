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

package net.arrowgene.dance.server.wedding;

public enum ProposeMsg {
    OK(0),
    TEXT_OVER_LENGTH(-1),
    LEVEL_TO_LOW(-2),
    YOU_NEED_WEDDING_RING(-3),
    WEDDING_RING_DOES_NOT_MATCH(-4),
    UNKNOWN_ERROR(-5),
    PLAYER_UNAVAILABLE(-6),
    PARTNER_MUST_BE_OPPOSITE_SEX(-7),
    YOU_CANNOT_MARRY_SO_SOON_AFTER_DIVORCE_WAIT_7_DAYS(-8),
    USE_LOVER_MODE_TO_REACH_REQUIRED_CHARM_LEVEL(-11),
    YOU_ARE_ALREADY_MARRIED_DIVORCE_FIRST(-12),
    YOUR_PARTNER_IS_ALREADY_MARRIED(-14);

    private int numValue;

    ProposeMsg(int numValue) {
        this.numValue = numValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public static ProposeMsg getType(int id) {
        for (ProposeMsg pt : ProposeMsg.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }
}
