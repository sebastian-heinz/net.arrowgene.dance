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

package net.arrowgene.dance.database.sqlite.modules;

import net.arrowgene.dance.database.sqlite.SQLiteController;
import net.arrowgene.dance.database.sqlite.SQLiteFactory;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteWedding {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteWedding(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertWeddingRecords(List<WeddingRecord> weddingRecords) throws SQLException {

        PreparedStatement insert = this.controller
            .createPreparedStatement("INSERT INTO ag_wedding VALUES (?,?,?,?,?,?,?,?,?);");
        PreparedStatement update = this.controller.createPreparedStatement(
            "UPDATE ag_wedding SET " +
                "wedding_groom_id=?," +
                "wedding_bride_id=?," +
                "wedding_married_date=?," +
                "wedding_divorce_date=?," +
                "wedding_engage_date=?," +
                "wedding_groom_state=?," +
                "wedding_bride_state=?," +
                "wedding_ring=? " +
                "WHERE wedding_id=?;");

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

                int id = this.controller.getAutoIncrement(insert);
                weddingRecord.setId(id);

                insert.execute();
            }
        }

        insert.close();
        update.close();
    }


    public void insertWeddingRecord(WeddingRecord weddingRecord) throws SQLException {

        if (weddingRecord.getId() > -1) {
            PreparedStatement update = this.controller.createPreparedStatement(
                "UPDATE ag_wedding SET " +
                    "wedding_groom_id=?," +
                    "wedding_bride_id=?," +
                    "wedding_married_date=?," +
                    "wedding_divorce_date=?," +
                    "wedding_engage_date=?," +
                    "wedding_groom_state=?," +
                    "wedding_bride_state=?," +
                    "wedding_ring=? " +
                    "WHERE wedding_id=?;");
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
            PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT INTO ag_wedding VALUES (?,?,?,?,?,?,?,?,?);");
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

            int id = this.controller.getAutoIncrement(insert);
            weddingRecord.setId(id);

            insert.close();
        }
    }

    public List<WeddingRecord> getWeddingRecords() throws SQLException {
        List<WeddingRecord> weddingRecords = new ArrayList<WeddingRecord>();

        PreparedStatement select = this.controller.createPreparedStatement("SELECT " +
            "ag_wedding.wedding_id, " +
            "ag_wedding.wedding_groom_id, " +
            "ag_wedding.wedding_bride_id, " +
            "ag_wedding.wedding_married_date, " +
            "ag_wedding.wedding_divorce_date, " +
            "ag_wedding.wedding_engage_date, " +
            "ag_wedding.wedding_groom_state," +
            "ag_wedding.wedding_bride_state," +
            "ag_wedding.wedding_ring, " +
            "groom.character_name AS groom_character_name, " +
            "bride.character_name AS bride_character_name " +
            "FROM ag_wedding " +
            "INNER JOIN ag_character AS groom ON ag_wedding.wedding_groom_id = groom.character_id " +
            "INNER JOIN ag_character AS bride ON ag_wedding.wedding_bride_id = bride.character_id;");


        ResultSet rs = select.executeQuery();
        while (rs.next()) {
            WeddingRecord weddingRecord = this.factory.createWeddingRecord(rs);
            weddingRecords.add(weddingRecord);
        }
        rs.close();
        select.close();
        return weddingRecords;
    }

    public void deleteWeddingRecord(int weddingRecordId) throws SQLException {
        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM ag_wedding WHERE wedding_id=?");
        delete.setInt(1, weddingRecordId);
        delete.execute();
        delete.close();
    }

    public void deleteWeddingRecords(List<WeddingRecord> weddingRecords) throws SQLException {
        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM ag_wedding WHERE wedding_id=?");

        for (WeddingRecord weddingRecord : weddingRecords) {
            delete.clearParameters();
            delete.setInt(1, weddingRecord.getId());
            delete.execute();
        }
        delete.close();
    }

}
