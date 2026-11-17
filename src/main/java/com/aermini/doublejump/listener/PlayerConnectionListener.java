package com.aermini.doublejump.listener;

import com.aermini.doublejump.AerDoublejump;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import java.util.UUID;

public class PlayerConnectionListener implements Listener {
    private final AerDoublejump plugin;
    public PlayerConnectionListener(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerStateManager().loadPlayer(player);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("aerdoublejump.fly")) {return;}
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            player.setAllowFlight(true);
            player.setFlying(true);
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        GameMode newMode = event.getNewGameMode();
        if (newMode == GameMode.CREATIVE || newMode == GameMode.SPECTATOR) {return;}
        if (!player.hasPermission("aerdoublejump.fly")) {return;}
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            player.setAllowFlight(true);
            player.setFlying(true);
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        plugin.getPlayerStateManager().savePlayer(player);
        plugin.getPlayerStateManager().unloadPlayer(uuid);
    }
}
