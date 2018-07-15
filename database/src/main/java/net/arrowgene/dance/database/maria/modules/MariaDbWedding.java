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

package net.arrowgene.dance.database.maria.modules;

import net.arrowgene.dance.database.maria.MariaDbController;
import net.arrowgene.dance.database.maria.MariaDbFactory;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDbWedding {

    private MariaDbController controller;
    private MariaDbFactory factory;

    public MariaDbWedding(MariaDbController controller, MariaDbFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertWeddingRecords(List<WeddingRecord> weddingRecords) throws SQLException {
        PreparedStatement insert = controller.createPreparedStatement("INSERT INTO dance_wedding VALUES (?,?,?,?,?,?,?,?,?);");
        PreparedStatement update = controller.createPreparedStatement(
            "UPDATE dance_wedding SET " +
                "groom_id=?," +
                "bride_id=?," +
                "married_date=?," +
                "divorce_date=?," +
                "engage_date=?," +
                "groom_state=?," +
                "bride_state=?," +
                "ring=? " +
                "WHERE id=?;");
        for (WeddingRecord weddingRecord : weddingRecords) {
            if (weddingRecord.getId() > -1) {
                update.setInt(1, weddingRecord.getGroomId());
                update.setInt(2, weddingRecord.getBrideId());
                update.setLong(3, weddingRecord.getMarriedDate());
                update.setLong(4, weddingRecord.getDivorceDate());
                update.setLong(5, weddingRecord.getEngageDate());
                update.setByte(6, (byte) weddingRecord.getGroomState().getNumValue());
                update.setByte(7, (byte) weddingRecord.getBrideState().getNumValue());
                update.setByte(8, (byte) weddingRecord.getRingType().getNumValue());
                update.setInt(9, weddingRecord.getId());
                update.execute();
            } else {
                insert.setInt(2, weddingRecord.getGroomId());
                insert.setInt(3, weddingRecord.getBrideId());
                insert.setLong(4, weddingRecord.getMarriedDate());
                insert.setLong(5, weddingRecord.getDivorceDate());
                insert.setLong(6, weddingRecord.getEngageDate());
                insert.setByte(7, (byte) weddingRecord.getGroomState().getNumValue());
                insert.setByte(8, (byte) weddingRecord.getBrideState().getNumValue());
                insert.setByte(9, (byte) weddingRecord.getRingType().getNumValue());
                int id = controller.getAutoIncrement(insert);
                weddingRecord.setId(id);
                insert.execute();
            }
        }
        insert.close();
        update.close();
    }


    public void insertWeddingRecord(WeddingRecord weddingRecord) throws SQLException {
        if (weddingRecord.getId() > -1) {
            PreparedStatement update = controller.createPreparedStatement(
                "UPDATE dance_wedding SET " +
                    "groom_id=?," +
                    "bride_id=?," +
                    "married_date=?," +
                    "divorce_date=?," +
                    "engage_date=?," +
                    "groom_state=?," +
                    "bride_state=?," +
                    "ring=? " +
                    "WHERE id=?;");
            update.clearParameters();
            update.setInt(1, weddingRecord.getGroomId());
            update.setInt(2, weddingRecord.getBrideId());
            update.setLong(3, weddingRecord.getMarriedDate());
            update.setLong(4, weddingRecord.getDivorceDate());
            update.setLong(5, weddingRecord.getEngageDate());
            update.setByte(6, (byte) weddingRecord.getGroomState().getNumValue());
            update.setByte(7, (byte) weddingRecord.getBrideState().getNumValue());
            update.setByte(8, (byte) weddingRecord.getRingType().getNumValue());
            update.setInt(9, weddingRecord.getId());
            update.execute();
        } else {
            PreparedStatement insert = controller.createPreparedStatement("INSERT INTO dance_wedding VALUES (?,?,?,?,?,?,?,?,?);");
            insert.clearParameters();
            insert.setInt(2, weddingRecord.getGroomId());
            insert.setInt(3, weddingRecord.getBrideId());
            insert.setLong(4, weddingRecord.getMarriedDate());
            insert.setLong(5, weddingRecord.getDivorceDate());
            insert.setLong(6, weddingRecord.getEngageDate());
            insert.setByte(7, (byte) weddingRecord.getGroomState().getNumValue());
            insert.setByte(8, (byte) weddingRecord.getBrideState().getNumValue());
            insert.setByte(9, (byte) weddingRecord.getRingType().getNumValue());
            insert.execute();
            int id = controller.getAutoIncrement(insert);
            weddingRecord.setId(id);
            insert.close();
        }
    }

    public List<WeddingRecord> getWeddingRecords() throws SQLException {
        List<WeddingRecord> weddingRecords = new ArrayList<>();
        PreparedStatement select = controller.createPreparedStatement("SELECT " +
            "dance_wedding.id, " +
            "dance_wedding.groom_id, " +
            "dance_wedding.bride_id, " +
            "dance_wedding.married_date, " +
            "dance_wedding.divorce_date, " +
            "dance_wedding.engage_date, " +
            "dance_wedding.groom_state," +
            "dance_wedding.bride_state," +
            "dance_wedding.ring, " +
            "groom.name AS groom_character_name, " +
            "bride.name AS bride_character_name " +
            "FROM dance_wedding " +
            "INNER JOIN dance_character AS groom ON dance_wedding.groom_id = groom.id " +
            "INNER JOIN dance_character AS bride ON dance_wedding.bride_id = bride.id;");
        ResultSet rs = select.executeQuery();
        while (rs.next()) {
            WeddingRecord weddingRecord = factory.createWeddingRecord(rs);
            weddingRecords.add(weddingRecord);
        }
        rs.close();
        select.close();
        return weddingRecords;
    }

    public void deleteWeddingRecord(int weddingRecordId) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM dance_wedding WHERE id=?;");
        delete.setInt(1, weddingRecordId);
        delete.execute();
        delete.close();
    }

    public void deleteWeddingRecords(List<WeddingRecord> weddingRecords) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM dance_wedding WHERE id=?;");
        for (WeddingRecord weddingRecord : weddingRecords) {
            delete.clearParameters();
            delete.setInt(1, weddingRecord.getId());
            delete.execute();
        }
        delete.close();
    }
}
