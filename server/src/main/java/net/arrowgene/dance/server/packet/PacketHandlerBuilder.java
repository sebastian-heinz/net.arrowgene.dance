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

package net.arrowgene.dance.server.packet;


import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.handle.*;

public class PacketHandlerBuilder {

    private DanceServer server;

    public PacketHandlerBuilder(DanceServer server) {
        this.server = server;
    }

    public PacketHandler build() {

        PacketHandler handler = new PacketHandler();

        handler.addHandle(PacketType.LOGIN_REQUEST_AUTHENTICATION, new _1000_x3E8_LOGIN_REQUEST_AUTHENTICATION(this.server));
        handler.addHandle(PacketType.LOGIN_REQUEST_CHANNEL_LIST, new _1002_x3EA_LOGIN_REQUEST_CHANNEL_LIST(this.server));
        handler.addHandle(PacketType.LOGIN_REQUEST_ENTER_LOBBY_FROM_CHANNEL_SELECTION, new _1004_x3EC_LOGIN_REQUEST_ENTER_LOBBY_FROM_CHANNEL_SELECTION(this.server));
        handler.addHandle(PacketType.LOGIN_REQUEST_CONTROLLER, new _1007_x3EF_LOGIN_REQUEST_CONTROLLER(this.server));
        handler.addHandle(PacketType.LOGIN_REQUEST_ENTER_CHANNEL_SELECTION_FROM_LOBBY, new _1009_x3F1_LOGIN_REQUEST_ENTER_CHANNEL_SELECTION_FROM_LOBBY(this.server));
        handler.addHandle(PacketType.LOGIN_REQUEST_DANCE_LESSON, new _1011_x3F3_LOGIN_REQUEST_DANCE_LESSON(this.server));
        handler.addHandle(PacketType.LOGIN_REQUEST_CREATE_CHARACTER, new _1015_x3F7_LOGIN_REQUEST_CREATE_CHARACTER(this.server));

        handler.addHandle(PacketType.LOBBY_REQUEST_CHARACTER_INFO, new _2000_x7D0_LOBBY_REQUEST_CHARACTER_INFO(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_ROOM_LIST, new _2002_x7D2_LOBBY_REQUEST_ROOM_LIST(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_CREATE_ROOM, new _2004_x7D4_LOBBY_REQUEST_CREATE_ROOM(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_ALL_LIST, new _2010_x7DA_LOBBY_REQUEST_ALL_LIST(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_MESSAGE_CHANNEL, new _2012_x7DC_LOBBY_REQUEST_MESSAGE_CHANNEL(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_MESSAGE_PRIVATE, new _2017_x7E1_LOBBY_REQUEST_MESSAGE_PRIVATE(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_PING, new _2029_x7ED_LOBBY_REQUEST_PING(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_BUDDY_LIST, new _2031_x7EF_LOBBY_REQUEST_BUDDY_LIST(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_BUDDY_ADD, new _2033_x7F1_LOBBY_REQUEST_BUDDY_ADD(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_BUDDY_DELETE, new _2035_x7F3_LOBBY_REQUEST_BUDDY_DELETE(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_ABOUT_ME, new _2037_x7F5_LOBBY_REQUEST_ABOUT_ME(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_SAVE_SETTINGS, new _2039_x7F7_LOBBY_REQUEST_SAVE_SETTINGS(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_LOAD_SETTINGS, new _2041_x7F9_LOBBY_REQUEST_LOAD_SETTINGS(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_SAVE_CHARACTER_SETTINGS, new _2043_x7FB_LOBBY_REQUEST_SAVE_CHARACTER_SETTINGS(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_ROOM_TIP, new _2048_x800_LOBBY_REQUEST_ROOM_TIP(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_TOP_MESSAGE_PLAYER_TO_PLAYER, new _2054_x806_LOBBY_REQUEST_TOP_MESSAGE_PLAYER_TO_PLAYER(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_INVITE_USER, new _2057_x809_LOBBY_REQUEST_INVITE_USER(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_BLACKLIST_LIST, new _2059_x80B_LOBBY_REQUEST_BLACKLIST_LIST(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_BLACKLIST_ADD, new _2061_x80D_LOBBY_REQUEST_BLACKLIST_ADD(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_ENTER_CHANNEL_SELECTION_FROM_LOGIN, new _2066_x812_LOBBY_REQUEST_ENTER_CHANNEL_SELECTION_FROM_LOGIN(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_UNKNOWN_1, new _2071_x817_LOBBY_REQUEST_UNKNOWN_1(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_UNKNOWN_2, new _2077_x81D_LOBBY_REQUEST_UNKNOWN_2(this.server));

        handler.addHandle(PacketType.LOBBY_REQUEST_COMPETITON_UNKNOWN_1, new _2500_x9C4_LOBBY_REQUEST_COMPETITION_UNKNOWN_1(this.server));
        handler.addHandle(PacketType.LOBBY_REQUEST_COMPETITON_JACKPOT, new _2509_x9CD_LOBBY_REQUEST_COMPETITION_JACKPOT(this.server));

        handler.addHandle(PacketType.ROOM_REQUEST_CHANGE_ROOMNAME, new _3000_xBB8_ROOM_REQUEST_CHANGE_ROOMNAME(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_JOIN_ROOM, new _3002_xBBA_ROOM_REQUEST_JOIN_ROOM(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_ENTER_LOBBY_FROM_ROOM, new _3005_xBBD_ROOM_REQUEST_ENTER_LOBBY_FROM_ROOM(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_MESSAGE_ROOM, new _3011_xBC3_ROOM_REQUEST_MESSAGE_ROOM(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_JOIN_ROOM_AFTER_GAME, new _3013_xBC5_ROOM_REQUEST_JOIN_ROOM_AFTER_GAME(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_ADD_FAVORITE_SONG, new _3015_xBC7_ROOM_REQUEST_ADD_FAVORITE_SONG(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_DELETE_FAVORITE_SONG, new _3017_xBC9_ROOM_REQUEST_DELETE_FAVORITE_SONG(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_FAVORITE_SONG_LIST, new _3019_xBCB_ROOM_REQUEST_FAVORITE_SONG_LIST(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_SET_VIEWER, new _3021_xBCD_ROOM_REQUEST_SET_VIEWER(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_FORMATION, new _3025_xBD1_ROOM_REQUEST_FORMATION(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_NUMBER_OF_VIEWERS, new _3027_xBD3_ROOM_REQUEST_NUMBER_OF_VIEWERS(this.server));
        handler.addHandle(PacketType.ROOM_REQUEST_ENTER_ROOM_1, new _3028_xBD4_ROOM_REQUEST_ENTER_ROOM_1(this.server));

        handler.addHandle(PacketType.GAMBLE_REQUEST_START, new _3500_xDAC_GAMBLE_REQUEST_START(this.server));

        handler.addHandle(PacketType.GAME_REQUEST_CHANGE_SONG, new _4000_xFA0_GAME_REQUEST_CHANGE_SONG(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_BACKGROUND, new _4002_xFA2_GAME_REQUEST_BACKGROUND(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_CHANGE_TEAM, new _4004_xFA4_GAME_REQUEST_CHANGE_TEAM(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_GAMEMODE, new _4006_xFA6_GAME_REQUEST_GAMEMODE(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_READY, new _4008_xFA8_GAME_REQUEST_READY(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_START_GAME, new _4010_xFAA_GAME_REQUEST_START_GAME(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_GAME_STATS, new _4014_xFAE_GAME_REQUEST_GAME_STATS(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_LOAD_STATS, new _4016_xFB0_GAME_REQUEST_LOAD_STATS(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_LOADING_READY, new _4027_xFBB_GAME_REQUEST_LOADING_READY(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_WALK, new _4033_xFC1_GAME_REQUEST_WALK(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_ENTER_ROOM_3, new _4039_xFC7_GAME_REQUEST_ENTER_ROOM_3(this.server));
        handler.addHandle(PacketType.GAME_REQUEST_BREAK_COMBO, new _4044_xFCC_GAME_REQUEST_BREAK_COMBO(this.server));

        handler.addHandle(PacketType.SHOP_REQUEST_BUY_ITEM, new _5002_x138A_SHOP_REQUEST_BUY_ITEM(this.server));
        handler.addHandle(PacketType.SHOP_REQUEST_DELETE_ITEM, new _5004_x138C_SHOP_REQUEST_DELETE_ITEM(this.server));
        handler.addHandle(PacketType.SHOP_REQUEST_DESTROY_ITEM, new _5006_x138E_SHOP_REQUEST_DESTROY_ITEM(this.server));
        handler.addHandle(PacketType.SHOP_REQUEST_GIFT_ITEM, new _5008_x1390_SHOP_REQUEST_GIFT_ITEM(this.server));
        handler.addHandle(PacketType.SHOP_REQUEST_ENTER, new _5012_x1394_SHOP_REQUEST_ENTER(this.server));
        handler.addHandle(PacketType.SHOP_REQUEST_PAY_BILL_EMAIL, new _5018_x139A_SHOP_PAY_BILL_EMAIL(this.server));

        handler.addHandle(PacketType.MAIL_REQUEST_MAIL_LIST, new _6000_x1770_MAIL_REQUEST_MAIL_LIST(this.server));
        handler.addHandle(PacketType.MAIL_REQUEST_READ_MAIL, new _6002_x1772_MAIL_REQUEST_READ_MAIL(this.server));
        handler.addHandle(PacketType.MAIL_REQUEST_SEND_MAIL, new _6004_x1774_MAIL_REQUEST_SEND_MAIL(this.server));
        handler.addHandle(PacketType.MAIL_REQUEST_DELETE_ONE, new _6006_x1776_MAIL_REQUEST_DELETE_ONE(this.server));
        handler.addHandle(PacketType.MAIL_REQUEST_DELETE_ALL, new _6008_x1778_MAIL_REQUEST_DELETE_ALL(this.server));
        handler.addHandle(PacketType.MAIL_REQUEST_ACCEPT_GIFT, new _6012_x177C_MAIL_REQUEST_ACCEPT_GIFT(this.server));

        handler.addHandle(PacketType.WEDDING_REQUEST_PROPOSE, new _6500_x1964_WEDDING_PROPOSE(this.server));
        handler.addHandle(PacketType.WEDDING_REQUEST_MAIL_DECISION, new _6502_x1966_WEDDING_MAIL_DECISION(this.server));
        handler.addHandle(PacketType.WEDDING_REQUEST_CANCEL_APPLICATION, new _6504_x1968_WEDDING_CANCEL_APPLICATION(this.server));
        handler.addHandle(PacketType.WEDDING_REQUEST_CHANGE_RING, new _6510_x196A_WEDDING_CHANGE_RING(this.server));
        handler.addHandle(PacketType.WEDDING_REQUEST_MAIL_CHANGE_RING, new _6510_x196E_WEDDING_MAIL_CHANGE_RING(this.server));
        handler.addHandle(PacketType.WEDDING_REQUEST_MY_APPLICATION, new _6512_x1970_WEDDING_MY_APPLICATION(this.server));

        handler.addHandle(PacketType.ITEM_REQUEST_ITEM_LIST, new _7000_x1B58_ITEM_REQUEST_ITEM_LIST(this.server));
        handler.addHandle(PacketType.ITEM_REQUEST_DRESS_ITEM, new _7002_x1B5A_ITEM_REQUEST_DRESS_ITEM(this.server));
        handler.addHandle(PacketType.ITEM_REQUEST_EXPAND_STORAGE, new _7008_x1B60_ITEM_REQUEST_EXPAND_STORAGE(this.server));
        handler.addHandle(PacketType.ITEM_REQUEST_ITEM_UNKNOWN_1, new _7012_x1B64_ITEM_REQUEST_ITEM_UNKNOWN_1(this.server));
        handler.addHandle(PacketType.ITEM_REQUEST_ITEM_UNKNOWN_2, new _7014_x1B66_ITEM_REQUEST_ITEM_UNKNOWN_2(this.server));

        handler.addHandle(PacketType.GROUP_REQUEST_AGREEMENT, new _7500_x1D4C_GROUP_REQUEST_AGREEMENT(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_CREATE, new _7502_x1D4E_GROUP_REQUEST_CREATE(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_DETAILS, new _7504_x1D50_GROUP_REQUEST_DETAILS(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_LEAVE, new _7506_x1D52_GROUP_REQUEST_LEAVE(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_MEMBER_LIST, new _7508_x1D54_GROUP_REQUEST_MEMBER_LIST(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_JOIN, new _7510_x1D56_GROUP_REQUEST_JOIN(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_DISMISS, new _7512_x1D58_GROUP_REQUEST_DISMISS(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_RULE_APPLICANT, new _7514_x1D5A_GROUP_REQUEST_RULE_APPLICANT(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_INFO, new _7516_x1D5C_GROUP_REQUEST_INFO(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_RANK_LIST, new _7517_x1D5E_GROUP_REQUEST_RANK_LIST(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_COUNTS, new _7523_x1D63_GROUP_REQUEST_COUNTS(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_CHECK_NAME, new _7525_x1D65_GROUP_REQUEST_CHECK_NAME(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_SEARCH_GROUP, new _7527_x1D67_GROUP_REQUEST_SEARCH_GROUP(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_CANCEL_APPLICATION, new _7531_x1D6B_GROUP_REQUEST_CANCEL_APPLICATION(this.server));
        handler.addHandle(PacketType.GROUP_REQUEST_REWRITE_BOARD, new _7533_x1D6D_GROUP_REQUEST_REWRITE_BOARD(this.server));

        handler.addHandle(PacketType.ITEM_REQUEST_UNKNOWN_UNKNOWN_1, new _9000_x2328_ITEM_REQUEST_UNKNOWN_UNKNOWN_1(this.server));

        handler.addHandle(PacketType.AWAY_REQUEST, new _10001_x2711_AWAY_REQUEST(this.server));

        return handler;
    }
}
