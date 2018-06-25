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

package net.arrowgene.dance.library.models.group;


public enum GroupAgreementType {


    OK(0),
    MSG_APPLIED_FOR_OTHER_GROUP(0xFFFFFFFB), //Has applied to join other group.
    MSG_MISSING_GROUP_CARD(0xFFFFFFFC), //To create group. Please purchase a group card from the item store.
    MSG_ALREADY_APPLIED_OR_JOINED(0xFFFFFFFD), //You are have already applied or joined a group.
    MSG_GROUPS_NOT_AVAILABLE(0xFFFFFFFE), //The group mode is not available.
    MSG_LEVEL_TO_LOW(0xFFFFFFFF), //You are not high enough Level!
    ;


    private int numValue;

    GroupAgreementType(int numValue) {
        this.numValue = numValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public static GroupAgreementType getType(int id) {
        for (GroupAgreementType pt : GroupAgreementType.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }

}
