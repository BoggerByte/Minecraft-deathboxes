package me.boggerbyte.deathboxes.tasks;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class RenderHologramTimerTask extends BukkitRunnable {
    private final Entity timerLine;
    private final String rawTimerLine;

    public RenderHologramTimerTask(Entity timerLine, String rawTimerLine, int secondsLeft) {
        this.timerLine = timerLine;
        this.rawTimerLine = rawTimerLine;

        this.secondsLeft = secondsLeft;
    }

    private int secondsLeft;

    @Override
    public void run() {
        if (secondsLeft < 0) cancel();

        var line = rawTimerLine.replace("%timer%", String.format("%ds", secondsLeft));
        timerLine.setCustomName(line);

        secondsLeft--;
    }
}
