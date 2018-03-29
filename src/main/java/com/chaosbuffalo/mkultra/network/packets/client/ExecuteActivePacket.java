package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.api.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ExecuteActivePacket implements IMessage {
    private int slotIndex;

    public ExecuteActivePacket() {
    }

    public ExecuteActivePacket(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slotIndex);
    }


    public static class Handler extends MessageHandler.Server<ExecuteActivePacket> {
        @Override
        public IMessage handleServerMessage(final EntityPlayer player,
                                            final ExecuteActivePacket message,
                                            MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {
                IPlayerData pData = PlayerDataProvider.get(player);
                if (pData == null)
                    return;

                pData.executeHotBarAbility(message.slotIndex);
            });
            return null;
        }
    }
}
