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

package net.arrowgene.dance.log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public abstract class Logger implements ILogger {

    protected static final DateFormat LOG_FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    protected static final String NEW_LINE = "\r\n";

    private final Object logsAccessLock = new Object();

    private String path;
    private ArrayList<Log> logs;
    private ArrayList<LogListener> listener;
    private ArrayList<LogType> printTypes;

    public Logger(String path) {
        this.path = path;
        this.logs = new ArrayList<>();
        this.listener = new ArrayList<>();
        this.printTypes = new ArrayList<>();
    }

    /**
     * Add a LogType to the console output.
     */
    public void addPrintType(LogType logType) {
        if (!this.printTypes.contains(logType)) {
            this.printTypes.add(logType);
        }
    }

    /**
     * Remove a LogType from the console output.
     */
    public void removePrintType(LogType logType) {
        this.printTypes.remove(logType);
    }

    /**
     * Clear the allowed LogTypes, causing all log types to be printed by the console.
     */
    public void clearPrintTypes() {
        this.printTypes.clear();
    }

    /**
     * Adds a listener who gets notified for every new log.
     */
    public void addListener(LogListener listener) {
        this.listener.add(listener);
    }

    public void removeListener(LogListener listener) {
        this.listener.remove(listener);
    }

    public void writeLog(Log log) {
        synchronized (logsAccessLock) {
            this.writeToConsole(log);
            this.logs.add(log);
            for (LogListener listener : this.listener) {
                listener.writeLog(log);
            }
        }
    }

    public void writeDebug(String text) {
        Log log = new Log(text, LogType.DEBUG);
        this.writeLog(log);
    }

    public void writeLog(LogType logType, String text) {
        Log log = new Log(text, logType);
        this.writeLog(log);
    }

    public void writeLog(LogType logType, String className, String methodName, String text) {
        text = className + "::" + methodName + ":" + text;
        Log log = new Log(text, logType);
        this.writeLog(log);
    }

    public void writeLog(LogType logType, String text, String account) {
        Log log = new Log(text, logType, account);
        this.writeLog(log);
    }

    public void writeLog(Throwable throwable) {
        String trace = "-no trace-";
        if (throwable != null) {
            trace = throwable.toString() + "\n";
            for (StackTraceElement el : throwable.getStackTrace()) {
                if (el != null) {
                    trace += "\t at " + el.toString() + "\n";
                }
            }
        }
        this.writeLog(LogType.ERROR, trace);
    }

    public void saveLogs() {

        ArrayList<Log> tmpPackets = new ArrayList<>();
        ArrayList<Log> tmpUnknownPackets = new ArrayList<>();
        ArrayList<Log> tmpLogs = new ArrayList<>();
        ArrayList<Log> chatLogs = new ArrayList<>();
        ArrayList<Log> combinedLogs = new ArrayList<>();

        synchronized (logsAccessLock) {
            for (Log log : this.logs) {
                combinedLogs.add(log);
                switch (log.getLogType()) {
                    case REQUEST_PACKET:
                    case RESPONSE_PACKET: {
                        tmpPackets.add(log);
                        break;
                    }
                    case UNKNOWN_PACKET: {
                        tmpUnknownPackets.add(log);
                        break;
                    }
                    case CHAT: {
                        chatLogs.add(log);
                        break;
                    }
                    default: {
                        tmpLogs.add(log);
                        break;
                    }
                }
            }
            this.logs.clear();
        }

        String date = LOG_FILE_DATE_FORMAT.format(new Date());
        this.writeToFile(date + "-CombinedLog.txt", combinedLogs);
        this.writeToFile(date + "-ServerLog.txt", tmpLogs);
        this.writeToFile(date + "-PacketLog.txt", tmpPackets);
        this.writeToFile(date + "-UnknownPacketLog.txt", tmpUnknownPackets);
        this.writeToFile(date + "-ChatLog.txt", chatLogs);
    }

    private void writeToConsole(Log log) {
        if (this.printTypes.size() == 0 || this.printTypes.contains(log.getLogType())) {
            System.out.println(log.toString());
        }
    }

    private void writeToFile(String fileName, ArrayList<Log> logs) {
        BufferedWriter writer = null;
        try {
            File file = new File(this.path, fileName);
            String filePath = file.getAbsolutePath();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "utf-8"));
            for (Log log : logs) {
                writer.write(log.toString());
                writer.newLine();
            }
            this.writeLog(LogType.INFO, "Logs saved to: " + filePath);
        } catch (IOException ex) {
            this.writeLog(ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception ex) {
                this.writeLog(ex);
            }
        }
    }

}
