package org.yuno.villagerriel.managers;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.yuno.rielitem.managers.RielManager;
import org.yuno.villagerriel.VillagerRiel;

import java.util.ArrayList;
import java.util.List;

public class TradeConverter {

    private final VillagerRiel plugin;
    private final RielManager rielManager;

    public TradeConverter(VillagerRiel plugin, RielManager rielManager) {
        this.plugin = plugin;
        this.rielManager = rielManager;
    }

    public void convertTrades(Villager villager) {
        try {
            int rate = plugin.getConfig().getInt("settings.emerald-to-riel-rate", 500);
            List<MerchantRecipe> original = villager.getRecipes();
            List<MerchantRecipe> converted = new ArrayList<>();
            for (MerchantRecipe recipe : original) {
                converted.add(convertRecipe(recipe, rate));
            }
            villager.setRecipes(converted);
        } catch (Exception e) {
            plugin.getLogger().warning("Error converting trades: " + e.getMessage());
        }
    }

    private MerchantRecipe convertRecipe(MerchantRecipe original, int rate) {
        MerchantRecipe newRecipe = new MerchantRecipe(
                original.getResult(),
                original.getUses(),
                original.getMaxUses(),
                original.hasExperienceReward(),
                original.getVillagerExperience(),
                original.getPriceMultiplier(),
                original.getDemand(),
                original.getSpecialPrice()
        );
        List<ItemStack> newIngredients = new ArrayList<>();
        for (ItemStack ingredient : original.getIngredients()) {
            if (ingredient != null && ingredient.getType() == Material.EMERALD) {
                int totalRiel = ingredient.getAmount() * rate;
                newIngredients.add(getBestRielItem(totalRiel));
            } else {
                newIngredients.add(ingredient);
            }
        }
        newRecipe.setIngredients(newIngredients);
        return newRecipe;
    }

    private ItemStack getBestRielItem(int totalRiel) {
        int[] denoms = {100000, 50000, 10000, 5000, 1000, 500, 100};
        for (int denom : denoms) {
            if (totalRiel >= denom) {
                int qty = totalRiel / denom;
                if (qty <= 64) {
                    ItemStack item = rielManager.createRielItem(denom, qty);
                    if (item != null) return item;
                }
            }
        }
        int qty = Math.min(64, Math.max(1, totalRiel / 100));
        ItemStack fallback = rielManager.createRielItem(100, qty);
        return fallback != null ? fallback : new ItemStack(Material.EMERALD, 1);
    }

    public int getRate() {
        return plugin.getConfig().getInt("settings.emerald-to-riel-rate", 500);
    }
}
