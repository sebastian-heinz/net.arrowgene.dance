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

package net.arrowgene.dance.gui.injector;

import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;
import net.arrowgene.dance.gui.server.ServerCreatedListener;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.tcp.ConnectedListener;
import net.arrowgene.dance.server.tcp.DisconnectedListener;
import net.arrowgene.dance.server.tcp.TcpClient;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerPacketInjector extends EditorFrame
        implements ActionListener, ServerCreatedListener, ConnectedListener, DisconnectedListener {

    private JPanel panel1;
    private JComboBox<DanceClient> targets;
    private JButton reloadClients;
    private JTextArea sendText;
    private JComboBox<PacketType> packetTypes;
    private JButton sendButton;
    private DanceServer server;

    public ServerPacketInjector(EditorConfig config) {
        super(config, "Packet Injector");
    }

    @Override
    public String getMenuCategory() {
        return "Server";
    }

    @Override
    public void menuClicked() {
        this.loadPacketTypes();
        this.loadTargets();
    }

    @Override
    public void init() {
        this.pack();
        this.setMinimumSize(new Dimension(600, 400));
        this.setSize(new Dimension(600, 400));
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);

        // this.panel1 = new JPanel();
        this.add(panel1);

        // this.reloadClients = new JButton();
        this.reloadClients.setActionCommand("reloadClients");
        this.reloadClients.addActionListener(this);

        //  this.sendButton = new JButton();
        this.sendButton.setActionCommand("send");
        this.sendButton.addActionListener(this);

        super.init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("reloadClients")) {
            loadTargets();
        } else if (e.getActionCommand().equals("send")) {

            DanceClient client = (DanceClient) targets.getSelectedItem();
            if (client != null) {
                if (packetTypes.getSelectedItem() instanceof PacketType) {
                    sendToTarget((DanceClient) targets.getSelectedItem(), (PacketType) packetTypes.getSelectedItem(),
                            sendText.getText());
                } else if (packetTypes.getSelectedItem() instanceof String) {
                    int packetId = -1;
                    String pId = (String) packetTypes.getSelectedItem();
                    try {
                        packetId = Short.parseShort(pId);
                    } catch (Exception ex) {
                        System.out.println("PacketInjector: '" + pId + "' is not a valid number.");
                    }
                    if (packetId >= 0) {
                        sendToTarget((DanceClient) targets.getSelectedItem(),
                                Short.parseShort((String) packetTypes.getSelectedItem()), sendText.getText());
                    }
                } else {
                    System.out.println("PacketInjector: Packet Type is not an instance of 'PacketType' or 'String'");
                }
            } else {
                System.out.println("PacketInjector: No Client Selected");
            }
        }
    }

    @Override
    public void clientConnected(TcpClient tcpClient) {
        this.loadTargets();
    }

    @Override
    public void clientDisconnected(TcpClient tcpClient) {
        this.loadTargets();
    }

    @Override
    public void serverCreated(DanceServer server) {
        this.server = server;
        this.server.getTcpServer().addConnectedListener(this);
        this.server.getTcpServer().addDisconnectedListener(this);
        this.loadTargets();
    }

    private void loadPacketTypes() {
        packetTypes.removeAllItems();
        for (PacketType t : PacketType.values()) {
            packetTypes.addItem(t);
        }
    }

    private void loadTargets() {
        targets.removeAllItems();
        if (this.server != null) {
            for (DanceClient t : this.server.getClientController().getClients()) {
                targets.addItem(t);
            }
        } else {
            System.out.println("PacketInjector: No Server Instance");
        }
    }

    private void sendToTarget(DanceClient client, PacketType packetType, String hexData) {
        SendPacket newPacket = new SendPacket(packetType);
        newPacket.addHEXString(hexData);
        client.sendPacket(newPacket);
    }

    private void sendToTarget(DanceClient client, short packetType, String hexData) {
        Packet newPacket = new Packet(packetType);
        newPacket.addHEXString(hexData);
        client.sendPacket(newPacket);
    }

}
