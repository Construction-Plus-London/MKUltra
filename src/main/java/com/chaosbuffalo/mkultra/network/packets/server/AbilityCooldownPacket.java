package com.chaosbuffalo.mkultra.network.packets.server;

import com.chaosbuffalo.mkultra.api.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AbilityCooldownPacket implements IMessage {

    ResourceLocation skillId;
    int cooldown;

    public AbilityCooldownPacket() {
    }

    public AbilityCooldownPacket(ResourceLocation abilityId, int cooldown) {
        this.skillId = abilityId;
        this.cooldown = cooldown;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        skillId = pb.readResourceLocation();
        cooldown = pb.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(skillId);
        pb.writeInt(cooldown);
    }

    public static class Handler extends MessageHandler.Client<AbilityCooldownPacket> {

        // Client reads the serialized data from the server
        @Override
        public IMessage handleClientMessage(final EntityPlayer player, final AbilityCooldownPacket msg, MessageContext ctx) {
            ClientUtils.addScheduledTask(() -> {
                if (player == null)
                    return;
                IPlayerData data = PlayerDataProvider.get(player);
                if (data == null)
                    return;

                data.setCooldown(msg.skillId, msg.cooldown);
            });
            return null;

        }
    }
}
