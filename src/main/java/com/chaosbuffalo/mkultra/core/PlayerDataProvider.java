package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.Capabilities;
import com.chaosbuffalo.mkultra.api.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;

public class PlayerDataProvider implements ICapabilitySerializable<NBTTagCompound> {

    private PlayerData data;

    public PlayerDataProvider(EntityPlayer player){
        this.data = new PlayerData(player);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == Capabilities.PLAYER_DATA_CAPABILITY;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (hasCapability(capability, facing)) {
            return Capabilities.PLAYER_DATA_CAPABILITY.cast(data);
        }
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Capabilities.PLAYER_DATA_CAPABILITY.readNBT(data, null, nbt);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound)Capabilities.PLAYER_DATA_CAPABILITY.writeNBT(data, null);
    }

    public static IPlayerData get(EntityPlayer player)
    {
        if (player.hasCapability(Capabilities.PLAYER_DATA_CAPABILITY, null)) {
            return player.getCapability(Capabilities.PLAYER_DATA_CAPABILITY, null);
        }
        return null;
    }
}
