package me.boggerbyte.deathboxes.tasks;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class RenderHologramTimerTask extends BukkitRunnable {
    private final Entity timerLine;
    private final Component rawTimerLine;

    public RenderHologramTimerTask(Entity timerLine, Component rawTimerLine, int secondsLeft) {
        this.timerLine = timerLine;
        this.rawTimerLine = rawTimerLine;

        this.secondsLeft = secondsLeft;
    }

    private int secondsLeft;

    @Override
    public void run() {
        if (secondsLeft < 0) cancel();

        var line = rawTimerLine.replaceText(config -> {
            config.match("%timer%");
            config.replacement(Component.text(secondsLeft).append(Component.text("s")));
        });
        timerLine.customName(line);

        secondsLeft--;
    }
}
