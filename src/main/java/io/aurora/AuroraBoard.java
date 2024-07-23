package io.aurora;

import io.aurora.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class AuroraBoard extends JavaPlugin implements Listener {
    private static final Logger LOGGER = Bukkit.getLogger();

    private Map<String, Integer> playerBlockCount = new HashMap<>();
    private ScoreboardManager manager;
    private Scoreboard board;
    private Objective objective;

    @Override
    public void onLoad() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is loading.");
    }

    @Override
    public void onEnable() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        Plugin config = AuroraBoard.getPlugin(AuroraBoard.class);

        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective("blockCount", "dummy", Objects.requireNonNull(config.getConfig().getString("name")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Override
    public void onDisable() {
        LOGGER.info(Color.YELLOW + "[Aurora] " + Color.RESET + "Plugin is disabled.");
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
}
