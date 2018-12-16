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

import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.task.tasks.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Schedules execution of tasks.
 */
public class TaskManager extends ServerComponent {


    private static final Logger logger = LogManager.getLogger(TaskManager.class);

    private HashMap<String, ScheduledFuture> schedules;
    private ScheduledExecutorService executor;

    public TaskManager(DanceServer server) {
        super(server);
    }

    /**
     * Schedules a task for execution.
     * TaskIds can not be used for multiple tasks,
     * if a task with the provided taskID exists, it will be cancelled and
     * a new task will be created.
     *
     * @param task Task to run.
     * @return TaskId used to interact with the task later.
     */
    public String schedule(Task task) {
        if (this.schedules.containsKey(task.getId())) {
            logger.info(String.format("Task with id '%s' already exists, will be overwritten.", task.getId()));
            ScheduledFuture<?> existingTask = this.schedules.get(task.getId());
            existingTask.cancel(true);
        }
        ScheduledFuture<?> future;
        if (task.isRepeat()) {
            future = this.executor.scheduleWithFixedDelay(task, task.getInitialDelay(), task.getDelay(), task.getTimeUnit());
        } else {
            future = this.executor.schedule(task, task.getDelay(), task.getTimeUnit());
        }
        this.schedules.put(task.getId(), future);
        return task.getId();
    }

    /**
     * Cancels the execution of a task.
     *
     * @param taskId Id of task to cancel.
     */
    public void cancel(String taskId) {
        if (this.schedules.containsKey(taskId)) {
            logger.info(String.format("Cancelling task: %s", taskId));
            ScheduledFuture<?> existingTask = this.schedules.remove(taskId);
            existingTask.cancel(true);
        } else {
            logger.info(String.format("Task: %s does't exist", taskId));
        }
    }

    @Override
    public void start() {
        this.executor = Executors.newScheduledThreadPool(1);
        this.schedules = new HashMap<>();

        this.schedule(new WorldSave(super.server));
        this.schedule(new UpdateStatus(super.server));
        if (super.server.getServerConfig().isDebugMode()) {
            this.schedule(new WriteDebugInfo(super.server));
            this.schedule(new CollectGarbage(super.server));
            this.schedule(new WriteProcessInfo(super.server));
        }
    }

    @Override
    public void stop() {
        if (this.executor != null) {
            this.executor.shutdown();
        }
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }

    @Override
    public void clientAuthenticated(DanceClient client) {

    }

    @Override
    public void clientDisconnected(DanceClient client) {

    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {

    }
}
