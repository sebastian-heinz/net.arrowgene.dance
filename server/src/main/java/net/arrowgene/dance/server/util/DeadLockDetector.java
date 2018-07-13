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

/*
 * Copyright (C) 2004-2016 L2J Server
 *
 * This file is part of L2J Server.
 *
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.arrowgene.dance.server.util;

import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.*;

public class DeadLockDetector extends Thread {


    private static final Logger logger = LogManager.getLogger(DeadLockDetector.class);

    private final ThreadMXBean tmx;
    private ServerConfig config;

    public DeadLockDetector(DanceServer server) {
        super("DeadLockDetector");
        tmx = ManagementFactory.getThreadMXBean();
        this.config = server.getServerConfig();
    }

    @Override
    public final void run() {
        boolean deadlock = false;
        while (!deadlock) {
            try {
                long[] ids = tmx.findDeadlockedThreads();

                if (ids != null) {
                    deadlock = true;
                    ThreadInfo[] tis = tmx.getThreadInfo(ids, true, true);
                    StringBuilder info = new StringBuilder();
                    info.append("DeadLock Found!");
                    info.append(ServerConfig.EOL);
                    for (ThreadInfo ti : tis) {
                        info.append(ti.toString());
                    }

                    for (ThreadInfo ti : tis) {
                        LockInfo[] locks = ti.getLockedSynchronizers();
                        MonitorInfo[] monitors = ti.getLockedMonitors();
                        if ((locks.length == 0) && (monitors.length == 0)) {
                            continue;
                        }

                        ThreadInfo dl = ti;
                        info.append("Java-level deadlock:");
                        info.append(ServerConfig.EOL);
                        info.append('\t');
                        info.append(dl.getThreadName());
                        info.append(" is waiting to lock ");
                        info.append(dl.getLockInfo().toString());
                        info.append(" which is held by ");
                        info.append(dl.getLockOwnerName());
                        info.append(ServerConfig.EOL);
                        while ((dl = tmx.getThreadInfo(new long[]
                            {
                                dl.getLockOwnerId()
                            }, true, true)[0]).getThreadId() != ti.getThreadId()) {
                            info.append('\t');
                            info.append(dl.getThreadName());
                            info.append(" is waiting to lock ");
                            info.append(dl.getLockInfo().toString());
                            info.append(" which is held by ");
                            info.append(dl.getLockOwnerName());
                            info.append(ServerConfig.EOL);
                        }
                    }
                    logger.warn(String.format("DeadLock detected: %s", info.toString()));
                }
                Thread.sleep(config.getDebugDetectDeadlockMS());
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }
}
