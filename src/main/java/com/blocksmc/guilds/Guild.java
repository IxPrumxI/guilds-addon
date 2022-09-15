package com.blocksmc.guilds;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Guild {
    private final int id;
    private final String name;
    private HashSet<ScorePlayerTeam> teams = new HashSet<>();

    public Guild(ScorePlayerTeam team) {
        this.id = getClanID(team);
        this.name = getClanName(team);

        updateTeams();
    }

    public static String getClanName(ScorePlayerTeam team) {
        Pattern namePattern = Pattern.compile("\\[(?<name>.+)\\]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher nameMatcher = namePattern.matcher(team.getColorSuffix());

        boolean found = nameMatcher.find();
        if(!found) return null;

        return nameMatcher.group("name");
    }

    public static int getClanID(ScorePlayerTeam team) {
        Pattern idPattern = Pattern.compile("C_(?<id>\\d+)");
        Matcher idMatcher = idPattern.matcher(team.getTeamName());

        boolean found = idMatcher.find();
        if(!found) return 0;

        return Integer.parseInt(idMatcher.group("id"));
    }

    public boolean inGuild(EntityPlayer player) {
        updateTeams();
        boolean found = false;
        for (ScorePlayerTeam team : teams) {
            if(player.getTeam().isSameTeam(team)) {
                   found = true;
                   break;
            }
        }

        return found;
    }

    private void updateTeams() {
        HashSet<ScorePlayerTeam> teams = new HashSet<>();
        for (ScorePlayerTeam team : Minecraft.getMinecraft().theWorld.getScoreboard().getTeams()) {
            if(team.getTeamName().endsWith("_" + id))
                teams.add(team);
        }

        this.teams = teams;
    }
}
