package com.minehut.tgm.util.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Created by katie on 2/14/17.
 */
public class PlayerMenu extends Menu {
    private UUID playerUuid;

    /**
     * Disables when the player closes menu or logs off.
     *
     * @param javaPlugin
     * @param name
     * @param slots
     * @param player
     */
    public PlayerMenu(JavaPlugin javaPlugin, String name, int slots, Player player) {
        super(javaPlugin, name, slots);
        this.playerUuid = player.getUniqueId();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId().equals(playerUuid)) {
            super.disable();
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getPlayer().getUniqueId().equals(playerUuid)) {
            super.disable();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals(inventory.getName())) {
            if (event.getPlayer().getUniqueId().equals(playerUuid)) {
                super.disable();
            }
        }
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUuid);
    }
}
