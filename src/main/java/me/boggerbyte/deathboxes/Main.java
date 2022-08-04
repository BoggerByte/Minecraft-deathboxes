package me.boggerbyte.deathboxes;

import me.boggerbyte.deathboxes.deathbox.DeathboxFactory;
import me.boggerbyte.deathboxes.listeners.DeathboxEventsListener;
import me.boggerbyte.deathboxes.listeners.PlayerDeathListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        var deathboxHologramLayout = """
                <rainbow>deathbox!!!</rainbow>
                <bold><red><player></red></bold>
                <gray>despawn after:</gray> <timer>""";
        var deathboxSpawner = new DeathboxFactory(deathboxHologramLayout, 30 * 20);
        getServer().getPluginManager().registerEvents(new DeathboxEventsListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(deathboxSpawner), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getInstance() {
        return Main.getPlugin(Main.class);
    }
}
