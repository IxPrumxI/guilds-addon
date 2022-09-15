package com.blocksmc.guilds;

import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Listener implements Consumer<Entity> {
    private final GuildsAddon GuildsInstance;

    public Listener(){
        GuildsInstance = GuildsAddon.getInstance();
    }

    public void accept(Entity entity) {
        EntityPlayer attacker =  Minecraft.getMinecraft().thePlayer;
        if(!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        Guild playerClan = GuildsInstance.getPlayerGuild(player);
        Guild attackerClan = GuildsInstance.getPlayerGuild(attacker);

        if(playerClan == null || attackerClan == null) return;

        if(playerClan.inGuild(attacker)) {
            Minecraft.getMinecraft().ingameGUI.setRecordPlaying("You are hitting your Clan!!", true);
            try {
                GuildsInstance.playSound();
            } catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
}
