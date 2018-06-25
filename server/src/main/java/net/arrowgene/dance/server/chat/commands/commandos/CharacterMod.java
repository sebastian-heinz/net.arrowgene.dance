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

package net.arrowgene.dance.server.chat.commands.commandos;

import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.library.models.wedding.WeddingState;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.chat.commands.ChatCommand;


public class CharacterMod implements ChatCommand {


    @Override
    public String[] exec(String[] command, DanceClient danceClient, DanceServer server) {

        String[] response = new String[1];
        boolean error = false;

        if (command.length > 2) {

            String property = command[1];
            Character character = danceClient.getCharacter();

            if (property.equals("level")) {
                character.setLevel(this.getValue(command[2]));
            } else if (property.equals("weight")) {
                character.setWeight(this.getValue(command[2]));
            } else if (property.equals("xp")) {
                character.setExperience(this.getValue(command[2]));
            } else if (property.equals("points")) {
                character.setPoints(this.getValue(command[2]));
            } else if (property.equals("coins")) {
                character.setCoins(this.getValue(command[2]));
            } else if (property.equals("bonus")) {
                character.setBonus(this.getValue(command[2]));
            } else if (property.equals("hearts")) {
                character.setHearts(this.getValue(command[2]));
            } else if (property.equals("loveready")) {
                character.setHearts(4);
                character.setLevel(10);
                character.setCoins(50000);
            } else if (property.equals("bride")) {
                if (danceClient.getWeddingRecord() != null) {
                    Character newBride = server.getCharacterManager().getCharacterByName(command[2]);
                    if (newBride != null) {
                        danceClient.getWeddingRecord().setBrideCharacterName(newBride.getName());
                        danceClient.getWeddingRecord().setBrideId(newBride.getCharacterId());
                    } else {
                        response[0] = "New Bride don't exist";
                        error = true;
                    }
                } else {
                    response[0] = "No Wedding Record";
                    error = true;
                }
            } else if (property.equals("groom")) {
                if (danceClient.getWeddingRecord() != null) {
                    Character newGroom = server.getCharacterManager().getCharacterByName(command[2]);
                    if (newGroom != null) {
                        danceClient.getWeddingRecord().setGroomCharacterName(newGroom.getName());
                        danceClient.getWeddingRecord().setGroomId(newGroom.getCharacterId());
                    } else {
                        response[0] = "New Groom don't exist";
                        error = true;
                    }
                } else {
                    response[0] = "No Wedding Record";
                    error = true;
                }
            } else if (property.equals("groom-state")) {
                WeddingState weddingState = WeddingState.getType(this.getValue(command[2], -1));
                if (weddingState != null) {
                    if (danceClient.getWeddingRecord() != null) {
                        danceClient.getWeddingRecord().setGroomState(weddingState);
                    } else {
                        response[0] = "No Wedding Record";
                        error = true;
                    }
                } else {
                    response[0] = "Unknown Wedding State (0=NOT_MARRIED | 1=ENGAGED | 2=DIVORCE | 3=MARRIED)";
                    error = true;
                }
            } else if (property.equals("bride-state")) {
                WeddingState weddingState = WeddingState.getType(this.getValue(command[2], -1));
                if (weddingState != null) {
                    if (danceClient.getWeddingRecord() != null) {
                        danceClient.getWeddingRecord().setBrideState(weddingState);
                    } else {
                        response[0] = "No Wedding Record";
                        error = true;
                    }
                } else {
                    response[0] = "Unknown Wedding State (0=NOT_MARRIED | 1=ENGAGED | 2=DIVORCE | 3=MARRIED)";
                    error = true;
                }
            } else if (property.equals("gender")) {
                CharacterSexTyp sexTyp = CharacterSexTyp.getType(this.getValue(command[2], -1));
                if (sexTyp != null && sexTyp != CharacterSexTyp.UNKNOWN) {
                    character.setSex(sexTyp);
                } else {
                    response[0] = "Unknown Gender (0=female | 1=male)";
                    error = true;
                }
            } else {
                response[0] = "Unknown property.";
                error = true;
            }
        } else if (command.length > 1 && command[1].equals("help")) {
            response = new String[2];
            response[0] = "Possible Commands:";
            response[1] = "level,weight,xp,points,coins,bonus,hearts,groom,bride,groom-state,bride-state,gender";
            error = true;
        } else {
            response[0] = "Bad commandos count. ex (/cm [Property] [Value])";
            error = true;
        }


        if (!error) {
            response[0] = "Updated [" + command[1] + "] new Value: " + command[2];
            danceClient.refreshCharacter();
        }


        return response;
    }

    private int getValue(String text) {
        int amount;
        try {
            amount = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            amount = 0;
        }

        return amount;
    }

    private int getValue(String text, int defaultValue) {
        int amount = defaultValue;
        try {
            amount = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            amount = defaultValue;
        }

        return amount;
    }

}
