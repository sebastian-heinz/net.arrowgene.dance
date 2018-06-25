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

package net.arrowgene.dance.library.models.item;


public enum ItemCategoryType {

    HEAD_N_HAIR_HAIRSTYLE_MALE(1),
    CLOTHING_TOPS_MALE(2),
    CLOTHING_BOTTOMS_MALE(3),
    CLOTHING_GLOVES_MALE(4),
    CLOTHING_SHOES_MALE(5),
    HEAD_N_HAIR_FACES_MALE(6),
    CLOTHING_GLASSES_MALE(7),
    CLOTHING_ONE_PIECE_MALE(50),
    CLOTHING_OUTFIT_MALE(201),

    HEAD_N_HAIR_HAIRSTYLE_FEMALE(101),
    CLOTHING_TOPS_FEMALE(102),
    CLOTHING_BOTTOMS_FEMALE(103),
    CLOTHING_GLOVES_FEMALE(104),
    CLOTHING_SHOES_FEMALE(105),
    HEAD_N_HAIR_FACES_FEMALE(106),
    CLOTHING_GLASSES_FEMALE(107),
    CLOTHING_ONE_PIECE_FEMALE(150),
    CLOTHING_OUTFIT_FEMALE(200),

    ITEMS_MAIN_CONSUMABLES(21000),
    ITEMS_AVATAR_EFFECTS(24000);


    private int numValue;

    ItemCategoryType(int numValue) {
        this.numValue = numValue;
    }

    public int getNumValue() {
        return numValue;
    }

    public static ItemCategoryType getType(int id) {
        for (ItemCategoryType pt : ItemCategoryType.values()) {
            if (pt.getNumValue() == id) {
                return pt;
            }
        }
        return null;
    }
}
