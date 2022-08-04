package me.boggerbyte.deathboxes.deathbox;

import me.boggerbyte.deathboxes.Main;
import me.boggerbyte.deathboxes.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class Deathbox {
    public static final String metadataKey = "deathbox";

    private final Plugin plugin = Main.getInstance();
    private final Inventory inventory;
    private final int duration;
    private final Hologram hologram;

    private Block block;

    public Deathbox(Inventory inventory, Hologram hologram, int duration) {
        this.inventory = inventory;
        this.hologram = hologram;
        this.duration = duration;
    }

    public void spawn(Location location) {
        var spawnLocation = location.toBlockLocation();

        var fallingBlock = spawnLocation.getWorld().spawnFallingBlock(spawnLocation, Material.CHEST.createBlockData());
        fallingBlock.setHurtEntities(false);
        fallingBlock.setDropItem(false);
        fallingBlock.setMetadata(Deathbox.metadataKey, new FixedMetadataValue(plugin, this));
//      fallingBlock.addPassenger(hologramFactory.getMount());

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> destroy(false), duration);
    }

    public void destroy(boolean dropContents) {
        hologram.remove();
        inventory.close();
        inventory.close();

        if (block != null) {
            block.setType(Material.AIR);
            block.removeMetadata(metadataKey, plugin);
        }

        if (dropContents) inventory.forEach(item -> {
            if (item != null) block.getWorld().dropItemNaturally(block.getLocation(), item);
        });
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
