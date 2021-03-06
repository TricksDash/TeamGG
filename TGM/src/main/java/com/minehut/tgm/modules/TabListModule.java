package com.minehut.tgm.modules;

import com.minehut.tgm.TGM;
import com.minehut.tgm.match.Match;
import com.minehut.tgm.match.MatchModule;
import com.minehut.tgm.match.MatchStatus;
import com.minehut.tgm.modules.team.MatchTeam;
import com.minehut.tgm.modules.team.TeamManagerModule;
import com.minehut.tgm.util.Strings;
import com.minehut.tgm.util.TitleAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListModule extends MatchModule implements Listener {
    @Getter protected int runnableId = -1;

    @Getter private TeamManagerModule teamManagerModule;

    @Override
    public void load(Match match) {
        teamManagerModule = match.getModule(TeamManagerModule.class);

        refreshAllTabs();

        runnableId = Bukkit.getScheduler().scheduleSyncRepeatingTask(TGM.get(), new Runnable() {
            @Override
            public void run() {
                refreshAllTabs();
            }
        }, 20L, 20L);
    }

    @Override
    public void unload() {
        Bukkit.getScheduler().cancelTask(runnableId);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        refreshTab(event.getPlayer());
    }

    private void refreshTab(Player player) {
        MatchStatus matchStatus = TGM.get().getMatchManager().getMatch().getMatchStatus();

        ChatColor timeColor = ChatColor.GREEN;
        if (matchStatus == MatchStatus.PRE) {
            timeColor = ChatColor.GOLD;
        } else if (matchStatus == MatchStatus.POST) {
            timeColor = ChatColor.RED;
        }

        String header = ChatColor.WHITE + ChatColor.BOLD.toString() + TGM.get().getMatchManager().getMatch().getMapContainer().getMapInfo().getGametype().toString() +
                        ChatColor.DARK_GRAY + " - " + timeColor + Strings.formatTime(TGM.get().getMatchManager().getMatch().getModule(TimeModule.class).getTimeElapsed()) +
                        ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + ChatColor.BOLD.toString() + "TEAM.GG";

        String footer = "";
        for (MatchTeam matchTeam : teamManagerModule.getTeams()) {
            if(matchTeam.isSpectator()) continue;
            footer += matchTeam.getColor() + matchTeam.getAlias() + ": " + ChatColor.WHITE + matchTeam.getMembers().size() + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + matchTeam.getMax();
            footer += ChatColor.DARK_GRAY + " - ";
        }
        footer += ChatColor.AQUA + "Spectators: " + ChatColor.WHITE + TGM.get().getModule(TeamManagerModule.class).getSpectators().getMembers().size();


        TitleAPI.sendTabTitle(player, header, footer);
    }
    private void refreshAllTabs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            refreshTab(player);
        }
    }
}
