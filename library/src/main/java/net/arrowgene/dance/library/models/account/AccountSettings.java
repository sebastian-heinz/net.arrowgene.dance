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

package net.arrowgene.dance.library.models.account;


public class AccountSettings {
    private int accId;
    private int keyArrowUp;
    private int keyArrowLeft;
    private int keyArrowRight;
    private int keyArrowDown;

    private int volBackground;
    private int volSoundEffect;
    private int volGameMusic;

    private int notePanelTransparency;
    private int videoSoften;
    private int effectsScene;
    private int effectsAvatar;
    private int cameraView;
    private int rate;


    public AccountSettings(int userId) {
        this.accId = userId;
        this.keyArrowLeft = 30;
        this.keyArrowUp = 37;
        this.keyArrowDown = 31;
        this.keyArrowRight = 38;

        this.volBackground = 38;
        this.volSoundEffect = 38;
        this.volGameMusic = 38;

        this.notePanelTransparency = 0x7f;
        this.videoSoften = 0;
        this.effectsScene = 0;
        this.effectsAvatar = 0;
        this.cameraView = 0;
        this.rate = 4;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public int getCameraView() {
        return cameraView;
    }

    public void setCameraView(int cameraView) {
        this.cameraView = cameraView;
    }

    public int getEffectsAvatar() {
        return effectsAvatar;
    }

    public void setEffectsAvatar(int effectsAvatar) {
        this.effectsAvatar = effectsAvatar;
    }

    public int getEffectsScene() {
        return effectsScene;
    }

    public void setEffectsScene(int effectsScene) {
        this.effectsScene = effectsScene;
    }

    public int getKeyArrowDown() {
        return keyArrowDown;
    }

    public void setKeyArrowDown(int keyArrowDown) {
        this.keyArrowDown = keyArrowDown;
    }

    public int getKeyArrowLeft() {
        return keyArrowLeft;
    }

    public void setKeyArrowLeft(int keyArrowLeft) {
        this.keyArrowLeft = keyArrowLeft;
    }

    public int getKeyArrowRight() {
        return keyArrowRight;
    }

    public void setKeyArrowRight(int keyArrowRight) {
        this.keyArrowRight = keyArrowRight;
    }

    public int getKeyArrowUp() {
        return keyArrowUp;
    }

    public void setKeyArrowUp(int keyArrowUp) {
        this.keyArrowUp = keyArrowUp;
    }

    public int getNotePanelTransparency() {
        return notePanelTransparency;
    }

    public void setNotePanelTransparency(int notePanelTransparency) {
        this.notePanelTransparency = notePanelTransparency;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getVideoSoften() {
        return videoSoften;
    }

    public void setVideoSoften(int videoSoften) {
        this.videoSoften = videoSoften;
    }

    public int getVolBackground() {
        return volBackground;
    }

    public void setVolBackground(int volBackground) {
        this.volBackground = volBackground;
    }

    public int getVolGameMusic() {
        return volGameMusic;
    }

    public void setVolGameMusic(int volGameMusic) {
        this.volGameMusic = volGameMusic;
    }

    public int getVolSoundEffect() {
        return volSoundEffect;
    }

    public void setVolSoundEffect(int volSoundEffect) {
        this.volSoundEffect = volSoundEffect;
    }
}
