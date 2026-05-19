package com.aermini.doublejump.listener;

import com.aermini.doublejump.AerDoublejump;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        plugin.getPlayerStateManager().savePlayer(player);
        plugin.getPlayerStateManager().unloadPlayer(uuid);
    }
}
