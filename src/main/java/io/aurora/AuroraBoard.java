package io.aurora;

import io.aurora.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class AuroraBoard extends JavaPlugin {
    private static final Logger LOGGER = Bukkit.getLogger();

    @Override
    public void onLoad() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is loading.");
    }

    @Override
    public void onEnable() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is enabled.");
    }

    @Override
    public void onDisable() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is disabled.");
    }
}
