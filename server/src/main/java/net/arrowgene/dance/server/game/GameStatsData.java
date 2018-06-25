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
import net.arrowgene.dance.server.packet.Packet;


public class GameStatsData {
    private int packetId;
    private int perfects = 0;
    private int cools = 0;
    private int bads = 0;
    private int misses = 0;
    private int maxCombo = 0;
    private int energy = 0;
    private int unknown = 0;

    public GameStatsData(Packet packet) {
        packetId = packet.getInt32();
        unknown = packet.getInt32();
        perfects = packet.getInt32();
        cools = packet.getInt32();
        bads = packet.getInt32();
        misses = packet.getInt32();
        maxCombo = packet.getInt32();
        energy = packet.getInt32();
    }

    public int getUnknown() {
        return unknown;
    }

    public int getPoints()
    {
        int points = 0;
        // Berechne Punkte für dieses Statistikpaket

        /*
         * LUA Version
         * cGajoong = {2.0, 1.5, 1.0, 0.7, 0.0 }
         * cLevelGajoong = {1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2}
         * cLevelScore = {0, 600, 750, 900, 1050, 1200, 1350, 1600, 1850, 2100, 2350, 2600}
         * local curScore = cLevelGajoong[prevInput+1] * cLevelScore[prevInput+1] * cGajoong[m_currPanjung+1] * (combo + 1) / 2.0
         *
         *  5576      82   *   68 (40x4)
         *  5576 = Perfect * combo
         *
         *  11228 - 5546 = 5682
         *  5712 =    84   *   68
         *
         *  12238 - 11228 = 1010 + 10
         *  1020 =    15   *   68
         *
         *  5422 = (74 * 68 + 7 * 58) - 1 * 16
         *  8932 = 5422 + 3510
         *  3510 = (81 * X - (11 * 10)) + 3 * Z = 81X - 110 + 3Z =
         *  3620  = 81X + 3Z
         *  126,6 = 27X + Z
         *
         *  4536 = 4870 - (16 * 5) - (127 * 2)
         *
         *
         *
         *                             |Points     |Energy     |Perfects   |Cools      |Bads       |Misses     |
         *   SL|PacketId   |           | 2728      | 674       | 27        | 21        | 26        |  9        |
         *   00 01 00 00 00 00 00 00 00 a8 0a 00 00 a2 02 00 00 1b 00 00 00 15 00 00 00 1a 00 00 00 09 00 00 00 00 00 00
         *                             | 5546      |1000       | 79        |  3        |  0        |  0        |
         *   01 01 00 00 00 3e 0a 5f 40 aa 15 00 00 e8 03 00 00 4f 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *                             | 4536      | 961       | 52        | 23        |  5        |  2        |
         *   02 01 00 00 00 6b bc 48 40 b8 11 00 00 c1 03 00 00 34 00 00 00 17 00 00 00 05 00 00 00 02 00 00 00 00 00 00
         *                             | 5422      | 998       | 74        |  7        |  1        |  0        |
         *   03 01 00 00 00 6b bc 48 40 2e 15 00 00 e6 03 00 00 4a 00 00 00 07 00 00 00 01 00 00 00 00 00 00 00 00 00 00
         *
         *                             | 5368      | 340       | 32        | 17        | 23        | 10        |
         *   00 02 00 00 00 00 00 00 00 f8 14 00 00 54 01 00 00 20 00 00 00 11 00 00 00 17 00 00 00 0a 00 00 00 00 00 00
         *                             |11228      |1000       | 81        |  3        |  0        |  0        |
         *   01 02 00 00 00 46 b6 df 40 dc 2b 00 00 e8 03 00 00 51 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *                             | 8016      | 915       | 43        | 33        |  3        |  5        |
         *   02 02 00 00 00 3f 57 c9 40 50 1f 00 00 93 03 00 00 2b 00 00 00 21 00 00 00 03 00 00 00 05 00 00 00 00 00 00
         *                             | 8932      | 811       | 70        | 11        |  0        |  3        |
         *   03 02 00 00 00 00 00 00 00 e4 22 00 00 2b 03 00 00 46 00 00 00 0b 00 00 00 00 00 00 00 03 00 00 00 00 00 00
         *
         *                             | 5848      | 286       |  3        |  5        |  7        |  1        |
         *   00 03 00 00 00 00 00 00 00 d8 16 00 00 1e 01 00 00 03 00 00 00 05 00 00 00 07 00 00 00 01 00 00 00 00 00 00
         *                             |12238      |1000       | 14        |  1        |  0        |  0        |
         *   01 03 00 00 00 34 33 df 40 ce 2f 00 00 e8 03 00 00 0e 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *   02 03 00 00 00 48 e1 c8 40 ee 21 00 00 a5 03 00 00 09 00 00 00 05 00 00 00 01 00 00 00 00 00 00 00 00 00 00
         *   03 03 00 00 00 34 33 df 40 28 26 00 00 45 03 00 00 0b 00 00 00 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *
         *   00 04 00 00 00 00 00 00 00 58 1c 00 00 f5 00 00 00 0f 00 00 00 0e 00 00 00 05 00 00 00 02 00 00 00 00 00 00
         *   01 04 00 00 00 34 33 df 40 54 39 00 00 e8 03 00 00 23 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *   02 04 00 00 00 34 33 df 40 6a 28 00 00 e2 03 00 00 1a 00 00 00 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *   03 04 00 00 00 34 33 df 40 b8 2f 00 00 8d 03 00 00 24 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *
         *   00 05 00 00 00 00 00 00 00 e8 20 00 00 2b 00 00 00 0e 00 00 00 0a 00 00 00 06 00 00 00 07 00 00 00 00 00 00
         *   01 05 00 00 00 f8 53 df 40 1e 43 00 00 e8 03 00 00 24 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *   02 05 00 00 00 c5 fe c8 40 20 2f 00 00 da 03 00 00 1b 00 00 00 08 00 00 00 02 00 00 00 01 00 00 00 00 00 00
         *   03 05 00 00 00 f8 53 df 40 82 39 00 00 d6 03 00 00 24 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *
         *   00 06 00 00 00 00 00 00 00 6e 26 00 00 ba ff ff ff 0e 00 00 00 0e 00 00 00 07 00 00 00 04 00 00 00 00 00 00
         *   01 06 00 00 00 34 33 df 40 ee 4d 00 00 e8 03 00 00 27 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *   02 06 00 00 00 00 00 00 00 38 35 00 00 ca 03 00 00 0d 00 00 00 14 00 00 00 06 00 00 00 01 00 00 00 00 00 00
         *   03 06 00 00 00 34 33 df 40 5c 44 00 00 e8 03 00 00 28 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00
         *
         *   00 18 00 00 00 00 00 00 00 1e 29 00 00 6a ff ff ff 04 00 00 00 0a 00 00 00 05 00 00 00 03 00 00 00 00 00 00
         *
         *
         */
        int currentCombo = getMaxCombo();

        int currentComboValue = 10;
        if(currentCombo > 10)
            currentComboValue = currentCombo;

        if(currentComboValue > 68)
            currentComboValue = 68;


        points = getPerfects() * currentComboValue + (getCools() * currentComboValue - 10);



        return points;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" p: " + perfects);
        sb.append(" c: " + cools);
        sb.append(" b: " + bads);
        sb.append(" m: " + misses);
        sb.append(" c: " + maxCombo);
        sb.append(" e: " + energy);

        return sb.toString();
    }

    public int getPacketId() {
        return packetId;
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
}
