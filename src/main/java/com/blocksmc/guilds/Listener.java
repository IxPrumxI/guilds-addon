package com.blocksmc.guilds;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Listener {
    private final GuildsAddon GuildsInstance;

    public Listener(){
        GuildsInstance = GuildsAddon.getInstance();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onNameRender(PlayerEvent.NameFormat event){
        EntityPlayer player = event.entityPlayer;
        GuildsAddon.getInstance().getPlayerGuild(player);
    }

    @SubscribeEvent()
    public void onPlayerAttack(AttackEntityEvent event){
        EntityPlayer attacker = (EntityPlayer) event.entityLiving;
        if(!(event.target instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.target;

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
