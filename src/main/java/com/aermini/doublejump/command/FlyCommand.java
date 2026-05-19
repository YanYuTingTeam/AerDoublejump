package com.aermini.doublejump.command;

import com.aermini.doublejump.AerDoublejump;
import com.aermini.doublejump.manager.MessageManager;
import com.aermini.doublejump.manager.PlayerStateManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class FlyCommand implements CommandExecutor {
    private final AerDoublejump plugin;
    public FlyCommand(AerDoublejump plugin) {
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
        PlayerStateManager stateManager = plugin.getPlayerStateManager();
        
        if (!player.hasPermission("aerdoublejump.fly")) {
            player.sendMessage(messageManager.getMessage("fly-noperm"));
            return true;
        }
        
        UUID uuid = player.getUniqueId();
        boolean currentState = stateManager.isFlyEnabled(uuid);
        boolean newState = !currentState;

        stateManager.setFlyEnabled(uuid, newState);
        player.setAllowFlight(newState);
        player.setFlying(newState);
        if (newState) {
            player.sendMessage(messageManager.getMessage("fly-enabled"));
        } else {
            player.sendMessage(messageManager.getMessage("fly-disabled"));
        }
        
        return true;
    }
}
