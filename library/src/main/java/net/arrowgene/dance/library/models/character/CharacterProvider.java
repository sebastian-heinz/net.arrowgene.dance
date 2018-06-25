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

package net.arrowgene.dance.library.models.character;



public class CharacterProvider {

    public static final int DEFAULT_CLOTH_ID = 0x7FFFFFFF;
    public static final int DEFAULT_SLOT_COUNT = 8;

    private static CharacterProvider instance = new CharacterProvider();

    public static CharacterProvider getInstance() {
        return instance;
    }

    public CharacterProvider() {

    }

    public Character createPlayer(int accountId, String characterName, CharacterSexTyp sex) {
        Character character = this.createBase();
        character.setAccountId(accountId);
        character.setSex(sex);
        character.setName(characterName);
        return character;
    }

    private Character createBase() {
        Character character = new Character();
        character.setAccountId(-1);
        character.setCharacterId(-1);
        character.setName("");
        character.setLevel(1);
        character.setSex(CharacterSexTyp.MALE);
        character.setFlag(2147483647);
        character.setHair(DEFAULT_CLOTH_ID);
        character.setGlasses(DEFAULT_CLOTH_ID);
        character.setTop(DEFAULT_CLOTH_ID);
        character.setShoes(DEFAULT_CLOTH_ID);
        character.setFace(DEFAULT_CLOTH_ID);
        character.setGloves(DEFAULT_CLOTH_ID);
        character.setPants(DEFAULT_CLOTH_ID);
        character.setExperience(0);
        character.setGames(0);
        character.setWins(0);
        character.setDraws(0);
        character.setLosses(0);
        character.setHearts(0);
        character.setMvp(0);
        character.setPerfects(0);
        character.setCools(0);
        character.setBads(0);
        character.setMisses(0);
        character.setPoints(0);
        character.setCoins(0);
        character.setBonus(0);
        character.setWeight(100);
        character.setRanking(0);
        character.setStatusAchieved(0);
        character.setBestScore(0);
        character.setAge(0);
        character.setZodiac("");
        character.setCity("");
        character.setCalorinsLostWeek(0);
        character.setPointsWon(0);
        character.setCompetitionWon(0);
        character.setCompetitionLost(0);
        character.setMedal(0);
        character.setAllTimeBestRanking(0);
        character.setTutorial(0);
        character.setInfo("");
        character.setTeam(GameTeam.SINGLE);
        character.setReady(false);
        character.setDirection(CharacterWalkDirection.SOUTH);
        character.setMoving(1);
        character.setX(-100);
        character.setY(0);
        character.setZ(-50);
        character.setItemSlotCount(DEFAULT_SLOT_COUNT);
        character.setClothSlotCount(DEFAULT_SLOT_COUNT);
        character.setController(ControllerType.Keyboard);

        return character;
    }


}
