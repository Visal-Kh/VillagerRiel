package org.yuno.villagerriel.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.yuno.villagerriel.VillagerRiel;

public class VTradeCommand implements CommandExecutor {

    private final VillagerRiel plugin;

    public VTradeCommand(VillagerRiel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() && !sender.hasPermission("villagerriel.admin")) {
            send(sender, "❌ អ្នកមិនមានសិទ្ធិ!", 0xFF5555);
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                plugin.reloadConfig();
                send(sender, "✅ Config reloaded!", 0x55FF55);
            }
            case "rate" -> {
                int rate = plugin.getTradeConverter().getRate();
                send(sender, "💱 Rate: 1 Emerald = " + rate + " ៛", 0xFFAA00);
            }
            case "convertall" -> {
                if (!(sender instanceof Player player)) {
                    send(sender, "❌ Only players!", 0xFF5555);
                    return true;
                }
                int count = 0;
                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof Villager villager) {
                        plugin.getTradeConverter().convertTrades(villager);
                        count++;
                    }
                }
                send(sender, "✅ Converted " + count + " Villagers!", 0x55FF55);
            }
            default -> sendHelp(sender);
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        send(sender, "─── VillagerRiel Commands ───", 0xFFD700);
        send(sender, "/vtrade rate          — មើល rate", 0xAAAAAA);
        send(sender, "/vtrade convertall    — convert ទាំងអស់", 0xAAAAAA);
        send(sender, "/vtrade reload        — reload config", 0xAAAAAA);
    }

    private void send(CommandSender sender, String msg, int hex) {
        sender.sendMessage(Component.text(msg).color(TextColor.color(hex)));
    }
}
