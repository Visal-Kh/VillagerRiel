package org.yuno.villagerriel;

import org.bukkit.plugin.java.JavaPlugin;
import org.yuno.rielitem.RielItem;
import org.yuno.rielitem.managers.RielManager;
import org.yuno.villagerriel.commands.VTradeCommand;
import org.yuno.villagerriel.listeners.VillagerTradeListener;
import org.yuno.villagerriel.managers.TradeConverter;

public class VillagerRiel extends JavaPlugin {

    private TradeConverter tradeConverter;
    private RielManager rielManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        RielItem rielPlugin = (RielItem) getServer().getPluginManager().getPlugin("RielItem");
        if (rielPlugin == null) {
            getLogger().severe("RielItem plugin not found! Disabling VillagerRiel.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.rielManager = rielPlugin.getRielManager();
        this.tradeConverter = new TradeConverter(this, rielManager);

        getServer().getPluginManager().registerEvents(new VillagerTradeListener(this), this);
        getCommand("vtrade").setExecutor(new VTradeCommand(this));

        getLogger().info("VillagerRiel enabled! Rate: 1 Emerald = "
                + getConfig().getInt("settings.emerald-to-riel-rate") + " ៛");
    }

    @Override
    public void onDisable() {
        getLogger().info("VillagerRiel disabled.");
    }

    public TradeConverter getTradeConverter() { return tradeConverter; }
    public RielManager getRielManager() { return rielManager; }
}
