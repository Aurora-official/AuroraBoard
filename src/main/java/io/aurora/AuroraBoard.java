package io.aurora;

import io.aurora.util.Color;
import io.aurora.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class AuroraBoard extends JavaPlugin implements Listener {
    private static final Logger LOGGER = Bukkit.getLogger();

    private Map<String, Integer> playerBlockCount = new HashMap<>();
    private ScoreboardManager manager;
    private Scoreboard board;
    private Objective objective;
    private File dataFile;
    private FileConfiguration dataConfig;

    @Override
    public void onLoad() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is loading.");
    }

    @Override
    public void onEnable() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is enabled.");
        getServer().getPluginManager().registerEvents(this, this);

        ConfigUtil config = new ConfigUtil("config.yml");

        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective("blockCount", "dummy", Objects.requireNonNull(config.getConfig().getString("name")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        dataFile = new File(getDataFolder(), "data.yml");

        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadBlockCounts();
    }

    @Override
    public void onDisable() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is disabled.");
        saveBlockCounts();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String playerName = event.getPlayer().getName();
        int count = playerBlockCount.getOrDefault(playerName, 0) + 1;
        playerBlockCount.put(playerName, count);
        Score score = objective.getScore(playerName);
        score.setScore(count);
        event.getPlayer().setScoreboard(board);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        int count = playerBlockCount.getOrDefault(playerName, 0);
        Score score = objective.getScore(playerName);
        score.setScore(count);
        event.getPlayer().setScoreboard(board);
    }

    private void loadBlockCounts() {
        for (String key : dataConfig.getKeys(false)) {
            int count = dataConfig.getInt(key);
            playerBlockCount.put(key, count);
            Score score = objective.getScore(key);
            score.setScore(count);
        }
    }

    private void saveBlockCounts() {
        for (Map.Entry<String, Integer> entry : playerBlockCount.entrySet()) {
            dataConfig.set(entry.getKey(), entry.getValue());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}