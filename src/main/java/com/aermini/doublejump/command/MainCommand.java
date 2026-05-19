package com.aermini.doublejump.command;

import com.aermini.doublejump.AerDoublejump;
import com.aermini.doublejump.manager.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    private final AerDoublejump plugin;
    public MainCommand(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§e§lAerDoublejump §r§fv" + plugin.getDescription().getVersion() + "§7for §b§lYanYuTing §8| §r§fby. §dAerMini");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("aerdoublejump.admin")) {
                sender.sendMessage(plugin.getMessageManager().getMessage("default-noperm"));
                return true;
            }
            
            plugin.reload();
            sender.sendMessage(plugin.getMessageManager().getMessage("reload"));
            return true;
        }
        
        sender.sendMessage("§e§lAerDoublejump §r§fv" + plugin.getDescription().getVersion() + "§7for §b§lYanYuTing §8| §r§fby. §dAerMini");
        return true;
    }
}
