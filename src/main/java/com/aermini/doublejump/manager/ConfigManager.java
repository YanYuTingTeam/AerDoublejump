package com.aermini.doublejump.manager;

import com.aermini.doublejump.AerDoublejump;
import org.bukkit.configuration.file.FileConfiguration;

// aicode
public class ConfigManager {
    private final AerDoublejump plugin;
    
    private String databaseAddress;
    private int databasePort;
    private String databaseUsername;
    private String databasePassword;
    private String databaseName;
    private String tablePrefix;
    
    private boolean keepDoubleJump;
    private double forwardVelocity;
    private double upVelocity;
    private double sprintingForwardVelocity;
    private double sprintingUpVelocity;
    private long doubleJumpCooldown;
    private int fallTest;
    
    private long fireCooldown;
    private String fireColor;
    private int fireFlytime;
    private int fireKeeptime;
    private int fireInterval;
    
    public ConfigManager(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        
        FileConfiguration config = plugin.getConfig();
        
        databaseAddress = config.getString("database.address", "127.0.0.1");
        databasePort = config.getInt("database.port", 3306);
        databaseUsername = config.getString("database.username", "root");
        databasePassword = config.getString("database.password", "password");
        databaseName = config.getString("database.database", "minecraft");
        tablePrefix = config.getString("database.table_prefix", "aerdulj_");
        
        keepDoubleJump = config.getBoolean("Velocity.keep", false);
        forwardVelocity = config.getDouble("Velocity.forward", 1.7);
        upVelocity = config.getDouble("Velocity.up", 1.2);
        sprintingForwardVelocity = config.getDouble("Velocity.sprintingForward", 2.0);
        sprintingUpVelocity = config.getDouble("Velocity.sprintingUp", 1.3);
        doubleJumpCooldown = config.getLong("Velocity.cooldown", 0);
        fallTest = config.getInt("Velocity.falltest", 2);
        
        fireCooldown = config.getLong("Fire.cooldown", 15000);
        fireColor = config.getString("Fire.color", "RANDOM");
        fireFlytime = config.getInt("Fire.flytime", 1000);
        fireKeeptime = config.getInt("Fire.keeptime", 5000);
        fireInterval = config.getInt("Fire.interval", 500);
    }
    
    public String getDatabaseAddress() { return databaseAddress; }
    public int getDatabasePort() { return databasePort; }
    public String getDatabaseUsername() { return databaseUsername; }
    public String getDatabasePassword() { return databasePassword; }
    public String getDatabaseName() { return databaseName; }
    public String getTablePrefix() { return tablePrefix; }
    
    public boolean isKeepDoubleJump() { return keepDoubleJump; }
    public double getForwardVelocity() { return forwardVelocity; }
    public double getUpVelocity() { return upVelocity; }
    public double getSprintingForwardVelocity() { return sprintingForwardVelocity; }
    public double getSprintingUpVelocity() { return sprintingUpVelocity; }
    public long getDoubleJumpCooldown() { return doubleJumpCooldown; }
    public int getFallTest() { return fallTest; }
    
    public long getFireCooldown() { return fireCooldown; }
    public String getFireColor() { return fireColor; }
    public int getFireFlytime() { return fireFlytime; }
    public int getFireKeeptime() { return fireKeeptime; }
    public int getFireInterval() { return fireInterval; }
}
