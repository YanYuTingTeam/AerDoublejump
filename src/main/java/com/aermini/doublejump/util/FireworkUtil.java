package com.aermini.doublejump.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import java.util.Random;

// aicode
public class FireworkUtil {
    private static final Random random = new Random();
    public static Color parseColor(String colorStr) {
        if (colorStr == null || colorStr.isEmpty()) {
            return getRandomColor();
        }
        
        String upper = colorStr.toUpperCase().trim();
        
        if (upper.equals("RANDOM")) {
            return getRandomColor();
        }
        
        if (upper.startsWith("#")) {
            try {
                int rgb = Integer.parseInt(upper.substring(1), 16);
                return Color.fromRGB(rgb);
            } catch (NumberFormatException e) {
                return getRandomColor();
            }
        }
        
        switch (upper) {
            case "RED":
                return Color.RED;
            case "BLUE":
                return Color.BLUE;
            case "GREEN":
                return Color.GREEN;
            case "YELLOW":
                return Color.YELLOW;
            case "ORANGE":
                return Color.ORANGE;
            case "PURPLE":
                return Color.PURPLE;
            case "PINK":
                return Color.FUCHSIA;
            case "WHITE":
                return Color.WHITE;
            case "BLACK":
                return Color.BLACK;
            case "AQUA":
                return Color.AQUA;
            case "GRAY":
            case "GREY":
                return Color.GRAY;
            case "LIME":
                return Color.LIME;
            case "MAROON":
                return Color.MAROON;
            case "NAVY":
                return Color.NAVY;
            case "OLIVE":
                return Color.OLIVE;
            case "SILVER":
                return Color.SILVER;
            case "TEAL":
                return Color.TEAL;
            default:
                return getRandomColor();
        }
    }
    
    public static Color getRandomColor() {
        return Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
    
    public static FireworkEffect createEffect(Color color, boolean flicker, boolean trail) {
        FireworkEffect.Type type = FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)];
        
        return FireworkEffect.builder()
                .with(type)
                .withColor(color)
                .withFade(getRandomColor())
                .flicker(flicker)
                .trail(trail)
                .build();
    }
    
    public static Firework spawnFirework(Location location, int power, FireworkEffect effect) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(effect);
        meta.setPower(power);
        firework.setFireworkMeta(meta);
        return firework;
    }
}
