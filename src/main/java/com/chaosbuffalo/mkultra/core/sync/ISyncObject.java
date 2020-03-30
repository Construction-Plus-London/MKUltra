package com.chaosbuffalo.mkultra.core.sync;

import net.minecraft.nbt.NBTTagCompound;

public interface ISyncObject {
    boolean isDirty();
    void deserializeUpdate(NBTTagCompound tag);
    void serializeUpdate(NBTTagCompound tag);
}
