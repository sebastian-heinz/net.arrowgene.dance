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

package net.arrowgene.dance.server.lobby;


import net.arrowgene.dance.library.models.channel.ChannelDetails;
import net.arrowgene.dance.library.models.channel.ChannelType;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.channel.Channel;
import net.arrowgene.dance.log.LogType;

import java.util.ArrayList;
import java.util.List;


public class Lobby extends ServerComponent {

    private final Object channelManagerLock = new Object();
    private ArrayList<Channel> channels;

    /**
     * Creates a new instance of lobby which gives access to controller selection, dance lessons and
     * holds multiple channels to join.
     *
     * @param server The server instance.
     */
    public Lobby(DanceServer server) {
        super(server);
        this.channels = new ArrayList<Channel>();
    }

    @Override
    public void load() {
        synchronized (this.channelManagerLock) {
            this.channels.clear();
        }
        List<ChannelDetails> dbChannelDetails = super.getDatabase().getChannels();
        for (ChannelDetails channelDetails : dbChannelDetails) {
            this.addChannel(new Channel(channelDetails));
        }
    }

    @Override
    public void save() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void clientAuthenticated(DanceClient client) {

    }

    @Override
    public void clientDisconnected(DanceClient client) {

    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {
        synchronized (this.channelManagerLock) {
            getLogger().writeLog(LogType.DEBUG, "Lobby", "writeDebugInfo", "Channels: " + this.channels.size());
        }
    }

    public void addChannel(Channel channel) {
        if (channel.getDetails().getPosition() >= 0) {
            if (this.getChannel(channel.getDetails().getType(), channel.getDetails().getPosition()) != null) {
                super.getLogger().writeLog(LogType.WARNING, "ChannelDetails already exists");
            } else {
                synchronized (this.channelManagerLock) {
                    this.channels.add(channel);
                }
            }
        } else {
            super.getLogger().writeLog(LogType.WARNING, "Invalid ChannelDetails number");
        }
    }

    public void removeChannel(Channel channel) {
        synchronized (this.channelManagerLock) {
            this.channels.remove(channel);
        }
    }

    public Channel getChannel(ChannelType type, int position) {
        Channel channel = null;
        synchronized (this.channelManagerLock) {
            for (Channel c : channels) {
                if (c.getDetails().getType() == type && c.getDetails().getPosition() == position) {
                    channel = c;
                }
            }
        }
        return channel;
    }

    public ArrayList<Channel> getChannels() {
        ArrayList<Channel> channels;
        synchronized (this.channelManagerLock) {
            channels = new ArrayList<Channel>(this.channels);
        }
        return channels;
    }

}
