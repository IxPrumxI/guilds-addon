package com.blocksmc.guilds;

import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuildsAddon extends LabyModAddon {

    private static GuildsAddon instance;
    private final HashSet<Guild> guilds = new HashSet<>();
    private boolean isSoundPlaying;
    private String soundPath;

    @Override
    public void onEnable() {
        instance = this;

        getApi().getEventManager().registerOnAttack(new Listener());
    }

    @Override
    public void loadConfig() {
        this.soundPath = getConfig().has("file") ? getConfig().get("file").getAsString() : null;
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        URL fileUri = getClass().getResource("/sound.jpg");
        if(fileUri == null) return;
        ControlElement.IconData iconData = new ControlElement.IconData(fileUri.getFile());
        list.add(new StringElement("Sound File - WAV Format Only", this, iconData, "file", soundPath));
    }

    public static GuildsAddon getInstance() {
        return instance;
    }

    public Set<Guild> getGuilds() {
        return guilds;
    }

    public Guild getPlayerGuild(EntityPlayer player){
        for (Guild guild : getGuilds()) {
            if(guild.inGuild(player)){
                return guild;
            }
        }

        ScorePlayerTeam guildTeam = player.getWorldScoreboard().getTeam(player.getTeam().getRegisteredName());
        int guildId = Guild.getClanID(guildTeam);
        if(guildId == 0) return null;

        return new Guild(guildTeam);
    }

    public void playSound() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        if(isSoundPlaying) return;
        this.isSoundPlaying = true;
        URL soundUri = getClass().getResource(soundPath);
        File soundFile;
        if(soundUri == null) {
            try {
                soundFile = new File(soundPath);
            } catch (Exception ignored) {
                return;
            }
        } else {
            soundFile = new File(soundUri.getPath());
        }

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(1);

        clip.start();
        clip.addLineListener(e -> {
            if (e.getType() == LineEvent.Type.STOP) {
                this.isSoundPlaying = false;
            }
        });
    }
}
