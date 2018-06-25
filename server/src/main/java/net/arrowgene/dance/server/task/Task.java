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

package net.arrowgene.dance.server.task;


import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.log.Logger;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class Task implements Runnable {

    private int initialDelay;
    private int delay;
    private boolean repeat;
    private TimeUnit timeUnit;
    private String id;
    protected String name;
    protected Logger logger;

    /**
     * Creates a new task that executes instantly on time.
     */
    public Task(Logger logger) {
        this.logger = logger;
        this.repeat = false;
        this.initialDelay = 0;
        this.delay = 0;
        this.id = UUID.randomUUID().toString();
        this.timeUnit = TimeUnit.MINUTES;
    }

    /**
     * Creates a new task that executes one time.
     *
     * @param initialDelay time before the task runs for the first time.
     */
    public Task(int initialDelay, TimeUnit timeUnit, Logger logger) {
        this.logger = logger;
        this.repeat = false;
        this.initialDelay = initialDelay;
        this.delay = 0;
        this.id = UUID.randomUUID().toString();
        this.timeUnit = timeUnit;
    }

    /**
     * Creates a new task that executes repeatedly.
     *
     * @param initialDelay time before the task runs for the first time.
     * @param delay        time to wait after the tasks completion before running again.
     */
    public Task(int initialDelay, int delay, TimeUnit timeUnit, Logger logger) {
        this.logger = logger;
        this.repeat = true;
        this.initialDelay = initialDelay;
        this.delay = delay;
        this.id = UUID.randomUUID().toString();
        this.timeUnit = timeUnit;
    }

    public abstract String getName();

    public abstract void execute();

    public int getInitialDelay() {
        return initialDelay;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public String getId() {
        return id;
    }

    public String getIdentity() {
        return String.format("%s [%s]", this.getName(), this.getId());
    }

    @Override
    public void run() {
        this.logger.writeLog(LogType.SERVER, "Task", "run", "Started Task: " + this.getIdentity());
        this.execute();
        this.logger.writeLog(LogType.SERVER, "Task", "run", "Finished Task: " + this.getIdentity());
    }
}
