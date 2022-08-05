package me.boggerbyte.deathboxes.deathbox;

import me.boggerbyte.deathboxes.Main;
import me.boggerbyte.deathboxes.hologram.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DeathboxFactory {
    private final Plugin plugin = Main.getInstance();

    private final String deathboxHologramLayout;
    private final int duration;

    public DeathboxFactory(String deathboxHologramLayout, int duration) {
        this.deathboxHologramLayout = deathboxHologramLayout;
        this.duration = duration;
    }

    public Deathbox create(Player owner) {
        var title = owner.getName() + (owner.getName().endsWith("s") ? "'" : "'s") + " " + "deathbox";
        var inventory = plugin.getServer().createInventory(null, 45, title);
        var contents = owner.getInventory().getContents();
        inventory.setContents(contents);

        var hologramRawLines = deathboxHologramLayout.lines()
                .map(line -> line.replace("%player%", owner.getName() + (owner.getName().endsWith("s") ? "'" : "'s")))
                .toList();
        var hologram = new Hologram(hologramRawLines, duration / 20);

        return new Deathbox(inventory, hologram, duration);
    }
}
