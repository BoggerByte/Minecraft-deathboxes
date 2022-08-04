package me.boggerbyte.deathboxes.listeners;

import me.boggerbyte.deathboxes.deathbox.DeathboxFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final DeathboxFactory deathboxFactory;

    public PlayerDeathListener(DeathboxFactory deathboxFactory) {
        this.deathboxFactory = deathboxFactory;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();

        var player = event.getPlayer();

        var deathbox = deathboxFactory.create(player);
        deathbox.spawn(player.getLocation());
    }
}
