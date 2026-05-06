package org.yuno.villagerriel.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.MerchantInventory;
import org.yuno.villagerriel.VillagerRiel;

public class VillagerTradeListener implements Listener {

    private final VillagerRiel plugin;

    public VillagerTradeListener(VillagerRiel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        try {
            if (!(event.getEntity() instanceof Villager villager)) return;
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                plugin.getTradeConverter().convertTrades(villager);
            });
        } catch (Exception e) {
            plugin.getLogger().warning("VillagerAcquireTrade error: " + e.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onVillagerReplenish(VillagerReplenishTradeEvent event) {
        try {
            if (!plugin.getConfig().getBoolean("settings.auto-convert-on-restock", true)) return;
            if (!(event.getEntity() instanceof Villager villager)) return;
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                plugin.getTradeConverter().convertTrades(villager);
            });
        } catch (Exception e) {
            plugin.getLogger().warning("VillagerReplenish error: " + e.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryOpen(InventoryOpenEvent event) {
        try {
            if (!plugin.getConfig().getBoolean("settings.convert-on-trade-open", true)) return;
            if (!(event.getInventory() instanceof MerchantInventory merchantInv)) return;
            if (!(event.getPlayer() instanceof Player)) return;
            if (!(merchantInv.getMerchant() instanceof Villager villager)) return;
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                plugin.getTradeConverter().convertTrades(villager);
            });
        } catch (Exception e) {
            plugin.getLogger().warning("InventoryOpen error: " + e.getMessage());
        }
    }
}
