package com.aermini.doublejump.database;

import com.aermini.doublejump.AerDoublejump;
import com.aermini.doublejump.manager.ConfigManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {
    private final AerDoublejump plugin;
    private HikariDataSource dataSource;
    public DatabaseManager(AerDoublejump plugin) {
        this.plugin = plugin;
    }
    
    public boolean initialize() {
        ConfigManager config = plugin.getConfigManager();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s?useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useLocalSessionState=true&cachePrepStmts=true&prepStmtCacheSize=250&prepStmtCacheSqlLimit=2048",
                config.getDatabaseAddress(),
                config.getDatabasePort(),
                config.getDatabaseName()));
        hikariConfig.setUsername(config.getDatabaseUsername());
        hikariConfig.setPassword(config.getDatabasePassword());
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        
        try {
            dataSource = new HikariDataSource(hikariConfig);
            createTables();
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
    }
    
    private void createTables() {
        String tableName = plugin.getConfigManager().getTablePrefix() + "players";
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "fly_enabled BOOLEAN DEFAULT FALSE," +
                "last_firework BIGINT DEFAULT 0)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }
    
    public boolean isFlyEnabled(UUID uuid) {
        String tableName = plugin.getConfigManager().getTablePrefix() + "players";
        String sql = "SELECT fly_enabled FROM " + tableName + " WHERE uuid = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("fly_enabled");
            }
        } catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }
        return false;
    }
    
    public void setFlyEnabled(UUID uuid, boolean enabled) {
        String tableName = plugin.getConfigManager().getTablePrefix() + "players";
        String sql = "INSERT INTO " + tableName + " (uuid, fly_enabled) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE fly_enabled = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setBoolean(2, enabled);
            statement.setBoolean(3, enabled);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }
    
    public long getLastFireworkTime(UUID uuid) {
        String tableName = plugin.getConfigManager().getTablePrefix() + "players";
        String sql = "SELECT last_firework FROM " + tableName + " WHERE uuid = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("last_firework");
            }
        } catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }
        return 0;
    }
    
    public void setLastFireworkTime(UUID uuid, long time) {
        String tableName = plugin.getConfigManager().getTablePrefix() + "players";
        String sql = "INSERT INTO " + tableName + " (uuid, last_firework) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE last_firework = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setLong(2, time);
            statement.setLong(3, time);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }
    
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
