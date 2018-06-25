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

package net.arrowgene.dance.server.shop;


public enum DressItemMsg {

    MSG_NO_ERROR(0),
    SORRY_YOU_MUST_MEET_THE_MINIMUM_LEVEL(0xfffffffe),
    NO_ITEM_INFORMATION(0xfffffffd),
    STORAGE_IS_FULL(0xfffffffc),
    CANT_EQUIP_ITEM_WITH_CURRENT_GENDER(0xfffffffb),
    DAY_CARD_CAN_ONLY_BE_USED_BETWEEN_5_TO_17(0xfffffffa),
    NIGHT_CARD_CAN_ONLY_BE_USED_BETWEEN_17_TO_5(0xfffffff9),
    FAILED_TO_WEAR_THE_RING_CHECK_CONDITIONS(0xfffffff8);


    private int numValue;

    DressItemMsg(int numValue) {
        this.numValue = numValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public static DressItemMsg getType(int id) {
        for (DressItemMsg pt : DressItemMsg.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }
}
