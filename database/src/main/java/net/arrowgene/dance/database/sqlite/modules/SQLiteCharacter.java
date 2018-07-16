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

package net.arrowgene.dance.database.sqlite.modules;

import net.arrowgene.dance.database.sqlite.SQLiteController;
import net.arrowgene.dance.database.sqlite.SQLiteFactory;
import net.arrowgene.dance.library.models.character.Character;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteCharacter {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteCharacter(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public Character getCharacter(String characterName) throws SQLException {
        Character character = null;

        PreparedStatement select = this.controller
            .createPreparedStatement("SELECT * FROM ag_character WHERE character_name=?;");

        select.setString(1, characterName);

        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            character = this.factory.createCharacter(rs);
        }

        rs.close();
        select.close();

        return character;

    }

    public Character getCharacter(int characterId) throws SQLException {
        Character character = null;

        PreparedStatement select = this.controller
            .createPreparedStatement("SELECT * FROM ag_character WHERE character_id=?;");

        select.setInt(1, characterId);

        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            character = this.factory.createCharacter(rs);
        }

        rs.close();
        select.close();

        return character;
    }

    public void insertCharacter(Character character) throws SQLException {

        if (character.getId() > -1) {
            PreparedStatement update = this.controller
                .createPreparedStatement("UPDATE [ag_character] SET [character_name]=?, "
                    + "[character_level]=?, [character_sex]=?, [character_flag]=?, "
                    + "[character_hair]=?, "
                    + "[character_glasses]=?, [character_top]=?, [character_shoes]=?, "
                    + "[character_face]=?, [character_gloves]=?, [character_pants]=?, "
                    + "[character_experience]=?, [character_games]=?, [character_wins]=?, "
                    + "[character_draws]=?, [character_losses]=?, [character_hearts]=?, "
                    + "[character_mvp]=?, [character_perfects]=?, [character_cools]=?, "
                    + "[character_bads]=?, [character_misses]=?, [character_points]=?, "
                    + "[character_coins]=?, [character_bonus]=?, [character_weight]=?, "
                    + "[character_ranking]=?, [character_status_achieved]=?, "
                    + "[character_best_score]=?, [character_age]=?, [character_zodiac]=?, "
                    + "[character_city]=?, [character_calorins_lost_week]=?, "
                    + "[character_points_won]=?, [character_competition_win]=?, "
                    + "[character_competition_lost]=?, [character_medal]=?, "
                    + "[character_alltime_best_ranking]=?, [character_tutorial]=?, "
                    + "[character_info]=?, [character_item_slot_count]=?, "
                    + "[character_cloth_slot_count]=? WHERE [character_id]=?");
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
            update.setInt(43, character.getId());
            update.execute();
            update.close();
        } else {
            PreparedStatement insert = this.controller.createPreparedStatement(
                "INSERT INTO [ag_character] VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
            insert.setString(2, character.getName());
            insert.setInt(3, character.getLevel());
            insert.setInt(4, character.getSex().getNumValue());
            insert.setInt(5, character.getFlag());
            insert.setInt(6, character.getHair());
            insert.setInt(7, character.getGlasses());
            insert.setInt(8, character.getTop());
            insert.setInt(9, character.getShoes());
            insert.setInt(10, character.getFace());
            insert.setInt(11, character.getGloves());
            insert.setInt(12, character.getPants());
            insert.setInt(13, character.getExperience());
            insert.setInt(14, character.getGames());
            insert.setInt(15, character.getWins());
            insert.setInt(16, character.getDraws());
            insert.setInt(17, character.getLosses());
            insert.setInt(18, character.getHearts());
            insert.setInt(19, character.getMvp());
            insert.setInt(20, character.getPerfects());
            insert.setInt(21, character.getCools());
            insert.setInt(22, character.getBads());
            insert.setInt(23, character.getMisses());
            insert.setInt(24, character.getPoints());
            insert.setInt(25, character.getCoins());
            insert.setInt(26, character.getBonus());
            insert.setInt(27, character.getWeight());
            insert.setInt(28, character.getRanking());
            insert.setInt(29, character.getStatusAchieved());
            insert.setInt(30, character.getBestScore());
            insert.setInt(31, character.getAge());
            insert.setString(32, character.getZodiac());
            insert.setString(33, character.getCity());
            insert.setInt(34, character.getCalorinsLostWeek());
            insert.setInt(35, character.getPointsWon());
            insert.setInt(36, character.getCompetitionWon());
            insert.setInt(37, character.getCompetitionLost());
            insert.setInt(38, character.getMedal());
            insert.setInt(39, character.getAllTimeBestRanking());
            insert.setInt(40, character.getTutorial());
            insert.setString(41, character.getInfo());
            insert.setInt(42, character.getItemSlotCount());
            insert.setInt(43, character.getClothSlotCount());
            insert.execute();

            int id = this.controller.getAutoIncrement(insert);
            character.setId(id);

            insert.close();
        }

    }

}
