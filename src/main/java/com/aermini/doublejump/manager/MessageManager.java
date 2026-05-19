package com.aermini.doublejump.manager;

import com.aermini.doublejump.AerDoublejump;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {
    private final AerDoublejump plugin;
    private final Map<String, String> messages = new HashMap<>();
    public MessageManager(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    public void loadMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(messagesFile);
        InputStream defaultStream = plugin.getResource("messages.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            config.setDefaults(defaultConfig);
        }
        
        messages.clear();
        messages.put("fly-enabled", config.getString("fly-enabled", "&b烟雨庭&7>> &a已打开飞行"));
        messages.put("fly-disabled", config.getString("fly-disabled", "&b烟雨庭&7>> &c已关闭飞行"));
        messages.put("fire-start", config.getString("fire-start", ""));
        messages.put("fire-over", config.getString("fire-over", ""));
        messages.put("fire-notification", config.getString("fire-notification", ""));
        messages.put("fire-noperm", config.getString("fire-noperm", "&b烟雨庭&7>> &c你还不是VIP"));
        messages.put("fly-noperm", config.getString("fly-noperm", "&b烟雨庭&7>> &c你还不是VIP"));
        messages.put("default-noperm", config.getString("default-noperm", "&b烟雨庭&7>> &c你没有权限使用这个命令"));
        messages.put("reload", config.getString("reload", "&b烟雨庭&7>> &e重载配置成功"));
        messages.put("fire-cooldown", config.getString("fire-cooldown", "&b烟雨庭&7>> &c烟花冷却中，请等待 {time} 秒"));
    }
    
    public String getMessage(String key) {
        String message = messages.get(key);
        if (message == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public String getMessage(String key, String placeholder, String value) {
        String message = getMessage(key);
        return message.replace("{" + placeholder + "}", value);
    }
}
