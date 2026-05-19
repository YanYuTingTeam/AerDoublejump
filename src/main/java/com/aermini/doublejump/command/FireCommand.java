package com.aermini.doublejump.command;

import com.aermini.doublejump.AerDoublejump;
import com.aermini.doublejump.manager.ConfigManager;
import com.aermini.doublejump.manager.MessageManager;
import com.aermini.doublejump.manager.PlayerStateManager;
import com.aermini.doublejump.util.FireworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.UUID;

public class FireCommand implements CommandExecutor {
    private final AerDoublejump plugin;
    public FireCommand(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用此命令!");
            return true;
        }
        
        Player player = (Player) sender;
        MessageManager messageManager = plugin.getMessageManager();
        ConfigManager configManager = plugin.getConfigManager();
        PlayerStateManager stateManager = plugin.getPlayerStateManager();

        if (!player.hasPermission("aerdoublejump.fire")) {
            player.sendMessage(messageManager.getMessage("fire-noperm"));
            return true;
        }
        
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long lastFirework = stateManager.getLastFireworkTime(uuid);
        long cooldown = configManager.getFireCooldown();
        
        if (cooldown > 0 && (now - lastFirework) < cooldown) {
            long remaining = (cooldown - (now - lastFirework)) / 1000;
            player.sendMessage(messageManager.getMessage("fire-cooldown", "time", String.valueOf(remaining)));
            return true;
        }
        
        stateManager.setLastFireworkTime(uuid, now);
        
        String notification = messageManager.getMessage("fire-notification", "player", player.getName());
        if (!notification.isEmpty()) {
            Bukkit.broadcastMessage(notification);
        }
        
        String startMsg = messageManager.getMessage("fire-start");
        if (!startMsg.isEmpty()) {
            player.sendMessage(startMsg);
        }
        launchFireworks(player, configManager);
        return true;
    }
    
    // aicode
    private void launchFireworks(Player player, ConfigManager config) {
        Location location = player.getLocation();
        int keeptime = config.getFireKeeptime();
        int interval = config.getFireInterval();
        int flytime = config.getFireFlytime();
        String colorStr = config.getFireColor();
        int count = keeptime / interval;

        new BukkitRunnable() {
            int launched = 0;
            @Override
            public void run() {
                if (launched >= count || !player.isOnline()) {
                    cancel();
                    if (player.isOnline()) {
                        String overMsg = plugin.getMessageManager().getMessage("fire-over");
                        if (!overMsg.isEmpty()) {
                            player.sendMessage(overMsg);
                        }
                    }
                    return;
                }
                
                Color color = FireworkUtil.parseColor(colorStr);
                FireworkEffect effect = FireworkUtil.createEffect(color, true, true);
                Location fireLocation = location.clone().add(
                        (Math.random() - 0.5) * 10,
                        0,
                        (Math.random() - 0.5) * 10
                );
                
                Firework firework = FireworkUtil.spawnFirework(fireLocation, 1 + random.nextInt(2), effect);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (firework.isValid()) {
                            firework.detonate();
                        }
                    }
                }.runTaskLater(plugin, flytime / 50);
                launched++;
            }
            private final java.util.Random random = new java.util.Random();
        }.runTaskTimer(plugin, 0L, interval / 50);
    }
}
