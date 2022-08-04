package me.boggerbyte.deathboxes.hologram;


import me.boggerbyte.deathboxes.Main;
import me.boggerbyte.deathboxes.tasks.RenderHologramTimerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hologram {
    private final Plugin plugin = Main.getInstance();
    private final List<Component> rawLines;
    private final int secondsLeft;

    public Hologram(List<Component> rawLines, int secondsLeft) {
        this.rawLines = rawLines;
        this.secondsLeft = secondsLeft;
    }

    private final List<Entity> currentLines = new ArrayList<>();
    private final List<BukkitRunnable> currentTasks = new ArrayList<>();

    public void spawn(Location location) {
        var reversedRawLines = new ArrayList<>(rawLines);
        Collections.reverse(reversedRawLines);

        List<Entity> lines = new ArrayList<>();
        for (Component reversedRawLine : reversedRawLines) {
            var line = location.getWorld().spawn(location, AreaEffectCloud.class);
            line.setWaitTime(0);
            line.setRadius(0);
            line.setDuration(Integer.MAX_VALUE);
            line.setCustomNameVisible(true);
            line.customName(reversedRawLine);
            lines.add(line);
        }

        var linesIterator = lines.iterator();
        var mount = linesIterator.next();
        while (linesIterator.hasNext()) {
            var nextLine = linesIterator.next();
            mount.addPassenger(nextLine);
            mount = nextLine;
        }

        var rawTimerLines = reversedRawLines.stream()
                .filter(line -> PlainTextComponentSerializer.plainText().serialize(line).contains("%timer%"))
                .toList();
        var timerLines = lines.stream()
                .filter(line -> rawTimerLines.contains(line.customName()))
                .toList();
        List<BukkitRunnable> tasks = new ArrayList<>();
        for (int i = 0; i < timerLines.size(); i++) {
            var renderer = new RenderHologramTimerTask(timerLines.get(i), rawTimerLines.get(i), secondsLeft);
            tasks.add(renderer);
            renderer.runTaskTimer(plugin, 0, 20);
        }

        currentLines.addAll(lines);
        currentTasks.addAll(tasks);
    }

    public void remove() {
        currentLines.forEach(Entity::remove);
        currentTasks.forEach(BukkitRunnable::cancel);
    }
}
