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

package net.arrowgene.dance.library.common;

import java.util.*;

public class ThreadSafeList<T> {

    private final Object listLock = new Object();
    private ArrayList<T> list;

    public ThreadSafeList() {
        this.list = new ArrayList<T>();
    }

    public ThreadSafeList(Collection<? extends T> list) {
        this.list = new ArrayList<T>(list);
    }

    public ThreadSafeList(ThreadSafeList<T> list) {
        this.list = list.getList();
    }

    public ArrayList<T> getList() {
        ArrayList<T> newList;
        synchronized (this.listLock) {
            newList = new ArrayList<T>(this.list);
        }
        return newList;
    }

    public T get(int index) {
        T item;
        synchronized (this.listLock) {
            item = this.list.get(index);
        }
        return item;
    }

    public T set(int index, T element) {
        T item;
        synchronized (this.listLock) {
            item = this.list.set(index, element);
        }
        return item;
    }

    public boolean add(T e) {
        boolean result;
        synchronized (this.listLock) {
            result = this.list.add(e);
        }
        return result;
    }

    public void add(int index, T element) {
        synchronized (this.listLock) {
            this.list.add(index, element);
        }
    }

    public T remove(int index) {
        T item;
        synchronized (this.listLock) {
            item = this.list.remove(index);
        }
        return item;
    }

    public boolean remove(T o) {
        boolean result;
        synchronized (this.listLock) {
            result = this.list.remove(o);
        }
        return result;
    }

}
