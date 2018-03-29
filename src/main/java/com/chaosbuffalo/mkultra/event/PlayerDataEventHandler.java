package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.api.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class PlayerDataEventHandler {

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onEntityJoinWorldEventServer(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            IPlayerData data = PlayerDataProvider.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onEntityJoinWorldEventClient(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP) {
            IPlayerData data = PlayerDataProvider.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdateTick(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            IPlayerData playerData = PlayerDataProvider.get((EntityPlayer) e.getEntityLiving());
            if (playerData != null) {
                playerData.onTick();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone e) {
        // I would hope this can't happen
        if (e.getEntityPlayer() == null)
            return;

        IPlayerData newData = PlayerDataProvider.get(e.getEntityPlayer());
        if (newData == null)
            return;

        // Die on the original so we can clone properly and not need an immediate update packet
        if (e.isWasDeath()) {
            IPlayerData oldData = PlayerDataProvider.get(e.getOriginal());
            if (oldData == null)
                return;
            oldData.doDeath();
        }

        newData.clone(e.getOriginal());
        newData.onRespawn();

    }

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer))
            return;

        event.addCapability(new ResourceLocation(MKUltra.MODID, "player_data"), new PlayerDataProvider((EntityPlayer) event.getObject()));
    }
}