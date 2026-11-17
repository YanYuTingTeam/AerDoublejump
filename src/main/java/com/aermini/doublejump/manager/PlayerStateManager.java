package com.aermini.doublejump.manager;

import com.aermini.doublejump.AerDoublejump;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStateManager {
    
    private final AerDoublejump plugin;
    private final Map<UUID, Boolean> hasDoubleJumped = new HashMap<>();
    private final Map<UUID, Long> lastDoubleJumpTime = new HashMap<>();
    private final Map<UUID, Long> lastFireworkTime = new HashMap<>();
    
    public PlayerStateManager(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    public void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        boolean canFly = player.hasPermission("aerdoublejump.fly");
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setAllowFlight(canFly);
        }
        if (canFly && player.getAllowFlight()) {
            player.setFlying(true);
        }
        
        hasDoubleJumped.put(uuid, false);
        lastDoubleJumpTime.put(uuid, 0L);
        long lastFirework = plugin.getDatabaseManager().getLastFireworkTime(uuid);
        lastFireworkTime.put(uuid, lastFirework);
    }
    
    public void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        
        Long fireworkTime = lastFireworkTime.get(uuid);
        if (fireworkTime != null) {
            plugin.getDatabaseManager().setLastFireworkTime(uuid, fireworkTime);
        }
    }
    
    public void saveAllPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            savePlayer(player);
        }
    }
    
    // 是否已二段跳. 在落地时重置
    public boolean hasDoubleJumped(UUID uuid) {
        return hasDoubleJumped.getOrDefault(uuid, false);
    }
    
    public void setHasDoubleJumped(UUID uuid, boolean jumped) {
        hasDoubleJumped.put(uuid, jumped);
    }
    
    public long getLastDoubleJumpTime(UUID uuid) {
        return lastDoubleJumpTime.getOrDefault(uuid, 0L);
    }
    
    public void setLastDoubleJumpTime(UUID uuid, long time) {
        lastDoubleJumpTime.put(uuid, time);
    }
    
    public long getLastFireworkTime(UUID uuid) {
        return lastFireworkTime.getOrDefault(uuid, 0L);
    }
    
    public void setLastFireworkTime(UUID uuid, long time) {
        lastFireworkTime.put(uuid, time);
        plugin.getDatabaseManager().setLastFireworkTime(uuid, time);
    }
    
    public void unloadPlayer(UUID uuid) {
        hasDoubleJumped.remove(uuid);
        lastDoubleJumpTime.remove(uuid);
        lastFireworkTime.remove(uuid);
    }
}
