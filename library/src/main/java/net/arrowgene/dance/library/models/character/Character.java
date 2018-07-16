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


public class Character {

    public static final int DEFAULT_CLOTH_ID = 0x7FFFFFFF;

    private boolean newCharacter;
    private int id;
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

    private GameTeam team;
    private boolean ready;
    private CharacterWalkDirection direction;
    private int moving;

    private int itemSlotCount;
    private int clothSlotCount;
    private int roomSlotId;
    private float x;
    private float y;
    private float z;
    private String name;
    private String zodiac;
    private String city;
    private String info;
    private ControllerType controller;
    private CharacterSexTyp sex;


    private boolean levelUpOnLastExperienceChange = false;


    public Character() {
        this.x = -100;
        this.y = 0;
        this.z = -50;
        this.direction = CharacterWalkDirection.SOUTH;
        this.moving = 1;

        this.controller = ControllerType.Keyboard;
        this.sex = CharacterSexTyp.MALE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Char: " + getName());

        return sb.toString();
    }

    public int getMoving() {
        return moving;
    }

    public void setMoving(int moving) {
        this.moving = moving;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void addExperience(int experience) {
        levelUpOnLastExperienceChange = false;
        this.experience += experience;

        checkLevelUp();
    }

    private void checkLevelUp() {
        int testLevel = 0;
        int testXP = this.experience;
        while (testXP > 200 * testLevel) {
            testXP -= 200 * testLevel;
            testLevel++;
        }
        if (testLevel != this.level) {
            this.level = testLevel;
            levelUpOnLastExperienceChange = true;
        }
    }

    public boolean levelUpOnLastExperienceChange() {
        return levelUpOnLastExperienceChange;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void addGame(int games) {
        this.games += games;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void addWins(int wins) {
        this.wins += wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void addDraws(int draws) {
        this.draws += draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void addLosses(int losses) {
        this.losses += losses;
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

    public void addPerfects(int perfects) {
        this.perfects += perfects;
    }

    public int getCools() {
        return cools;
    }

    public void setCools(int cools) {
        this.cools = cools;
    }

    public void addCools(int cools) {
        this.cools += cools;
    }

    public int getBads() {
        return bads;
    }

    public void setBads(int bads) {
        this.bads = bads;
    }

    public void addBads(int bads) {
        this.bads += bads;
    }

    public int getMisses() {
        return misses;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }

    public void addMisses(int misses) {
        this.misses += misses;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
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

    public GameTeam getTeam() {
        return team;
    }

    public void setTeam(GameTeam team) {
        this.team = team;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public CharacterWalkDirection getDirection() {
        return direction;
    }

    public void setDirection(CharacterWalkDirection direction) {
        this.direction = direction;
    }

    public int getItemSlotCount() {
        return itemSlotCount;
    }

    public void setItemSlotCount(int itemSlotCount) {
        this.itemSlotCount = itemSlotCount;
    }

    public int getClothSlotCount() {
        return clothSlotCount;
    }

    public void setClothSlotCount(int clothSlotCount) {
        this.clothSlotCount = clothSlotCount;
    }

    public int getRoomSlotId() {
        return roomSlotId;
    }

    public void setRoomSlotId(int roomSlotId) {
        this.roomSlotId = roomSlotId;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
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

    public ControllerType getController() {
        return controller;
    }

    public void setController(ControllerType controller) {
        this.controller = controller;
    }

    public CharacterSexTyp getSex() {
        return sex;
    }

    public void setSex(CharacterSexTyp sex) {
        this.sex = sex;
    }

    public void expandClothSlots() {
        this.clothSlotCount += 8;
    }

    public void expandItemSlots() {
        this.itemSlotCount += 8;
    }

    public boolean isNewCharacter() {
        return newCharacter;
    }

    public void setNewCharacter(boolean newCharacter) {
        this.newCharacter = newCharacter;
    }
}
