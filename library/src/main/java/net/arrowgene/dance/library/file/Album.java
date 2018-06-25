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

import net.arrowgene.dance.library.common.ByteBuffer;
import net.arrowgene.dance.library.common.FileOp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Read/Write Album.dat
 */
public class Album {

    private List<Item> items;

    public Album() {
        items = new ArrayList<>();
    }

    public Album(String albumPath) throws Exception {
        this(Paths.get(albumPath));
    }

    public Album(Path albumPath) throws Exception {
        this();
        if (!Files.isRegularFile(albumPath)) {
            throw new Exception("Invalid albumPath");
        }
        byte[] file = FileOp.readFile(albumPath);
        ByteBuffer buffer = new ByteBuffer(file);
        // TODO
    }

    public List<Item> getItems() {
        return items;
    }

    public void add(Item item) {
        items.add(item);
    }

    public void remove(Item item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }


    public void save() {

    }

    public static class Item {

    }
}
