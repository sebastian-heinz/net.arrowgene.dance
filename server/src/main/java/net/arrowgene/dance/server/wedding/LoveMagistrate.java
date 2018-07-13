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

package net.arrowgene.dance.server.wedding;

import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.library.models.wedding.RingType;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;
import net.arrowgene.dance.library.models.wedding.WeddingState;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.client.DanceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Manages wedding records.
 */
public class LoveMagistrate extends ServerComponent {


    private static final Logger logger = LogManager.getLogger(LoveMagistrate.class);

    private static final int MAX_TEXT_LENGTH = 100;
    private static final int MIN_LEVEL = 10;
    private static final int MIN_HEARTS = 4; // (1 = 1/2 Heart)

    private static final int ID_LOVE_FLOWER_M = 3801;
    private static final int ID_GOLD_HEART_M = 3802;
    private static final int ID_PURITY_M = 3803;
    private static final int ID_RUBY_PASSION_M = 3804;
    private static final int ID_FOREVER_DIAMOND_M = 3805;
    private static final int ID_OCEAN_HEART_M = 3806;

    private static final int ID_GOLD_HEART_F = 3807;
    private static final int ID_PURITY_F = 3808;
    private static final int ID_RUBY_PASSION_F = 3809;
    private static final int ID_FOREVER_DIAMOND_F = 3810;
    private static final int ID_OCEAN_HEART_F = 3811;
    private static final int ID_LOVE_FLOWER_F = 3812;

    private final Object weddingRecordsLock = new Object();

    private List<WeddingRecord> weddingRecords;

    public LoveMagistrate(DanceServer server) {
        super(server);
        this.weddingRecords = new ArrayList<>();
    }

    @Override
    public void load() {
        this.weddingRecords = super.getDatabase().getWeddingRecords();
    }

    @Override
    public void save() {
        this.cleanWeddingRecords();
        super.getDatabase().syncWeddingRecords(this.getWeddingRecords());
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void clientAuthenticated(DanceClient client) {
        if (client.getCharacter() != null) {
            WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(client.getCharacter().getCharacterId());
            client.setWeddingRecord(weddingRecord);
        }
    }

    @Override
    public void clientDisconnected(DanceClient client) {

    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {
        synchronized (this.weddingRecordsLock) {
            logger.debug(String.format("Wedding Records: %d", weddingRecords.size()));
        }
    }

    /**
     * A list of all wedding records.
     *
     * @return All wedding records.
     */
    public ArrayList<WeddingRecord> getWeddingRecords() {
        ArrayList<WeddingRecord> weddingRecords;
        synchronized (this.weddingRecordsLock) {
            weddingRecords = new ArrayList<WeddingRecord>(this.weddingRecords);
        }
        return weddingRecords;
    }

    /**
     * Get a wedding record by the grooms or brides character id.
     *
     * @param characterId Id of grooms or brides character.
     * @return The relevant wedding record.
     */
    public WeddingRecord getWeddingRecordByCharacterId(int characterId) {
        WeddingRecord result = null;
        ArrayList<WeddingRecord> weddingRecords = this.getWeddingRecords();
        for (WeddingRecord weddingRecord : weddingRecords) {
            if (weddingRecord.getBrideId() == characterId || weddingRecord.getGroomId() == characterId) {
                result = weddingRecord;
                break;
            }
        }
        return result;
    }

    /**
     * Get a wedding record by the grooms or brides character name.
     *
     * @param characterName Name of grooms or brides character.
     * @return The relevant wedding record.
     */
    public WeddingRecord getWeddingRecordByCharacterName(String characterName) {
        WeddingRecord result = null;
        ArrayList<WeddingRecord> weddingRecords = this.getWeddingRecords();
        for (WeddingRecord weddingRecord : weddingRecords) {
            if (weddingRecord.getBrideCharacterName().equals(characterName) || weddingRecord.getGroomCharacterName().equals(characterName)) {
                result = weddingRecord;
                break;
            }
        }
        return result;
    }

    /**
     * Add a wedding record.
     *
     * @param weddingRecord Record to be added.
     */
    public void addWeddingRecord(WeddingRecord weddingRecord) {
        synchronized (this.weddingRecordsLock) {
            this.weddingRecords.add(weddingRecord);
        }
    }

    /**
     * Remove a wedding record.
     *
     * @param weddingRecord Record to be removed.
     */
    public void removeWeddingRecord(WeddingRecord weddingRecord) {
        synchronized (this.weddingRecordsLock) {
            this.weddingRecords.remove(weddingRecord);
        }
        DanceClient bride = super.server.getClientController().getClientByCharacterId(weddingRecord.getBrideId());
        if (bride != null) {
            bride.setWeddingRecord(null);
        }
        DanceClient groom = super.server.getClientController().getClientByCharacterId(weddingRecord.getGroomId());
        if (groom != null) {
            groom.setWeddingRecord(null);
        }
    }

    /**
     * Propose to another player.
     *
     * @param client      Client who makes the propose.
     * @param partnerName Character name of the partner to propose to.
     * @param message     Message to send to the partner.
     * @param ringType    Type of ring for wedding.
     * @return Enum indicating the success or failure.
     */
    public ProposeMsg propose(DanceClient client, String partnerName, String message, RingType ringType) {
        if (message.length() > MAX_TEXT_LENGTH) {
            return ProposeMsg.TEXT_OVER_LENGTH;
        }
        if (client.getCharacter().getLevel() < MIN_LEVEL) {
            return ProposeMsg.LEVEL_TO_LOW;
        }
        DanceClient partnerClient = super.server.getClientController().getClientByCharacterName(partnerName);
        if (partnerClient == null) {
            return ProposeMsg.PLAYER_UNAVAILABLE;
        }
        WeddingRecord existingRecord = client.getWeddingRecord();
        if (existingRecord != null) {
            WeddingState myState = client.getWeddingRecord().getWeddingState(client.getCharacter().getCharacterId());
            if (myState == WeddingState.NOT_MARRIED) {
                if (existingRecord.getDivorceDate() > 0) {
                    // An existing wedding record with a divorce date will disappear after 7 days.
                    // Until then you can not marry again
                    return ProposeMsg.YOU_CANNOT_MARRY_SO_SOON_AFTER_DIVORCE_WAIT_7_DAYS;
                } else {
                    int characterId = client.getCharacter().getCharacterId();
                    int partnerCharacterId = partnerClient.getCharacter().getCharacterId();
                    if (existingRecord.isMatchingRecord(characterId, partnerCharacterId)) {
                        // Two player proposed to each other, marry them.
                        existingRecord.setBrideState(WeddingState.MARRIED);
                        existingRecord.setGroomState(WeddingState.MARRIED);
                        existingRecord.setMarriedDate(DanceServer.getUnixTimeNow());
                        return ProposeMsg.OK;
                    } else {
                        // Client got a propose, but proposes to someone else, remove the existing record
                        // and notify both parties that the existing proposal expired.
                        // A new proposal will be created with the new partner if all checks pass.
                        super.server.getPostOffice().sendProposeExpireNotice(characterId, existingRecord.getPartnerCharacterId(characterId));
                        this.removeWeddingRecord(existingRecord);
                    }
                }
            } else {
                return ProposeMsg.YOU_ARE_ALREADY_MARRIED_DIVORCE_FIRST;
            }
        }
        if (client.getCharacter().getHearts() < MIN_HEARTS) {
            return ProposeMsg.USE_LOVER_MODE_TO_REACH_REQUIRED_CHARM_LEVEL;
        }
        if (partnerClient.getWeddingRecord() != null) {
            return ProposeMsg.YOUR_PARTNER_IS_ALREADY_MARRIED;
        }
        boolean matchingSex = this.isSexMatching(client.getCharacter().getSex(), partnerClient.getCharacter().getSex());
        if (!matchingSex) {
            return ProposeMsg.PARTNER_MUST_BE_OPPOSITE_SEX;
        }
        if (partnerClient.getCharacter().getHearts() < MIN_HEARTS) {
            return ProposeMsg.USE_LOVER_MODE_TO_REACH_REQUIRED_CHARM_LEVEL;
        }
        WeddingRecord record = new WeddingRecord();
        int ringId = -1;
        int partnerRingId = -1;
        if (client.getCharacter().getSex() == CharacterSexTyp.MALE) {
            ringId = this.getMaleRingId(ringType);
            partnerRingId = this.getFemaleRingId(ringType);
            record.setGroomId(client.getCharacter().getCharacterId());
            record.setGroomCharacterName(client.getCharacter().getName());
            record.setBrideId(partnerClient.getCharacter().getCharacterId());
            record.setBrideCharacterName(partnerClient.getCharacter().getName());
            record.setGroomState(WeddingState.ENGAGED);
            record.setBrideState(WeddingState.NOT_MARRIED);
        } else {
            ringId = this.getFemaleRingId(ringType);
            partnerRingId = this.getMaleRingId(ringType);
            record.setBrideId(client.getCharacter().getCharacterId());
            record.setBrideCharacterName(client.getCharacter().getName());
            record.setGroomId(partnerClient.getCharacter().getCharacterId());
            record.setGroomCharacterName(partnerClient.getCharacter().getName());
            record.setGroomState(WeddingState.NOT_MARRIED);
            record.setBrideState(WeddingState.ENGAGED);
        }
        InventoryItem ring = client.getInventory().getItemByShopItemId(ringId);
        if (ring == null) {
            return ProposeMsg.YOU_NEED_WEDDING_RING;
        }
        InventoryItem partnerRing = partnerClient.getInventory().getItemByShopItemId(partnerRingId);
        if (partnerRing == null) {
            return ProposeMsg.WEDDING_RING_DOES_NOT_MATCH;
        }
        boolean matching = this.isRingMatching(ring.getShopItem().getId(), partnerRing.getShopItem().getId());
        if (!matching) {
            return ProposeMsg.WEDDING_RING_DOES_NOT_MATCH;
        }
        record.setEngageDate(DanceServer.getUnixTimeNow());
        record.setRingType(ringType);
        this.addWeddingRecord(record);
        client.setWeddingRecord(record);
        partnerClient.setWeddingRecord(record);
        super.server.getPostOffice().sendMailPropose(client.getCharacter().getCharacterId(),
            partnerClient.getCharacter().getCharacterId(), message, partnerRing.getShopItem().getId());
        return ProposeMsg.OK;
    }

    /**
     * Cancel a propose.
     *
     * @param characterId Id of character who cancelled the propose
     * @return Enum indicating the success or failure.
     */
    public CancelProposeMsg cancelPropose(int characterId) {
        WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(characterId);
        if (weddingRecord != null) {
            this.removeWeddingRecord(weddingRecord);
            return CancelProposeMsg.MARRIAGE_APPLICATION_CANCELED;
        }
        return CancelProposeMsg.NO_MARRIAGE_APPLICATION;
    }

    /**
     * Accept a propose.
     *
     * @param characterId Id of character who accepted the propose.
     * @return Enum indicating the success or failure.
     */
    public AcceptProposeMsg acceptPropose(int characterId) {
        WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(characterId);
        if (weddingRecord != null) {
            WeddingState myState = weddingRecord.getWeddingState(characterId);
            WeddingState partnerState = weddingRecord.getPartnerWeddingState(characterId);
            if (myState == WeddingState.NOT_MARRIED && partnerState == WeddingState.ENGAGED) {
                weddingRecord.setBrideState(WeddingState.MARRIED);
                weddingRecord.setGroomState(WeddingState.MARRIED);
                weddingRecord.setMarriedDate(DanceServer.getUnixTimeNow());
                return AcceptProposeMsg.PROPOSAL_ACCEPTED;
            }
        }
        return AcceptProposeMsg.NICKNAME_INVALID;
    }

    /**
     * Reject a propose.
     *
     * @param characterId Id of character who rejected the propose.
     * @return Enum indicating the success or failure.
     */
    public RejectProposeMsg rejectPropose(int characterId) {
        WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(characterId);
        if (weddingRecord != null) {
            WeddingState myState = weddingRecord.getWeddingState(characterId);
            WeddingState partnerState = weddingRecord.getPartnerWeddingState(characterId);
            if (myState == WeddingState.NOT_MARRIED && partnerState == WeddingState.ENGAGED) {
                this.removeWeddingRecord(weddingRecord);
                return RejectProposeMsg.MARRIAGE_APPLICATION_CANCELED;
            }
        }
        return RejectProposeMsg.WRONG_SERVER;
    }

    /**
     * Request a divorce
     *
     * @param characterId Id of character who initiated of the divorce request.
     * @return Enum indicating the success or failure.
     */
    public DivorceMsg divorce(int characterId, String message) {
        WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(characterId);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        long divorceDateUnixTime = calendar.getTimeInMillis() / 1000;
        weddingRecord.setDivorceDate(divorceDateUnixTime);
        weddingRecord.setWeddingState(characterId, WeddingState.DIVORCE);
        super.server.getPostOffice().sendMailDivorce(characterId, weddingRecord.getPartnerCharacterId(characterId), message);
        return DivorceMsg.OK_YOUR_DIVORCE_APPLICATION_HAS_BEEN_SEND;
    }

    /**
     * Cancel a divorce request.
     *
     * @param characterId Id of character who canceled the divorce request.
     * @return Enum indicating the success or failure.
     */
    public CancelDivorceMsg cancelDivorce(int characterId) {
        WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(characterId);
        if (weddingRecord != null) {
            weddingRecord.setDivorceDate(-1);
            weddingRecord.setWeddingState(characterId, WeddingState.MARRIED);
            return CancelDivorceMsg.MARRIAGE_APPLICATION_CANCELED;
        }
        return CancelDivorceMsg.NO_MARRIAGE_APPLICATION;
    }

    /**
     * Accept a divorce request.
     *
     * @param characterId Id of character who accept the divorce request.
     * @return Enum indicating the success or failure.
     */
    public AcceptDivorceMsg acceptDivorce(int characterId) {
        WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(characterId);
        if (weddingRecord != null) {
            WeddingState myState = weddingRecord.getWeddingState(characterId);
            WeddingState partnerState = weddingRecord.getPartnerWeddingState(characterId);
            if (myState == WeddingState.MARRIED && partnerState == WeddingState.DIVORCE) {
                weddingRecord.setGroomState(WeddingState.NOT_MARRIED);
                weddingRecord.setBrideState(WeddingState.NOT_MARRIED);
                return AcceptDivorceMsg.YOU_ARE_NOW_DIVORCE_FROM;
            }
        }
        return AcceptDivorceMsg.YOU_ARE_NOW_DIVORCE_FROM;
        // TODO find an error msg
    }

    /**
     * Reject a divorce.
     * <p>
     * You can not stop a divorce.
     * A rejected divorce will delay the divorce by 7 days.
     *
     * @param characterId Id of character who rejects the divorce request.
     * @return Enum indicating the success or failure.
     */
    public RejectDivorceMsg rejectDivorce(int characterId) {
        WeddingRecord weddingRecord = this.getWeddingRecordByCharacterId(characterId);
        if (weddingRecord != null) {
        }
        return RejectDivorceMsg.REJECTED_DIVORCE_MARRIED_TERMINATED_IN_7_DAYS;
    }

    /**
     * Request to change the wedding ring.
     *
     * @param characterId Id of character who requests the change.
     * @param message     A message to send to the partner.
     * @param newRing     The new ring to wear.
     * @return Enum indicating the success or failure.
     */
    public ChangeRingMsg changeRing(int characterId, String message, RingType newRing) {
        return ChangeRingMsg.DONE_WAIT_FOR_PARTNER_RESPONSE;
    }

    /**
     * Accept a request to change the wedding ring.
     *
     * @param characterId Id of character who accepted the request.
     * @return Enum indicating the success or failure.
     */
    public AcceptChangeRingMsg acceptChangeRing(int characterId) {
        return AcceptChangeRingMsg.ACCEPTED;
    }

    /**
     * Reject a request to change the wedding ring.
     *
     * @param characterId Id of character who rejected the ring change.
     * @return Enum indicating the success or failure.
     */
    public RejectChangeRingMsg rejectChangeRing(int characterId) {
        return RejectChangeRingMsg.REJECTED;
    }

    public int getMaleRingId(RingType ringType) {
        switch (ringType) {
            case LOVE_FLOWER:
                return ID_LOVE_FLOWER_M;
            case PURITY:
                return ID_PURITY_M;
            case GOLD_HEART:
                return ID_GOLD_HEART_M;
            case RUBY_PASSION:
                return ID_RUBY_PASSION_M;
            case FOREVER_DIAMOND:
                return ID_FOREVER_DIAMOND_M;
            case OCEAN_HEART:
                return ID_OCEAN_HEART_M;
        }
        return -1;
    }

    public int getFemaleRingId(RingType ringType) {
        switch (ringType) {
            case LOVE_FLOWER:
                return ID_LOVE_FLOWER_F;
            case PURITY:
                return ID_PURITY_F;
            case GOLD_HEART:
                return ID_GOLD_HEART_F;
            case RUBY_PASSION:
                return ID_RUBY_PASSION_F;
            case FOREVER_DIAMOND:
                return ID_FOREVER_DIAMOND_F;
            case OCEAN_HEART:
                return ID_OCEAN_HEART_F;
        }
        return -1;
    }

    /**
     * After 7 Days the wedding record is removed to allow remarriage.
     */
    private void cleanWeddingRecords() {
        List<WeddingRecord> weddingRecords = this.getWeddingRecords();
        long unixTimeNow = DanceServer.getUnixTimeNow();
        for (WeddingRecord weddingRecord : weddingRecords) {
            if (weddingRecord.getDivorceDate() > 0 && weddingRecord.getDivorceDate() < unixTimeNow) {
                this.removeWeddingRecord(weddingRecord);
            }
        }
    }

    private boolean isRingMatching(int ringIdA, int ringIdB) {

        if (ringIdA == ID_LOVE_FLOWER_F && ringIdB == ID_LOVE_FLOWER_M
            || ringIdA == ID_LOVE_FLOWER_M && ringIdB == ID_LOVE_FLOWER_F) {
            return true;
        }
        if (ringIdA == ID_PURITY_F && ringIdB == ID_PURITY_M || ringIdA == ID_PURITY_M && ringIdB == ID_PURITY_F) {
            return true;
        }
        if (ringIdA == ID_GOLD_HEART_F && ringIdB == ID_GOLD_HEART_M
            || ringIdA == ID_GOLD_HEART_M && ringIdB == ID_GOLD_HEART_F) {
            return true;
        }
        if (ringIdA == ID_RUBY_PASSION_F && ringIdB == ID_RUBY_PASSION_M
            || ringIdA == ID_RUBY_PASSION_M && ringIdB == ID_RUBY_PASSION_F) {
            return true;
        }
        if (ringIdA == ID_FOREVER_DIAMOND_F && ringIdB == ID_FOREVER_DIAMOND_M
            || ringIdA == ID_FOREVER_DIAMOND_M && ringIdB == ID_FOREVER_DIAMOND_F) {
            return true;
        }
        if (ringIdA == ID_OCEAN_HEART_F && ringIdB == ID_OCEAN_HEART_M
            || ringIdA == ID_OCEAN_HEART_M && ringIdB == ID_OCEAN_HEART_F) {
            return true;
        }

        return false;
    }

    private boolean isSexMatching(CharacterSexTyp sexA, CharacterSexTyp sexB) {

        if (sexA == CharacterSexTyp.FEMALE && sexB == CharacterSexTyp.MALE) {
            return true;
        }

        if (sexA == CharacterSexTyp.MALE && sexB == CharacterSexTyp.FEMALE) {
            return true;
        }

        return false;
    }

}
