package nl.warofeurope.event;

import nl.warofeurope.event.utils.runnables.AsyncTask;
import nl.warofeurope.event.utils.runnables.SyncTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static nl.warofeurope.event.utils.Colors.color;
import static nl.warofeurope.event.utils.ScoreboardUtil.replaceScore;

public class ScoreboardHandler {
    public Scoreboard scoreboard;

    public ScoreboardHandler(){
        this.initialize();
    }

    public void updateScoreboard(){
        final int[] start = {17};
        Objective objective = this.scoreboard.getObjective("sidebar");

        List<Teams> teams = Arrays.stream(Teams.getValues()).sorted((o1, o2) -> o2.getPlayers().size() - o1.getPlayers().size()).collect(Collectors.toList());
        for (Teams team : teams) {
            replaceScore(objective, start[0], color(team.getDisplay() + "&f: &6" + team.getPlayers().size() + " &7&o(Kills: " + team.getKills() + ")"));
            start[0]--;
        }
    }

    public void initialize(){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective boardObject = this.scoreboard.registerNewObjective("sidebar", "dummy");
        boardObject.setDisplaySlot(DisplaySlot.SIDEBAR);
        boardObject.setDisplayName(color("&6&lLand Aantallen &7(Levend)"));

        for (Teams internalTeam : Teams.getValues()){
            Team team = this.scoreboard.registerNewTeam("t-" + internalTeam.toString());
            team.setPrefix(color(internalTeam.prefix + " "));
            team.setAllowFriendlyFire(false);
            team.setCanSeeFriendlyInvisibles(true);
        }

        this.updateScoreboard();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public static enum Teams {
        NEDERLAND("&6[Ned]&r", "&6Nederland", new Location(Bukkit.getWorld("world"), -5, 67, 316, 180, 0)),
        BELGIE("&0[B&ee&4l]&r", "&0Be&elg&4ië", new Location(Bukkit.getWorld("world"), -155, 67, 280, 180, 0)),
        PORTUGAL("&2[Po&4r]&r", "&2Por&4tugal", new Location(Bukkit.getWorld("world"), -257, 68, 188, 270, 0)),
        NOORWEGEN("&4[No&9o]&r", "&4Noor&9wegen", new Location(Bukkit.getWorld("world"), -300, 68, 93, 270, 0)),
        IJSLAND("&b[IJ&fs]&r", "&bIJs&fland", new Location(Bukkit.getWorld("world"), -316, 67, -3, 270, 0)),
//        ZWEDEN("&9[Z&ewe]&r", "&9Zwe&eden", new Location(Bukkit.getWorld("world"), -281, 67, -137, 270, 0)),
        MONACO("&d[Mo&fn]&r", "&dMon&faco", new Location(Bukkit.getWorld("world"), -109, 69, -300, 0, 0)),
        TURKIJE("&5[Tu&fr]&r", "&5Tur&fkije", new Location(Bukkit.getWorld("world"), 59, 68, -310, 0, 0)),
        ITALIE("&2[I&ft&4a]&r", "&2It&fal&4ië", new Location(Bukkit.getWorld("world"), 200, 68, -246, 0, 0)),
        RUSLAND("&9[Ru&cs]&r", "&9Rus&cland", new Location(Bukkit.getWorld("world"), 290, 68, -135, 90, 0)),
        SPANJE("&6[S&epa]&r", "&6Spa&enje", new Location(Bukkit.getWorld("world"), 315, 67, -15, 90, 0)),
        ZWITSERLAND("&c[Z&fwi]&r", "&cZ&fwit&cserland", new Location(Bukkit.getWorld("world"), 300, 67, 107, 90, 0)),
        ENGELAND("&f[E&4ng]&r", "&fEnge&4land", new Location(Bukkit.getWorld("world"), 230, 68, 216, 90, 0)),
        FRANKRIJK("&9[F&fr&4a]&r", "&9Fra&fnk&4rijk", new Location(Bukkit.getWorld("world"), 129, 67,290, 180, 0)),
        DUITSLAND("&0[D&4u&6i]&r", "&0Du&4its&6land", new Location(Bukkit.getWorld("world"), -210, 68, -238, 0, 0)),
        OEKRAINE("&3[Oe&ek]&r", "&3Oekr&eaine&r", new Location(Bukkit.getWorld("world"), -281, 67, -137, 270, 0));

        private final String prefix;
        private final String display;
        private int kills;
        private final Set<Player> players;
        private final Location spawnLocations;

        Teams(String prefix, String display, Location spawnLocation) {
            this.prefix = prefix;
            this.display = display;
            this.kills = 0;
            this.spawnLocations = spawnLocation;
            this.players = new HashSet<>();
        }

        public void addKill(){
            this.kills++;
        }

        public int getKills() {
            return this.kills;
        }

        public Set<Player> getPlayers() {
            return players;
        }

        public Location getSpawnLocations() {
            return this.spawnLocations;
        }

        public void teleport(Player player){
            player.teleport(this.getSpawnLocations());
        }

        public void teleport(String permission){
            for (Player player : Bukkit.getOnlinePlayers()){
                if (!player.hasPermission("eventbypass")){
                    if (player.hasPermission(permission)){
                        this.teleport(player);
                    }
                }
            }
        }

        public String getPrefix() {
            return prefix;
        }

        public String getDisplay() {
            return display;
        }

        public static Teams[] getValues(){
            return Teams.values();
        }
    }
}
