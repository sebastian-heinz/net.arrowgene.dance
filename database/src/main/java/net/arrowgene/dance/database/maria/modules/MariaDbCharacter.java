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

package net.arrowgene.dance.database.maria.modules;

import net.arrowgene.dance.database.maria.MariaDbController;
import net.arrowgene.dance.database.maria.MariaDbFactory;
import net.arrowgene.dance.library.models.character.Character;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDbCharacter {

    private MariaDbController controller;
    private MariaDbFactory factory;

    public MariaDbCharacter(MariaDbController controller, MariaDbFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public Character getCharacter(String characterName) throws SQLException {
        Character character = null;
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_character` WHERE `name`=?;");
        select.setString(1, characterName);
        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            character = factory.createCharacter(rs);
        }
        rs.close();
        select.close();
        return character;
    }

    public Character getCharacter(int characterId) throws SQLException {
        Character character = null;
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_character` WHERE id=?;");
        select.setInt(1, characterId);
        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            character = factory.createCharacter(rs);
        }
        rs.close();
        select.close();
        return character;
    }

    public void insertCharacter(Character character) throws SQLException {
        if (character.getCharacterId() > -1) {
            PreparedStatement update = controller
                .createPreparedStatement("UPDATE `dance_character` SET `name`=?, "
                    + "`level`=?, `sex`=?, `flag`=?, "
                    + "`hair`=?, "
                    + "`glasses`=?, `top`=?, `shoes`=?, "
                    + "`face`=?, `gloves`=?, `pants`=?, "
                    + "`experience`=?, `games`=?, `wins`=?, "
                    + "`draws`=?, `losses`=?, `hearts`=?, "
                    + "`mvp`=?, `perfects`=?, `cools`=?, "
                    + "`bads`=?, `misses`=?, `points`=?, "
                    + "`coins`=?, `bonus`=?, `weight`=?, "
                    + "`ranking`=?, `status_achieved`=?, "
                    + "`best_score`=?, `age`=?, `zodiac`=?, "
                    + "`city`=?, `calorins_lost_week`=?, "
                    + "`points_won`=?, `competition_win`=?, "
                    + "`competition_lost`=?, `medal`=?, "
                    + "`alltime_best_ranking`=?, `tutorial`=?, "
                    + "`info`=?, `item_slot_count`=?, "
                    + "`cloth_slot_count`=? WHERE `id`=?;");
            update.setString(1, character.getName());
            update.setInt(2, character.getLevel());
            update.setInt(3, character.getSex().getNumValue());
            update.setInt(4, character.getFlag());
            update.setInt(5, character.getHair());
            update.setInt(6, character.getGlasses());
            update.setInt(7, character.getTop());
            update.setInt(8, character.getShoes());
            update.setInt(9, character.getFace());
            update.setInt(10, character.getGloves());
            update.setInt(11, character.getPants());
            update.setInt(12, character.getExperience());
            update.setInt(13, character.getGames());
            update.setInt(14, character.getWins());
            update.setInt(15, character.getDraws());
            update.setInt(16, character.getLosses());
            update.setInt(17, character.getHearts());
            update.setInt(18, character.getMvp());
            update.setInt(19, character.getPerfects());
            update.setInt(20, character.getCools());
            update.setInt(21, character.getBads());
            update.setInt(22, character.getMisses());
            update.setInt(23, character.getPoints());
            update.setInt(24, character.getCoins());
            update.setInt(25, character.getBonus());
            update.setInt(26, character.getWeight());
            update.setInt(27, character.getRanking());
            update.setInt(28, character.getStatusAchieved());
            update.setInt(29, character.getBestScore());
            update.setInt(30, character.getAge());
            update.setString(31, character.getZodiac());
            update.setString(32, character.getCity());
            update.setInt(33, character.getCalorinsLostWeek());
            update.setInt(34, character.getPointsWon());
            update.setInt(35, character.getCompetitionWon());
            update.setInt(36, character.getCompetitionLost());
            update.setInt(37, character.getMedal());
            update.setInt(38, character.getAllTimeBestRanking());
            update.setInt(39, character.getTutorial());
            update.setString(40, character.getInfo());
            update.setInt(41, character.getItemSlotCount());
            update.setInt(42, character.getClothSlotCount());
            update.setInt(43, character.getCharacterId());
            update.execute();
            update.close();
        } else {
            PreparedStatement insert = controller.createPreparedStatement(
                "INSERT INTO `dance_character` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            insert.setInt(2, character.getAccountId());
            insert.setString(3, character.getName());
            insert.setInt(4, character.getLevel());
            insert.setInt(5, character.getSex().getNumValue());
            insert.setInt(6, character.getFlag());
            insert.setInt(7, character.getHair());
            insert.setInt(8, character.getGlasses());
            insert.setInt(9, character.getTop());
            insert.setInt(10, character.getShoes());
            insert.setInt(11, character.getFace());
            insert.setInt(12, character.getGloves());
            insert.setInt(13, character.getPants());
            insert.setInt(14, character.getExperience());
            insert.setInt(15, character.getGames());
            insert.setInt(16, character.getWins());
            insert.setInt(17, character.getDraws());
            insert.setInt(18, character.getLosses());
            insert.setInt(19, character.getHearts());
            insert.setInt(20, character.getMvp());
            insert.setInt(21, character.getPerfects());
            insert.setInt(22, character.getCools());
            insert.setInt(23, character.getBads());
            insert.setInt(24, character.getMisses());
            insert.setInt(25, character.getPoints());
            insert.setInt(26, character.getCoins());
            insert.setInt(27, character.getBonus());
            insert.setInt(28, character.getWeight());
            insert.setInt(29, character.getRanking());
            insert.setInt(30, character.getStatusAchieved());
            insert.setInt(31, character.getBestScore());
            insert.setInt(32, character.getAge());
            insert.setString(33, character.getZodiac());
            insert.setString(34, character.getCity());
            insert.setInt(35, character.getCalorinsLostWeek());
            insert.setInt(36, character.getPointsWon());
            insert.setInt(37, character.getCompetitionWon());
            insert.setInt(38, character.getCompetitionLost());
            insert.setInt(39, character.getMedal());
            insert.setInt(40, character.getAllTimeBestRanking());
            insert.setInt(41, character.getTutorial());
            insert.setString(42, character.getInfo());
            insert.setInt(43, character.getItemSlotCount());
            insert.setInt(44, character.getClothSlotCount());
            insert.execute();
            int id = controller.getAutoIncrement(insert);
            character.setCharacterId(id);
            insert.close();
        }
    }
}
