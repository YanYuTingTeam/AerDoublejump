package com.aermini.doublejump;

import com.aermini.doublejump.command.FireCommand;
import com.aermini.doublejump.command.FlyCommand;
import com.aermini.doublejump.command.MainCommand;
import com.aermini.doublejump.database.DatabaseManager;
import com.aermini.doublejump.listener.DoubleJumpListener;
import com.aermini.doublejump.listener.PlayerConnectionListener;
import com.aermini.doublejump.manager.ConfigManager;
import com.aermini.doublejump.manager.MessageManager;
import com.aermini.doublejump.manager.PlayerStateManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AerDoublejump extends JavaPlugin {
    private static AerDoublejump instance;
    private DatabaseManager databaseManager;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private PlayerStateManager playerStateManager;
    
    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        messageManager = new MessageManager(this);
        messageManager.loadMessages();
        databaseManager = new DatabaseManager(this);
        if (!databaseManager.initialize()) {
            getLogger().severe("数据库连接失败. 禁用插件");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        playerStateManager = new PlayerStateManager(this);
        getServer().getPluginManager().registerEvents(new DoubleJumpListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("fire").setExecutor(new FireCommand(this));
        getCommand("aerdoublejump").setExecutor(new MainCommand(this));
        getLogger().info("AerDoublejump 已启用");
    }
    
    @Override
    public void onDisable() {
        if (playerStateManager != null) {
            playerStateManager.saveAllPlayers();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("AerDoublejump 已禁用");
    }
    
    public void reload() {
        configManager.loadConfig();
        messageManager.loadMessages();
    }
    
    public static AerDoublejump getInstance() {
        return instance;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public MessageManager getMessageManager() {
        return messageManager;
    }
    
    public PlayerStateManager getPlayerStateManager() {
        return playerStateManager;
    }
}
