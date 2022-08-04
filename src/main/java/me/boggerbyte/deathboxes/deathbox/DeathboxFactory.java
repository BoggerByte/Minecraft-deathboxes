package me.boggerbyte.deathboxes.deathbox;

import me.boggerbyte.deathboxes.Main;
import me.boggerbyte.deathboxes.hologram.Hologram;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        var title = Component.text()
                .append(Component.text(owner.getName()))
                .append(Component.text(owner.getName().endsWith("s") ? "'" : "'s"))
                .append(Component.space())
                .append(Component.text("deathbox"))
                .build();
        var inventory = plugin.getServer().createInventory(null, 45, title);
        var contents = owner.getInventory().getContents();
        inventory.setContents(contents);

        var hologramRawLines = deathboxHologramLayout.lines()
                .map(line -> MiniMessage.miniMessage().deserialize(line,
                        Placeholder.component("player", Component.text(owner.getName())
                                .append(Component.text(owner.getName().endsWith("s") ? "'" : "'s"))),
                        Placeholder.component("timer", Component.text("%timer%"))))
                .toList();
        var hologram = new Hologram(hologramRawLines, duration / 20);

        return new Deathbox(inventory, hologram, duration);
    }
}
