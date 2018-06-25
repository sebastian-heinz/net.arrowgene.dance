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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Log {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private LogType logType;
    private String text;
    private String account;
    private Date date;


    public Log(String text, LogType logType) {
        this.date = new Date();
        this.text = text;
        this.logType = logType;
        this.account = "";
    }

    public Log(String text, LogType logType, String account) {
        this(text, logType);
        this.account = account;
    }

    public LogType getLogType() {
        return logType;
    }

    @Override
    public String toString() {
        String result = "[" + DATE_FORMAT.format(this.date) + "]";
        result += "[" + this.logType.toString() + "]";
        if (this.account.length() > 0) {
            result += "(" + this.account + ")";
        }
        result += this.text;
        return result;
    }
}
