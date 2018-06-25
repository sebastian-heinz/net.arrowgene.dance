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

package net.arrowgene.dance.server.game;

import net.arrowgene.dance.server.client.DanceClient;


public class ScoreUser {
    private DanceClient client;
    private int packetId = 0;
    private int unknown = 0;
    private int perfects = 0;
    private int cools = 0;
    private int bads = 0;
    private int misses = 0;
    private int maxCombo = 0;
    private int points = 0;
    private int energy = 0;
    private int place = -1;
    private boolean finished = false;

    public ScoreUser(DanceClient client) {
        this.client = client;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getCharacterPoints()
    {
        int points = 0;

        if(finished)
        {
            int notesCount = misses+bads+cools+perfects;
            if(misses+bads == 0)
                points = 100; // maximale Punkte
            else{
                points = 100 - ((misses+bads) / 20);
                if(points < 20)points = 20;
            }

            int activeUsers = this.client.getRoom().getActiveCount();
            points *= (activeUsers - getPlace()) * (int)(this.client.getCharacter().getLevel() * 1.1);
        }
        if(points < 0)
        {
            points = 0;
        }

        return points;
    }

    public int getCharacterCoins()
    {
        int coins = 0;
/*
        if(finished)
        {
            int activeUsers = this.client.getRoom().getActiveCount();
            if(misses+bads == 0)
            {
                coins = 1;
            }


            coins *= (activeUsers - getPlace()) * (int)(this.client.getCharacter().getLevel() * 1.1);
        }
*/
        return coins;
    }

    public int getCharacterExperience()
    {
        int xp = 0;

        if(finished)
        {
            int activeUsers = this.client.getRoom().getActiveCount();
            if(misses+bads == 0)
                xp = 30; // maximale Punkte
            else{
                xp = 30 - ((misses+bads) / 5);
                if(xp < 10)xp = 10;
            }


            xp *= (activeUsers - getPlace());// * (int)(this.client.getCharacter().getLevel() * 1.1);
        }

        return xp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("points: " + points);
        sb.append(" p: " + perfects);
        sb.append(" c: " + cools);
        sb.append(" b: " + bads);
        sb.append(" m: " + misses);
        sb.append(" c: " + maxCombo);
        sb.append(" e: " + energy);

        return sb.toString();
    }

    public void setUnknown(int unknown) {
        this.unknown = unknown;
    }

    public int getUnknown() {
        return unknown;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addPerfects(int perfects) {
        this.perfects += perfects;
    }

    public void addCools(int cools) {
        this.cools += cools;
    }

    public void addBads(int bads) {
        this.bads += bads;
    }

    public void addMisses(int misses) {
        this.misses += misses;
    }

    public int getBads() {
        return bads;
    }

    public int getCools() {
        return cools;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public void setMaxCombo(int maxCombo) {
        this.maxCombo = maxCombo;
    }

    public int getMisses() {
        return misses;
    }

    public int getPerfects() {
        return perfects;
    }

    public int getPoints() {
        return points;
    }

    public DanceClient getClient() {
        return client;
    }
}
