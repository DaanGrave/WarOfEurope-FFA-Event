package nl.warofeurope.event.listeners;

import nl.warofeurope.event.EventPlugin;
import nl.warofeurope.event.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Locale;

public class JoinListener implements Listener {
    private final EventPlugin eventPlugin;

    public JoinListener(EventPlugin eventPlugin) {
        this.eventPlugin = eventPlugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();

        for (ScoreboardHandler.Teams teams : ScoreboardHandler.Teams.getValues()){
            teams.getPlayers().remove(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        player.setScoreboard(this.eventPlugin.scoreboardHandler.scoreboard);

        if (eventPlugin.game.started){
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            if (!player.hasPermission("eventbypass")){
                boolean found = false;
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setGameMode(GameMode.ADVENTURE);

                for (ScoreboardHandler.Teams team : ScoreboardHandler.Teams.getValues()){
                    if (player.hasPermission("group." + team.toString().toLowerCase(Locale.ROOT))){
                        team.teleport(player);
                        Scoreboard scoreboard = this.eventPlugin.scoreboardHandler.scoreboard;
                        Team scoreboardTeam = scoreboard.getTeam("t-" + team);
                        scoreboardTeam.addEntry(player.getName());
                        found = true;
                        team.getPlayers().add(player);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit event " + player.getName());
                        break;
                    }
                }

                if (!found){
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
            this.eventPlugin.scoreboardHandler.updateScoreboard();
        }
    }
}
