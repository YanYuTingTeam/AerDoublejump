package com.aermini.doublejump.listener;

import com.aermini.doublejump.AerDoublejump;
import com.aermini.doublejump.manager.ConfigManager;
import com.aermini.doublejump.manager.PlayerStateManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;
import java.util.UUID;

public class DoubleJumpListener implements Listener {
    private final AerDoublejump plugin;
    public DoubleJumpListener(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerStateManager stateManager = plugin.getPlayerStateManager();
        ConfigManager configManager = plugin.getConfigManager();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {return;}
        if (stateManager.isFlyEnabled(uuid)) {return;}
        event.setCancelled(true);
        player.setAllowFlight(false);
        long lastJump = stateManager.getLastDoubleJumpTime(uuid);
        long cooldown = configManager.getDoubleJumpCooldown();
        long now = System.currentTimeMillis();
        if (cooldown > 0 && (now - lastJump) < cooldown) {return;}
        // 借鉴了一下 TreysDoubleJump
        double forward = configManager.getForwardVelocity();
        double up = configManager.getUpVelocity();
        
        if (player.isSprinting()) {
            forward = configManager.getSprintingForwardVelocity();
            up = configManager.getSprintingUpVelocity();
        }
        Vector velocity = player.getLocation().getDirection().multiply(forward).setY(up);
        player.setVelocity(velocity);
        stateManager.setLastDoubleJumpTime(uuid, now);

        if (!configManager.isKeepDoubleJump()) {
            stateManager.setHasDoubleJumped(uuid, true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerStateManager stateManager = plugin.getPlayerStateManager();
        ConfigManager configManager = plugin.getConfigManager();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {return;}
        if (stateManager.isFlyEnabled(uuid)) {return;}
        
        boolean onGround = isOnGround(player);
        if (onGround) {
            if (stateManager.hasDoubleJumped(uuid)) {
                stateManager.setHasDoubleJumped(uuid, false);
                player.setAllowFlight(true);
            } else if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
        }
    }

    private boolean isOnGround(Player player) {
        if (player.isOnGround()) {return true;}
        int fallTest = plugin.getConfigManager().getFallTest();
        Location loc = player.getLocation().clone().subtract(0, 1, 0);
        
        for (int i = 0; i < fallTest; i++) {
            if (loc.getBlock().getType() != Material.AIR) {
                return true;
            }
            loc.subtract(0, 1, 0);
        }
        
        return false;
    }
}
