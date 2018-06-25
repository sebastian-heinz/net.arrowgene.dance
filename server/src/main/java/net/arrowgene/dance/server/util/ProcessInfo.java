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

package net.arrowgene.dance.server.util;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProcessInfo {

    public static int BYTE_PER_MB = 1048576;

    public static long getFreeMemoryMB() {
        return ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / BYTE_PER_MB;
    }

    public static long getTotalMemoryMB() {
        return Runtime.getRuntime().maxMemory() / BYTE_PER_MB;
    }

    public static long getUsedMemoryMB() {
        return ProcessInfo.getTotalMemoryMB() - ProcessInfo.getFreeMemoryMB();
    }

    public static String getMemoryStats() {
        long freeMem = ProcessInfo.getFreeMemoryMB();
        long totalMem = ProcessInfo.getTotalMemoryMB();
        return String.format("total: %s Mb; free: %s Mb; used: %s Mb", totalMem, freeMem, totalMem - freeMem);
    }

    public static int getThreadCount() {
        return ManagementFactory.getThreadMXBean().getThreadCount();
    }

    public static int getThreadPeakCount() {
        return ManagementFactory.getThreadMXBean().getPeakThreadCount();
    }

    public static long getThreadTotalCount() {
        return ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();
    }

    public static List<String> getThreadNames() {
        List<String> names = new ArrayList<>();
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            names.add(t.getName());
        }
        return names;
    }

    public static String getThreadStats() {
        long current = ProcessInfo.getThreadCount();
        long peak = ProcessInfo.getThreadPeakCount();
        long total = ProcessInfo.getThreadTotalCount();
        return String.format("current: %s threads; peak: %s threads; total started: %s threads", current, peak, total);
    }

    public static int getProcessorCount() {
        return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }

    public static double getSystemLoadAverage() {
        return ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
    }

    public static String getSystemStats() {
        long processors = ProcessInfo.getProcessorCount();
        double average = ProcessInfo.getSystemLoadAverage();
        return String.format("average system load: %s; processor count: %s", average, processors);
    }

}
