package nl.warofeurope.event.listeners;

import nl.warofeurope.event.EventPlugin;
import nl.warofeurope.event.ScoreboardHandler;
import nl.warofeurope.event.utils.factories.ItemstackFactory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

import static nl.warofeurope.event.utils.Colors.color;

public class DeathListener implements Listener {
    private final EventPlugin eventPlugin;

    public DeathListener(EventPlugin eventPlugin) {
        this.eventPlugin = eventPlugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player entity = event.getEntity();
        entity.setGameMode(GameMode.SPECTATOR);

        for (ScoreboardHandler.Teams teams : ScoreboardHandler.Teams.getValues()){
            teams.getPlayers().remove(entity);
        }

        if (entity.getKiller() != null){
            event.setDeathMessage(color("&c" + entity.getName() + " &7is dood gegaan door &c" + entity.getKiller().getName() + "&7."));
            entity.getKiller().getInventory().addItem(new ItemstackFactory(Material.GOLDEN_APPLE, 5));
            for (ItemStack armorContent : entity.getInventory().getArmorContents()) {
                if (armorContent != null){
                    int newDurability = (int) armorContent.getDurability() - 50;
                    armorContent.setDurability((short) newDurability);
                    if (armorContent.getType().toString().contains("HELMET")){
                        entity.getKiller().getInventory().setHelmet(armorContent);
                    } else if (armorContent.getType().toString().contains("CHESTPLATE")){
                        entity.getKiller().getInventory().setChestplate(armorContent);
                    } else if (armorContent.getType().toString().contains("LEGGINGS")){
                        entity.getKiller().getInventory().setLeggings(armorContent);
                    } else if (armorContent.getType().toString().contains("BOOTS")){
                        entity.getKiller().getInventory().setBoots(armorContent);
                    }
                }
            }

            for (ScoreboardHandler.Teams teams : ScoreboardHandler.Teams.getValues()){
                if (teams.getPlayers().contains(entity.getKiller())){
                    teams.addKill();
                    break;
                }
            }
            this.eventPlugin.scoreboardHandler.updateScoreboard();
        } else {
            event.setDeathMessage(null);
        }
    }

    private boolean didWin(ScoreboardHandler.Teams teams){
        return Arrays.stream(ScoreboardHandler.Teams.getValues()).filter(i -> teams.getPlayers().size() > 0).count() == 1;
    }
}
