package nl.warofeurope.event.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import nl.warofeurope.event.EventPlugin;
import nl.warofeurope.event.models.Game;
import nl.warofeurope.event.utils.runnables.SyncDelayedTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static nl.warofeurope.event.utils.Colors.color;

@CommandAlias("event")
@CommandPermission("event.event")
@Description("Main event command")
public class EventCommand extends BaseCommand {
    private final EventPlugin eventPlugin;

    public EventCommand(EventPlugin eventPlugin) {
        this.eventPlugin = eventPlugin;
    }

    @Subcommand("start")
    @CommandAlias("startevent")
    @CommandPermission("event.startevent")
    public void start(CommandSender sender){
        this.broadcast("&4&l[Event] &cDe Kingdom FFA begint over &4&l1 &cminuut!");
        new SyncDelayedTask(20 * 30, () -> {
            this.broadcast("&4&l[Event] &cDe Kingdom FFA begint over &4&l30 &cseconden!");
            new SyncDelayedTask(20 * 10, () -> {
                this.broadcast("&4&l[Event] &cDe Kingdom FFA begint over &4&l10 &cseconden!");
                new SyncDelayedTask(20 * 5, () -> {
                    this.broadcast("&4&l[Event] &cDe Kingdom FFA begint over &4&l5 &cseconden!");
                    new SyncDelayedTask(20 * 3, () -> {
                        this.broadcast("&4&l[Event] &cDe Kingdom FFA begint over &4&l3 &cseconden!");
                        new SyncDelayedTask(20 * 2, () -> {
                            this.broadcast("&4&l[Event] &cDe Kingdom FFA begint over &4&l2 &cseconden!");
                            new SyncDelayedTask(20, () -> {
                                this.broadcast("&4&l[Event] &cDe Kingdom FFA begint over &4&l1 &cseconden!");
                                new SyncDelayedTask(20, () -> {
                                    this.eventPlugin.game.started = true;
                                    this.broadcast("&4&l[Event] &cDe Kingdom FFA is begonnen! &4&lSUCCES!");
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    private void broadcast(String message){
        message = color(message);
        for (Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(message);
        }
    }

    @Subcommand("reset")
    @CommandAlias("resetevent")
    @CommandPermission("event.reset")
    public void reset(CommandSender sender){
        this.eventPlugin.game = new Game(this.eventPlugin);
        sender.sendMessage(color("&aGame gereset"));

        for (Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setGameMode(GameMode.ADVENTURE);
        }
    }
}
