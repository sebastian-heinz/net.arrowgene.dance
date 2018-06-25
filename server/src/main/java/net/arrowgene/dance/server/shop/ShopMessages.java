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


public enum ShopMessages {

    MSG_NO_ERROR(0),
    MSG_ERROR_PLEASE_CHECK(0xffffffff),
    MSG_WRONG_PASSWORD_PLEASE_CHECK(0xfffffffe),
    MSG_YOU_DO_NOT_HAVE_ENOUGH_COINS_POINTS_OR_BONUS_FOR_THIS_ITEM(0xfffffffd),
    MSG_YOU_HAVE_NO_MORE_ROOM(0xfffffffc),
    MSG_SORRY_YOU_CANT_PURCHASE(0xfffffff8),
    MSG_ERROR(0xFFFFFFFB);


    private int numValue;

    ShopMessages(int numValue) {
        this.numValue = numValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public static ShopMessages getType(int id) {
        for (ShopMessages pt : ShopMessages.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }
}
