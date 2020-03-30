package com.chaosbuffalo.mkultra.core.sync;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SyncResourceLocation extends SyncObject<ResourceLocation> {

    static void serialize(NBTTagCompound tag, SyncObject<ResourceLocation> instance) {
        tag.setString(instance.name, instance.get().toString());
    }

    static void deserialize(NBTTagCompound tag, SyncObject<ResourceLocation> instance) {
        instance.set(new ResourceLocation(tag.getString(instance.name)));
    }

    public SyncResourceLocation(String name, ResourceLocation value) {
        super(name, value, SyncResourceLocation::serialize, SyncResourceLocation::deserialize);
    }
}
