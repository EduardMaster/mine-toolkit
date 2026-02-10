package br.com.eduard.mine_toolkit.manager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import br.com.eduard.mine_utils.BukkitTimeHandler;

/**
 * A comprehensive manager for handling Bukkit tasks, timers, and delays.
 * * This class simplifies the creation and lifecycle management of both synchronous
 * and asynchronous tasks, providing built-in tracking for task start times and durations.
 * * @author Eduard
 * @since 1.0
 */
public class TimeManager extends EventsManager implements Runnable, BukkitTimeHandler {

    /** The duration of the task in server ticks. */
    transient long taskDuration = 20;

    /** The system timestamp (ms) when the task was initiated. */
    transient long taskStart;

    /** The current active BukkitTask instance. */
    transient BukkitTask taskUsed;

    /**
     * Checks if a task is currently assigned to this manager.
     * @return true if a task exists, false otherwise.
     */
    public boolean existsTask() {
        return taskUsed != null;
    }

    /**
     * Safely cancels and shuts down the currently running task or delay.
     * This method clears the task reference and stops it from the Bukkit Scheduler.
     */
    public void stopTask() {
        if (existsTask()) {
            try {
                int id = taskUsed.getTaskId();
                taskUsed.cancel();
                Bukkit.getScheduler().cancelTask(id);
                taskUsed = null;
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        // To be implemented by subclasses
    }

    @Override
    public Plugin getPluginConnected() {
        return getPlugin();
    }

    /**
     * Checks if the scheduled task is actively being executed by the scheduler.
     * @return true if the task is running, false otherwise.
     */
    public boolean isRunning() {
        return existsTask() && Bukkit.getScheduler().isCurrentlyRunning(taskUsed.getTaskId());
    }

    /**
     * Default constructor with a 1-tick duration.
     */
    public TimeManager() {
        this(1);
    }

    /**
     * Initializes the manager with a specific duration in ticks.
     * @param ticks Duration in server ticks.
     */
    public TimeManager(long ticks) {
        this.taskDuration = ticks;
    }

    /**
     * Initializes the manager with a specific duration in seconds.
     * @param seconds Duration in seconds (converted to 20 ticks per second).
     */
    public TimeManager(int seconds) {
        this.taskDuration = 20L * seconds;
    }

    /**
     * Schedules a Synchronous Delay.
     * The task will run once after the specified duration on the main thread.
     * @return The created [BukkitTask].
     */
    public BukkitTask syncDelay() {
        taskUsed = newTask(taskDuration, false, false, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }

    /**
     * Schedules a Synchronous Repeating Timer.
     * The task will run repeatedly on the main thread.
     * @return The created [BukkitTask].
     */
    public BukkitTask syncTimer() {
        taskUsed = newTask(taskDuration, true, false, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }

    /**
     * Schedules an Asynchronous Repeating Timer.
     * The task will run repeatedly on a separate thread.
     * @return The created [BukkitTask].
     */
    public BukkitTask asyncTimer() {
        taskUsed = newTask(taskDuration, true, true, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }

    /**
     * Schedules an Asynchronous Delay.
     * The task will run once after the specified duration on a separate thread.
     * @return The created [BukkitTask].
     */
    public BukkitTask asyncDelay() {
        taskUsed = newTask(taskDuration, false, true, this);
        taskStart = System.currentTimeMillis();
        return taskUsed;
    }
}