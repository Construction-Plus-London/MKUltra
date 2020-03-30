package com.chaosbuffalo.mkultra.core.sync;

import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class CompositeUpdater implements ISyncObject {
    List<ISyncObject> components = new ArrayList<>();

    public CompositeUpdater(ISyncObject... syncs) {
        for (ISyncObject sync : syncs) {
            add(sync);
        }
    }

    public void add(ISyncObject sync) {
        components.add(sync);
    }

    @Override
    public boolean isDirty() {
        return components.stream().anyMatch(ISyncObject::isDirty);
    }

    @Override
    public void deserializeUpdate(NBTTagCompound tag) {
        components.forEach(c -> c.deserializeUpdate(tag));
    }

    @Override
    public void serializeUpdate(NBTTagCompound tag) {
        components.stream().filter(ISyncObject::isDirty).forEach(c -> c.serializeUpdate(tag));
    }
}