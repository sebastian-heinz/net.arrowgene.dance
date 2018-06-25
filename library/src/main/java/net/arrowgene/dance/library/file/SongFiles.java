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

package net.arrowgene.dance.library.file;

import net.arrowgene.dance.library.models.song.Song;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Import/Export songs
 */
public class SongFiles {

    private SongList songList;
    private SongEncode songEncode;
    private Path musicPath;

    public SongFiles(String rootDir) throws Exception {
        this(Paths.get(rootDir));
    }

    public SongFiles(Path rootPath) throws Exception {
        this(rootPath.resolve("/music/SongList.dat"), rootPath.resolve("/music/SongEncode.dat"), rootPath.resolve("/music"));
    }

    public SongFiles(String songlistPath, String songencodePath, String musicPath) throws Exception {
        this(Paths.get(songlistPath), Paths.get(songencodePath), Paths.get(musicPath));
    }

    public SongFiles(Path songlistPath, Path songencodePath, Path musicPath) throws Exception {
        this.musicPath = musicPath;
        if (!Files.isDirectory(this.musicPath)) {
            throw new Exception("Invalid musicPath");
        }
        songList = new SongList(songlistPath);
        songEncode = new SongEncode(songencodePath);
        System.out.println("EncodeItems: " + songEncode.getItems().size());
        System.out.println("SongListItems: " + songList.getItems().size());
    }

    /**
     * Creates a list of songs found
     */
    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();

        return songs;
    }

    /**
     * Exports songs from this SongFiles into another SongFiles target.
     */
    public void exportSongs(SongFiles target) {

    }

    public void importSongs(List<Item> importItems) {
        for (Item importItem : importItems) {
            importSong(importItem);
        }
    }

    public void importSong(Item importItem) {
        try {
            Files.copy(importItem.getGn(), musicPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(importItem.getSdm(), musicPath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(importItem.getExperSdm(), musicPath.resolve("/exper"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SongList.Item songlistItem = new SongList.Item();
        SongEncode.Item songencodeItem = new SongEncode.Item();

        songEncode.add(songencodeItem);
        songList.add(songlistItem);

        songEncode.save();
        songList.save();
    }
    // TODO Listen abgleichen und Song-Items erstellen -> StepFiles Daten laden
    // TODO später SongList exporter bauen
//        for (SongListItem songListItem : songListItems) {
//            Song tmpSong = null;
//            for (Song s : items) {
//                if (s.getSongId() == songListItem.getSongId()) {
//                    tmpSong = s;
//                    break;
//                }
//            }
//
//            byte[] key = getKeyFromSongEncode(songEncodeItems, songListItem);
//            if (key != null) {
//                //checke key an music-file
//                File sdmFile = new File(this.pathToGame + "music\\sdom" + String.format("%04d", songListItem.getFileId()) + ".sdm");
//                if (sdmFile.exists()) {
//                    byte[] sdmData = FileOp.readFile(this.pathToGame + "music\\sdom" + String.format("%04d", songListItem.getFileId()) + ".sdm");
//                    byte[] oggData = SongEncryption.decryptSDM(Arrays.copyOfRange(key, 0, 8), Arrays.copyOfRange(key, 8, key.length), sdmData);
//                    if (SongEncryption.isValidOgg(oggData)) {
//
//                        FileOp.writeFile(this.pathToGame + "music\\", "tmp.sdom" + String.format("%04d", songListItem.getFileId()) + ".ogg", oggData);
//
//
//                        if (tmpSong == null) {
//                            tmpSong = new Song();
//
//                            tmpSong.setName(songListItem.getName());
//                            tmpSong.setKey(key);
//                            tmpSong.setSongId(songListItem.getSongId());
//                            tmpSong.setFileId(songListItem.getFileId());
//
//                            items.add(tmpSong);
//                        }
//
//                        if (songListItem.getKeyType() == 0x00) {
//                            tmpSong.setNoteT(getSongNote(songListItem.getGnFile(), songListItem.getGnFileStart()));
//                        } else {
//                            tmpSong.setNoteK(getSongNote(songListItem.getGnFile(), songListItem.getGnFileStart()));
//                        }
//                    }
//                }
//            } else {
//                System.out.println("Not Found: " + songListItem.getName() + " (" + songListItem.getSongId() + ")");
//            }
//        }
//
//        ArrayList<Song> items2 = new ArrayList<Song>();
//        for (Song s : items) {
//            if (s.getNoteK() != null || s.getNoteT() != null) {
//                items2.add(s);
//            }
//        }
//
//        return items2.toArray(new Song[items2.size()]);
//    }
//
//    private SongNote getSongNote(String fileName, byte[] expectedData) {
//        SongNote tmpSongNote = null;
//
//        byte[] stepFileKey = getKeyFromGNFile(fileName, expectedData);
//        if (stepFileKey != null) {
//            byte[] stepFileCRC32 = getCRC32FromGNFile(fileName);
//            tmpSongNote = new SongNote(getFileSizeFromGNFile(fileName), Converter.getInt(stepFileCRC32), Converter.getInt(stepFileKey));
//        }
//
//        return tmpSongNote;
//    }
//
//    private int getFileSizeFromGNFile(String fileName) {
//        File fileKeyFile = new File(this.pathToGame + "music\\" + fileName);
//        if (fileKeyFile.exists()) {
//            byte[] keyFileData = FileOp.readFile(this.pathToGame + "music\\" + fileName);
//
//            return keyFileData.length;
//        }
//        return 0;
//    }
//
//    private byte[] getCRC32FromGNFile(String fileName) {
//        File fileKeyFile = new File(this.pathToGame + "music\\" + fileName);
//        if (fileKeyFile.exists()) {
//            byte[] keyFileData = FileOp.readFile(this.pathToGame + "music\\" + fileName);
//
//            if (keyFileData != null) {
//                byte[] crc32 = SongEncryption.createGNChecksum(keyFileData);
//
//                if (crc32 != null) {
//                    return crc32;
//                }
//            }
//        }
//        return null;
//    }
//
//    private byte[] getKeyFromGNFile(String fileName, byte[] expectedData) {
//        File fileKeyFile = new File(this.pathToGame + "music\\" + fileName);
//        if (fileKeyFile.exists()) {
//            byte[] keyFileData = FileOp.readFile(this.pathToGame + "music\\" + fileName);
//
//            if (keyFileData != null) {
//                byte[] keyFileTestKey = SongEncryption.crackKey(keyFileData, expectedData);
//
//                if (keyFileTestKey != null) {
//                    return keyFileTestKey;
//                }
//            }
//        }
//        return null;
//    }

//    private byte[] getKeyFromSongEncode(SongEncodeItem[] items, SongListItem song) {
//        for (SongEncodeItem item : items) {
//            if (item.getSongId() == song.getSongId()) {
//                return item.getKey();
//            }
//        }
//        return null;
//    }

    public static class Item {
        private Path gn;
        private Path sdm;
        private Path experSdm;

        public Path getGn() {
            return gn;
        }

        public void setGn(Path gn) {
            this.gn = gn;
        }

        public Path getSdm() {
            return sdm;
        }

        public void setSdm(Path sdm) {
            this.sdm = sdm;
        }

        public Path getExperSdm() {
            return experSdm;
        }

        public void setExperSdm(Path experSdm) {
            this.experSdm = experSdm;
        }
    }
}
