package me.boggerbyte.deathboxes.listeners;

import me.boggerbyte.deathboxes.Main;
import me.boggerbyte.deathboxes.deathbox.Deathbox;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

public class DeathboxEventsListener implements Listener {
    private final Plugin plugin = Main.getInstance();

    private Deathbox fetchDeathbox(Metadatable metadatable) {
        var deathboxMeta = metadatable.getMetadata(Deathbox.metadataKey);
        if (deathboxMeta.isEmpty()) return null;
        if (!(deathboxMeta.get(0).value() instanceof Deathbox deathbox)) return null;
        return deathbox;
    }

    @EventHandler
    public void onLanding(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock fallingBlock)) return;
        var deathbox = fetchDeathbox(fallingBlock);
        if (deathbox == null) return;

        event.setCancelled(true);

        deathbox.setBlock(event.getBlock());
        deathbox.getHologram().spawn(event.getBlock().getLocation().toCenterLocation());

        event.getEntity().remove();
        event.getBlock().setType(Material.CHEST);
        event.getBlock().setMetadata(Deathbox.metadataKey, new FixedMetadataValue(plugin, deathbox));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        var block = event.getClickedBlock();
        if (block == null) return;
        var deathbox = fetchDeathbox(block);
        if (deathbox == null) return;

        event.setCancelled(true);

        if (event.getAction().isLeftClick()) {
            deathbox.destroy(true);
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, 0.8f, 1);
        }
        if (event.getAction().isRightClick()) {
            event.getPlayer().openInventory(deathbox.getInventory());
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.8f, 1);
        }
    }
}
