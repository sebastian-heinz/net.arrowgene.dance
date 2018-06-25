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

package net.arrowgene.dance.query.models;


import net.arrowgene.dance.library.models.character.Character;

import java.util.ArrayList;
import java.util.List;

public class CharacterResponse extends QueryResponse {


    public static List<CharacterResponse> asList(List<Character> characters) {
        List<CharacterResponse> characterResponses = new ArrayList<>();
        for (Character character : characters) {
            characterResponses.add(new CharacterResponse(character, true));
        }
        return characterResponses;
    }

    public static List<CharacterResponse> asList(List<Character> characters, boolean safe) {
        List<CharacterResponse> characterResponses = new ArrayList<>();
        for (Character character : characters) {
            characterResponses.add(new CharacterResponse(character, safe));
        }
        return characterResponses;
    }

    private int accountId;
    private int characterId;
    private int level;
    private int flag;
    private int hair;
    private int glasses;
    private int top;
    private int shoes;
    private int face;
    private int gloves;
    private int pants;
    private int experience;
    private int games;
    private int wins;
    private int draws;
    private int losses;
    private int hearts;
    private int mvp;
    private int perfects;
    private int cools;
    private int bads;
    private int misses;
    private int points;
    private int coins;
    private int bonus;
    private int weight;
    private int ranking;
    private int statusAchieved;
    private int bestScore;
    private int age;
    private int calorinsLostWeek;
    private int pointsWon;
    private int competitionWon;
    private int competitionLost;
    private int medal;
    private int allTimeBestRanking;
    private int tutorial;
    private String name;
    private String zodiac;
    private String city;
    private String info;
    private int sex;

    public CharacterResponse(Character character) {
        this(character, true);
    }

    public CharacterResponse(Character character, boolean safe) {
        if (!safe) {
            this.accountId = character.getAccountId();
            this.characterId = character.getCharacterId();
            this.points = character.getPoints();
            this.coins = character.getCoins();
            this.bonus = character.getBonus();
        }

        this.level = character.getLevel();
        this.flag = character.getFlag();
        this.hair = character.getHair();
        this.glasses = character.getGlasses();
        this.top = character.getTop();
        this.shoes = character.getShoes();
        this.face = character.getFace();
        this.gloves = character.getGloves();
        this.pants = character.getPants();
        this.experience = character.getExperience();
        this.games = character.getGames();
        this.wins = character.getWins();
        this.draws = character.getDraws();
        this.losses = character.getLosses();
        this.hearts = character.getHearts();
        this.mvp = character.getMvp();
        this.perfects = character.getPerfects();
        this.cools = character.getCools();
        this.bads = character.getBads();
        this.misses = character.getMisses();
        this.weight = character.getWeight();
        this.ranking = character.getRanking();
        this.statusAchieved = character.getStatusAchieved();
        this.bestScore = character.getBestScore();
        this.age = character.getAge();
        this.calorinsLostWeek = character.getCalorinsLostWeek();
        this.pointsWon = character.getPointsWon();
        this.competitionWon = character.getCompetitionWon();
        this.competitionLost = character.getCompetitionLost();
        this.medal = character.getMedal();
        this.allTimeBestRanking = character.getAllTimeBestRanking();
        this.tutorial = character.getTutorial();
        this.name = character.getName();
        this.zodiac = character.getZodiac();
        this.city = character.getCity();
        this.info = character.getInfo();
        this.sex = character.getSex().getNumValue();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public int getGlasses() {
        return glasses;
    }

    public void setGlasses(int glasses) {
        this.glasses = glasses;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getShoes() {
        return shoes;
    }

    public void setShoes(int shoes) {
        this.shoes = shoes;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getGloves() {
        return gloves;
    }

    public void setGloves(int gloves) {
        this.gloves = gloves;
    }

    public int getPants() {
        return pants;
    }

    public void setPants(int pants) {
        this.pants = pants;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getMvp() {
        return mvp;
    }

    public void setMvp(int mvp) {
        this.mvp = mvp;
    }

    public int getPerfects() {
        return perfects;
    }

    public void setPerfects(int perfects) {
        this.perfects = perfects;
    }

    public int getCools() {
        return cools;
    }

    public void setCools(int cools) {
        this.cools = cools;
    }

    public int getBads() {
        return bads;
    }

    public void setBads(int bads) {
        this.bads = bads;
    }

    public int getMisses() {
        return misses;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getStatusAchieved() {
        return statusAchieved;
    }

    public void setStatusAchieved(int statusAchieved) {
        this.statusAchieved = statusAchieved;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCalorinsLostWeek() {
        return calorinsLostWeek;
    }

    public void setCalorinsLostWeek(int calorinsLostWeek) {
        this.calorinsLostWeek = calorinsLostWeek;
    }

    public int getPointsWon() {
        return pointsWon;
    }

    public void setPointsWon(int pointsWon) {
        this.pointsWon = pointsWon;
    }

    public int getCompetitionWon() {
        return competitionWon;
    }

    public void setCompetitionWon(int competitionWon) {
        this.competitionWon = competitionWon;
    }

    public int getCompetitionLost() {
        return competitionLost;
    }

    public void setCompetitionLost(int competitionLost) {
        this.competitionLost = competitionLost;
    }

    public int getMedal() {
        return medal;
    }

    public void setMedal(int medal) {
        this.medal = medal;
    }

    public int getAllTimeBestRanking() {
        return allTimeBestRanking;
    }

    public void setAllTimeBestRanking(int allTimeBestRanking) {
        this.allTimeBestRanking = allTimeBestRanking;
    }

    public int getTutorial() {
        return tutorial;
    }

    public void setTutorial(int tutorial) {
        this.tutorial = tutorial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
